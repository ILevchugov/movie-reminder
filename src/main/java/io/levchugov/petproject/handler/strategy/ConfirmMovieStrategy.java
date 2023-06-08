package io.levchugov.petproject.handler.strategy;

import io.levchugov.petproject.message.MessageFactory;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

@RequiredArgsConstructor
@Component
public class ConfirmMovieStrategy implements CallBackProcessStrategy {

    private final MovieJdbcRepository movieJdbcRepository;

    @Override
    public PartialBotApiMethod<? extends Serializable> invoke(CallbackQuery callback) {
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

    @Override
    public CallbackData getTypeOfCallback() {
        return CallbackData.CONFIRM_MOVIE;
    }
}
