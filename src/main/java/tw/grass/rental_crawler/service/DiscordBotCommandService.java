package tw.grass.rental_crawler.service;

import net.dv8tion.jda.api.EmbedBuilder;

public interface DiscordBotCommandService {

    /*
     * 說明指令: ex: !help
     */
    EmbedBuilder helpCommand();

    /*
     * 訂閱指令，建議使用者設定好後再訂閱: ex: !sub
     */
    EmbedBuilder subscribeCommand(String userID, String channel);

    /*
     * 解除訂閱指令: ex: !unsub
     */
    EmbedBuilder unsubscribeCommand(String userID);

    /*
     * 清楚使用者定義的條件: ex: !reset
     */
    EmbedBuilder resetCommand(String userID);

    /*
     * 用戶資訊顯示: ex: !info
     */
    EmbedBuilder showInfoCommand(String userID);

    /*
     * 指定房型，可指定多個，使用空白分隔，新的指令覆蓋舊的 ex: !sp_room 整層住家 獨立套房
     * 可輸入選項:不限,整層住家,獨立套房,分租套房,雅房,車位,其他
     */
    EmbedBuilder specifyRoomTypeCommand(String userID, String inputSting);

    /*
     * 指定價格區間，新的指令覆蓋舊的 ex: !sp_price 10000~20000
     * 說明10000至20000 含10000及20000
     */
    EmbedBuilder specifyPriceCommand(String userID, String inputSting);

    /*
     * 指定地址，可指定多個，使用空白分隔，新的指令覆蓋舊的
     * 可輸入選項:大安區,內湖區,士林區,文山區,北投區,中山區,信義區,松山區,萬華區,中正區,大同區,南港區
     *  ex: !sp_Address 內湖區 士林區
     * 說明:10000至20000 含10000及20000
     */
    EmbedBuilder specifyAddressCommand(String userID, String inputSting);

    /*
     * 指定樓層，新的指令覆蓋舊的  ex: !sp_floor 4
     * 說明:4樓
     */
    EmbedBuilder specifyFloorCommand(String userID, String inputSting);
    /*
     * 指定設備，新的指令覆蓋舊的，可指定多個，使用空白分隔 ex: !sp_item 冰箱 洗衣機
     * 說明:需含冰箱及洗衣機
     * 可輸入選項:冰箱,洗衣機,電視,冷氣,熱水器,床,衣櫃,第四台,網路,天然瓦斯,沙發,桌子,陽台,電梯,車位
     */
    EmbedBuilder specifyItemCommand(String userID, String inputSting);

    EmbedBuilder unKnowCommand();

}
