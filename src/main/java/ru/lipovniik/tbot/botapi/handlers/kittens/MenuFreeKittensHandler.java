package ru.lipovniik.tbot.botapi.handlers.kittens;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;
import ru.lipovniik.tbot.botapi.parsers.amatycay.AmatyCayParser;
import ru.lipovniik.tbot.cache.CatsDataCache;
import ru.lipovniik.tbot.cache.UserDataCache;
import ru.lipovniik.tbot.service.InlineKeyboardService;
import ru.lipovniik.tbot.service.ReplyMessagesService;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class MenuFreeKittensHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final CatsDataCache catsDataCache;
    private final ReplyMessagesService messagesService;
    private final InlineKeyboardService inlineKeyboardService;

    MenuFreeKittensHandler(UserDataCache userDataCache, CatsDataCache catsDataCache, ReplyMessagesService messagesService, InlineKeyboardService inlineKeyboardService) {
        this.userDataCache = userDataCache;
        this.catsDataCache = catsDataCache;
        this.messagesService = messagesService;
        this.inlineKeyboardService = inlineKeyboardService;
    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        long chatId = message.getChatId();
        int userId = message.getFrom().getId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.kittens");
        InlineKeyboardMarkup inlineKeyboard = inlineKeyboardService.getInlineKeyboard(getKeyboardData(), 2);
        replyToUser.setReplyMarkup(inlineKeyboard);
        userDataCache.setUsersCurrentBotState(userId, BotState.WAITING_IN_MAIN_MENU);

        return replyToUser;
    }

    private Map<String, String> getKeyboardData() {
        Map<String, String> keyboard = new LinkedHashMap<>();
        Map<String, Map<String, String>> kittens = catsDataCache.getKittens();
        for (Map.Entry<String, Map<String, String>> db: kittens.entrySet()){
            keyboard.put(db.getKey(), db.getKey());
        }
        return keyboard;
    }


    @Override
    public BotState getHandlerName() {
        return BotState.MENU_FREE_KITTENS;
    }
}
