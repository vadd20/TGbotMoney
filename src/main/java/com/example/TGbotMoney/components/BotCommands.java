package com.example.TGbotMoney.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> BOT_COMMAND_LIST = List.of(
            new BotCommand("/start", "Start"),
            new BotCommand("/help", "Bot info")
    );

    String HELP_TEXT = """
            This financial bot ready to help you track your income and expenses. Here you can record and analyze your financial transactions. If you have questions or need help, just email my developer @vadd20.
            Get your finances right!

            /start - start the bot
            /help - help menu""";
}
