package io.levchugov.petproject.handler.strategy;

import io.levchugov.petproject.message.MessageFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;

@Component
public class DefaultCallbackStrategy implements CallBackProcessStrategy {


    @Override
    public PartialBotApiMethod<? extends Serializable> invoke(CallbackQuery callback) {
        return MessageFactory.greeting(callback.getMessage().getChatId());
    }

    @Override
    public CallbackData getTypeOfCallback() {
        return CallbackData.DEFAULT_CALLBACK;
    }
}
