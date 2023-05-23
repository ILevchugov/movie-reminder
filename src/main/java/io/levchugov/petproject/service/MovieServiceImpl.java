package io.levchugov.petproject.service;

import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@AllArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieJdbcRepository movieJdbcRepository;

    @Override
    public List<Movie> getMoviesByPagination(Long chatId, int numberOfUnitInOnePage, int size) {
        if (chatId == null || !movieJdbcRepository.findAllChatIds().contains(chatId)) {
            return movieJdbcRepository.findAllSavedMovieInDB();
        } else {
            return movieJdbcRepository.findAllMoviesSeparatedBySizedPages(chatId, numberOfUnitInOnePage, size);
        }
    }
}
