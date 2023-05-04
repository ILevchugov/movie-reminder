package io.levchugov.petproject.repository;

import io.levchugov.petproject.client.model.imdb.SearchMovieResult;
import io.levchugov.petproject.model.Movie;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorage {

    public final static Map<Long, Boolean> AWAITS_TITLE = new ConcurrentHashMap<>();
    public final static Map<Long, String> MOVIES_AWAITS_CONFIRMATION = new ConcurrentHashMap<>();
    public final static Map<Long, List<Movie>> MOVIES = new ConcurrentHashMap<>();
    public final static Map<Long, List<SearchMovieResult>> RESPONSE_FROM_IMDB = new ConcurrentHashMap<>();
    public final static Map<Long, Integer> NO_COUNTER = new ConcurrentHashMap<>();

}
