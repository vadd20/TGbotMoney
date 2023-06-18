package com.example.TGbotMoney.service;

import com.example.TGbotMoney.model.Category;
import com.example.TGbotMoney.components.BotCommands;
import com.example.TGbotMoney.components.buttons.AddButtons;
import com.example.TGbotMoney.components.buttons.CategoryButtons;
import com.example.TGbotMoney.components.buttons.StartButtons;
import com.example.TGbotMoney.config.BotConfig;
import com.example.TGbotMoney.model.Expense;
import com.example.TGbotMoney.model.User;
import com.example.TGbotMoney.repository.ExpenseRepository;
import com.example.TGbotMoney.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.LocalDateTime;


//  todo при нажатии на трату->food эта категория куда-нибудь записывалась, а потом при вводе суммы это число
//  todo записывалось в бд именно в фуд
@Component
@Slf4j
public class TgBot extends TelegramLongPollingBot implements BotCommands {
    private BotConfig config;
    private UserRepository userRepository;
    private ExpenseRepository expenseRepository;
    private int sum;

    @Autowired
    public TgBot(BotConfig config, UserRepository userRepository, ExpenseRepository expenseRepository) {
        this.config = config;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        try {
            this.execute(new SetMyCommands(BOT_COMMAND_LIST, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void updateDB(long userId, String userName) {
        if (userRepository.findById(userId).isEmpty()) {
            User user = new User();
            user.setId(userId);
            user.setName(userName);

            userRepository.save(user);
            log.info("added to db:" + user);
        } else {
            log.info("this id in db");
        }
    }

    @Override
    public void onUpdateReceived(@NotNull Update update) {
        long chatId = 0;
        long userId = 0;
        String userName = "";
        String receivedMessage;
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                chatId = update.getMessage().getChatId();
                userId = update.getMessage().getFrom().getId();
                userName = update.getMessage().getFrom().getFirstName();
                receivedMessage = update.getMessage().getText();

                if (isNumeric(receivedMessage)) {
                    sum = Integer.parseInt(receivedMessage);
                    addRecord(chatId);
                } else {
                    botAnswerUtils(receivedMessage, chatId, userName, userId); // выбирает, какое действие сделать
                }                                                      // (в зависимости от receivedMessage)
            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            userId = update.getCallbackQuery().getMessage().getFrom().getId();
            userName = update.getCallbackQuery().getMessage().getFrom().getFirstName();
            receivedMessage = update.getCallbackQuery().getData();
            botAnswerUtils(receivedMessage, chatId, userName, userId);
        }
        updateDB(userId, userName);
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName, long userId) {
        switch (receivedMessage) {
            case "/start" -> startBot(chatId, userName);
            case "/help" -> sendHelpMessage(chatId, HELP_TEXT);
            case "/add" -> enterSum(chatId);
            case "/stats" -> showStats(chatId);
            case "/addExpense" -> addExpense(chatId);
            case "/addIncome" -> addIncome(chatId);

            case "/addFoodExpense" -> addExpenseInCategory(userId, Category.FOOD);
            case "/addClothingCosmeticsExpense" ->
                    addExpenseInCategory(userId, Category.CLOTHING_AND_COSMETICS);
            case "/addTransportExpense" -> addExpenseInCategory(userId, Category.TRANSPORT);
            case "/addEntertainmentExpense" -> addExpenseInCategory(userId, Category.ENTERTAINMENT);
            case "/addEducationExpense" -> addExpenseInCategory(userId, Category.EDUCATION);
            case "/addOtherExpense" -> addExpenseInCategory(userId, Category.OTHER);

            default -> sendDefaultMessage(chatId);
        }
    }

    private void addExpenseInCategory(long userId, Category category) {
        Expense expense = new Expense();
        expense.setUser_id(userId);
        expense.setDate(LocalDateTime.now());
        switch (category) {
            case FOOD -> expense.setCategory(Category.FOOD.toString());
            case CLOTHING_AND_COSMETICS -> expense.setCategory(Category.CLOTHING_AND_COSMETICS.toString());
            case TRANSPORT -> expense.setCategory(Category.TRANSPORT.toString());
            case ENTERTAINMENT -> expense.setCategory(Category.ENTERTAINMENT.toString());
            case EDUCATION -> expense.setCategory(Category.EDUCATION.toString());
            case OTHER -> expense.setCategory(Category.OTHER.toString());
        }
        expense.setSum(sum);

        expenseRepository.saveAndFlush(expense);
        log.info("added to db:" + expense);
        // todo: запись в таблице expense - это запись в бд. Возможно переделать бд так,
        //  чтобы был id, date, category, sum. Надо подумать, как потом собирать статистику. тяжело...
    }

    private void enterSum(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Enter sum to order");

        try {
            execute(sendMessage);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void toDoOrder(long chatId) {

    }

    private void addIncome(long chatId) {
    }

    private void addExpense(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Choose category you want to add");
        sendMessage.setReplyMarkup(CategoryButtons.inlineKeyboardMarkup());

        try {
            execute(sendMessage);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void showStats(long chatId) {
    }


    private void addRecord(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Choose what you want to add");
        sendMessage.setReplyMarkup(AddButtons.inlineKeyboardMarkup());

        try {
            execute(sendMessage);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendDefaultMessage(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText("Choose from available");
        try {
            execute(sendMessage);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void sendHelpMessage(long chatId, String textToSend) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(textToSend);
        try {
            execute(sendMessage);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void startBot(Long chatId, String userName) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Hello, " + userName + ". I am finance bot by @vadd20");
        sendMessage.setReplyMarkup(StartButtons.inlineKeyboardMarkup());

        try {
            execute(sendMessage);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }
}
