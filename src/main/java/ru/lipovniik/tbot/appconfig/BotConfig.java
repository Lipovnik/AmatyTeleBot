package ru.lipovniik.tbot.appconfig;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import ru.lipovniik.tbot.AmatyTeleBot;
import ru.lipovniik.tbot.botapi.TelegramFacade;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    private DefaultBotOptions.ProxyType proxyType;
    private String proxyHost;
    private int proxyPort;

    @Bean
    public AmatyTeleBot amatyTeleBot(TelegramFacade telegramFacade){
        DefaultBotOptions options = new DefaultBotOptions();

        /*options.setProxyHost(proxyHost);
        options.setProxyPort(proxyPort);
        options.setProxyType(proxyType);*/

        AmatyTeleBot amatyTeleBot = new AmatyTeleBot(options, telegramFacade);
        amatyTeleBot.setBotUserName(botUserName);
        amatyTeleBot.setBotToken(botToken);
        amatyTeleBot.setWebHookPath(webHookPath);

        return amatyTeleBot;
    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
