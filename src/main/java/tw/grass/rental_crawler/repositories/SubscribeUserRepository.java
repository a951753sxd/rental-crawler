package tw.grass.rental_crawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.grass.rental_crawler.entity.SubscribeUser;

public interface SubscribeUserRepository extends JpaRepository<SubscribeUser, Long> {
    SubscribeUser findByUserId(String userId);
}