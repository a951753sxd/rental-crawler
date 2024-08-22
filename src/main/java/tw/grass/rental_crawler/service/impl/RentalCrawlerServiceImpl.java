package tw.grass.rental_crawler.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import tw.grass.rental_crawler.service.RentalCrawlerService;

@Service
public class RentalCrawlerServiceImpl implements RentalCrawlerService {

    Logger log = LoggerFactory.getLogger(RentalCrawlerService.class);

    @Override
    public void fetchRentalData() {
        log.info("Starting to fetch rental data...");
        try {
            //這邊使用591的條件other=newPost:新上架、sort=posttime_desc:排序為新到舊
            String urlString = "https://rent.591.com.tw/list?other=newPost&sort=posttime_desc";

            // 提取連結中的HTML資訊
            Document doc = Jsoup.connect(urlString).get();
            // 在這裡解析HTML並提取所需數據
            parseHTML(doc);


            log.info("Successfully fetched rental data");
        } catch (Exception e) {
            log.error("Error while fetching rental data", e);
        }
    }

    //TODO: 處理資料邏輯代寫
    private void parseHTML(Document doc) {

        log.info("doc is : {}", doc);
        
    }
}
