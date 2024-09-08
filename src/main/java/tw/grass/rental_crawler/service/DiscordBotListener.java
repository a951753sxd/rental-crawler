package tw.grass.rental_crawler.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

@Service
public class DiscordBotListener extends ListenerAdapter {

    @Autowired
    DiscordBotService discordBotService;

    @Autowired
    DiscordBotCommandService discordBotCommandService;

    Logger log = LoggerFactory.getLogger(ListenerAdapter.class);

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot())
            return;

        String message = event.getMessage().getContentRaw();
        if (message.startsWith("!")) {
            String channelId = event.getChannel().getId();
            log.info("所在頻道為: {}", channelId);
            String userId = event.getAuthor().getId();
            log.info("下指令者為: {}", userId);
            log.info("收到指令: {}", message);
            doBotCommand(message, channelId, userId);
        }
    }

    private void doBotCommand(String message, String channelId, String userId) {
        // 取出指令後的參數部分
        String[] messageParts = message.split("\\s+", 2);
        String command = messageParts[0]; // 取出指令名稱 (例如 !sp_room)
        Set<String> arguments = messageParts.length == 1 ? new HashSet<String>() : new HashSet<>(Arrays.asList(messageParts[1].split("\\s+"))); // 取出指令參數

        EmbedBuilder botResponeMessage;
        switch (command) {
            case "!help":
                botResponeMessage = discordBotCommandService.helpCommand();
                break;
            case "!sub":
                botResponeMessage = discordBotCommandService.subscribeCommand(userId, channelId);
                break;
            case "!unsub":
                botResponeMessage = discordBotCommandService.unsubscribeCommand(userId);
                break;
            case "!sp_room":
                botResponeMessage = discordBotCommandService.specifyRoomTypeCommand(userId, arguments);
                break;
            case "!sp_price":
                botResponeMessage = discordBotCommandService.specifyPriceCommand(userId, arguments);
                break;
            case "!sp_address":
                botResponeMessage = discordBotCommandService.specifyAddressCommand(userId, arguments);
                break;
            case "!sp_floor":
                botResponeMessage = discordBotCommandService.specifyFloorCommand(userId, arguments);
                break;
            case "!sp_item":
                botResponeMessage = discordBotCommandService.specifyItemCommand(userId, arguments);
                break;
            case "!info":
                botResponeMessage = discordBotCommandService.showInfoCommand(userId);
                break;
            case "!reset":
                botResponeMessage = discordBotCommandService.resetCommand(userId);
                break;
            default:
                botResponeMessage = discordBotCommandService.unKnowCommand();
                break;
        }
        discordBotService.sendMessage(channelId, botResponeMessage);
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
