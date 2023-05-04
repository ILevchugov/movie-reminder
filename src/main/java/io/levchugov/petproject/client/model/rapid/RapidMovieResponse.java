package io.levchugov.petproject.client.model.rapid;

import lombok.Data;

import java.util.List;

@Data
public class RapidMovieResponse {
    List<RapidMovieResult> results;
}
