package tw.grass.rental_crawler.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class DiscordBotListener extends ListenerAdapter {

    Logger log = LoggerFactory.getLogger(ListenerAdapter.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        // 過濾掉機器人傳送的訊息
        if (event.getAuthor().isBot())
            return;
        String message = event.getMessage().getContentRaw();
        if (message.startsWith("!")) {
            log.info("收到指令: {}", message);
            
            String channelId = event.getChannel().getId();
            log.info("所在頻道為: {}", channelId);
            String userId = event.getAuthor().getId();
            log.info("下指令者為: {}", userId);
            // 這裡可以加入解析指令的邏輯
        }
    }

    @Override
    public void onGuildJoin(GuildJoinEvent event) {
        TextChannel defaultChannel = event.getGuild().getDefaultChannel().asTextChannel();
        if (defaultChannel != null) {
            String welcomeMessage = "大家好！我是小草租屋推播機器人，請使用 `!help` 查看我能幫助你做些什麼！";
            defaultChannel.sendMessage(welcomeMessage).queue();
        }
    }

}
