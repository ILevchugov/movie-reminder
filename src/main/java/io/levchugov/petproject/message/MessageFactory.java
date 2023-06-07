package io.levchugov.petproject.message;

import io.levchugov.petproject.client.model.imdb.SearchMovieResult;
import io.levchugov.petproject.handler.strategy.CallbackData;
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
                                .callbackData(CallbackData.CONFIRM_MOVIE.getExplanation() + "_" + movie.id())
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("No")
                                .callbackData(CallbackData.SHOW_NEXT_MOVIE.getExplanation() + "_" + counter.toString())
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Cancel")
                                .callbackData(CallbackData.CANCEL.getExplanation())
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
                                .callbackData(CallbackData.ADD_MOVIE.getExplanation())
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Pick")
                                .callbackData(CallbackData.ROLL_MOVIE.getExplanation())
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
                                .callbackData(CallbackData.ADD_MOVIE.getExplanation())
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
                                .callbackData(CallbackData.ADD_MOVIE.getExplanation())
                                .build(),
                        InlineKeyboardButton.builder()
                                .text("Pick")
                                .callbackData(CallbackData.ROLL_MOVIE.getExplanation())
                                .build()
                )
        );
        rowList.add(List.of(
                InlineKeyboardButton.builder()
                        .text("Watched")
                        .callbackData(CallbackData.WATCHED_MOVIE.getExplanation() + "_" + movie.id())
                        .build()
        ));
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
