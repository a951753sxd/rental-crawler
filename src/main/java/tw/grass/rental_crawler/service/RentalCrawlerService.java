package tw.grass.rental_crawler.service;

import java.util.List;

import tw.grass.rental_crawler.model.RentalCatalog;
import tw.grass.rental_crawler.model.RentalDetail;

public interface RentalCrawlerService {
    //取得最新的591租屋目錄資訊
    List<RentalCatalog> fetchLatestRentalCatalog();
    //取得591租屋詳細資訊
    RentalDetail fetchRentalDetail(RentalCatalog rentalCatalog);
}
