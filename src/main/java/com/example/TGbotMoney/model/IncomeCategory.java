package com.example.TGbotMoney.model;

public enum IncomeCategory {
    MAIN_JOB,
    PART_TIME_JOB,
    GIFT,
    CASHBACK,
    INVESTMENTS;

    @Override
    public String toString() {
        String enumName = this.name();
        String firstChar = enumName.substring(0, 1);
        String remainingChars = enumName.substring(1).toLowerCase().replace('_', ' ');
        return firstChar + remainingChars;
    }
}
