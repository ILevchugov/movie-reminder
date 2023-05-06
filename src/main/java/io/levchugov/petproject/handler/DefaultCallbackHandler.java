package io.levchugov.petproject.handler;

import io.levchugov.petproject.message.MessageFactory;
import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;
import java.util.Random;

import static io.levchugov.petproject.repository.InMemoryStorage.AWAITS_TITLE;
import static io.levchugov.petproject.repository.InMemoryStorage.MOVIES_AWAITS_CONFIRMATION;
import static io.levchugov.petproject.repository.InMemoryStorage.NO_COUNTER;
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
        if (callback.getData().equals("confirm_movie")) {
            var movieId = MOVIES_AWAITS_CONFIRMATION.get(callback.getMessage().getChatId());
            if (movieId != null) {
                var existed = movieJdbcRepository.count(
                        callback.getMessage().getChatId(), movieId
                );
                if (existed == 0) {
                    movieJdbcRepository.saveMovieToWatchList(callback.getMessage().getChatId(), movieId);
                }
                return MessageFactory.gotIt(callback.getMessage().getChatId());
            } else {
                return MessageFactory.greeting(callback.getMessage().getChatId());
            }
        }
        if (callback.getData().equals("show_next_movie")) {
            var noCounter = NO_COUNTER.getOrDefault(callback.getMessage().getChatId(), 0);
            noCounter++;
            var movies = RESPONSE_FROM_IMDB.get(callback.getMessage().getChatId());
            NO_COUNTER.put(callback.getMessage().getChatId(), noCounter);
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
                MOVIES_AWAITS_CONFIRMATION.put(
                        callback.getMessage().getChatId(),
                        movies.get(noCounter).id()
                );
                try {
                    return MessageFactory.movieConfirmation(
                            callback.getMessage().getChatId(),
                            movies.get(noCounter).image());
                } catch (Exception e) {
                    return MessageFactory.gotIt(callback.getMessage().getChatId());
                }
            } else {
                return MessageFactory.foundNothing(callback.getMessage().getChatId(), ":(");
            }
        }
        return MessageFactory.greeting(callback.getMessage().getChatId());
    }

}
