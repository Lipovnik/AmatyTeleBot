package ru.lipovniik.tbot.botapi.handlers.updatecats;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;
import ru.lipovniik.tbot.cache.CatsDataCache;
import ru.lipovniik.tbot.cache.UserDataCache;
import ru.lipovniik.tbot.service.ReplyMessagesService;

@Slf4j
@Component
public class UpdateCatsHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final ReplyMessagesService messagesService;
    private final CatsDataCache catsDataCache;

    public UpdateCatsHandler(UserDataCache userDataCache, ReplyMessagesService messagesService, CatsDataCache catsDataCache) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.catsDataCache = catsDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMsg){
        long chatId = inputMsg.getChatId();
        int userId = inputMsg.getFrom().getId();
        if (chatId == (-465515338)) {
            catsDataCache.updateCatsMaps();
            SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.update");
            userDataCache.setUsersCurrentBotState(userId, BotState.WAITING_IN_MAIN_MENU);
            return replyToUser;
        }

        userDataCache.setUsersCurrentBotState(userId, BotState.WAITING_IN_MAIN_MENU);
        return null;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.UPDATE_CATS;
    }
}
