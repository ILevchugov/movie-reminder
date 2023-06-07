package io.levchugov.petproject.handler.strategy;

import io.levchugov.petproject.message.MessageFactory;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

import static io.levchugov.petproject.repository.InMemoryStorage.AWAITS_TITLE;

public class AddMovieStrategy implements Strategy {

    @Override
    public PartialBotApiMethod<? extends Serializable> invoke(CallbackQuery callback) {
        AWAITS_TITLE.put(callback.getMessage().getChatId(), Boolean.TRUE);
        return MessageFactory.enterName(callback.getMessage().getChatId());
    }

    @Override
    public CallbackData getTypeOfCallback() {
        return CallbackData.ADD_MOVIE;
    }

}
