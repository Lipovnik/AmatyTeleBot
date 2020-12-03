package ru.lipovniik.tbot.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.lipovniik.tbot.AmatyTeleBot;

@RestController
public class WebHookController {
    private final AmatyTeleBot teleBot;

    public WebHookController(AmatyTeleBot teleBot) {
        this.teleBot = teleBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        return teleBot.onWebhookUpdateReceived(update);
    }
}
