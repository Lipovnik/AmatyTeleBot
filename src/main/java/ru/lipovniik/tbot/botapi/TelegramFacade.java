package ru.lipovniik.tbot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
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

    public TelegramFacade(BotStateContext botStateContext, UserDataCache userDataCache,
                          HandleCallbackQuery handleCallbackQuery) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
        this.handleCallbackQuery = handleCallbackQuery;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> replyMessage = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info("New callbackQuery from User:{}, userId:{}, with data:{}", callbackQuery.getFrom().getUserName(),
                    callbackQuery.getFrom().getId(), callbackQuery.getData());
            return handleCallbackQuery.processCallbackQuery(callbackQuery);
        }

        Message message = update.getMessage();

        if (message != null && message.hasPhoto()) {
            System.out.println(message.getPhoto());
            return null;
        }
        if (message != null && message.hasDocument()) {
            System.out.println(message.getDocument());
            return null;
        }

        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId:{}, text:{}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private BotApiMethod<?> handleInputMessage(Message message) {
        String msgText = message.getText();
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        BotApiMethod<?> replyMessage;
        BotState botState;

        if (message.getReplyToMessage() != null && chatId == -465515338){
            botState = BotState.SENDING_ANSWER;
            replyMessage = botStateContext.processInputMessage(botState, message);
            return replyMessage;
        }else if (userDataCache.getUsersCurrentBotState(userId).equals(BotState.WAITING_FOR_QUESTION)) {
            botState = BotState.SENDING_QUESTION;
        } else {
            botState = switch (msgText) {
                case "/start" -> BotState.MAIN_MENU;
                case "Об AmatyCay!" -> BotState.ABOUT_US;
                case "FAQ" -> BotState.FAQ;
                case "Где нас найти" -> BotState.LINKS;
                case "Задать вопрос!\uD83D\uDE40" -> BotState.QUESTION;
                case "Коты" -> BotState.ADULT_MALE_CATS;
                case "Кошки" -> BotState.ADULT_FEMALE_CATS;
                default -> BotState.IGNORE_MESSAGE;
            };
        }
        /*botState = switch (msgText) {
            case "/start" -> BotState.MAIN_MENU;
            case "Об AmatyCay!" -> BotState.ABOUT_US;
            case "FAQ" -> BotState.FAQ;
            case "Коты" -> BotState.IGNORE_MESSAGE;
            default -> BotState.IGNORE_MESSAGE;
        };*/

        if (botState.equals(BotState.IGNORE_MESSAGE))
            return null;

        userDataCache.setUsersCurrentBotState(userId, botState);

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

}
