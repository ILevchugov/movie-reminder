package io.levchugov.petproject.controller;

import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import io.levchugov.petproject.service.MovieService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@AllArgsConstructor
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/movies")
    public ResponseEntity<List<Movie>> getMovieList (
            @RequestParam(value = "chatId") Long chatId,
            @RequestParam(value = "page") int page,
            @RequestParam(value = "unitsOnPage") int unitsOnPage
    )
    {
        return ResponseEntity.ok(movieService.getMoviesByPagination(chatId, page, unitsOnPage));

    }

}
