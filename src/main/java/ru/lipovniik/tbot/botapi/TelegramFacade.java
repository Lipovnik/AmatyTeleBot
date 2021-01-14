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
        final long adminChatId = -465515338;

        long chatId = message.getChatId();
        BotApiMethod<?> replyMessage;

        if (chatId == adminChatId)
            replyMessage = getAdminReplyMessage(message);
        else
            replyMessage = getReplyMessage(message);

        return replyMessage;
    }

    private BotApiMethod<?> getReplyMessage(Message message) {
        BotState botState;
        BotApiMethod<?> replyMessage;
        String msgText = message.getText();
        int userId = message.getFrom().getId();

        if (userDataCache.getUsersCurrentBotState(userId).equals(BotState.WAITING_FOR_QUESTION)) {
            botState = BotState.SENDING_QUESTION;
        } else {
            botState = switch (msgText) {
                case "/start" -> BotState.MAIN_MENU;
                case "/update" -> BotState.UPDATE_CATS;
                case "О питомнике" -> BotState.ABOUT_US;
                case "Вопросы" -> BotState.FAQ;
                case "Где нас найти" -> BotState.LINKS;
                case "Задать вопрос" -> BotState.QUESTION;
                case "Отзывы" -> BotState.REVIEWS;
                case "Коты" -> BotState.ADULT_MALE_CATS;
                case "Кошки" -> BotState.ADULT_FEMALE_CATS;
                case "Свободные котята" -> BotState.MENU_FREE_KITTENS;
                default -> BotState.IGNORE_MESSAGE;
            };
        }

        if (botState.equals(BotState.IGNORE_MESSAGE))
            return null;

        userDataCache.setUsersCurrentBotState(userId, botState);
        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    private BotApiMethod<?> getAdminReplyMessage(Message message) {
        BotApiMethod<?> replyMessage = null;
        String msgText = message.getText();
        System.out.println(message);
        BotState botState = switch (msgText) {
            case "/update" -> BotState.UPDATE_CATS;
            default -> BotState.IGNORE_MESSAGE;
        };
        if (message.getReplyToMessage() != null)
            replyMessage = botStateContext.processInputMessage(BotState.SENDING_ANSWER, message);

        if (botState.equals(BotState.IGNORE_MESSAGE))
            return null;

        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

}
