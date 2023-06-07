package io.levchugov.petproject.handler;

import io.levchugov.petproject.handler.strategy.CallbackData;
import io.levchugov.petproject.handler.strategy.Strategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

import java.io.Serializable;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DefaultCallbackHandler implements CallbackHandler {

    private final Map<CallbackData, Strategy> strategyMap;


    @Override
    public PartialBotApiMethod<? extends Serializable> handle(CallbackQuery callback) {

        return strategyMap.get(getCallbackDataFromItsStringValue(callback)).invoke(callback);
    }

    private CallbackData getCallbackDataFromItsStringValue(CallbackQuery callback) {
        Map<String, CallbackData> stringCallbackDataMap = strategyMap.keySet().stream()
                .collect(Collectors.toMap(CallbackData::getExplanation, Function.identity()));
        String[] datas = callback.getData().split("_");
        String data = "";
        if (datas.length == 3 || datas.length == 2) {
            data = datas[0] + "_" + datas[1];
        }
        if (datas.length == 4) {
            data = datas[0] + "_" + datas[1] + "_" + datas[2];
        }
        if (datas.length == 1) {
            data = datas[0];
        }
        return stringCallbackDataMap.get(data);
    }
}

