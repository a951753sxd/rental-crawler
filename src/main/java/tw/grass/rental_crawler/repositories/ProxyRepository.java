package tw.grass.rental_crawler.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.grass.rental_crawler.entity.Proxy;

public interface ProxyRepository extends JpaRepository<Proxy, Long> {
}