package io.levchugov.petproject.controller;

import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMovieList(
            @RequestParam(value = "chatId", required = false) Long chatId,
            @RequestParam(value = "unitsOnPage", defaultValue = "100", required = false) int unitsOnPage,
            @RequestParam(value = "size", defaultValue = "0", required = false) int size
    ) {
        return ResponseEntity.ok(movieService.getMoviesByPagination(chatId, unitsOnPage, size));

    }

}
