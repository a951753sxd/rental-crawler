package tw.grass.rental_crawler.model;

import lombok.Data;

@Data
public class RentalDetail {
    String rentalDescription;
    String houseRules;
    String address;
    String price;
    String floorAndArea;
    boolean hasFridge;
    boolean hasWasher;
    boolean hasTv;
    boolean hasCold;
    boolean hasHeater;
    boolean hasBed;
    boolean hasCloset;
    boolean hasFourth;
    boolean hasWifi;
    boolean hasGas;
    boolean hasSofa;
    boolean hasTable;
    boolean hasBalcony;
    boolean hasLift;
    boolean hasParking;

    public String getInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Rental Description: " + rentalDescription);
        sb.append("\n");
        sb.append("House Rules: " + houseRules);
        sb.append("\n");
        sb.append("冰箱:" + (hasFridge ? "有" : "無"));
        sb.append("\n");
        sb.append("洗衣機:" + (hasWasher ? "有" : "無"));
        sb.append("\n");
        sb.append("電視:" + (hasTv ? "有" : "無"));
        sb.append("\n");
        sb.append("冷氣:" + (hasCold ? "有" : "無"));
        sb.append("\n");
        sb.append("熱水器:" + (hasHeater ? "有" : "無"));
        sb.append("\n");
        sb.append("床:" + (hasBed ? "有" : "無"));
        sb.append("\n");
        sb.append("衣櫃:" + (hasCloset ? "有" : "無"));
        sb.append("\n");
        sb.append("第四台:" + (hasFourth ? "有" : "無"));
        sb.append("\n");
        sb.append("網路:" + (hasWifi ? "有" : "無"));
        sb.append("\n");
        sb.append("天然瓦斯:" + (hasGas ? "有" : "無"));
        sb.append("\n");
        sb.append("沙發:" + (hasSofa ? "有" : "無"));
        sb.append("\n");
        sb.append("桌子:" + (hasTable ? "有" : "無"));
        sb.append("\n");
        sb.append("陽台:" + (hasBalcony ? "有" : "無"));
        sb.append("\n");
        sb.append("電梯:" + (hasLift ? "有" : "無"));
        sb.append("\n");
        sb.append("車位:" + (hasParking ? "有" : "無"));
        sb.append("\n");
        return sb.toString();
    }
}
