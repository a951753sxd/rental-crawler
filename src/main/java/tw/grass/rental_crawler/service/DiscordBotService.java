package tw.grass.rental_crawler.service;

import net.dv8tion.jda.api.EmbedBuilder;

public interface DiscordBotService {
    String sendMessage(String channelId, String message);
    String tagUserAndSendMessage(String channelId, String userId, String message);
    String sendMessage(String channelId, EmbedBuilder embed);
    String tagUserAndSendMessage(String channelId, String userId, EmbedBuilder embed);
}