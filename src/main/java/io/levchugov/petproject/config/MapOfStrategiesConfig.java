package io.levchugov.petproject.config;

import io.levchugov.petproject.handler.strategy.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
public class MapOfStrategiesConfig {

    @Bean
    Map<CallbackData, CallBackProcessStrategy> strategyMap(
            List<CallBackProcessStrategy> strategies
    ) {
        return strategies.stream()
                .collect(Collectors.toMap(CallBackProcessStrategy::getTypeOfCallback, Function.identity()));
    }
}
