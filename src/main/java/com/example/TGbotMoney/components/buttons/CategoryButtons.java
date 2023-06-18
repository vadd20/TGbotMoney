package com.example.TGbotMoney.components.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class CategoryButtons {

    private static final InlineKeyboardButton FOOD_BUTTON = new InlineKeyboardButton("Food");
    private static final InlineKeyboardButton CLOTHING_AND_COSMETICS_BUTTON = new InlineKeyboardButton("Clothing and cosmetics");
    private static final InlineKeyboardButton TRANSPORT_BUTTON = new InlineKeyboardButton("Transport");
    private static final InlineKeyboardButton ENTERTAINMENT_BUTTON = new InlineKeyboardButton("Entertainment");
    private static final InlineKeyboardButton EDUCATION_BUTTON = new InlineKeyboardButton("Education");
    private static final InlineKeyboardButton OTHER_BUTTON = new InlineKeyboardButton("Other");



    // функция, возвращающая объект разметки клавиатуры
    public static InlineKeyboardMarkup inlineKeyboardMarkup() {
        FOOD_BUTTON.setCallbackData("/addFoodExpense");
        CLOTHING_AND_COSMETICS_BUTTON.setCallbackData("/addClothingCosmeticsExpense");
        TRANSPORT_BUTTON.setCallbackData("/addTransportExpense");
        ENTERTAINMENT_BUTTON.setCallbackData("/addEntertainmentExpense");
        EDUCATION_BUTTON.setCallbackData("/addEducationExpense");
        OTHER_BUTTON.setCallbackData("/addOtherExpense");
        // ряд кнопок
        List<InlineKeyboardButton> firstRowInline = List.of(FOOD_BUTTON, CLOTHING_AND_COSMETICS_BUTTON, TRANSPORT_BUTTON);
        List<InlineKeyboardButton> secondRowInline = List.of(ENTERTAINMENT_BUTTON, EDUCATION_BUTTON, OTHER_BUTTON);
        // несколько рядов кнопок (клавиатура)
        List<List<InlineKeyboardButton>> rowsInline = List.of(firstRowInline, secondRowInline);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsInline);

        return inlineKeyboardMarkup;
    }
}
