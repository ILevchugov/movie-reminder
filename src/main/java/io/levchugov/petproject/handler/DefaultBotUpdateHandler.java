package io.levchugov.petproject.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.io.Serializable;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultBotUpdateHandler implements BotUpdateHandler {

    private final MessageHandler messageHandler;
    private final CallbackHandler callbackHandler;

    @Override
    public PartialBotApiMethod<? extends Serializable> handle(Update update) {
        log.info("start handling update {}", update);

        var message = update.getMessage();
        var callback = update.getCallbackQuery();
        if (message != null) {
            return messageHandler.handle(message);
        }
        if (callback != null) {
            return callbackHandler.handle(callback);
        }

        log.info("finish handling update {}", update);
        return null;
    }

}
