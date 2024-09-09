package tw.grass.rental_crawler.service.impl;

import java.util.Objects;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.dv8tion.jda.api.EmbedBuilder;
import tw.grass.rental_crawler.entity.SubscribeUser;
import tw.grass.rental_crawler.repositories.SubscribeUserRepository;
import tw.grass.rental_crawler.service.DiscordBotCommandService;
import tw.grass.rental_crawler.service.DiscordBotService;

@Service
public class DiscordBotCommandServiceImpl implements DiscordBotCommandService {

    @Autowired
    DiscordBotService discordBotService;

    @Autowired
    SubscribeUserRepository subscribeUserRepository;

    @Override
    public EmbedBuilder helpCommand() {

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Bot 指令集");
        embedBuilder.setDescription("以下是本 Bot 支援的所有指令:");

        // 添加指令詳細說明
        embedBuilder.addField("`!help`", "顯示所有可用指令的詳細說明。", false);
        embedBuilder.addField("`!sub`", "訂閱租屋資訊推播，當符合條件的租屋資訊出現時，通知使用者。範例: `!sub`", false);
        embedBuilder.addField("`!unsub`", "解除訂閱租屋資訊推播。範例: `!unsub`", false);
        embedBuilder.addField("`!reset`", "清除使用者目前設定的所有搜尋條件。範例: `!reset`", false);
        embedBuilder.addField("`!info`", "顯示使用者目前設定的條件，包括訂閱的房型、價格範圍、地區等。範例: `!info`", false);
        embedBuilder.addField("`!sp_room`", "指定房型條件。可選項: 不限, 整層住家, 獨立套房, 分租套房, 雅房, 車位, 其他。範例: `!sp_room 整層住家 獨立套房`",
                false);
        embedBuilder.addField("`!sp_price`", "指定價格區間，格式為 最低價格~最高價格。範例: `!sp_price 10000~20000`", false);
        embedBuilder.addField("`!sp_address`",
                "指定搜尋地區或地址。可選項: 大安區,內湖區,士林區,文山區,北投區,中山區,信義區,松山區,萬華區,中正區,大同區,南港區, 等。範例: `!sp_address 內湖區`", false);
        embedBuilder.addField("`!sp_floor`", "指定樓層區間。範例: `!sp_floor 4~6`", false);
        embedBuilder.addField("`!sp_item`",
                "指定房屋設備要求。可選項: 冰箱,洗衣機,電視,冷氣,熱水器,床,衣櫃,第四台,網路,天然瓦斯,沙發,桌子,陽台,電梯,車位。範例: `!sp_item 冰箱 洗衣機`", false);

        // 設定訊息顏色及發送
        embedBuilder.setColor(0x00FF00); // 綠色

        return embedBuilder;
    }

    @Override
    public EmbedBuilder subscribeCommand(String userID, String channel) {

        // 目前沒有多個訂閱的機制，每次更新都覆蓋掉之前的資料
        SubscribeUser subscribeUser = getOrNewSubscribeUser(userID, channel);
        subscribeUser.setIsSubscribe(true);
        subscribeUserRepository.save(subscribeUser);

        return stringToEmbed("訂閱成功");
    }

    @Override
    public EmbedBuilder unsubscribeCommand(String userID) {

        SubscribeUser subscribeUser = getOrNewSubscribeUser(userID, "");
        subscribeUser.setIsSubscribe(false);
        subscribeUserRepository.save(subscribeUser);

        return stringToEmbed("解除訂閱成功");
    }

    @Override
    public EmbedBuilder showInfoCommand(String userID) {
        SubscribeUser subscribeUser = subscribeUserRepository.findByUserId(userID);

        return subscribeUser.toEmbed();
    }

    private SubscribeUser getOrNewSubscribeUser(String userID, String channel) {
        SubscribeUser subscribeUser = subscribeUserRepository.findByUserId(userID);
        if (Objects.isNull(subscribeUser)) {
            subscribeUser = new SubscribeUser();
            subscribeUser.setUserId(userID);
        }
        subscribeUser.setChannelId(channel);
        return subscribeUser;
    }

    private EmbedBuilder stringToEmbed(String string) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(string);
        return embedBuilder;
    }

    @Override
    public EmbedBuilder resetCommand(String userID) {
        // 獲取用戶資料，若不存在則新建一個空的訂閱用戶
        SubscribeUser subscribeUser = getOrNewSubscribeUser(userID, "");

        // 訂閱狀態也解除
        subscribeUser.setIsSubscribe(false);
        // 清空用戶的所有設定
        subscribeUser.getRooms().clear();// 清空房型
        subscribeUser.getAddress().clear();// 清空地址
        subscribeUser.getItems().clear();// 清空設備
        subscribeUser.setLowestPrice(0); // 清空最低價格
        subscribeUser.setHighestPrice(Integer.MAX_VALUE); // 清空最高價格
        subscribeUser.setLowestFloor(0); // 清空最低樓層
        subscribeUser.setHighestFloor(Integer.MAX_VALUE); // 清空最高樓層

        // 儲存變更到資料庫
        subscribeUserRepository.save(subscribeUser);

        // 返回重置成功的回應
        return stringToEmbed("所有租屋條件已重置成功！");
    }

    @Override
    public EmbedBuilder specifyPriceCommand(String userID, Set<String> arguments) {
        SubscribeUser subscribeUser = getOrNewSubscribeUser(userID, "");
        String argumentString = arguments.iterator().next();
        String[] messageParts = argumentString.split("~", 2);
        subscribeUser.setLowestPrice(Integer.valueOf(messageParts[0]));
        subscribeUser.setHighestPrice(Integer.valueOf(messageParts[1]));
        subscribeUserRepository.save(subscribeUser);
        return stringToEmbed("價格區間新增成功");
    }

    @Override
    public EmbedBuilder specifyFloorCommand(String userID, Set<String> arguments) {
        SubscribeUser subscribeUser = getOrNewSubscribeUser(userID, "");
        String argumentString = arguments.iterator().next();
        String[] messageParts = argumentString.split("~", 2);
        subscribeUser.setLowestFloor(Integer.valueOf(messageParts[0]));
        subscribeUser.setHighestFloor(Integer.valueOf(messageParts[1]));
        subscribeUserRepository.save(subscribeUser);
        return stringToEmbed("樓層區間新增成功");
    }

    @Override
    public EmbedBuilder specifyRoomTypeCommand(String userID, Set<String> arguments) {
        SubscribeUser subscribeUser = getOrNewSubscribeUser(userID, "");
        subscribeUser.setRooms(arguments);
        subscribeUserRepository.save(subscribeUser);

        return stringToEmbed("房型類別新增成功");
    }

    @Override
    public EmbedBuilder specifyAddressCommand(String userID, Set<String> arguments) {
        SubscribeUser subscribeUser = getOrNewSubscribeUser(userID, "");
        subscribeUser.setAddress(arguments);
        subscribeUserRepository.save(subscribeUser);

        return stringToEmbed("地址、地區新增成功");
    }

    @Override
    public EmbedBuilder specifyItemCommand(String userID, Set<String> arguments) {
        SubscribeUser subscribeUser = getOrNewSubscribeUser(userID, "");
        subscribeUser.setItems(arguments);
        subscribeUserRepository.save(subscribeUser);

        return stringToEmbed("設備新增成功");
    }

    @Override
    public EmbedBuilder unKnowCommand() {
        EmbedBuilder embedBuilder = stringToEmbed("未知的指令，請檢查指令後重新輸入，也可輸入`!help`確認指令。");
        embedBuilder.setColor(0xFF0000); // 紅色
        return embedBuilder;
    }

}
