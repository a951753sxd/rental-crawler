package tw.grass.rental_crawler.model;

import lombok.Data;

@Data
public class RentalCatalogDTO {
    private String title;
    private String link;
    private String address;
    private String price;
    private String roomType;
    private String floorAndArea;
    private String distanceToMRT;
}