package io.levchugov.petproject.telegram;

import io.levchugov.petproject.MessageFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieTelegramBot extends TelegramLongPollingBot {

    private final static Map<Long, List<String>> MOVIE_MAP = new ConcurrentHashMap<>();
    private final static Map<Long, Boolean> AWAITS_TITLE = new ConcurrentHashMap<>();

    @Value(value = "${config.telegram.bot.username}")
    private String botUserName;
    @Value(value = "${config.telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            log.info("received update {}", update.getMessage());
            log.info("received callback {}", update.getCallbackQuery());

            var message = update.getMessage();
            var callback = update.getCallbackQuery();
            if (callback == null) {
                if (AWAITS_TITLE.get(message.getChatId()) != null && AWAITS_TITLE.get(message.getChatId())) {
                    if (message.getText().equals("Hui")) {
                        execute(MessageFactory.prohibitedName(message.getChatId(), message.getText()));
                        return;
                    }
                    var movieList = MOVIE_MAP.getOrDefault(message.getChatId(), new ArrayList<>());
                    movieList.add(message.getText());
                    MOVIE_MAP.put(message.getChatId(), movieList);
                    AWAITS_TITLE.put(message.getChatId(), Boolean.FALSE);
                    execute(MessageFactory.gotIt(message.getChatId()));
                } else {
                    execute(MessageFactory.greeting(update));
                }
            } else {
                if (callback.getData().equals("add_movie")) {
                    execute(MessageFactory.enterName(update));
                    AWAITS_TITLE.put(update.getCallbackQuery().getMessage().getChatId(), Boolean.TRUE);
                }
                if (callback.getData().equals("roll_movie")) {
                    var movies = MOVIE_MAP.getOrDefault(update.getCallbackQuery().getMessage().getChatId(), new ArrayList<>());
                    var movie = "nothing";
                    if (!movies.isEmpty()) {
                        Random random = new Random();
                        movie = movies.get(random.nextInt(movies.size()));
                    }
                    execute(MessageFactory.presentMovie(movie, update.getCallbackQuery().getMessage().getChatId()));
                    AWAITS_TITLE.put(update.getCallbackQuery().getMessage().getChatId(), Boolean.FALSE);
                }
            }
        } catch (Exception e) {
            log.error("error while process update", e);
        }
    }
}
