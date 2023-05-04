package io.levchugov.petproject.handler;

import io.levchugov.petproject.client.ImdbApiClient;
import io.levchugov.petproject.message.MessageFactory;
import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;
import java.util.ArrayList;

import static io.levchugov.petproject.repository.InMemoryStorage.AWAITS_TITLE;
import static io.levchugov.petproject.repository.InMemoryStorage.MOVIES_AWAITS_CONFIRMATION;
import static io.levchugov.petproject.repository.InMemoryStorage.NO_COUNTER;
import static io.levchugov.petproject.repository.InMemoryStorage.RESPONSE_FROM_IMDB;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultMessageHandler implements MessageHandler {

    private final MovieJdbcRepository movieJdbcRepository;
    private final ImdbApiClient imdbApiClient;

    @Override
    public PartialBotApiMethod<? extends Serializable> handle(Message message) {
        if (AWAITS_TITLE.get(message.getChatId()) != null && AWAITS_TITLE.get(message.getChatId())) {
            var response = imdbApiClient.findMovie(message.getText());
            RESPONSE_FROM_IMDB.put(message.getChatId(), response.results());
            NO_COUNTER.put(message.getChatId(), 0);

            var movies = response.results();

            log.info("found {} ", response);
            log.info("after filter {}", movies);
            if (!movies.isEmpty()) {
                var movie = movies.get(0);
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
                AWAITS_TITLE.put(message.getChatId(), Boolean.FALSE);
                MOVIES_AWAITS_CONFIRMATION.put(
                        message.getChatId(),
                        movies.get(0).id()
                );
                try {
                    return MessageFactory.movieConfirmation(
                            message.getChatId(),
                            movies.get(0).image());
                } catch (Exception e) {
                    return MessageFactory.gotIt(message.getChatId());
                }
            } else {
                return MessageFactory.foundNothing(message.getChatId(), message.getText());
            }
        } else {
            return MessageFactory.greeting(message.getChatId());
        }
    }
}
