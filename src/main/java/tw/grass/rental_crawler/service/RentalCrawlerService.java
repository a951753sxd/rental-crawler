package tw.grass.rental_crawler.service;

import java.util.List;

import tw.grass.rental_crawler.model.RentalCatalogDTO;
import tw.grass.rental_crawler.model.RentalDetailDTO;

public interface RentalCrawlerService {
    //取得最新的591租屋目錄資訊
    List<RentalCatalogDTO> fetchLatestRentalCatalog();
    //取得591租屋詳細資訊
    RentalDetailDTO fetchRentalDetail(RentalCatalogDTO rentalCatalog);
}
