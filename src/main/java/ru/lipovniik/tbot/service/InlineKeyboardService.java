package ru.lipovniik.tbot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Bogolei Roman
 */
@Service
public class InlineKeyboardService {
    /**
     * All buttons in one column
     * @param buttonsData map with buttons data, key - text and values - data
     * @return {@code InlineKeyboardMarkup}
     */
    public InlineKeyboardMarkup getInlineKeyboard(Map<String, String> buttonsData){
        return getInlineKeyboard(buttonsData, 1);
    }

    /**
     * @param buttonsData map with buttons data, key - text and values - data
     * @param columns the number of columns from buttons
     * @return {@code InlineKeyboardMarkup}
     */
    public InlineKeyboardMarkup getInlineKeyboard(Map<String, String> buttonsData, int columns){
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        int rows = (buttonsData.size() + columns - 1) / columns;
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        Iterator<Map.Entry<String, String>> iterator = buttonsData.entrySet().iterator();
        for (int i = 0; i < rows; i++) {
            List<InlineKeyboardButton> buttonsRow = new ArrayList<>();
            int countButtonInRow = 0;

            while (iterator.hasNext()){
                Map.Entry<String, String> buttonData = iterator.next();
                InlineKeyboardButton button = new InlineKeyboardButton();
                button.setText(buttonData.getKey());
                button.setCallbackData(buttonData.getValue());
                buttonsRow.add(button);
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
