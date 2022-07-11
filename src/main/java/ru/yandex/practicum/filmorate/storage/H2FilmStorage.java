package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmGenre;
import ru.yandex.practicum.filmorate.model.FilmRating;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class H2FilmStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public H2FilmStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void addFilm(Film film) {
        String queryFilms = "INSERT INTO film" +
                "(film_id, name, description, release_date, duration, mpa_id) " +
                "VALUES(?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(queryFilms,
                film.getId(),
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().ordinal() + 1);
        if (!film.getGenres().isEmpty()) {
            insertFilmGenre(film);
        }
        if (!film.getLikes().isEmpty()) {
            insertFilmLikes(film);
        }
    }

    private void deleteFilmGenreByFilm(Film film) {
        String queryDelete = "DELETE FROM film_genre WHERE film_id=?";
        jdbcTemplate.update(queryDelete, film.getId());
    }

    private void deleteFilmLikesByFilm(Film film) {
        String queryDelete = "DELETE FROM film_like WHERE film_id=?";
        jdbcTemplate.update(queryDelete, film.getId());
    }

    @Override
    public void updateFilm(Film film) {
        String query = "UPDATE film " +
                "SET " +
                "name=?, " +
                "description=?, " +
                "release_date=?, " +
                "duration=?, " +
                "mpa_id=? " +
                "WHERE film_id=?";
        jdbcTemplate.update(query,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().ordinal() + 1,
                film.getId());
        deleteFilmGenreByFilm(film);
        deleteFilmLikesByFilm(film);
        insertFilmGenre(film);
        insertFilmLikes(film);
    }

    @Override
    public Collection<Film> getFilms() {
        String query = "SELECT * FROM film";
        return jdbcTemplate.query(query, (rs, rowNum) -> makeFilm(rs.getLong("film_id"), rs));
    }

    @Override
    public boolean contains(Film film) {
        String query = "SELECT * FROM film where film_id=?";
        List<Film> films = jdbcTemplate.query(query, (rs, rowNum) -> makeFilm(film.getId(), rs), film.getId());
        return !films.isEmpty();
    }

    @Override
    public Film getFilmById(Long id) throws FilmNotFoundException {
        String filmQuery = "SELECT * FROM film WHERE film_id=?";
        List<Film> films = jdbcTemplate.query(filmQuery, (rs, rowNum) -> makeFilm(id, rs), id);
        if (films.isEmpty()) {
            throw new FilmNotFoundException();
        }
        return films.get(0);
    }

    @Override
    public int getStartId() {
        String query = "SELECT MAX(film_id) AS max_film_id FROM film";
        Integer maxFilmId = jdbcTemplate.queryForObject(query, Integer.class);
        if (maxFilmId == null) {
            maxFilmId = 0;
        }
        return maxFilmId + 1;
    }

    @Override
    public Film addLike(Film film, User user) {
        film.addLike(user.getId());
        insertFilmLikes(film);
        return film;
    }

    @Override
    public Film removeLike(Film film, User user) {
        String query = "DELETE FROM FILM_LIKE WHERE film_id=? AND user_id=?";
        jdbcTemplate.update(query, film.getId(), user.getId());
        film.removeLike(user.getId());
        return film;
    }

    private void insertFilmGenre(Film film) {
        String queryGenres = "INSERT INTO film_genre" +
                "(film_id, genre_id) " +
                "VALUES(?, ?)";
        List<FilmGenre> filmGenres = film.getGenres().stream().distinct().collect(Collectors.toList());
        jdbcTemplate.batchUpdate(queryGenres, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                FilmGenre genre = filmGenres.get(i);
                ps.setLong(1, film.getId());
                ps.setInt(2, genre.ordinal() + 1);
            }

            @Override
            public int getBatchSize() {
                return filmGenres.size();
            }
        });
        film.setGenres(filmGenres);
    }

    private void insertFilmLikes(Film film) {
        String queryLikes = "INSERT INTO film_like" +
                "(film_id, user_id) " +
                "VALUES(?, ?)";
        jdbcTemplate.batchUpdate(queryLikes, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                Long userId = film.getLikes().get(i);
                ps.setLong(1, film.getId());
                ps.setLong(2, userId);
            }

            @Override
            public int getBatchSize() {
                return film.getLikes().size();
            }
        });
    }

    private Film makeFilm(Long filmId, ResultSet rs) throws SQLException {
        FilmRating mpa = FilmRating.values()[rs.getInt("mpa_id") - 1];
        String name = rs.getString("name");
        String description = rs.getString("description");
        LocalDate releaseDate = rs.getDate("release_date").toLocalDate();
        int duration = rs.getInt("duration");
        Film film = new Film(filmId, mpa, name, description, releaseDate, duration);

        String filmGenreQuery = "SELECT * FROM film_genre WHERE film_id=?";
        List<FilmGenre> filmGenres =
                jdbcTemplate.query(filmGenreQuery, (rsGenre, rowNum) -> makeFilmGenre(rsGenre), filmId);
        film.setGenres(filmGenres);

        String filmLikeQuery = "SELECT user_id FROM film_like WHERE film_id=?";
        List<Long> filmLike =
                jdbcTemplate.queryForList(filmLikeQuery, Long.class, filmId);
        film.setLikes(filmLike);

        return film;
    }

    private FilmGenre makeFilmGenre(ResultSet rs) throws SQLException {
        int genreId = rs.getInt("genre_id");
        return FilmGenre.values()[genreId - 1];
    }
}
