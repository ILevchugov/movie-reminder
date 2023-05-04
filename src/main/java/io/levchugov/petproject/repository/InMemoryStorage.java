package io.levchugov.petproject.repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStorage {

    public final static Map<Long, List<String>> MOVIE_MAP = new ConcurrentHashMap<>();
    public final static Map<Long, Boolean> AWAITS_TITLE = new ConcurrentHashMap<>();

}
