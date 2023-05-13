package io.levchugov.petproject.usecase;

import io.levchugov.petproject.repository.MovieJdbcRepository;
import io.levchugov.petproject.telegram.MovieTelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SendMessageToAllChatsUseCase {

    private final MovieJdbcRepository movieJdbcRepository;
    private final MovieTelegramBot movieTelegramBot;

    public SendMessageResponse invoke(String message) {
        List<Long> sentList = new ArrayList<>();
        List<Long> failedList = new ArrayList<>();
        movieJdbcRepository.findAllChatIds()
                .forEach(chatId -> {
                    SendMessage sendMessage = new SendMessage();
                    sendMessage.setText(message);
                    sendMessage.setChatId(chatId);
                    sendMessage.setParseMode("HTML");
                    try {
                        movieTelegramBot.execute(sendMessage);
                        sentList.add(chatId);
                    } catch (TelegramApiException e) {
                        failedList.add(chatId);
                        log.error("Didn't manage to send a message {} for {}", message, chatId, e);
                    }
                });

        return new SendMessageResponse(sentList, failedList);
    }

}
