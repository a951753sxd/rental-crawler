package tw.grass.rental_crawler.service;

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
        EmbedBuilder botResponeMessage;
        if (message.startsWith("!help")) {
            botResponeMessage = discordBotCommandService.helpCommand();
        } else if (message.startsWith("!sub")) {
            botResponeMessage = discordBotCommandService.subscribeCommand(userId, channelId);
        } else if (message.startsWith("!unsub")) {
            botResponeMessage = discordBotCommandService.unsubscribeCommand(userId);
        } else if (message.startsWith("!info")) {
            botResponeMessage = discordBotCommandService.showInfoCommand(userId);
        } else {
            botResponeMessage = discordBotCommandService.unKnowCommand();
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
