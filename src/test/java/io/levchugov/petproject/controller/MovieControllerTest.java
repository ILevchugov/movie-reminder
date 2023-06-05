package io.levchugov.petproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.levchugov.petproject.model.Movie;
import io.levchugov.petproject.service.MovieService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
class MovieControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MovieService movieService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getMovieList() throws Exception {
        var movie = new Movie("tt1375666", "Inception", "https://m.media-amazon.com/images/M/MV5BMjAxMzY3NjcxNF5BMl5BanBnXkFtZTcwNTI5OTM0Mw@@._V1_Ratio0.6757_AL_.jpg", "2010 Leonardo DiCaprio, Joseph Gordon-Levitt");

        Mockito.doReturn(List.of(movie)).when(movieService).getMoviesByPagination("Inception", null, 100, 0);

        final String expectedResponseContent = objectMapper.writeValueAsString(List.of(movie));

        this.mockMvc.perform(get("/movies?q=Inception&size=100&page=0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponseContent))
                .andReturn().getResponse();

        Mockito.verify(movieService).getMoviesByPagination("Inception", null, 100, 0);
    }
}