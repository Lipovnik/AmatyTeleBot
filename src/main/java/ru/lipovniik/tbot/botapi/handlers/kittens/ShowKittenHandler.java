package ru.lipovniik.tbot.botapi.handlers.kittens;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;
import ru.lipovniik.tbot.cache.CatsDataCache;
import ru.lipovniik.tbot.cache.UserDataCache;
import ru.lipovniik.tbot.service.SendMediaService;

import java.util.Map;

@Component
public class ShowKittenHandler implements InputMessageHandler {
    private final UserDataCache userDataCache;
    private final SendMediaService mediaService;
    private final CatsDataCache catsDataCache;

    public ShowKittenHandler(UserDataCache userDataCache, SendMediaService mediaService, CatsDataCache catsDataCache) {
        this.userDataCache = userDataCache;
        this.mediaService = mediaService;
        this.catsDataCache = catsDataCache;
    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        return processUsersInput(message);
    }

    private BotApiMethod<?> processUsersInput(Message inputMsg) {
        sendPhoto(inputMsg);
        userDataCache.setUsersCurrentBotState(inputMsg.getFrom().getId(), BotState.WAITING_IN_MAIN_MENU);

        return null;
    }

    private void sendPhoto(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        String birthday = inputMsg.getText();
        Map<String, String> map = catsDataCache.getKittens().get(birthday);
        for (Map.Entry<String, String> pair: map.entrySet()){
            mediaService.sendPhoto(chatId,  pair.getKey(), pair.getValue());
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SHOW_KITTENS;
    }
}
