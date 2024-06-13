package ru.yandex.practicum.filmorate.repository.mappers;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Component
public class GenreResultExtractor implements ResultSetExtractor<Map<Integer, Genre>> {
    @Override
    public Map<Integer, Genre> extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<Integer, Genre> genreMap = new HashMap<>();
        while (rs.next()) {
            Genre genre = new Genre();
            genre.setId(rs.getInt("genre_id"));
            genre.setName(rs.getString("genre_name"));
            genreMap.put(genre.getId(), genre);
        }
        return genreMap;
    }
}
