package tw.grass.rental_crawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import tw.grass.rental_crawler.service.DiscordBotService;

@Component
public class DiscordBotRunner implements ApplicationRunner {

    @Autowired
    DiscordBotService discordBotService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        discordBotService.sendMessage("你的Channel Id", "使用Service服務發送訊息。");
    }

}
