package io.levchugov.petproject.controller;

import io.levchugov.petproject.controller.model.Message;
import io.levchugov.petproject.usecase.SendMessageResponse;
import io.levchugov.petproject.usecase.SendMessageToAllChatsUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AdminController {

    @Value(value = "${config.telegram.bot.admin-token}")
    private String botToken;

    private final SendMessageToAllChatsUseCase sendMessageToAllChatsUseCase;

    @PostMapping("/admin/{adminKey}/message")
    public ResponseEntity<SendMessageResponse> sendMessage(
            @PathVariable String adminKey,
            @RequestBody Message message
    ) {
        if (adminKey.equals(botToken)) {
            return ResponseEntity.ok(
                    sendMessageToAllChatsUseCase.invoke(message.text())
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).build();
        }
    }

}
