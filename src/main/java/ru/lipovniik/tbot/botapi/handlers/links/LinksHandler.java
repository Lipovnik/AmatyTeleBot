package ru.lipovniik.tbot.botapi.handlers.links;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;
import ru.lipovniik.tbot.cache.UserDataCache;
import ru.lipovniik.tbot.service.InlineKeyboardService;
import ru.lipovniik.tbot.service.ReplyMessagesService;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class LinksHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final ReplyMessagesService messagesService;
    private final InlineKeyboardService keyboardService;

    public LinksHandler(UserDataCache userDataCache, ReplyMessagesService messagesService, InlineKeyboardService keyboardService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.keyboardService = keyboardService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMsg){
        long chatId = inputMsg.getChatId();
        int userId = inputMsg.getFrom().getId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.links");
        InlineKeyboardMarkup inlineKeyboard = keyboardService.getLinksInlineKeyboard(getKeyboardData(), 1);
        replyToUser.setReplyMarkup(inlineKeyboard);
        userDataCache.setUsersCurrentBotState(userId, BotState.WAITING_IN_MAIN_MENU);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.LINKS;
    }

    private Map<String, String> getKeyboardData(){
        Map<String, String> keyboardData = new LinkedHashMap<>();

        keyboardData.put("Instagram", "https://instagram.com/amatycay.cats");
        keyboardData.put("WhatsApp", "wa.me/79137004577");
        keyboardData.put("Наш сайт", "https://amatycay154.ru/");
        keyboardData.put("VK", "https://vk.com/amaty_cay_bengal_serval");

        return keyboardData;
    }
}
