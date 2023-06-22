package com.example.TGbotMoney.components.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class IncomeCategoryButtons {
    private static final InlineKeyboardButton MAIN_JOB_BUTTON = new InlineKeyboardButton("Main job");
    private static final InlineKeyboardButton PART_TIME_JOB_BUTTON = new InlineKeyboardButton("Part-time job");
    private static final InlineKeyboardButton GIFT_BUTTON = new InlineKeyboardButton("Gift");
    private static final InlineKeyboardButton CASHBACK = new InlineKeyboardButton("Cashback");
    private static final InlineKeyboardButton INVESTMENTS_BUTTON = new InlineKeyboardButton("Investments");
    private static final InlineKeyboardButton BACK_BUTTON = new InlineKeyboardButton("Back");


    public static InlineKeyboardMarkup inlineKeyboardMarkup() {
        MAIN_JOB_BUTTON.setCallbackData("/addMainJobIncome");
        PART_TIME_JOB_BUTTON.setCallbackData("/addPartTimeJobIncome");
        GIFT_BUTTON.setCallbackData("/addGiftIncome");
        CASHBACK.setCallbackData("/addCashbackIncome");
        INVESTMENTS_BUTTON.setCallbackData("/addInvestmentsIncome");
        BACK_BUTTON.setCallbackData("/backWhenChooseCategory");

        return getInlineKeyboardMarkup();
    }

    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {
        List<InlineKeyboardButton> firstRowInline = List.of(MAIN_JOB_BUTTON, PART_TIME_JOB_BUTTON);
        List<InlineKeyboardButton> secondRowInline = List.of(GIFT_BUTTON, CASHBACK, INVESTMENTS_BUTTON);
        List<InlineKeyboardButton> thirdRowInline = List.of(BACK_BUTTON);

        List<List<InlineKeyboardButton>> rowsInline = List.of(firstRowInline, secondRowInline, thirdRowInline);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsInline);
        return inlineKeyboardMarkup;
    }
}
