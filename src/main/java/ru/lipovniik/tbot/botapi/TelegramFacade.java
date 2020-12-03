package ru.lipovniik.tbot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.lipovniik.tbot.cache.UserDataCache;

@Component
@Slf4j
public class TelegramFacade {
    private final BotStateContext botStateContext;
    private final UserDataCache userDataCache;

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
    }

    public BotApiMethod<?> handleUpdate(Update update){
        SendMessage replyMessage = null;

        if (update.hasCallbackQuery()){
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User:{}, userId:{}, with data:{}", callbackQuery.getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), callbackQuery.getData());
            return processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId:{}, text:{}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message){
        String msgText = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

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

    private BotApiMethod<?> processCallbackQuery(CallbackQuery callbackQuery){
        return null;
    }
}
