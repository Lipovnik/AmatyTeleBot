package ru.lipovniik.tbot.botapi.handlers.faq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
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
public class FAQCategoriesHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final InlineKeyboardService inlineKeyboardService;
    private final EditMessagesService messagesService;

    public FAQCategoriesHandler(UserDataCache userDataCache, EditMessagesService messagesService, InlineKeyboardService inlineKeyboardService) {
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

        EditMessageText replyToUser = messagesService.getEditMessage(chatId, messageId, inputMsg.getText());
        InlineKeyboardMarkup inlineKeyboard = inlineKeyboardService.getInlineKeyboard(getKeyboardData());
        replyToUser.setReplyMarkup(inlineKeyboard);
        replyToUser.enableHtml(true);
        userDataCache.setUsersCurrentBotState(userId, BotState.WAITING_IN_MAIN_MENU);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.FAQ_CATEGORY;
    }

    private Map<String, String> getKeyboardData() {
        Map<String, String> keyboardData = new LinkedHashMap<>();

        keyboardData.put("Назад", "faq");

        return keyboardData;
    }
}
