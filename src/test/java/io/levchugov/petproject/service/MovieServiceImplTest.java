package io.levchugov.petproject.service;

import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.repository.MovieJdbcRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MovieServiceImplTest {
    @MockBean
    private MovieJdbcRepository movieJdbcRepository;
    @Autowired
    private MovieService movieService;
    private static final List<Movie> listForCheckFirstAndSecondAndFifthCondition = new ArrayList<>();
    private static final List<Movie> listForCheckFourthCondition = new ArrayList<>();

    @BeforeAll()
    public static void init() {
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
                .findAllSavedMovieInDB(100, 0))
                .thenReturn(List.of(
                        new Movie("tt1375666", "Inception",
                                "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg",
                                "2010 Leonardo DiCaprio, Joseph Gordon-Levitt")
                ));

        List<Movie> movieList = movieService.getMoviesByPagination(null, null, 100, 0);
        assertEquals(listForCheckFirstAndSecondAndFifthCondition, movieList);
        Mockito.verify(movieJdbcRepository).findAllSavedMovieInDB(100, 0);
    }

    @Test
    void testSecondCondition() {
        Mockito.when(movieJdbcRepository
                        .findMovieByTitle("Inception", 100, 0))
                .thenReturn(List.of(
                        new Movie("tt1375666", "Inception",
                                "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg",
                                "2010 Leonardo DiCaprio, Joseph Gordon-Levitt")
                ));

        List<Movie> movieList = movieService.getMoviesByPagination("Inception", null, 100, 0);
        assertEquals(listForCheckFirstAndSecondAndFifthCondition, movieList);
    }

    @Test
    void testThirdCondition() {
        Mockito.when(movieJdbcRepository
                .findAllChatIds())
                .thenReturn(List.of(11111L, 222222L));

        List<Movie> movieList = new ArrayList<>();
        assertEquals(movieList, movieService.getMoviesByPagination("Inception", 12342314L, 100, 0));

    }

    @Test
    void fourthCondition() {
        Mockito.when(movieJdbcRepository
                        .findAllChatIds())
                .thenReturn(Collections.singletonList(277787442L));

        Mockito.when(movieJdbcRepository
                .findAllMoviesSeparatedBySizedPages(277787442L, 100, 0))
                .thenReturn(List.of(
                        new Movie("tt1375666", "Inception",
                                "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg",
                                "2010 Leonardo DiCaprio, Joseph Gordon-Levitt"),
                        new Movie("tt4154664", "Captain Marvel",
                                "https://m.media-amazon.com/images/M/MV5BMTE0YWFmOTMtYTU2ZS00ZTIxLWE3OTEtYTNiYzBkZjViZThiXkEyXkFqcGdeQXVyODMzMzQ4OTI@._V1_Ratio0.6757_AL_.jpg",
                                "2019 Brie Larson, Samuel L. Jackson")

                ));
        List<Movie> movieList = movieService.getMoviesByPagination(null, 277787442L, 100, 0);
        assertEquals(listForCheckFourthCondition, movieList);

    }

    @Test
    void testForFifthCondition() {
        Mockito.when(movieJdbcRepository
                        .findAllChatIds())
                .thenReturn(Collections.singletonList(277787442L));
        Mockito.when(movieJdbcRepository
                .findMovieOfUserByTitle("Inception", 277787442L, 100, 0))
                .thenReturn(List.of(
                        new Movie("tt1375666", "Inception",
                                "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg",
                                "2010 Leonardo DiCaprio, Joseph Gordon-Levitt")
                ));

        List<Movie> movieList = movieService.getMoviesByPagination("Inception", 277787442L, 100, 0);
        assertEquals(listForCheckFirstAndSecondAndFifthCondition, movieList);
    }

}