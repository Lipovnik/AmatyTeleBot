package ru.lipovniik.tbot.botapi.handlers.adultcats;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;
import ru.lipovniik.tbot.botapi.parsers.amatycay.AmatyCayParser;
import ru.lipovniik.tbot.cache.CatsDataCache;
import ru.lipovniik.tbot.cache.UserDataCache;
import ru.lipovniik.tbot.service.SendMediaService;

import java.util.Map;

@Component
class AdultMaleCatsHandler implements InputMessageHandler {
    private final SendMediaService mediaService;
    private final UserDataCache userDataCache;
    private final CatsDataCache catsDataCache;

    AdultMaleCatsHandler(SendMediaService mediaService, UserDataCache userDataCache, AmatyCayParser amatyCayParser, CatsDataCache catsDataCache) {
        this.mediaService = mediaService;
        this.userDataCache = userDataCache;
        this.catsDataCache = catsDataCache;
    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        sendPhoto(message);
        userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.WAITING_IN_MAIN_MENU);
        return null;
    }

    private void sendPhoto(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        Map<String, String> map = catsDataCache.getMaleCatsPhoto();
        for (Map.Entry<String, String> pair: map.entrySet()){
            mediaService.sendPhoto(chatId, pair.getValue(), pair.getKey());
        }
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ADULT_MALE_CATS;
    }
}
