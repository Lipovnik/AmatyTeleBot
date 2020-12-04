package ru.lipovniik.tbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class InlineKeyboardService {
    public InlineKeyboardMarkup getInlineKeyboard(Map<String, String> buttonsData){
        return getInlineKeyboard(buttonsData, 1);
    }

    public InlineKeyboardMarkup getInlineKeyboard(Map<String, String> buttonsData, int columns){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        int countButtons = buttonsData.size();
        int countAddedButtons = 0;
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        while (countAddedButtons != countButtons) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
            int countButtonInRow = 0;
            for (Map.Entry<String, String> pair : buttonsData.entrySet()) {
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(pair.getKey());
                button.setCallbackData(pair.getValue());
                buttonsRow.add(button);
                countAddedButtons++;
                countButtonInRow++;
                if (countButtonInRow == columns)
                    break;
            }
            rowList.add(buttonsRow);
        }

        inlineKeyboardMarkup.setKeyboard(rowList);

        return inlineKeyboardMarkup;
    }
}
