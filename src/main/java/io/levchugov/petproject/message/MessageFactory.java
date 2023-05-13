package io.levchugov.petproject.message;

import io.levchugov.petproject.client.model.imdb.SearchMovieResult;
import io.levchugov.petproject.model.Movie;
import lombok.experimental.UtilityClass;
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

@UtilityClass
public class MessageFactory {
    public static SendMessage greeting(Long chatId) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "Hi, you can share with me any movie, which you want to watch near future:)\n" +
                        "Or I'll pick it randomly if you already have the list)"
        );
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(buildAndAndRollButtons());
        return sendMessage;
    }

    public static SendMessage defaultMessage(Long chatId) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "You can share or pick:)"
        );
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(buildAndAndRollButtons());
        return sendMessage;
    }

    public static SendMessage enterName(Long chatId) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "Text me the movie title or send me a voice"
        );
        sendMessage.setChatId(chatId);
        return sendMessage;
    }

    public static SendMessage gotIt(Long chatId) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "Got it! You can add more movies, or pick!"
        );
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(buildAndAndRollButtons());
        return sendMessage;
    }

    public static SendPhoto movieConfirmation(
            Long chatId,
            SearchMovieResult movie,
            Integer counter
    ) throws IOException {
        SendPhoto sendPhoto = new SendPhoto();

        URL u = new URL(movie.image());
        InputStream in = u.openStream();

        var file = new InputFile();
        file.setMedia(in, "movie_pic.jpg");
        sendPhoto.setPhoto(file);
        sendPhoto.setChatId(chatId);
        sendPhoto.setCaption("Correct?");
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Yes")
                                .callbackData("confirm_movie_" + movie.id())
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("No")
                                .callbackData("show_next_movie_" + counter)
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Cancel")
                                .callbackData("cancel")
                                .build()
                )
        );
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        sendPhoto.setReplyMarkup(inlineKeyboardMarkup);
        return sendPhoto;
    }

    public static PartialBotApiMethod<Message> presentMovie(Movie movie, Long chatId) {
        var buttons = buildAndAndRollAndWatchedButtons(movie);
        if ("nothing".equals(movie.title()) || movie.title() == null) {
            var sendMessage = new SendMessage();
            sendMessage.setText(
                    "Tonight you will watch" + " " + movie.title() + "!!!"
            );
            sendMessage.setReplyMarkup(buildAddButton());
            sendMessage.setChatId(chatId);
            return sendMessage;
        }
        try {
            SendPhoto sendPhoto = new SendPhoto();

            URL u = new URL(movie.image());
            InputStream in = u.openStream();
            var file = new InputFile();
            file.setMedia(in, "movie_pic.jpg");
            sendPhoto.setPhoto(file);
            sendPhoto.setChatId(chatId);
            sendPhoto.setCaption("Tonight you will watch " + movie.title() + "!!!");
            sendPhoto.setReplyMarkup(buttons);
            return sendPhoto;
        } catch (IOException e) {
            var sendMessage = new SendMessage();
            sendMessage.setText(
                    "Tonight you will watch" + " " + movie.title() + "!!!"
            );
            sendMessage.setReplyMarkup(buttons);
            sendMessage.setChatId(chatId);
            return sendMessage;
        }
    }


    public static SendMessage foundNothing(Long chatId, String text) {
        var sendMessage = new SendMessage();
        sendMessage.setText(
                "Found nothing(" + " " + text + "!!!"
        );
        sendMessage.setChatId(chatId);
        sendMessage.setReplyMarkup(buildAndAndRollButtons());

        return sendMessage;
    }

    private static InlineKeyboardMarkup buildAndAndRollButtons() {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Add")
                                .callbackData("add_movie")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Pick")
                                .callbackData("roll_movie")
                                .build()
                )
        );
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    private static InlineKeyboardMarkup buildAddButton() {
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
        return inlineKeyboardMarkup;
    }

    private static InlineKeyboardMarkup buildAndAndRollAndWatchedButtons(Movie movie) {
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(
                List.of(
                        InlineKeyboardButton.builder()
                                .text("Add")
                                .callbackData("add_movie")
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Pick")
                                .callbackData("roll_movie")
                                .build()
                )
        );
        rowList.add(List.of(
                InlineKeyboardButton.builder()
                        .text("Watched")
                        .callbackData("watched_movie_" + movie.id())
                        .build()
        ));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
