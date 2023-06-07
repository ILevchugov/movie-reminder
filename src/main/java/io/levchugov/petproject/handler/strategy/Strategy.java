package io.levchugov.petproject.handler.strategy;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

public interface Strategy {

    PartialBotApiMethod<? extends Serializable> invoke(CallbackQuery callback);

    CallbackData getTypeOfCallback();
}
