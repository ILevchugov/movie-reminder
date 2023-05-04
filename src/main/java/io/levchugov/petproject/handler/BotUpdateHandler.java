package io.levchugov.petproject.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

public interface BotUpdateHandler {

    BotApiMethod<? extends Serializable> handle(Update update);

}
