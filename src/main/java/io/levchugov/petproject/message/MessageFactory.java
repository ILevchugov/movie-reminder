package io.levchugov.petproject.message;

import io.levchugov.petproject.model.Movie;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MessageFactory {
    public static SendMessage greeting(Long chatId) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "Hi, you can share with me any movie, which you want to want near future:) +\n" +
                        "Or roll if you've already have one)"
        );
        sendMessage.setChatId(chatId);

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

        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        return sendMessage;
    }

    public static SendMessage enterName(Long chatId) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "text me title"
        );
        sendMessage.setChatId(chatId);
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

    public static SendPhoto movieConfirmation(
            Long chatId,
            String url
    ) throws IOException {
        SendPhoto sendPhoto = new SendPhoto();

        URL u = new URL(url);
        InputStream in = u.openStream();

        var file = new InputFile();
        file.setMedia(in, "movie_pic.jpg");
        sendPhoto.setPhoto(file);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption("correct?");
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Yes")
                                .callbackData("confirm_movie")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("No")
                                .callbackData("show_next_movie")
                                .build()
                )
        );
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        return sendPhoto;
    }

    public static PartialBotApiMethod<Message> presentMovie(Movie movie, Long chatId) {
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

        try {
            SendPhoto sendPhoto = new SendPhoto();

            URL u = new URL(movie.image());
            InputStream in = u.openStream();
            var file = new InputFile();
            file.setMedia(in, "movie_pic.jpg");
            sendPhoto.setPhoto(file);
            sendPhoto.setChatId(chatId);
            sendPhoto.setCaption("Tonight you will watch " + movie.title() + "!!!");
            sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
            return sendPhoto;
        } catch (IOException e) {
            var sendMessage = new SendMessage();
            sendMessage.setText(
                    "Tonight you will watch" + " " + movie.title() + "!!!"
            );
            sendMessage.setReplyMarkup(inlineKeyboardMarkup);
            sendMessage.setChatId(chatId);
            return sendMessage;
        }
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

    public static SendMessage foundNothing(Long chatId, String text) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "Found nothing(" + " " + text + "!!!"
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
