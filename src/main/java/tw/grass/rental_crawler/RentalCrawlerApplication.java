package tw.grass.rental_crawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class RentalCrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RentalCrawlerApplication.class, args);
	}

}
