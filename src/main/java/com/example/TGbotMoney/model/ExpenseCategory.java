package com.example.TGbotMoney.model;

public enum ExpenseCategory {
    FOOD,
    CLOTHING_AND_COSMETICS,
    COMMUNAL,
    TRANSPORT,
    EDUCATION,
    ENTERTAINMENT,
    APPLIANCES,
    MEDICINE,
    SOCIAL,
    INVESTMENTS,
    OTHER;

    @Override
    public String toString() {
        String enumName = this.name();
        String firstChar = enumName.substring(0, 1);
        String remainingChars = enumName.substring(1).toLowerCase().replace('_', ' ');
        return firstChar + remainingChars;
    }
}
