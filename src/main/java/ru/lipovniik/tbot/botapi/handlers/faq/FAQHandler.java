package ru.lipovniik.tbot.botapi.handlers.faq;

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

@Slf4j
@Component
public class FAQHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private InlineKeyboardService inlineKeyboardService;

    public FAQHandler(UserDataCache userDataCache, ReplyMessagesService messagesService, InlineKeyboardService inlineKeyboardService) {
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

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.faqTitle");
        InlineKeyboardMarkup inlineKeyboard = inlineKeyboardService.getInlineKeyboard(getKeyboardData(), 2);
        replyToUser.setReplyMarkup(inlineKeyboard);
        userDataCache.setUsersCurrentBotState(userId, BotState.WAITING_IN_MAIN_MENU);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FAQ;
    }

    private Map<String, String> getKeyboardData(){
        Map<String, String> keyboardData = new LinkedHashMap<>();

        keyboardData.put("Основные вопросы", "faq1");
        keyboardData.put("Про покупку котят", "faq2");
        keyboardData.put("Про содержание котят", "faq3");
        keyboardData.put("Прочие вопросы", "faq4");

        return keyboardData;
    }
}
