package ru.lipovniik.tbot.botapi;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.lipovniik.tbot.cache.CatsDataCache;
import ru.lipovniik.tbot.cache.UserDataCache;

import java.util.ArrayList;
import java.util.Map;

@Component
public class HandleCallbackQuery {
    private final BotStateContext stateContext;
    private UserDataCache dataCache;
    private final CatsDataCache catsDataCache;

    private HandleCallbackQuery(BotStateContext stateContext, UserDataCache dataCache, CatsDataCache catsDataCache) {
        this.stateContext = stateContext;
        this.dataCache = dataCache;
        this.catsDataCache = catsDataCache;
    }

    public BotApiMethod<?> processCallbackQuery(CallbackQuery buttonQuery){
        String buttonData = buttonQuery.getData();
        BotApiMethod<?> callBackAnswer = null;
        ArrayList<String> KittenButtons = getKittenButtons();

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
            case "faq4" -> {
                Message message = buttonQuery.getMessage();
                message.setText("reply.faq4");
                callBackAnswer = stateContext.processInputMessage(BotState.FAQ_CATEGORY, message);
            }
            case "faq" -> callBackAnswer = stateContext.processInputMessage(BotState.BACK_FAQ, buttonQuery.getMessage());
            //Cancel Question
            case "cancelQuestion" ->  callBackAnswer = stateContext.processInputMessage(BotState.CANCEL_QUESTION, buttonQuery.getMessage());
        }

        if (KittenButtons.contains(buttonData)){
            Message message = buttonQuery.getMessage();
            message.setText(buttonData);
            callBackAnswer = stateContext.processInputMessage(BotState.SHOW_KITTENS, message);
        }

        return callBackAnswer;
    }

    private ArrayList<String> getKittenButtons() {
        ArrayList<String> buttons = new ArrayList<>();
        for (Map.Entry<String, Map<String, String>> kit: catsDataCache.getKittens().entrySet()){
            buttons.add(kit.getKey());
        }
        return buttons;
    }
}
