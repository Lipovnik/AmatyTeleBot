package ru.lipovniik.tbot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.cache.UserDataCache;

@Component
public class HandleCallbackQuery {
    private final BotStateContext stateContext;
    private UserDataCache dataCache;

    private HandleCallbackQuery(BotStateContext stateContext, UserDataCache dataCache) {
        this.stateContext = stateContext;
        this.dataCache = dataCache;
    }

    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery){
        String buttonData = buttonQuery.getData();
        BotApiMethod<?> callBackAnswer = null;


        switch (buttonData) {
            //From FAQ choose button
            case "faq1" -> {
                Message message = buttonQuery.getMessage();
                message.setText("reply.faq1");
                callBackAnswer = stateContext.processInputMessage(BotState.FAQ_CATEGORY, message);
            }
            case "faq2" -> {
                Message message = buttonQuery.getMessage();
                message.setText("reply.faq2");
                callBackAnswer = stateContext.processInputMessage(BotState.FAQ_CATEGORY, message);
            }
            case "faq3" -> {
                Message message = buttonQuery.getMessage();
                message.setText("reply.faq3");
                callBackAnswer = stateContext.processInputMessage(BotState.FAQ_CATEGORY, message);
            }
            case "faq" -> callBackAnswer = stateContext.processInputMessage(BotState.BACK_FAQ, buttonQuery.getMessage());
            //Cancel Question
            case "cancelQuestion" ->  callBackAnswer = stateContext.processInputMessage(BotState.FAQ_CATEGORY, buttonQuery.getMessage());

        }

        return callBackAnswer;
    }
}
