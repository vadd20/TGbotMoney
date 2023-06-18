package com.example.TGbotMoney.components;

import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;

import java.util.List;

public interface BotCommands {
    List<BotCommand> BOT_COMMAND_LIST = List.of(
            new BotCommand("/start", "start"),
            new BotCommand("/help", "bot info")
    );

    String HELP_TEXT = """
            This bot will help to count the number of messages in the chat. The following commands are available to you:

            /start - start the bot
            /help - help menu""";
}
