package io.levchugov.petproject.config;

import io.levchugov.petproject.handler.strategy.*;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class StrategiesConfig {

    @Bean
    public ConfirmMovieStrategy confirmMovieStrategy(MovieJdbcRepository movieJdbcRepository) {
        return new ConfirmMovieStrategy(movieJdbcRepository);
    }

    @Bean
    public RollMovieStrategy rollMovieStrategy(MovieJdbcRepository movieJdbcRepository) {
        return new RollMovieStrategy(movieJdbcRepository);
    }

    @Bean
    public ShowNextMovieStrategy showNextMovieStrategy(MovieJdbcRepository movieJdbcRepository) {
        return new ShowNextMovieStrategy(movieJdbcRepository);
    }

    @Bean
    public WatchedMovieStrategy watchedMovieStrategy(MovieJdbcRepository movieJdbcRepository) {
        return new WatchedMovieStrategy(movieJdbcRepository);
    }

    @Bean
    public AddMovieStrategy addMovieStrategy() {
        return new AddMovieStrategy();
    }

    @Bean
    public CancelStrategy cancelStrategy() {
        return new CancelStrategy();
    }

    @Bean
    public DefaultCallbackStrategy defaultCallbackStrategy() {
        return new DefaultCallbackStrategy();
    }

    @Bean
    Map<CallbackData, Strategy> strategyMap(
            List<Strategy> strategies
    ) {
        return strategies.stream()
                .collect(Collectors.toMap(Strategy::getTypeOfCallback, Function.identity()));
    }
}
