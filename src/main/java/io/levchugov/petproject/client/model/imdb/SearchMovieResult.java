package io.levchugov.petproject.client.model.imdb;

public record SearchMovieResult(
        String id,
        String resultType,
        String image,
        String title,
        String description
) {
}