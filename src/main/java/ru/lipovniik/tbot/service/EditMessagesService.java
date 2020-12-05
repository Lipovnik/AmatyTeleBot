package ru.lipovniik.tbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;

@Service
public class EditMessagesService {
    private LocalMessageService localMessageService;

    public EditMessagesService(LocalMessageService localMessageService) {
        this.localMessageService = localMessageService;
    }

    public EditMessageText getEditMessage(long chatId, int messageId, String replyMessage){
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(chatId));
        editMessage.setMessageId(messageId);
        editMessage.setText(localMessageService.getMessage(replyMessage));
        return editMessage;
    }

    /*public SendMessage getEditMessage(long chatId, String replyMessage, Object... args) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), localMessageService.getMessage(replyMessage, args));
        return sendMessage;
    }*/
}
