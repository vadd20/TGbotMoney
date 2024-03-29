package com.example.TGbotMoney.components.buttons;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.List;

public class ExpenseCategoryButtons {

    private static final InlineKeyboardButton FOOD_BUTTON = new InlineKeyboardButton("\uD83E\uDD66 Food");
    private static final InlineKeyboardButton CLOTHING_AND_COSMETICS_BUTTON = new InlineKeyboardButton("\uD83E\uDDE5 Clothe&cosmetics");
    private static final InlineKeyboardButton COMMUNAL_BUTTON = new InlineKeyboardButton("\uD83D\uDCA1 Communal");
    private static final InlineKeyboardButton TRANSPORT_BUTTON = new InlineKeyboardButton("\uD83D\uDE97 Transport");
    private static final InlineKeyboardButton EDUCATION_BUTTON = new InlineKeyboardButton("\uD83D\uDCDA Education");
    private static final InlineKeyboardButton ENTERTAINMENT_BUTTON = new InlineKeyboardButton("\uD83C\uDF89 Entertainment");
    private static final InlineKeyboardButton APPLIANCES_BUTTON = new InlineKeyboardButton("\uD83D\uDD0C Appliances");
    private static final InlineKeyboardButton MEDICINE_BUTTON = new InlineKeyboardButton("\uD83D\uDC8A Medicine");
    private static final InlineKeyboardButton SOCIAL_BUTTON = new InlineKeyboardButton("\uD83E\uDD1D Social");
    private static final InlineKeyboardButton INVESTMENTS_BUTTON = new InlineKeyboardButton("\uD83D\uDCC8 Investments");
    private static final InlineKeyboardButton OTHER_BUTTON = new InlineKeyboardButton("\uD83C\uDF1F Other");
    private static final InlineKeyboardButton BACK_BUTTON = new InlineKeyboardButton("↩️ Back");


    public static InlineKeyboardMarkup inlineKeyboardMarkup() {
        FOOD_BUTTON.setCallbackData("/addFoodExpense");
        CLOTHING_AND_COSMETICS_BUTTON.setCallbackData("/addClothingCosmeticsExpense");
        COMMUNAL_BUTTON.setCallbackData("/addCommunalExpense");
        TRANSPORT_BUTTON.setCallbackData("/addTransportExpense");
        EDUCATION_BUTTON.setCallbackData("/addEducationExpense");
        ENTERTAINMENT_BUTTON.setCallbackData("/addEntertainmentExpense");
        APPLIANCES_BUTTON.setCallbackData("/addApplianceExpense");
        MEDICINE_BUTTON.setCallbackData("/addMedicineExpense");
        SOCIAL_BUTTON.setCallbackData("/addSocialExpense");
        INVESTMENTS_BUTTON.setCallbackData("/addInvestmentsExpense");
        OTHER_BUTTON.setCallbackData("/addOtherExpense");
        BACK_BUTTON.setCallbackData("/backWhenChooseCategory");

        return getInlineKeyboardMarkup();
    }

    private static InlineKeyboardMarkup getInlineKeyboardMarkup() {
        List<InlineKeyboardButton> firstRowInline = List.of(
                FOOD_BUTTON, CLOTHING_AND_COSMETICS_BUTTON, COMMUNAL_BUTTON);
        List<InlineKeyboardButton> secondRowInline = List.of(
                TRANSPORT_BUTTON, EDUCATION_BUTTON, ENTERTAINMENT_BUTTON, APPLIANCES_BUTTON);
        List<InlineKeyboardButton> thirdRowInline = List.of(
                MEDICINE_BUTTON, SOCIAL_BUTTON, INVESTMENTS_BUTTON, OTHER_BUTTON);
        List<InlineKeyboardButton> fourthRowInline = List.of(BACK_BUTTON);

        List<List<InlineKeyboardButton>> rowsInline = List.of(firstRowInline, secondRowInline,
                thirdRowInline, fourthRowInline);

        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowsInline);
        return inlineKeyboardMarkup;
    }
}
