package ru.lipovniik.tbot.botapi.handlers.faq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;
import ru.lipovniik.tbot.cache.UserDataCache;
import ru.lipovniik.tbot.service.EditMessagesService;
import ru.lipovniik.tbot.service.InlineKeyboardService;

import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Component
public class BackFAQHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private InlineKeyboardService inlineKeyboardService;
    private EditMessagesService messagesService;

    public BackFAQHandler(UserDataCache userDataCache, EditMessagesService messagesService, InlineKeyboardService inlineKeyboardService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.inlineKeyboardService = inlineKeyboardService;
    }

    @Override
    public EditMessageText handle(Message message) {
        return processUsersInput(message);
    }

    private EditMessageText processUsersInput(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        int userId = inputMsg.getFrom().getId();
        int messageId = inputMsg.getMessageId();

        EditMessageText replyToUser = messagesService.getEditMessage(chatId, messageId, "reply.faqTitle");
        InlineKeyboardMarkup inlineKeyboard = inlineKeyboardService.getInlineKeyboard(getKeyboardData(), 2);
        replyToUser.setReplyMarkup(inlineKeyboard);
        replyToUser.enableHtml(true);
        userDataCache.setUsersCurrentBotState(userId, BotState.WAITING_IN_MAIN_MENU);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.BACK_FAQ;
    }

    private Map<String, String> getKeyboardData() {
        Map<String, String> keyboardData = new LinkedHashMap<>();

        keyboardData.put("Категория 1", "data1");
        keyboardData.put("Категория 2", "data2");
        keyboardData.put("Категория 3", "data3");

        return keyboardData;
    }
}
