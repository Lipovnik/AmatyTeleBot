package ru.lipovniik.tbot.botapi.handlers.question;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ForwardMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;
import ru.lipovniik.tbot.cache.UserDataCache;
import ru.lipovniik.tbot.service.ReplyMessagesService;

@Component
@Slf4j
public class SendingQuestionHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final ReplyMessagesService messagesService;

    public SendingQuestionHandler(UserDataCache userDataCache, ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMsg){
        long fromChatId = inputMsg.getChatId();
        //int messageId = inputMsg.getMessageId();
        long chatId = -465515338;
        int userId = inputMsg.getFrom().getId();
        String questionText = inputMsg.getText();
        String firstName = inputMsg.getFrom().getFirstName();
        String lastName = inputMsg.getFrom().getLastName();


        String text = fromChatId + " - id" +
                "\nfirstName: " + firstName +
                "\nlastName: " + lastName +
                "\nText:\n" + questionText;

        SendMessage replyToUser = new SendMessage();
        replyToUser.setChatId(String.valueOf(chatId));
        replyToUser.setText(text);

        //SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.question");
        userDataCache.setUsersCurrentBotState(userId, BotState.WAITING_IN_MAIN_MENU);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SENDING_QUESTION;
    }
}
