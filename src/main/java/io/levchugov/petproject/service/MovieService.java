package io.levchugov.petproject.service;

import io.levchugov.petproject.model.Movie;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface MovieService {

    List<Movie> getMoviesByPagination(Long chatId, int numberOfUnitInOnePage, int size);
}
