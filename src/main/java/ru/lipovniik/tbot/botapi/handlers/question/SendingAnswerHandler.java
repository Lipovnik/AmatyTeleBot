package ru.lipovniik.tbot.botapi.handlers.question;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;


@Component
@Slf4j
public class SendingAnswerHandler implements InputMessageHandler {

    public SendingAnswerHandler() {
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMsg){
        SendMessage replyToUser = new SendMessage();
        String infoText = inputMsg.getReplyToMessage().getText();
        try {
            String chatIdStr = infoText.substring(0, infoText.indexOf(' '));
            long chatId = Long.parseLong(chatIdStr);
            String text = inputMsg.getText();
            replyToUser.setChatId(String.valueOf(chatId));
            replyToUser.setText(text);
        }catch (Exception e){
            e.printStackTrace();
            replyToUser.setChatId(String.valueOf(inputMsg.getChatId()));
            replyToUser.setText("Упс! Произошла ошибочка:(");
        }

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SENDING_ANSWER;
    }
}
