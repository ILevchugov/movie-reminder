package io.levchugov.petproject.handler.strategy;

import io.levchugov.petproject.message.MessageFactory;
import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;
import java.util.Random;

import static io.levchugov.petproject.repository.InMemoryStorage.AWAITS_TITLE;

@RequiredArgsConstructor
public class WatchedMovieStrategy implements Strategy {

    private static final Random random = new Random();

    private final MovieJdbcRepository movieJdbcRepository;

    @Override
    public PartialBotApiMethod<? extends Serializable> invoke(CallbackQuery callback) {
        var movieId = callback.getData().substring(14);
        movieJdbcRepository.markMovieWatched(callback.getMessage().getChatId(), movieId);
        return roll(callback);
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

    @Override
    public CallbackData getTypeOfCallback() {
        return CallbackData.WATCHED_MOVIE;
    }
}
