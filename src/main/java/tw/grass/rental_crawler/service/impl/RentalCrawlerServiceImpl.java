package tw.grass.rental_crawler.service.impl;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import tw.grass.rental_crawler.model.RentalInfo;
import tw.grass.rental_crawler.service.RentalCrawlerService;

@Service
public class RentalCrawlerServiceImpl implements RentalCrawlerService {

    Logger log = LoggerFactory.getLogger(RentalCrawlerService.class);
    
    @Value("${webdriver.name}")
    private String webdriverName;
    
    @Value("${webdriver.path}")
    private String webdriverPath;

    @Override
    public void fetchRentalData() {
        log.info("Starting to fetch rental data...");

        WebDriver driver = webDriverInit();

        try {
            //這邊使用591的條件other=newPost:新上架、sort=posttime_desc:排序為新到舊
            String urlString = "https://rent.591.com.tw/list?other=newPost&sort=posttime_desc";
            driver.get(urlString);
            
            String pageSource = driver.getPageSource();
            Document doc = Jsoup.parse(pageSource);
            
            parseHTML(doc);
            log.info("Successfully fetched rental data");
        } catch (Exception e) {
            log.error("Error while fetching rental data", e);
        } finally {
            driver.quit();
        }
    }


    private WebDriver webDriverInit() {
        // 設定 ChromeDriver 路徑
        System.setProperty(webdriverName, webdriverPath);

        // 設定 ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 如果你不想看到瀏覽器，可以啟用無頭模式
        options.addArguments("--disable-gpu"); // 避免某些操作系統的問題
        options.addArguments("--window-size=1920,1080"); // 設置窗口大小

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        return driver;
    }


    private void parseHTML(Document doc) {
        List<RentalInfo> list = new ArrayList<RentalInfo>();
        
        Elements listings = doc.select("div.item-info");
        for (Element listing : listings) {
            String title = listing.select("a.link").attr("title");
            String link = listing.select("a.link").attr("href");
            String address = listing.select("div.item-info-txt i.house-place").next().text();
            String price = listing.select("div.item-info-price strong").text();
            String floorAndArea = listing.select("div.item-info-txt").get(1).text();
            String distanceToMrtName = listing.select("div.item-info-txt i.house-metro").next().text();
            String distanceToMRT = listing.select("div.item-info-txt i.house-metro").next().next().text();

            RentalInfo rentalIfo = new RentalInfo();
            rentalIfo.setTitle(title);
            rentalIfo.setLink(link);
            rentalIfo.setAddress(address);
            rentalIfo.setPrice(price);
            rentalIfo.setFloorAndArea(floorAndArea);
            rentalIfo.setDistanceToMRT(distanceToMrtName+distanceToMRT);
            list.add(rentalIfo);
        }
    }
}
