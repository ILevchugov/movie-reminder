package io.levchugov.petproject.repository;

import io.levchugov.petproject.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class MovieJdbcRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Integer findById(String id) {
        var query = "select count(*) from movies where movies.id = :id";
        return jdbcTemplate.queryForObject(
                query,
                new MapSqlParameterSource("id", id),
                Integer.class
        );
    }

    public void save(Movie movie) {
        var saveQuery =
                "insert into movies (id, title, image, description) " +
                        "values (:id, :title, :image, :description)";
        var namedParameters = new MapSqlParameterSource(
                Map.of(
                        "id", movie.id(),
                        "title", movie.title(),
                        "image", movie.image(),
                        "description", movie.description()
                )
        );
        jdbcTemplate.update(saveQuery, namedParameters);
    }

    public Integer count(Long chatId, String movieId) {
        String select =
                "select count(*) from chat_movie_list " +
                        "where chat_movie_list.chat_id = :chat_id " +
                        "and chat_movie_list.movie_id = :movie_id";

        var namedParameters = new MapSqlParameterSource(
                Map.of(
                        "chat_id", chatId,
                        "movie_id", movieId
                )
        );
        return jdbcTemplate.queryForObject(select, namedParameters, Integer.class);
    }


    public void saveMovieToWatchList(Long chatId, String movieId) {
        String saveQuery =
                "insert into chat_movie_list (chat_id, movie_id)" +
                        " values (:chat_id, :movie_id)";

        var namedParameters = new MapSqlParameterSource(
                Map.of(
                        "chat_id", chatId,
                        "movie_id", movieId
                )
        );

        jdbcTemplate.update(saveQuery, namedParameters);
    }

    public List<Movie> findUsersListToWatchByChatId(Long chatId) {
        String query = "select * from movies " +
                "left join chat_movie_list on movies.id = chat_movie_list.movie_id" +
                " where chat_movie_list.chat_id = :chat_id" +
                " and chat_movie_list.recently_picked = false" +
                " and chat_movie_list.watched = false";

        return jdbcTemplate.query(
                query,
                new MapSqlParameterSource("chat_id", chatId),
                (rs, rowNum) ->
                        new Movie(
                                rs.getString("id"),
                                rs.getString("title"),
                                rs.getString("image"),
                                rs.getString("description")
                        ));

    }

    public void markMoviePicked(Long chatId, String movieId) {
        String query = "update chat_movie_list " +
                "set recently_picked = true " +
                "where chat_movie_list.chat_id = :chat_id " +
                "and chat_movie_list.movie_id = :movie_id";

        var namedParameters = new MapSqlParameterSource(
                Map.of(
                        "chat_id", chatId,
                        "movie_id", movieId
                )
        );

        jdbcTemplate.update(query, namedParameters);
    }

    public void markAllMovieNotPicked(Long chatId) {
        String query = "update chat_movie_list" +
                " set recently_picked = false" +
                " where chat_movie_list.chat_id = :chat_id";

        jdbcTemplate.update(
                query,
                new MapSqlParameterSource("chat_id", chatId)
        );
    }

    public void markMovieWatched(Long chatId, String movieId) {
        String query = "update chat_movie_list" +
                " set watched = true" +
                " where chat_movie_list.chat_id = :chat_id" +
                " and chat_movie_list.movie_id = :movie_id";

        var namedParameters = new MapSqlParameterSource(
                Map.of(
                        "chat_id", chatId,
                        "movie_id", movieId
                )
        );

        jdbcTemplate.update(
                query,
                namedParameters
        );
    }

    public void markMovieUnWatched(Long chatId, String movieId) {
        String query = "update chat_movie_list" +
                " set watched = false" +
                " where chat_movie_list.chat_id = :chat_id" +
                " and chat_movie_list.movie_id = :movie_id";

        var namedParameters = new MapSqlParameterSource(
                Map.of(
                        "chat_id", chatId,
                        "movie_id", movieId
                )
        );

        jdbcTemplate.update(
                query,
                namedParameters
        );
    }

    public List<Long> findAllChatIds() {
        String query = "select distinct chat_id from chat_movie_list";

        return jdbcTemplate.query(
                query,
                (rs, rowNum) ->
                        rs.getLong("chat_id")
        );

    }

    public List<Movie> findAllMoviesSeparatedBySizedPages(Long chatId, int size, int page) {
        int offsetUnits = page * size;

        String query = "select distinct * from movies " +
                "left join chat_movie_list on movies.id = chat_movie_list.movie_id " +
                "where chat_movie_list.chat_id = :chat_id " +
                "order by movies.id " +
                "offset :offset_units " +
                "fetch first :number_of_unit_in_one_page rows only";

        return jdbcTemplate.query(
                query,
                new MapSqlParameterSource(
                        Map.of(
                                "chat_id", chatId,
                                "offset_units", offsetUnits,
                                "number_of_unit_in_one_page", size
                        )
                ),
                (rs, rowNum) ->
                        new Movie(
                                rs.getString("id"),
                                rs.getString("title"),
                                rs.getString("image"),
                                rs.getString("description")
                        ));
    }

    public Integer calculateSizeOfMovieCatalogueByChatId(Long chatId) {
        String select =
                "select count(*) from chat_movie_list " +
                        "where chat_movie_list.chat_id = :chat_id";


        return jdbcTemplate.queryForObject(select,
                new MapSqlParameterSource("chat_id", chatId),
                Integer.class
        );
    }

    public List<Movie> findAllSavedMovieInDB(int size, int page) {
        int offsetUnits = page * size;

        String query = "select distinct * from movies " +
                "order by movies.id " +
                "offset :offset_units " +
                "fetch first :number_of_unit_in_one_page rows only";

        return jdbcTemplate.query(
                query,
                new MapSqlParameterSource(
                        Map.of(
                                "offset_units", offsetUnits,
                                "number_of_unit_in_one_page", size
                        )
                ),
                (rs, rowNum) ->
                        new Movie(
                                rs.getString("id"),
                                rs.getString("title"),
                                rs.getString("image"),
                                rs.getString("description")
                        ));
    }

    public List<Movie> findMovieByTitle(String title) {
        String query = "select distinct * from movies " +
                "where movies.title = :title";

        return jdbcTemplate.query(
                query,
                new MapSqlParameterSource("title", title),
                (rs, rowNum) ->
                        new Movie(
                                rs.getString("id"),
                                rs.getString("title"),
                                rs.getString("image"),
                                rs.getString("description")
                        ));
    }

    public List<Movie> findMovieOfUserByTitle(String title, Long chatId) {
        String query = "select distinct * from movies " +
                "left join chat_movie_list on movies.id = chat_movie_list.movie_id " +
                "where chat_movie_list.chat_id = :chat_id " +
                "and movies.title = :title";

        var namedParameters = new MapSqlParameterSource(
                Map.of(
                        "title", title,
                        "chat_id", chatId
                )
        );

        return jdbcTemplate.query(
                query,
                namedParameters,
                (rs, rowNum) ->
                        new Movie(
                                rs.getString("id"),
                                rs.getString("title"),
                                rs.getString("image"),
                                rs.getString("description")
                        ));
    }
}
