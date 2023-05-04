package io.levchugov.petproject.repository;

import io.levchugov.petproject.model.Movie;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class MovieJdbcRepository {

    private final JdbcOperations jdbcOperations;

    public Integer findById(String id) {
        String select = "select count(*) from movies where movies.id = ?";

        return jdbcOperations.queryForObject(
                select,
                new Object[]{id},
                (rs, rowNum) ->
                        rs.getInt("count")
        );
    }

    public void save(Movie movie) {
        String saveQuery = "insert into movies (id, title, image, description)" +
                " values (?, ?, ?, ?)";

        jdbcOperations.update(
                saveQuery,
                movie.id(),
                movie.title(),
                movie.image(),
                movie.description()
        );
    }

    public Integer count(Long chatId, String movieId) {
        String select =
                "select count(*) from chat_movie_list " +
                        "where chat_movie_list.chat_id = ? " +
                        "and chat_movie_list.movie_id = ?";

        return jdbcOperations.queryForObject(select,
                new Object[]{chatId, movieId},
                (rs, rowNum) ->
                        rs.getInt("count")
        ) ;
    }

    public void saveMovieToWatchList(Long chatId, String movieId) {
        String saveQuery =
                "insert into chat_movie_list (chat_id, movie_id)" +
                        " values (?, ?)";

        jdbcOperations.update(
                saveQuery,
                chatId,
                movieId
        );
    }

    public List<Movie> findUsersListToWatchByChatId(Long chatId) {
        String query = "select * from movies " +
                "left join chat_movie_list on movies.id = chat_movie_list.movie_id" +
                " where chat_movie_list.chat_id = ?";

        return jdbcOperations.query(
                query,
                new Object[]{chatId},
                (rs, rowNum) ->
                        new Movie(
                                rs.getString("id"),
                                rs.getString("title"),
                                rs.getString("image"),
                                rs.getString("description")
                        )
        );
    }
}
