package tw.grass.rental_crawler.service.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import tw.grass.rental_crawler.entity.RentalDetail;
import tw.grass.rental_crawler.model.RentalCatalogDTO;
import tw.grass.rental_crawler.model.RentalDetailDTO;
import tw.grass.rental_crawler.repositories.RentalDetailRepository;
import tw.grass.rental_crawler.service.RentalCrawlerService;

@Service
public class RentalCrawlerServiceImpl implements RentalCrawlerService {

    Logger log = LoggerFactory.getLogger(RentalCrawlerService.class);

    @Autowired
    private RentalDetailRepository rentalDetailRepository;

    @Value("${webdriver.name}")
    private String webdriverName;

    @Value("${webdriver.path}")
    private String webdriverPath;

    private WebDriver driver;

    @PostConstruct
    public void init() {
        webDriverInit();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            driver.quit();
        }));
    }

    @Override
    public List<RentalCatalogDTO> fetchLatestRentalCatalog() {
        log.info("Starting to fetch rental data...");
        try {
            // 這邊使用591的條件other=newPost:新上架、sort=posttime_desc:排序為新到舊
            Document doc = getJsoupDoc("https://rent.591.com.tw/list?other=newPost&sort=posttime_desc");
            List<RentalCatalogDTO> rentaCatalogList = parseRentalCatalog(doc);
            
            // 591一頁為30個租屋資訊，所以當有30筆新資料時，去第二頁再做一次
            if (rentaCatalogList.size() == 30) {
                log.info("第一頁皆為新資料，去第二頁進行爬去");
                doc = getJsoupDoc("https://rent.591.com.tw/list?other=newPost&sort=posttime_desc&page=2");
                rentaCatalogList.addAll(parseRentalCatalog(doc));
            }
            
            rentaCatalogList = rentaCatalogList.stream().limit(3).collect(Collectors.toList());
            log.info("Successfully fetched rental data");
            return rentaCatalogList;
        } catch (Exception e) {
            log.error("Error while fetching rental data", e);
            return Collections.emptyList();
        }
    }

    private Document getJsoupDoc(String urlString) {
        driver.get(urlString);
        String pageSource = driver.getPageSource();
        Document doc = Jsoup.parse(pageSource);
        return doc;
    }

    private void webDriverInit() {
        // 設定 ChromeDriver 路徑
        System.setProperty(webdriverName, webdriverPath);

        // 設定 ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 如果你不想看到瀏覽器，可以啟用無頭模式
        options.addArguments("--disable-gpu"); // 避免某些操作系統的問題
        options.addArguments("--window-size=1920,1080"); // 設置窗口大小

        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    private List<RentalCatalogDTO> parseRentalCatalog(Document doc) {
        List<RentalCatalogDTO> list = new ArrayList<RentalCatalogDTO>();

        Elements listings = doc.select("div.item-info");
        for (Element listing : listings) {
            String title = listing.select("a.link").attr("title");
            String link = listing.select("a.link").attr("href");
            String address = listing.select("div.item-info-txt i.house-place").next().text();
            String price = listing.select("div.item-info-price strong").text();
            String floorAndArea = listing.select("div.item-info-txt").get(1).text();
            String distanceToMrtName = listing.select("div.item-info-txt i.house-metro").next().text();
            String distanceToMRT = listing.select("div.item-info-txt i.house-metro").next().next().text();

            //確認是否有重複資料
            RentalDetail findByLink = rentalDetailRepository.findByLink(link);
            
            //沒有就寫入，有就跳過
            if (findByLink == null) {
                RentalCatalogDTO rentalIfo = new RentalCatalogDTO();
                setRentalCatalogValue(title, link, address, price, floorAndArea, distanceToMrtName, distanceToMRT, rentalIfo);
                list.add(rentalIfo);

                //寫入資料庫
                RentalDetail entity = new RentalDetail();
                entity.setLink(link);
                rentalDetailRepository.save(entity);
                log.info("寫入:{}", title);
            } else {
                log.info("重複:{}", title);
            }
        }
        return list;
    }

    private void setRentalCatalogValue(String title, String link, String address, String price, String floorAndArea,
            String distanceToMrtName, String distanceToMRT, RentalCatalogDTO rentalIfo) {
        rentalIfo.setTitle(title);
        rentalIfo.setLink(link);
        rentalIfo.setAddress(address);
        rentalIfo.setPrice(price);
        rentalIfo.setFloorAndArea(floorAndArea);
        rentalIfo.setDistanceToMRT(distanceToMrtName + distanceToMRT);
    }

    @Override
    public RentalDetailDTO fetchRentalDetail(RentalCatalogDTO rentalCatalog) {
        log.info("Starting to fetch rental detail data...");
        String detailUrl = rentalCatalog.getLink();
        Document doc = getJsoupDoc(detailUrl);

        RentalDetailDTO rentalDetail = parseRentalDetail(doc);
        rentalDetail.setAddress(rentalCatalog.getAddress());
        rentalDetail.setPrice(rentalCatalog.getPrice());
        rentalDetail.setFloorAndArea(rentalCatalog.getFloorAndArea());

        log.info(rentalDetail.getInfo());
        return rentalDetail;
    }

    private RentalDetailDTO parseRentalDetail(Document doc) {
        RentalDetailDTO rentalDetail = new RentalDetailDTO();

        // 租住說明
        String rentalDescription = doc.select("div.service-cate:has(i.icon-desc) span").text();
        rentalDetail.setRentalDescription(rentalDescription);
        // 房屋守則
        String houseRules = doc.select("div.service-cate:has(i.icon-rule) span").text();
        rentalDetail.setHouseRules(houseRules);

        // 設備列表
        rentalDetail.setHasFridge(!doc.select("dl:has(i.house-fridge-big)").hasClass("del"));
        rentalDetail.setHasWasher(!doc.select("dl:has(i.house-washer)").hasClass("del"));
        rentalDetail.setHasTv(!doc.select("dl:has(i.house-tv)").hasClass("del"));
        rentalDetail.setHasCold(!doc.select("dl:has(i.house-cold)").hasClass("del"));
        rentalDetail.setHasHeater(!doc.select("dl:has(i.house-heater)").hasClass("del"));
        rentalDetail.setHasBed(!doc.select("dl:has(i.house-bed)").hasClass("del"));
        rentalDetail.setHasCloset(!doc.select("dl:has(i.house-closet)").hasClass("del"));
        rentalDetail.setHasFourth(!doc.select("dl:has(i.house-fourth)").hasClass("del"));
        rentalDetail.setHasWifi(!doc.select("dl:has(i.house-wifi-big)").hasClass("del"));
        rentalDetail.setHasGas(!doc.select("dl:has(i.house-gas)").hasClass("del"));
        rentalDetail.setHasSofa(!doc.select("dl:has(i.house-sofa)").hasClass("del"));
        rentalDetail.setHasTable(!doc.select("dl:has(i.house-table)").hasClass("del"));
        rentalDetail.setHasBalcony(!doc.select("dl:has(i.house-balcony)").hasClass("del"));
        rentalDetail.setHasLift(!doc.select("dl:has(i.house-lift)").hasClass("del"));
        rentalDetail.setHasParking(!doc.select("dl:has(i.house-parking)").hasClass("del"));

        return rentalDetail;
    }
}
