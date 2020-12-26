package ru.lipovniik.tbot.botapi.handlers.reviews;

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
public class ReviewsHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final ReplyMessagesService messagesService;
    private final InlineKeyboardService keyboardService;

    public ReviewsHandler(UserDataCache userDataCache, ReplyMessagesService messagesService, InlineKeyboardService keyboardService) {
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

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.reviews");
        InlineKeyboardMarkup inlineKeyboard = keyboardService.getLinksInlineKeyboard(getKeyboardData(), 1);
        replyToUser.setReplyMarkup(inlineKeyboard);
        userDataCache.setUsersCurrentBotState(userId, BotState.WAITING_IN_MAIN_MENU);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.REVIEWS;
    }

    private Map<String, String> getKeyboardData(){
        Map<String, String> keyboardData = new LinkedHashMap<>();

        keyboardData.put("2GIS", "https://2gis.ru/novosibirsk/firm/70000001019659383/tab/reviews");
        keyboardData.put("Flamp", "https://novosibirsk.flamp.ru/firm/amaty_cay_pitomnik_servalov_i_bengalskikh_koshek-70000001019659383#reviews");
        keyboardData.put("Отзовик", "https://otzovik.com/reviews/pitomnik_bengalskih_koshek_i_servalov_amaty_cay_russia_novosibirsk/");

        return keyboardData;
    }
}
