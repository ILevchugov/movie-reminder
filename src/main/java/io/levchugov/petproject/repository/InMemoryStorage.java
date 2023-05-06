package io.levchugov.petproject.repository;

import io.levchugov.petproject.client.model.imdb.SearchMovieResult;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@UtilityClass
public class InMemoryStorage {

    public static final Map<Long, Boolean> AWAITS_TITLE = new ConcurrentHashMap<>();
    public static final Map<Long, String> MOVIES_AWAITS_CONFIRMATION = new ConcurrentHashMap<>();
    public static final Map<Long, List<SearchMovieResult>> RESPONSE_FROM_IMDB = new ConcurrentHashMap<>();
    public static final Map<Long, Integer> NO_COUNTER = new ConcurrentHashMap<>();

}
