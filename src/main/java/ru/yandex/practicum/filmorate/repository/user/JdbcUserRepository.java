package ru.yandex.practicum.filmorate.repository.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.InternalServerException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.repository.mappers.UserRowMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class JdbcUserRepository implements UserRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final UserRowMapper userRowMapper;

    @Override
    public User create(User user) {
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        String sql = "insert into users (email, login, name, birthday) " +
                "values (:email, :login, :name, :birthday)";
        jdbc.update(sql, new MapSqlParameterSource().addValue("email", user.getEmail())
                        .addValue("login", user.getLogin())
                        .addValue("name", user.getName())
                        .addValue("birthday", user.getBirthday()),
                keyHolder
        );
        if (Objects.nonNull(keyHolder.getKey())) {
            user.setId(keyHolder.getKey().intValue());
        } else {
            throw new InternalServerException("Ошибка создания пользователя");
        }
        return user;
    }

    @Override
    public Optional<User> findById(int id) {
        String sql = "select * from users where user_id = :id";
        try {
            User result = jdbc.queryForObject(sql, Map.of("id", id), userRowMapper);
            return Optional.ofNullable(result);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(User user) {
        String sql = "update users set email = :email," +
                "login = :login," +
                "name = :name," +
                "birthday = :birthday " +
                "where user_id = :id";
        jdbc.update(sql, Map.of("email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday(),
                "id", user.getId()));
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from users";
        return jdbc.query(sql, userRowMapper);
    }
}
