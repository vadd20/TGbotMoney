package com.example.TGbotMoney.service;

import com.example.TGbotMoney.components.buttons.*;
import com.example.TGbotMoney.model.*;
import com.example.TGbotMoney.components.BotCommands;
import com.example.TGbotMoney.config.BotConfig;
import com.example.TGbotMoney.repository.ExpenseRepository;
import com.example.TGbotMoney.repository.IncomeRepository;
import com.example.TGbotMoney.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalField;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.Locale;


@Component
@Slf4j
public class TgBot extends TelegramLongPollingBot implements BotCommands {
    private BotConfig config;
    private UserRepository userRepository;
    private ExpenseRepository expenseRepository;
    private IncomeRepository incomeRepository;
    private int sum;
    private long userId;
    private String userName;

    @Autowired
    public TgBot(BotConfig config, UserRepository userRepository,
                 ExpenseRepository expenseRepository, IncomeRepository incomeRepository) {
        this.config = config;
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.incomeRepository = incomeRepository;
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
        int messageId = 0;
        String receivedMessage;
        if (update.hasMessage()) {
            if (update.getMessage().hasText()) {
                chatId = update.getMessage().getChatId();
                this.userId = update.getMessage().getFrom().getId();
                this.userName = update.getMessage().getFrom().getFirstName();
                receivedMessage = update.getMessage().getText();
                messageId = update.getMessage().getMessageId();

                if (isNumeric(receivedMessage)) {
                    sum = Integer.parseInt(receivedMessage);
                    isCorrectSum(chatId, sum);
                } else {
                    botAnswerUtils(receivedMessage, chatId, userName, userId, messageId); // выбирает, какое действие сделать
                }                                                                        // (в зависимости от receivedMessage)
            }
        } else if (update.hasCallbackQuery()) {
            chatId = update.getCallbackQuery().getMessage().getChatId();
            receivedMessage = update.getCallbackQuery().getData();
            messageId = update.getCallbackQuery().getMessage().getMessageId();
            botAnswerUtils(receivedMessage, chatId, userName, userId, messageId);
        }
        updateDB(userId, userName);
    }

    private void botAnswerUtils(String receivedMessage, long chatId, String userName, long userId, int messageId) {
        switch (receivedMessage) {
            case "/start" -> startBot(chatId, userName);
            case "/help" -> sendHelpMessage(chatId, HELP_TEXT);

            case "/add" -> enterSum(chatId);
            case "/stats" -> showStats(chatId);

            case "/addExpense" -> addExpense(chatId, messageId);
            case "/addIncome" -> addIncome(chatId, messageId);

            case "/continueCheckSum" -> addRecord(chatId);
            case "/againCheckSum" -> enterSum(chatId);

            case "/addFoodExpense" -> addExpenseInDB(userId, ExpenseCategory.FOOD, chatId, messageId);
            case "/addClothingCosmeticsExpense" ->
                    addExpenseInDB(userId, ExpenseCategory.CLOTHING_AND_COSMETICS, chatId, messageId);
            case "/addCommunalExpense" -> addExpenseInDB(userId, ExpenseCategory.COMMUNAL, chatId, messageId);
            case "/addTransportExpense" -> addExpenseInDB(userId, ExpenseCategory.TRANSPORT, chatId, messageId);
            case "/addEducationExpense" -> addExpenseInDB(userId, ExpenseCategory.EDUCATION, chatId, messageId);
            case "/addEntertainmentExpense" -> addExpenseInDB(userId, ExpenseCategory.ENTERTAINMENT, chatId, messageId);
            case "/addApplianceExpense" -> addExpenseInDB(userId, ExpenseCategory.APPLIANCES, chatId, messageId);
            case "/addMedicineExpense" -> addExpenseInDB(userId, ExpenseCategory.MEDICINE, chatId, messageId);
            case "/addSocialExpense" -> addExpenseInDB(userId, ExpenseCategory.SOCIAL, chatId, messageId);
            case "/addInvestmentsExpense" -> addExpenseInDB(userId, ExpenseCategory.INVESTMENTS, chatId, messageId);
            case "/addOtherExpense" -> addExpenseInDB(userId, ExpenseCategory.OTHER, chatId, messageId);

            case "/addMainJobIncome" -> addIncomeInDB(userId, IncomeCategory.MAIN_JOB, chatId, messageId);
            case "/addPartTimeJobIncome" -> addIncomeInDB(userId, IncomeCategory.PART_TIME_JOB, chatId, messageId);
            case "/addGiftIncome" -> addIncomeInDB(userId, IncomeCategory.GIFT, chatId, messageId);
            case "/addCashbackIncome" -> addIncomeInDB(userId, IncomeCategory.CASHBACK, chatId, messageId);
            case "/addInvestmentsIncome" -> addIncomeInDB(userId, IncomeCategory.INVESTMENTS, chatId, messageId);

            case "/backWhenChooseCategory" -> addRecord(chatId);

            case "/showDayStats" -> showStatsForDay(userId, chatId, messageId);
            case "/showWeekStats" -> showStatsForCurrentWeek(userId, chatId, messageId);
            case "/showMonthStats" -> showStatsForCurrentMonth(userId, chatId, messageId);
            case "/showYearStats" -> showStatsForCurrentYear(userId, chatId, messageId);
            case "/showAllTimeStats" -> showStatsForAllTime(userId, chatId, messageId);

            default -> sendDefaultMessage(chatId);
        }
    }

    private void showStatsForAllTime(long userId, long chatId, int messageId) {
        LocalDateTime startOfLife = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0);

        String messageText = getTextStatsForPeriod(userId, startOfLife);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        editMessageText.setText("Stats for all time:\n" + messageText);

        try {
            execute(editMessageText);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void showStatsForCurrentYear(long userId, long chatId, int messageId) {
        LocalDateTime startOfYear = LocalDateTime.now().toLocalDate().atStartOfDay().withDayOfYear(1);

        String messageText = getTextStatsForPeriod(userId, startOfYear);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        editMessageText.setText("Stats for current year:\n" + messageText);

        try {
            execute(editMessageText);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void showStatsForCurrentMonth(long userId, long chatId, int messageId) {
        LocalDateTime startOfMonth = LocalDateTime.now().toLocalDate().atStartOfDay().withDayOfMonth(1);

        String messageText = getTextStatsForPeriod(userId, startOfMonth);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        editMessageText.setText("Stats for current month:\n" + messageText);

        try {
            execute(editMessageText);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void showStatsForDay(long userId, long chatId, int messageId) {
        LocalDateTime startOfToday = LocalDateTime.now().toLocalDate().atStartOfDay();

        String messageText = getTextStatsForPeriod(userId, startOfToday);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        editMessageText.setText("Stats for today:\n" + messageText);

        try {
            execute(editMessageText);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void showStatsForCurrentWeek(long userId, long chatId, int messageId) {
        LocalDateTime startOfWeek = LocalDateTime.now().toLocalDate().atStartOfDay().with(DayOfWeek.MONDAY);

        String messageText = getTextStatsForPeriod(userId, startOfWeek);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        editMessageText.setText("Stats for current week:\n" + messageText);

        try {
            execute(editMessageText);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private String getTextStatsForPeriod(long userId, LocalDateTime date) {
        List<Expense> weekExpenses = expenseRepository.findAllAfterDate(userId, date);
        List<Income> weekIncomes = incomeRepository.findAllAfterDate(userId, date);

        StringBuilder statsForPeriod = new StringBuilder();

        for (Expense expense : weekExpenses) {
            statsForPeriod.append(expense.toString()).append("\n");
        }

        for (Income income : weekIncomes) {
            statsForPeriod.append(income.toString()).append("\n");
        }

        return statsForPeriod.toString();
    }

    private void addIncomeInDB(long userId, IncomeCategory category, long chatId, int messageId) {
        Income income = new Income();
        income.setUser_id(userId);
        income.setDate(LocalDateTime.now());
        income.setCategory(category.toString());
        income.setSum(sum);
        incomeRepository.saveAndFlush(income);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        editMessageText.setText("Added to income: " + category + " - " + sum);
        editMessageText.setReplyMarkup(StartButtons.inlineKeyboardMarkup());

        try {
            execute(editMessageText);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }

        log.info("added to db:" + income);
    }

    private void addExpenseInDB(long userId, ExpenseCategory category, long chatId, int messageId) {
        Expense expense = new Expense();
        expense.setUser_id(userId);
        expense.setDate(LocalDateTime.now());
        expense.setCategory(category.toString());
        expense.setSum(sum);
        expenseRepository.saveAndFlush(expense);

        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        editMessageText.setText("Added to expense: " + category + " - " + sum);
        editMessageText.setReplyMarkup(StartButtons.inlineKeyboardMarkup());

        try {
            execute(editMessageText);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }

        log.info("added to db:" + expense);
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

    private void isCorrectSum(long chatId, int sum) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Your sum is " + sum + ". Click next to continue");
        sendMessage.setReplyMarkup(CheckSumButtons.inlineKeyboardMarkup());

        try {
            execute(sendMessage);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void addIncome(long chatId, int messageId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        editMessageText.setText("Choose category you want to add for income");
        editMessageText.setReplyMarkup(IncomeCategoryButtons.inlineKeyboardMarkup());

        try {
            execute(editMessageText);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void addExpense(long chatId, int messageId) {
        EditMessageText editMessageText = new EditMessageText();
        editMessageText.setChatId(String.valueOf(chatId));
        editMessageText.setMessageId(messageId);
        editMessageText.setText("Choose category you want to add for expense");
        editMessageText.setReplyMarkup(ExpenseCategoryButtons.inlineKeyboardMarkup());

        try {
            execute(editMessageText);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    private void showStats(long chatId) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText("Choose period of statistics");
        sendMessage.setReplyMarkup(PeriodsButtons.inlineKeyboardMarkup());

        try {
            execute(sendMessage);
            log.info("Sent");
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
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
