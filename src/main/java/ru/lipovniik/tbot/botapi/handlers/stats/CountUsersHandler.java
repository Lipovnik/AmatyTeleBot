package ru.lipovniik.tbot.botapi.handlers.stats;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;
import ru.lipovniik.tbot.cache.UserDataCache;

@Component
public class CountUsersHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;

    public CountUsersHandler(UserDataCache userDataCache) {
        this.userDataCache = userDataCache;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMsg){
        String text = "Количество уникальных пользователей: " + userDataCache.getCountUsers();
        SendMessage replyToUser = new SendMessage(String.valueOf(inputMsg.getChatId()), text);
        userDataCache.setUsersCurrentBotState(inputMsg.getFrom().getId(), BotState.WAITING_IN_MAIN_MENU);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.USERS;
    }
}
