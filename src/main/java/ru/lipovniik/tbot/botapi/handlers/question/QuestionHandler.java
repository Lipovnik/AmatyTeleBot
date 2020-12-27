package ru.lipovniik.tbot.botapi.handlers.question;

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
public class QuestionHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final ReplyMessagesService messagesService;
    private final InlineKeyboardService inlineKeyboardService;

    public QuestionHandler(UserDataCache userDataCache, ReplyMessagesService messagesService, InlineKeyboardService inlineKeyboardService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.inlineKeyboardService = inlineKeyboardService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMsg){
        long chatId = inputMsg.getChatId();
        int userId = inputMsg.getFrom().getId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.question1");
        InlineKeyboardMarkup inlineKeyboardMarkup = inlineKeyboardService.getInlineKeyboard(getKeyboardData());
        replyToUser.setReplyMarkup(inlineKeyboardMarkup);
        userDataCache.setUsersCurrentBotState(userId, BotState.WAITING_FOR_QUESTION);

        return replyToUser;
    }

    private Map<String, String> getKeyboardData(){
        Map<String, String> map = new LinkedHashMap<>();

        map.put("Отмена", "cancelQuestion");

        return map;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.QUESTION;
    }
}
