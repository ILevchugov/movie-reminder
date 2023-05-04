package io.levchugov.petproject.handler;

import io.levchugov.petproject.message.MessageFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import static io.levchugov.petproject.repository.InMemoryStorage.AWAITS_TITLE;
import static io.levchugov.petproject.repository.InMemoryStorage.MOVIE_MAP;

@Component
public class DefaultCallbackHandler implements CallbackHandler {

    @Override
    public BotApiMethod<? extends Serializable> handle(CallbackQuery callback) {
        if (callback.getData().equals("add_movie")) {
            AWAITS_TITLE.put(callback.getMessage().getChatId(), Boolean.TRUE);
            return MessageFactory.enterName(callback.getMessage().getChatId());

        }
        if (callback.getData().equals("roll_movie")) {
            var movies = MOVIE_MAP.getOrDefault(callback.getMessage().getChatId(), new ArrayList<>());
            var movie = "nothing";
            if (!movies.isEmpty()) {
                Random random = new Random();
                movie = movies.get(random.nextInt(movies.size()));
            }
            AWAITS_TITLE.put(callback.getMessage().getChatId(), Boolean.FALSE);
            return MessageFactory.presentMovie(movie, callback.getMessage().getChatId());
        }
        return MessageFactory.greeting(callback.getMessage().getChatId());
    }

}
