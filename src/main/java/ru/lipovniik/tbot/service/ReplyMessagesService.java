package ru.lipovniik.tbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Service
public class ReplyMessagesService {
    private LocalMessageService localMessageService;

    public ReplyMessagesService(LocalMessageService localMessageService) {
        this.localMessageService = localMessageService;
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage){
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), localMessageService.getMessage(replyMessage));
        return sendMessage;
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage, Object... args) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), localMessageService.getMessage(replyMessage, args));
        return sendMessage;
    }
}
