package io.levchugov.petproject.service;

import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
class MovieServiceImplTest {
    @MockBean
    private MovieJdbcRepository movieJdbcRepository;
    private MovieService movieService;
    private final List<Movie> listForCheckFirstAndSecondAndFifthCondition = new ArrayList<>();
    private final List<Movie> listForCheckFourthCondition = new ArrayList<>();

    @BeforeEach()
    public void init() {
        movieService = new MovieServiceImpl(movieJdbcRepository);
        listForCheckFirstAndSecondAndFifthCondition.add(new Movie("tt1375666", "Inception", "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg", "2010 Leonardo DiCaprio, Joseph Gordon-Levitt"));
        listForCheckFourthCondition.addAll(List.of(
                new Movie("tt1375666", "Inception",
                        "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg",
                        "2010 Leonardo DiCaprio, Joseph Gordon-Levitt"),
                new Movie("tt4154664", "Captain Marvel",
                        "https://m.media-amazon.com/images/M/MV5BMTE0YWFmOTMtYTU2ZS00ZTIxLWE3OTEtYTNiYzBkZjViZThiXkEyXkFqcGdeQXVyODMzMzQ4OTI@._V1_Ratio0.6757_AL_.jpg",
                        "2019 Brie Larson, Samuel L. Jackson")));
    }

    @Test
    void testFirstCondition() {
        Mockito.when(movieJdbcRepository
                .findAllSavedMovieInDB())
                .thenReturn(List.of(
                        new Movie("tt1375666", "Inception",
                                "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg",
                                "2010 Leonardo DiCaprio, Joseph Gordon-Levitt")
                ));

        List<Movie> movieList = movieService.getMoviesByPagination(null, null, 0, 100);
        assertEquals(listForCheckFirstAndSecondAndFifthCondition, movieList);
    }

    @Test
    void testSecondCondition() {
        Mockito.when(movieJdbcRepository
                        .findMovieByTitle("Inception"))
                .thenReturn(List.of(
                        new Movie("tt1375666", "Inception",
                                "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg",
                                "2010 Leonardo DiCaprio, Joseph Gordon-Levitt")
                ));

        List<Movie> movieList = movieService.getMoviesByPagination("Inception", null, 0, 100);
        assertEquals(listForCheckFirstAndSecondAndFifthCondition, movieList);
    }

    @Test
    void testThirdCondition() {
        Mockito.when(movieJdbcRepository
                .findAllChatIds())
                .thenReturn(List.of(11111L, 222222L));

        List<Movie> movieList = new ArrayList<>();
        assertEquals(movieList, movieService.getMoviesByPagination("Inception", 12342314L, 0, 100));

    }

    @Test
    void fourthCondition() {
        Mockito.when(movieJdbcRepository
                        .findAllChatIds())
                .thenReturn(Collections.singletonList(277787442L));

        Mockito.when(movieJdbcRepository
                .findAllMoviesSeparatedBySizedPages(277787442L, 0, 100))
                .thenReturn(List.of(
                        new Movie("tt1375666", "Inception",
                                "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg",
                                "2010 Leonardo DiCaprio, Joseph Gordon-Levitt"),
                        new Movie("tt4154664", "Captain Marvel",
                                "https://m.media-amazon.com/images/M/MV5BMTE0YWFmOTMtYTU2ZS00ZTIxLWE3OTEtYTNiYzBkZjViZThiXkEyXkFqcGdeQXVyODMzMzQ4OTI@._V1_Ratio0.6757_AL_.jpg",
                                "2019 Brie Larson, Samuel L. Jackson")

                ));
        List<Movie> movieList = movieService.getMoviesByPagination(null, 277787442L, 0, 100);
        assertEquals(listForCheckFourthCondition, movieList);

    }

    @Test
    void testForFifthCondition() {
        Mockito.when(movieJdbcRepository
                        .findAllChatIds())
                .thenReturn(Collections.singletonList(277787442L));
        Mockito.when(movieJdbcRepository
                .findMovieOfUserByTitle("Inception", 277787442L))
                .thenReturn(List.of(
                        new Movie("tt1375666", "Inception",
                                "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg",
                                "2010 Leonardo DiCaprio, Joseph Gordon-Levitt")
                ));

        List<Movie> movieList = movieService.getMoviesByPagination("Inception", 277787442L, 0 ,100);
        assertEquals(listForCheckFirstAndSecondAndFifthCondition, movieList);
    }

}