package io.levchugov.petproject.repository;

import io.levchugov.petproject.PostgresExtension;
import io.levchugov.petproject.config.BotInitializer;
import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.telegram.MovieTelegramBot;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith({PostgresExtension.class, SpringExtension.class})
@ActiveProfiles({"test"})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MovieJdbcRepositoryTest {

    @MockBean
    MovieTelegramBot movieTelegramBot;

    @MockBean
    BotInitializer botInitializer;

    @Autowired
    MovieJdbcRepository movieJdbcRepository;

    @Test
    void saveTestOk() {
        movieJdbcRepository.save(new Movie("id", "title", "image", "desc"));
        var actual = movieJdbcRepository.findById("id");
        Assertions.assertEquals(1, actual);
    }
}