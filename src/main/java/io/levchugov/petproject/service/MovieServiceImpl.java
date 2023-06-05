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
    public List<Movie> getMoviesByPagination(String title, Long chatId, int size, int page) {
        if (chatId == null && title == null) {
            return movieJdbcRepository.findAllSavedMovieInDB(size, page);
        }
        if (chatId == null) {
            return movieJdbcRepository.findMovieByTitle(title);
        }
        if (!movieJdbcRepository.findAllChatIds().contains(chatId)) {
            return new ArrayList<>();
        }
        if (title == null) {
            return movieJdbcRepository.findAllMoviesSeparatedBySizedPages(chatId, size, page);
        }
        return movieJdbcRepository.findMovieOfUserByTitle(title, chatId);
    }
}
