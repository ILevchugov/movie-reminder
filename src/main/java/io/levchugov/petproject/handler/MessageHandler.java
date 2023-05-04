package io.levchugov.petproject.handler;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.Serializable;

public interface MessageHandler {

    BotApiMethod<? extends Serializable> handle(Message message);

}
