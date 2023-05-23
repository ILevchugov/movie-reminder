package io.levchugov.petproject.controller;

import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;


@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieService;

    @Test
    void getMovieList() throws Exception {
       Mockito.when(movieService
               .getMoviesByPagination("Inception", null, 0, 100))
               .thenReturn(List.of(
                       new Movie("tt1375666", "Inception",
                               "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg",
                               "2010 Leonardo DiCaprio, Joseph Gordon-Levitt")
               ));

       var movie = new Movie("tt1375666", "Inception",
               "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg",
               "2010 Leonardo DiCaprio, Joseph Gordon-Levitt");

       this.mockMvc.perform(get("/movies"))
               .andDo(print())
               .andExpect(status().isOk());

       assertEquals(Collections.singletonList(movie), movieService.getMoviesByPagination("Inception", null, 0, 100));
    }
}