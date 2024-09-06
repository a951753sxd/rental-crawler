package tw.grass.rental_crawler.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.dv8tion.jda.api.EmbedBuilder;
import tw.grass.rental_crawler.service.DiscordBotCommandService;
import tw.grass.rental_crawler.service.DiscordBotService;

@Service
public class DiscordBotCommandServiceImpl implements DiscordBotCommandService {

    @Autowired
    DiscordBotService discordBotService;

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
        embedBuilder.addField("`!sp_address`", "指定搜尋地區或地址。可選項: 大安區,內湖區,士林區,文山區,北投區,中山區,信義區,松山區,萬華區,中正區,大同區,南港區, 等。範例: `!sp_address 內湖區`", false);
        embedBuilder.addField("`!sp_floor`", "指定樓層。範例: `!sp_floor 4`", false);
        embedBuilder.addField("`!sp_item`", "指定房屋設備要求。可選項: 冰箱,洗衣機,電視,冷氣,熱水器,床,衣櫃,第四台,網路,天然瓦斯,沙發,桌子,陽台,電梯,車位。範例: `!sp_item 冰箱 洗衣機`", false);

        // 設定訊息顏色及發送
        embedBuilder.setColor(0x00FF00); // 綠色

        return embedBuilder;
    }

    @Override
    public String subscribeCommand(String userID, String channel) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'subscribeCommand'");
    }

    @Override
    public String unsubscribeCommand(String userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'unsubscribeCommand'");
    }

    @Override
    public String resetCommand(String userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetCommand'");
    }

    @Override
    public String showInfoCommand(String userID) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'showInfoCommand'");
    }

    @Override
    public String specifyRoomTypeCommand(String userID, String inputSting) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'specifyRoomTypeCommand'");
    }

    @Override
    public String specifyPriceCommand(String userID, String inputSting) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'specifyPriceCommand'");
    }

    @Override
    public String specifyAddressCommand(String userID, String inputSting) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'specifyAddressCommand'");
    }

    @Override
    public String specifyFloorCommand(String userID, String inputSting) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'specifyFloorCommand'");
    }

    @Override
    public String specifyItemCommand(String userID, String inputSting) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'specifyItemCommand'");
    }

}
