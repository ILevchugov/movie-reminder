package io.levchugov.petproject.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

public interface CallbackHandler {

    BotApiMethod<? extends Serializable> handle(CallbackQuery callback);

}
