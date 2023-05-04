package io.levchugov.petproject.handler;

import io.levchugov.petproject.message.MessageFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;

import static io.levchugov.petproject.repository.InMemoryStorage.AWAITS_TITLE;
import static io.levchugov.petproject.repository.InMemoryStorage.MOVIE_MAP;

@Component
public class DefaultMessageHandler implements MessageHandler {

    @Override
    public SendMessage handle(Message message) {
        if (AWAITS_TITLE.get(message.getChatId()) != null && AWAITS_TITLE.get(message.getChatId())) {
            if (message.getText().equals("Hui")) {
                return MessageFactory.prohibitedName(message.getChatId(), message.getText());
            }
            var movieList = MOVIE_MAP.getOrDefault(message.getChatId(), new ArrayList<>());
            movieList.add(message.getText());
            MOVIE_MAP.put(message.getChatId(), movieList);
            AWAITS_TITLE.put(message.getChatId(), Boolean.FALSE);
            return MessageFactory.gotIt(message.getChatId());
        } else {
            return MessageFactory.greeting(message.getChatId());
        }
    }
}
