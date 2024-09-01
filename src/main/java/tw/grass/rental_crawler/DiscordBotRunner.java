package tw.grass.rental_crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

@Component
public class DiscordBotRunner implements ApplicationRunner {

    @Value("${discord.token}")
    private String discordToken;
    
    Logger log = LoggerFactory.getLogger(DiscordBotRunner.class);

    @Override
    public void run(ApplicationArguments args) throws Exception {

        JDABuilder builder = JDABuilder.createDefault(discordToken);
        JDA jda = builder.build();
        log.info("Discord bot creation completed.");
    }

}
