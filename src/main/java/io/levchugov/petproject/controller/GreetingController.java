package io.levchugov.petproject.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    @GetMapping("/greeting")
    public ResponseEntity<String> getGreeting() {
        return ResponseEntity.ok(
                "hello, this is the server of Ivan Levchugov." +
                        " It handles t.me/your_movie_reminder_bot"
        );
    }

}
