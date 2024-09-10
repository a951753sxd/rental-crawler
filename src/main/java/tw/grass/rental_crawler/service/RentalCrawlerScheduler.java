package tw.grass.rental_crawler.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.EmbedBuilder;
import tw.grass.rental_crawler.entity.SubscribeUser;
import tw.grass.rental_crawler.model.RentalCatalogDTO;
import tw.grass.rental_crawler.model.RentalDetailDTO;
import tw.grass.rental_crawler.repositories.SubscribeUserRepository;

@Service
public class RentalCrawlerScheduler {

    @Autowired
    RentalCrawlerService rentalCrawlerService;

    @Autowired
    SubscribeUserRepository subscribeUserRepository;

    @Autowired
    DiscordBotService discordBotService;

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
        List<RentalDetailDTO> rentalDetailDTOList = list.stream()
                .map(obj -> rentalCrawlerService.fetchRentalDetail(obj)).collect(Collectors.toList());

        // 從資料庫獲取當前用戶的訂閱條件 (假設你有一個方法能獲取用戶條件)
        List<SubscribeUser> allSubscribeUser = subscribeUserRepository.findAll();

        allSubscribeUser.stream().forEach(subscribeUser -> {
            // 根據條件過濾符合的租屋資料
            List<RentalDetailDTO> filteredList = rentalDetailDTOList.stream()
                    .filter(detail -> filterBySubscribe(detail, subscribeUser))
                    .filter(detail -> filterByPrice(detail, subscribeUser))
                    .filter(detail -> filterByFloor(detail, subscribeUser))
                    .filter(detail -> filterByRooms(detail, subscribeUser))
                    .filter(detail -> filterByItems(detail, subscribeUser))
                    .filter(detail -> filterByAddress(detail, subscribeUser))
                    .collect(Collectors.toList());

            // 組成 EmbedBuilder
            filteredList.forEach(detail -> {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("租屋資訊");
                embedBuilder.setDescription(detail.getInfo());

                // 將 EmbedBuilder 傳送到 Discord 頻道
                discordBotService.tagUserAndSendMessage(subscribeUser.getChannelId(),subscribeUser.getUserId(), embedBuilder);
            });
        });

    }

    // 以下是一些過濾方法的實現
    private boolean filterBySubscribe(RentalDetailDTO detail, SubscribeUser subscribeUser) {
        return subscribeUser.getIsSubscribe() == true;
    }

    private boolean filterByPrice(RentalDetailDTO detail, SubscribeUser subscribeUser) {
        int price = Integer.parseInt(detail.getPrice().replaceAll(",", ""));
        return price >= subscribeUser.getLowestPrice() && price <= subscribeUser.getHighestPrice();
    }

    private boolean filterByFloor(RentalDetailDTO detail, SubscribeUser subscribeUser) {
        // String[] floorAndAreaParts = detail.getFloorAndArea().split(" "); //
        // 假設這是樓層的格式
        // int floor = Integer.parseInt(floorAndAreaParts[0]); // 提取樓層
        // return floor >= subscribeUser.getLowestFloor() && floor <=
        // subscribeUser.getHighestFloor();
        return true; // TODO: 樓層邏輯有問題
    }

    private boolean filterByRooms(RentalDetailDTO detail, SubscribeUser subscribeUser) {
        // 假設 detail 中有方法能判斷房型是否匹配
        if (subscribeUser.getRooms().size() == 0) {
            return true;
        }

        return subscribeUser.getRooms().contains(detail.getRoomType());
    }

    private boolean filterByItems(RentalDetailDTO detail, SubscribeUser subscribeUser) {
        // 比對設備是否符合用戶需求
        Set<String> requiredItems = subscribeUser.getItems();
        if (requiredItems.contains("冰箱") && !detail.isHasFridge())
            return false;
        if (requiredItems.contains("洗衣機") && !detail.isHasWasher())
            return false;
        if (requiredItems.contains("電視") && !detail.isHasTv())
            return false;
        if (requiredItems.contains("冷氣") && !detail.isHasCold())
            return false;
        if (requiredItems.contains("熱水器") && !detail.isHasHeater())
            return false;
        if (requiredItems.contains("床") && !detail.isHasBed())
            return false;
        if (requiredItems.contains("衣櫃") && !detail.isHasCloset())
            return false;
        if (requiredItems.contains("第四台") && !detail.isHasFourth())
            return false;
        if (requiredItems.contains("網路") && !detail.isHasWifi())
            return false;
        if (requiredItems.contains("天然瓦斯") && !detail.isHasGas())
            return false;
        if (requiredItems.contains("沙發") && !detail.isHasSofa())
            return false;
        if (requiredItems.contains("桌子") && !detail.isHasTable())
            return false;
        if (requiredItems.contains("陽台") && !detail.isHasBalcony())
            return false;
        if (requiredItems.contains("電梯") && !detail.isHasLift())
            return false;
        if (requiredItems.contains("車位") && !detail.isHasParking())
            return false;
        return true;
    }

    private boolean filterByAddress(RentalDetailDTO detail, SubscribeUser subscribeUser) {
        // 比對地址是否符合用戶訂閱的地區
        if (subscribeUser.getAddress().size() == 0) {
            return true;
        }
        return subscribeUser.getAddress().stream()
                .anyMatch(address -> detail.getAddress().contains(address));
    }
}