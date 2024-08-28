package tw.grass.rental_crawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.grass.rental_crawler.entity.RentalDetail;

public interface RentalDetailRepository extends JpaRepository<RentalDetail, Long> {
    RentalDetail findByLink(String link);
}