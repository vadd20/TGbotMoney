package com.example.TGbotMoney.components.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class AddButtons {
    private static final InlineKeyboardButton EXPENSE_BUTTON = new InlineKeyboardButton("Expense");
    private static final InlineKeyboardButton INCOME_BUTTON = new InlineKeyboardButton("Income");

    // функция, возвращающая объект разметки клавиатуры
    public static InlineKeyboardMarkup inlineKeyboardMarkup() {
        EXPENSE_BUTTON.setCallbackData("/addExpense");
        INCOME_BUTTON.setCallbackData("/addIncome");
        // ряд кнопок
        List<InlineKeyboardButton> rowInline = List.of(EXPENSE_BUTTON, INCOME_BUTTON);
        // несколько рядов кнопок (клавиатура)
        List<List<InlineKeyboardButton>> rowsInline = List.of(rowInline);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsInline);

        return inlineKeyboardMarkup;
    }
}
