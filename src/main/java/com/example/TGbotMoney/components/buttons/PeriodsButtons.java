package com.example.TGbotMoney.components.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class PeriodsButtons {

    private static final InlineKeyboardButton DAY_BUTTON = new InlineKeyboardButton("Day");
    private static final InlineKeyboardButton WEEK_BUTTON = new InlineKeyboardButton("Week");
    private static final InlineKeyboardButton MONTH_BUTTON = new InlineKeyboardButton("Month");
    private static final InlineKeyboardButton ONE_YEAR_BUTTON = new InlineKeyboardButton("Year");
    private static final InlineKeyboardButton ALL_TIME_BUTTON = new InlineKeyboardButton("All time");
    private static final InlineKeyboardButton BACK_BUTTON = new InlineKeyboardButton("↩️ Back");



    public static InlineKeyboardMarkup inlineKeyboardMarkup() {
        DAY_BUTTON.setCallbackData("/showDayStats");
        WEEK_BUTTON.setCallbackData("/showWeekStats");
        MONTH_BUTTON.setCallbackData("/showMonthStats");
        ONE_YEAR_BUTTON.setCallbackData("/showYearStats");
        ALL_TIME_BUTTON.setCallbackData("/showAllTimeStats");
        BACK_BUTTON.setCallbackData("/backWhenChooseStats");

        return getInlineKeyboardMarkup();
    }

    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {
        List<InlineKeyboardButton> firstRowInline = List.of(DAY_BUTTON, WEEK_BUTTON, MONTH_BUTTON);
        List<InlineKeyboardButton> secondRowInline = List.of(ONE_YEAR_BUTTON, ALL_TIME_BUTTON);
        List<InlineKeyboardButton> thirdRowInline = List.of(BACK_BUTTON);

        List<List<InlineKeyboardButton>> rowsInline = List.of(firstRowInline, secondRowInline, thirdRowInline);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsInline);
        return inlineKeyboardMarkup;
    }

}