package io.levchugov.petproject.handler.strategy;

import io.levchugov.petproject.message.MessageFactory;
import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

import static io.levchugov.petproject.repository.InMemoryStorage.AWAITS_TITLE;
import static io.levchugov.petproject.repository.InMemoryStorage.RESPONSE_FROM_IMDB;

@RequiredArgsConstructor
@Component
public class ShowNextMovieStrategy implements CallBackProcessStrategy {

    private final MovieJdbcRepository movieJdbcRepository;

    @Override
    public PartialBotApiMethod<? extends Serializable> invoke(CallbackQuery callback) {
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

    @Override
    public CallbackData getTypeOfCallback() {
        return CallbackData.SHOW_NEXT_MOVIE;
    }
}
