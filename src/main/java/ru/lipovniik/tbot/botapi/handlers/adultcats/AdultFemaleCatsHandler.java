package ru.lipovniik.tbot.botapi.handlers.adultcats;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.botapi.BotState;
import ru.lipovniik.tbot.botapi.InputMessageHandler;
import ru.lipovniik.tbot.cache.UserDataCache;
import ru.lipovniik.tbot.service.SendMediaService;

@Component
public class AdultFemaleCatsHandler implements InputMessageHandler {
    private final SendMediaService mediaService;
    private final UserDataCache userDataCache;

    AdultFemaleCatsHandler(SendMediaService mediaService, UserDataCache userDataCache) {
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
        mediaService.sendPhoto(chatId, "AgACAgQAAxkDAAIFf1_gwpH0-CUNoXNpOvWCS5xg469sAAK6qzEbuCgNU1KzrM6Fhzf4qjC9Kl0AAwEAAwIAA3cAA5k1AAIeBA", "Ginger");
        mediaService.sendPhoto(chatId, "AgACAgQAAxkDAAIFgF_gwutNPkYB9tBv6CCaCLNlmey9AAKhqzEbE5wNU7w49OmaXjW8VpX8KF0AAwEAAwIAA3cAA63uAQABHgQ", "Tyra");
        mediaService.sendPhoto(chatId, "AgACAgQAAxkDAAIFgV_gwzQ4kj6wcEiTe4kmsm3ifoylAAKRqzEbwOINU7NnMKEu3HiT4DdTKF0AAwEAAwIAA3cAA1LMAQABHgQ", "Safia");
        mediaService.sendPhoto(chatId, "AgACAgQAAxkDAAIFgl_gw3Kb8hdJH8Mv59Dcfc-tRtvkAALkqzEboIwEUwUBUMvGpR2pHmHAJl0AAwEAAwIAA3cAA4W_AwABHgQ", "Laysan");
    }

    @Override
    public BotState getHandlerName() {
        return BotState.ADULT_FEMALE_CATS;
    }
}
