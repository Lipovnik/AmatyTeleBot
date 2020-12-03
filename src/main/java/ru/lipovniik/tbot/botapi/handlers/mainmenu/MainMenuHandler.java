package ru.lipovniik.tbot.botapi.handlers.mainmenu;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;
import ru.lipovniik.tbot.cache.UserDataCache;
import ru.lipovniik.tbot.service.MainMenuService;
import ru.lipovniik.tbot.service.ReplyMessagesService;

@Slf4j
@Component
public class MainMenuHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private MainMenuService messagesService;

    public MainMenuHandler(UserDataCache userDataCache, MainMenuService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public SendMessage handle(Message message) {
        return processUsersInput(message);
    }

    private SendMessage processUsersInput(Message inputMsg){
        int userId = inputMsg.getFrom().getId();
        long chatId = inputMsg.getChatId();

        SendMessage replyToUser = messagesService.getMainMenuMessage(chatId, "Я Котобот\nБла-бла-бла\nСнизу меню!");
        userDataCache.setUsersCurrentBotState(userId, BotState.WAITING_IN_MAIN_MENU);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.MAIN_MENU;
    }
}
