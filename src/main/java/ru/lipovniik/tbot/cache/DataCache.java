package ru.lipovniik.tbot.cache;

import ru.lipovniik.tbot.botapi.BotState;

public interface DataCache {
    void setUsersCurrentBotState(int userId, BotState botState);

    BotState getUsersCurrentBotState(int userId);
}
