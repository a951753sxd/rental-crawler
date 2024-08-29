package tw.grass.rental_crawler.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import tw.grass.rental_crawler.model.RentalCatalogDTO;

@Service
public class RentalCrawlerScheduler {


    @Autowired
    RentalCrawlerService rentalCrawlerService;

    @PostConstruct
    public void runOnceAtStartup() {
        fetchRentalData();
    }

    @Scheduled(cron = "${rental.crawler.schedule}")
    public void scheduledCrawl() throws Exception {
        fetchRentalData();
    }

    
    private void fetchRentalData() {
        List<RentalCatalogDTO> list = rentalCrawlerService.fetchLatestRentalCatalog();
        list.stream().forEach(obj -> rentalCrawlerService.fetchRentalDetail(obj));
    }

}