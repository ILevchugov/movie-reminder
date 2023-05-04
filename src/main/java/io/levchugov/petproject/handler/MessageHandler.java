package io.levchugov.petproject.handler;

import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;

public interface MessageHandler {

    PartialBotApiMethod<? extends Serializable> handle(Message message);

}
