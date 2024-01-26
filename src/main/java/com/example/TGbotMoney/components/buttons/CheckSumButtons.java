package com.example.TGbotMoney.components.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class CheckSumButtons {

    private static final InlineKeyboardButton CONTINUE_BUTTON = new InlineKeyboardButton("\uD83D\uDE80 Continue");
    private static final InlineKeyboardButton AGAIN_BUTTON = new InlineKeyboardButton("\uD83D\uDD04 Enter again");

    public static InlineKeyboardMarkup inlineKeyboardMarkup() {
        CONTINUE_BUTTON.setCallbackData("/continueCheckSum");
        AGAIN_BUTTON.setCallbackData("/againCheckSum");

        return getInlineKeyboardMarkup();
    }

    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {
        List<InlineKeyboardButton> rowInline = List.of(CONTINUE_BUTTON, AGAIN_BUTTON);
        List<List<InlineKeyboardButton>> rowsInline = List.of(rowInline);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsInline);

        return inlineKeyboardMarkup;
    }
}
