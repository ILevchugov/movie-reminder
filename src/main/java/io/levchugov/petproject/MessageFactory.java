package io.levchugov.petproject;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class MessageFactory {
    public static SendMessage greeting(Update update) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "Hi, you can share with me any movie, which you want to want near future:)"
        );
        sendMessage.setChatId(update.getMessage().getChatId());

        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Add")
                                .callbackData("add_movie")
                                .build()
                )
        );
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    public static SendMessage enterName(Update update) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "text me title"
        );
        sendMessage.setChatId(update.getCallbackQuery().getMessage().getChatId());
        return sendMessage;
    }

    public static SendMessage gotIt(Long chatId) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "Got it! You can add more movies, or roll"
        );
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Add")
                                .callbackData("add_movie")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Roll")
                                .callbackData("roll_movie")
                                .build()
                )
        );
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    public static SendMessage presentMovie(String movie, Long chatId) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "Tonight you will watch" + " " + movie + "!!!"
        );
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Add")
                                .callbackData("add_movie")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Roll")
                                .callbackData("roll_movie")
                                .build()
                )
        );
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

    public static SendMessage prohibitedName(Long chatId, String text) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "Sorry but you can't add this movie" + " " + text + "!!!"
        );
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Add")
                                .callbackData("add_movie")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Roll")
                                .callbackData("roll_movie")
                                .build()
                )
        );
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);

        return sendMessage;
    }

}
