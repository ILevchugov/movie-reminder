package io.levchugov.petproject.handler;

import io.levchugov.petproject.telegram.MovieTelegramBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

public interface BotUpdateHandler {

    PartialBotApiMethod<? extends Serializable> handle(Update update, MovieTelegramBot movieTelegramBot);

}
