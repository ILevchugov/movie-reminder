package io.levchugov.petproject.service;

import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieJdbcRepository movieJdbcRepository;

    @Override
    public List<Movie> getMoviesByPagination(Long chatId, int page, int numberOfUnitInOnePage) {
        return movieJdbcRepository.findAllMoviesSeparatedBySizedPages(chatId, page, numberOfUnitInOnePage);
    }
}
