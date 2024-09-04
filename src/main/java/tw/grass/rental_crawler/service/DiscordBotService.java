package tw.grass.rental_crawler.service;

public interface DiscordBotService {
    String sendMessage(String channelId, String message);
    String tagUserAndSendMessage(String channelId, String userId, String message);
}