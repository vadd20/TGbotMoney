package com.example.TGbotMoney.components.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class StartButtons {
    // кнопки с текстом
    private static final InlineKeyboardButton ADD_BUTTON = new InlineKeyboardButton("Add");
    private static final InlineKeyboardButton STATS_BUTTON = new InlineKeyboardButton("Stats");

    // функция, возвращающая объект разметки клавиатуры
    public static InlineKeyboardMarkup inlineKeyboardMarkup() {
        ADD_BUTTON.setCallbackData("/add");
        STATS_BUTTON.setCallbackData("/stats");
        // ряд кнопок
        List<InlineKeyboardButton> rowInline = List.of(ADD_BUTTON, STATS_BUTTON);
        // несколько рядов кнопок (клавиатура)
        List<List<InlineKeyboardButton>> rowsInline = List.of(rowInline);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsInline);

        return inlineKeyboardMarkup;
    }
}
