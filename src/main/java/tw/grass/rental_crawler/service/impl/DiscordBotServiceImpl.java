package tw.grass.rental_crawler.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.requests.GatewayIntent;
import tw.grass.rental_crawler.listener.DiscordBotListener;
import tw.grass.rental_crawler.service.DiscordBotService;

@Service
public class DiscordBotServiceImpl implements DiscordBotService {

    @Value("${discord.token}")
    private String discordToken;

    Logger log = LoggerFactory.getLogger(DiscordBotServiceImpl.class);

    JDA jda;

    @PostConstruct
    public void init() {
        JDABuilder builder = JDABuilder.createDefault(discordToken);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.addEventListeners(new DiscordBotListener());
        jda = builder.build();
        log.info("Discord bot creation completed.");
    }

    @Override
    public String sendMessage(String channelId, String message) {
        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel != null) {
            channel.sendMessage(message).queue();
        }
        return "success";
    }

    @Override
    public String tagUserAndSendMessage(String channelId, String userId, String message) {
        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel != null) {
            String taggedMessage = "<@" + userId + "> " + message;
            channel.sendMessage(taggedMessage).queue();
        }
        return "success";
    }

}
