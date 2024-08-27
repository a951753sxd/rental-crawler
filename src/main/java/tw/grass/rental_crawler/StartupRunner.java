package tw.grass.rental_crawler;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import tw.grass.rental_crawler.model.RentalCatalogDTO;
import tw.grass.rental_crawler.service.RentalCrawlerService;

@Component
public class StartupRunner implements ApplicationRunner {

    @Autowired
    RentalCrawlerService rentalCrawlerService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<RentalCatalogDTO> list = rentalCrawlerService.fetchLatestRentalCatalog();
        list.stream().forEach(obj ->rentalCrawlerService.fetchRentalDetail(obj));
    }
}