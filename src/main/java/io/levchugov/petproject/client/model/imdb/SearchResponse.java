package io.levchugov.petproject.client.model.imdb;


import java.util.List;

public record SearchResponse(
        String searchType,
        String expression,
        List<SearchMovieResult> results
) {
}