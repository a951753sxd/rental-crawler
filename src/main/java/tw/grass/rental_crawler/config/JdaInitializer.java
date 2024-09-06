package tw.grass.rental_crawler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import tw.grass.rental_crawler.service.DiscordBotListener;

@Configuration
public class JdaInitializer {

    @Value("${discord.token}")
    private String discordToken;

    @Autowired
    private DiscordBotListener discordBotListener;

    @PostConstruct
    public void init() throws Exception {
        JDABuilder builder = JDABuilder.createDefault(discordToken);
        builder.enableIntents(GatewayIntent.MESSAGE_CONTENT);
        builder.addEventListeners(discordBotListener);
        builder.build();
    }

}