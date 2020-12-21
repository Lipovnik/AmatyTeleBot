package ru.lipovniik.tbot.botapi.handlers.adultcats;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;
import ru.lipovniik.tbot.cache.UserDataCache;
import ru.lipovniik.tbot.service.SendMediaService;

@Component
class AdultMaleCatsHandler implements InputMessageHandler {
    private final SendMediaService mediaService;
    private final UserDataCache userDataCache;

    AdultMaleCatsHandler(SendMediaService mediaService, UserDataCache userDataCache) {
        this.mediaService = mediaService;
        this.userDataCache = userDataCache;
    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        sendPhoto(message);
        userDataCache.setUsersCurrentBotState(message.getFrom().getId(), BotState.WAITING_IN_MAIN_MENU);
        return null;
    }

    private void sendPhoto(Message inputMsg) {
        long chatId = inputMsg.getChatId();
        mediaService.sendPhoto(chatId, "AgACAgQAAxkDAAIFfF_gvom3Vq45fuRhNAom36a241q7AAKGqzEbe-cMU9rrCrCLsRCQV4b5KF0AAwEAAwIAA3cAA93vAQABHgQ", "Konsul");
        mediaService.sendPhoto(chatId, "AgACAgQAAxkDAAIFfV_gv4pO2Gabc5D_-N-I7llEWKZlAAKCqzEbzCQMU3PU9_rlZ0saCL_VJl0AAwEAAwIAA3gAA5e-AwABHgQ", "Diesel");
        mediaService.sendPhoto(chatId, "AgACAgQAAxkDAAIFfl_gwGHJD87UOD1KZ-n25G-8dOrtAAK7qzEbfFoFUxcdTqUVKYcSQmrgJl0AAwEAAwIAA3kAA8yJAwABHgQ", "Tom Crise");
        mediaService.sendPhoto(chatId, "AgACAgQAAxkDAAIFXV_gr4kON9HJ3VSvCP2jiOVz7eFcAAJ8qzEbMf8NU5zph5gLuugcOC5IJ10AAwEAAwIAA3cAAyyDAwABHgQ", "Ostin");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ADULT_MALE_CATS;
    }
}
