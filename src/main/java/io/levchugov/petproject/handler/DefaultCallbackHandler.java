package io.levchugov.petproject.handler;

import io.levchugov.petproject.message.MessageFactory;
import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;
import java.util.Random;

import static io.levchugov.petproject.repository.InMemoryStorage.AWAITS_TITLE;
import static io.levchugov.petproject.repository.InMemoryStorage.RESPONSE_FROM_IMDB;

@Component
@RequiredArgsConstructor
public class DefaultCallbackHandler implements CallbackHandler {
    private static final Random random = new Random();

    private final MovieJdbcRepository movieJdbcRepository;

    @Override
    public PartialBotApiMethod<? extends Serializable> handle(CallbackQuery callback) {
        if (callback.getData().equals("add_movie")) {
            AWAITS_TITLE.put(callback.getMessage().getChatId(), Boolean.TRUE);
            return MessageFactory.enterName(callback.getMessage().getChatId());
        }
        if (callback.getData().equals("roll_movie")) {
            return roll(callback);
        }

        if (callback.getData().contains("watched_movie")) {
            var movieId = callback.getData().substring(14);
            movieJdbcRepository.markMovieWatched(
                    callback.getMessage().getChatId(),
                    movieId
            );
            return roll(callback);

        }
        if (callback.getData().equals("cancel")) {
            return MessageFactory.defaultMessage(callback.getMessage().getChatId());
        }

        if (callback.getData().contains("confirm_movie")) {
            var movieId = callback.getData().substring(14);
            var existed = movieJdbcRepository.count(
                    callback.getMessage().getChatId(), movieId
            );
            if (existed == 0) {
                movieJdbcRepository.saveMovieToWatchList(callback.getMessage().getChatId(), movieId);
            } else {
                movieJdbcRepository.markMovieUnWatched(callback.getMessage().getChatId(), movieId);
            }
            return MessageFactory.gotIt(callback.getMessage().getChatId());
        }

        if (callback.getData().contains("show_next_movie")) {
            var noCounter = Integer.parseInt(callback.getData().substring(16));
            var movies = RESPONSE_FROM_IMDB.get(callback.getMessage().getChatId());
            if (!movies.isEmpty() && movies.size() >= noCounter) {
                var movie = movies.get(noCounter);
                var count = movieJdbcRepository.findById(movie.id());
                if (count == 0) {
                    movieJdbcRepository.save(
                            new Movie(
                                    movie.id(),
                                    movie.title(),
                                    movie.image(),
                                    movie.description()
                            )
                    );
                }
                AWAITS_TITLE.put(callback.getMessage().getChatId(), Boolean.FALSE);
                try {
                    return MessageFactory.movieConfirmation(
                            callback.getMessage().getChatId(),
                            movies.get(noCounter),
                            noCounter + 1
                    );
                } catch (Exception e) {
                    return MessageFactory.gotIt(callback.getMessage().getChatId());
                }
            } else {
                return MessageFactory.foundNothing(callback.getMessage().getChatId(), ":(");
            }
        }
        return MessageFactory.greeting(callback.getMessage().getChatId());
    }

    private PartialBotApiMethod<Message> roll(CallbackQuery callback) {
        var chatId = callback.getMessage().getChatId();
        var movies = movieJdbcRepository.findUsersListToWatchByChatId(chatId);
        if (movies.isEmpty()) {
            movieJdbcRepository.markAllMovieNotPicked(chatId);
            movies = movieJdbcRepository.findUsersListToWatchByChatId(chatId);
        }
        var movie = new Movie(null, "nothing", null, null);
        if (!movies.isEmpty()) {
            movie = movies.get(random.nextInt(movies.size()));
            movieJdbcRepository.markMoviePicked(chatId, movie.id());
        }
        AWAITS_TITLE.put(callback.getMessage().getChatId(), Boolean.FALSE);
        return MessageFactory.presentMovie(movie, callback.getMessage().getChatId());
    }

}
