package ru.lipovniik.tbot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.cache.UserDataCache;

@Component
public class HandleCallbackQuery {
    private BotStateContext stateContext;
    private UserDataCache dataCache;

    private HandleCallbackQuery(BotStateContext stateContext, UserDataCache dataCache) {
        this.stateContext = stateContext;
        this.dataCache = dataCache;
    }

    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery){
        String buttonData = buttonQuery.getData();
        BotApiMethod<?> callBackAnswer = null;

        //From FAQ choose button
        switch (buttonData) {
            case "data1" -> {
                Message message = buttonQuery.getMessage();
                message.setText("reply.faq1");
                callBackAnswer = stateContext.processInputMessage(BotState.FAQ_CATEGORY, message);
                break;
            }
            case "data2" -> {
                Message message = buttonQuery.getMessage();
                message.setText("reply.faq2");
                callBackAnswer = stateContext.processInputMessage(BotState.FAQ_CATEGORY, message);
                break;
            }
            case "data3" -> {
                Message message = buttonQuery.getMessage();
                message.setText("reply.faq3");
                callBackAnswer = stateContext.processInputMessage(BotState.FAQ_CATEGORY, message);
                break;
            }
            case "faq" -> {
                callBackAnswer = stateContext.processInputMessage(BotState.BACK_FAQ, buttonQuery.getMessage());
                break;
            }
        }

        return callBackAnswer;
    }
}
