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
    public List<Movie> getMoviesByPagination(String title, Long chatId, int numberOfUnitInOnePage, int size) {
        if (chatId == null && title == null)
            return movieJdbcRepository.findAllSavedMovieInDB();
        else if(chatId == null)
            return movieJdbcRepository.findMovieByTitle(title);
        else if (!movieJdbcRepository.findAllChatIds().contains(chatId))
            return new ArrayList<>();
        else if (title == null)
            return movieJdbcRepository.findAllMoviesSeparatedBySizedPages(chatId, numberOfUnitInOnePage, size);
        else
            return movieJdbcRepository.findMovieOfUserByTitle(title, chatId);
    }
}
