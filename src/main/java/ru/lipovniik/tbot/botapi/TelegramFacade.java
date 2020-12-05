package ru.lipovniik.tbot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.lipovniik.tbot.cache.UserDataCache;

@Component
@Slf4j
public class TelegramFacade {
    private final BotStateContext botStateContext;
    private final UserDataCache userDataCache;
    private final HandleCallbackQuery handleCallbackQuery;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache, HandleCallbackQuery handleCallbackQuery) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.handleCallbackQuery = handleCallbackQuery;
    }

    public BotApiMethod<?> handleUpdate(Update update){
        BotApiMethod<?> replyMessage = null;

        if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User:{}, userId:{}, with data:{}", callbackQuery.getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), callbackQuery.getData());
            return handleCallbackQuery.processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId:{}, text:{}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private BotApiMethod<?> handleInputMessage(Message message){
        String msgText = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        BotApiMethod<?> replyMessage;

        botState = switch (msgText) {
            case "/start" -> BotState.MAIN_MENU;
            case "Об AmatyCay!" -> BotState.ABOUT_US;
            case "FAQ" -> BotState.FAQ;
            default -> BotState.IGNORE_MESSAGE;
        };

        if (botState.equals(BotState.IGNORE_MESSAGE))
            return null;

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    /*private BotApiMethod<?> p(CallbackQuery callbackQuery){
        Message inputMsg = callbackQuery.getMessage();
        long chatId = inputMsg.getChatId();
        int userId = inputMsg.getFrom().getId();
        int messageId = inputMsg.getMessageId();
        EditMessageText emt = new EditMessageText();
        emt.setMessageId(messageId);
        emt.setChatId(String.valueOf(chatId));
        emt.setText("хахаах");
        return emt;
    }*/
}
