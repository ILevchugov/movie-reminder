package io.levchugov.petproject.telegram;

import io.levchugov.petproject.handler.BotUpdateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieTelegramBot extends TelegramLongPollingBot {

    private final BotUpdateHandler botUpdateHandler;

    @Value(value = "${config.telegram.bot.username}")
    private String botUserName;
    @Value(value = "${config.telegram.bot.token}")
    private String botToken;

    @Override
    public String getBotUsername() {
        return botUserName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    @Override
    public void onUpdateReceived(Update update) {
        try {
            log.info("received update {}", update.getMessage());
            log.info("received callback {}", update.getCallbackQuery());
            execute(botUpdateHandler.handle(update));
        } catch (Exception e) {
            log.error("error while process update", e);
        }
    }
}
