package io.levchugov.petproject.handler;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

public interface CallbackHandler {

    PartialBotApiMethod<? extends Serializable> handle(CallbackQuery callback);

}
