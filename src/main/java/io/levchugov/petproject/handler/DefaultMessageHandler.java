package io.levchugov.petproject.handler;

import io.levchugov.petproject.client.ImdbApiClient;
import io.levchugov.petproject.client.TextRecognitionClient;
import io.levchugov.petproject.message.MessageFactory;
import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import io.levchugov.petproject.telegram.MovieTelegramBot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Voice;

import java.io.File;
import java.io.Serializable;

import static io.levchugov.petproject.repository.InMemoryStorage.AWAITS_TITLE;
import static io.levchugov.petproject.repository.InMemoryStorage.RESPONSE_FROM_IMDB;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultMessageHandler implements MessageHandler {

    private final MovieJdbcRepository movieJdbcRepository;
    private final ImdbApiClient imdbApiClient;
    private final TextRecognitionClient textRecognitionClient;

    @Override
    public PartialBotApiMethod<? extends Serializable> handle(Message message, MovieTelegramBot movieTelegramBot) {
        if (AWAITS_TITLE.get(message.getChatId()) != null && AWAITS_TITLE.get(message.getChatId())) {
            String title;
            if (message.getVoice() != null) {
                title = processVoice(message.getVoice(), movieTelegramBot);
                if (title == null) {
                    return MessageFactory.defaultMessage(message.getChatId());
                }
            } else {
                title = message.getText();
            }
            var response = imdbApiClient.findMovie(title);
            RESPONSE_FROM_IMDB.put(message.getChatId(), response.results());
            var movies = response.results();

            log.info("found {} ", response);
            if (!movies.isEmpty()) {
                var movie = movies.get(0);
                var count = movieJdbcRepository.findById(movie.id());
                if (count == 0) {
                    movieJdbcRepository.save(
                            new Movie(
                                    movie.id(),
                                    movie.title(),
                                    movie.image(),
                                    movie.description()
                            )
                    );
                }
                AWAITS_TITLE.put(message.getChatId(), Boolean.FALSE);

                try {
                    return MessageFactory.movieConfirmation(
                            message.getChatId(),
                            movies.get(0),
                            1
                    );
                } catch (Exception e) {
                    return MessageFactory.gotIt(message.getChatId());
                }
            } else {
                return MessageFactory.foundNothing(message.getChatId(), message.getText());
            }
        } else {
            return MessageFactory.greeting(message.getChatId());
        }
    }

    private String processVoice(Voice voice, MovieTelegramBot movieTelegramBot) {
        try {
            var fileId = voice.getFileId();

            GetFile getFile = new GetFile();
            getFile.setFileId(fileId);
            String filePath = movieTelegramBot.execute(getFile).getFilePath();
            File inputAudio = movieTelegramBot.downloadFile(filePath);
            log.info("file {}", inputAudio.getName());


            var result = textRecognitionClient.recognize(inputAudio);
            log.info("Result: {}", result);

            return result;
        } catch (Exception e) {
            log.error("Error while recognition: ", e);
            return null;
        }
    }
}
