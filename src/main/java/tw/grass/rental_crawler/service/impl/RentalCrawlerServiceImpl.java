package tw.grass.rental_crawler.service.impl;

import java.time.Duration;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import tw.grass.rental_crawler.service.RentalCrawlerService;

@Service
public class RentalCrawlerServiceImpl implements RentalCrawlerService {

    Logger log = LoggerFactory.getLogger(RentalCrawlerService.class);

    @Override
    public void fetchRentalData() {
        log.info("Starting to fetch rental data...");

        // 設定 ChromeDriver 路徑
        System.setProperty("webdriver.chrome.driver", "./chromedriver-win64/chromedriver.exe");

        // 設定 ChromeOptions
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless"); // 如果你不想看到瀏覽器，可以啟用無頭模式
        options.addArguments("--disable-gpu"); // 避免某些操作系統的問題
        options.addArguments("--window-size=1920,1080"); // 設置窗口大小

        WebDriver driver = new ChromeDriver(options);
        try {
            //這邊使用591的條件other=newPost:新上架、sort=posttime_desc:排序為新到舊
            String urlString = "https://rent.591.com.tw/list?other=newPost&sort=posttime_desc";
            //使用webDriver前往該網址
            driver.get(urlString);
            
            // 等待 JavaScript 加載完畢，最多等10秒
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            // 取得網頁的 HTML
            String pageSource = driver.getPageSource();

            // 使用 Jsoup 解析 HTML
            Document doc = Jsoup.parse(pageSource);
            
            // 在這裡解析HTML並提取所需數據
            parseHTML(doc);
            log.info("Successfully fetched rental data");

        } catch (Exception e) {
            log.error("Error while fetching rental data", e);
        } finally {
            // 關閉 WebDriver
            driver.quit();
        }
    }

    private void parseHTML(Document doc) {
        Elements listings = doc.select("div.item-info");

        // 迴圈遍歷每個房源資訊
        for (Element listing : listings) {
            // 提取標題
            String title = listing.select("a.link").attr("title");
            // 提取連結
            String link = listing.select("a.link").attr("href");
            // 提取地址
            String address = listing.select("div.item-info-txt i.house-place").next().text();
            // 提取價格
            String price = listing.select("div.item-info-price strong").text();
            // 提取樓層與坪數
            String floorAndArea = listing.select("div.item-info-txt").get(1).text();
            // 提取距離捷運
            String distanceToMrtName = listing.select("div.item-info-txt i.house-metro").next().text();
            String distanceToMRT = listing.select("div.item-info-txt i.house-metro").next().next().text();
            
            // 輸出房源資訊
            log.info("Title: {}", title);
            log.info("Link: {}", link);
            log.info("Address: {}", address);
            log.info("Price: {} 元/月", price);
            log.info("Floor and Area: {}", floorAndArea);
            log.info("Distance to MRT: {}", distanceToMrtName + distanceToMRT);
            log.info("---------------------------------------");
        }
    }
}
