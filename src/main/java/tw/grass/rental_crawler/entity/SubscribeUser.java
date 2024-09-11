package tw.grass.rental_crawler.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.Data;
import net.dv8tion.jda.api.EmbedBuilder;

@Entity
@Data
public class SubscribeUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String channelId = "";
    private String userId;
    private Boolean isSubscribe = false;
    private Integer lowestPrice = 0;
    private Integer highestPrice = Integer.MAX_VALUE;
    private Integer lowestFloor = 0;
    private Integer highestFloor = Integer.MAX_VALUE;
    // 使用 @ElementCollection 儲存房型列表
    @ElementCollection(fetch = FetchType.EAGER) //關閉延遲加載
    @CollectionTable(name = "user_rooms", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "room_type")
    private Set<String> rooms = new HashSet<>();

    // 使用 @ElementCollection 儲存地址列表
    @ElementCollection(fetch = FetchType.EAGER) //關閉延遲加載
    @CollectionTable(name = "user_address", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "address")
    private Set<String> address = new HashSet<>();

    // 使用 @ElementCollection 儲存設備列表
    @ElementCollection(fetch = FetchType.EAGER) //關閉延遲加載
    @CollectionTable(name = "user_items", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "item_name")
    private Set<String> items = new HashSet<>();

    // 新增將訂閱資訊轉換為 EmbedBuilder 的方法
    public EmbedBuilder toEmbed() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("訂閱資訊");
        embedBuilder.setColor(0x00FF00); // 綠色

        // 設定基本資訊，避免ID洩漏，先不呈現用戶ID及頻道ID
        // embedBuilder.addField("用戶ID", userId, false);
        // embedBuilder.addField("頻道ID", channelId, false);
        embedBuilder.addField("訂閱狀態", isSubscribe != null && isSubscribe ? "已訂閱" : "未訂閱", false);

        // 處理價格區間
        String priceRange = (lowestPrice != null ? lowestPrice : "無下限") + " ~ "
                + (highestPrice != null ? highestPrice : "無上限");
        embedBuilder.addField("價格區間", priceRange, false);

        // 處理樓層區間
        String floorRange = (lowestFloor != null ? lowestFloor : "無下限") + " ~ "
                + (highestFloor != null ? highestFloor : "無上限");
        embedBuilder.addField("樓層區間", floorRange, false);

        // 處理房型
        embedBuilder.addField("房型條件", rooms.isEmpty() ? "無指定" : String.join(", ", rooms), false);

        // 處理地址
        embedBuilder.addField("搜尋地區", address.isEmpty() ? "無指定" : String.join(", ", address), false);

        // 處理設備要求
        embedBuilder.addField("設備要求", items.isEmpty() ? "無指定" : String.join(", ", items), false);

        return embedBuilder;
    }

}
