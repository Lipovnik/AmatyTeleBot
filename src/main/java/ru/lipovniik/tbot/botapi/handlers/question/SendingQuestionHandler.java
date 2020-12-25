package ru.lipovniik.tbot.botapi.handlers.question;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;
import ru.lipovniik.tbot.cache.UserDataCache;
import ru.lipovniik.tbot.service.LocalMessageService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

@Component
@Slf4j
public class SendingQuestionHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final LocalMessageService localMessageService;

    public SendingQuestionHandler(UserDataCache userDataCache, LocalMessageService localMessageService) {
        this.userDataCache = userDataCache;
        this.localMessageService = localMessageService;
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

        userDataCache.setUsersCurrentBotState(userId, BotState.WAITING_IN_MAIN_MENU);

        sendMessage(fromChatId, localMessageService.getMessage("reply.question2"));

        return replyToUser;
    }

    private void sendMessage(long chatId, String text){
        String rec = String.format("https://api.telegram.org/bot884989390:AAHd6PLB6mq5OOzFsNkr_eW1yJyVM4J8N48/sendMessage?chat_id=%s&text=%s",
                chatId, text);
        try{
            URL url = new URL(rec);
            BufferedReader in = new BufferedReader(new InputStreamReader( url.openStream()));
            in.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SENDING_QUESTION;
    }
}
