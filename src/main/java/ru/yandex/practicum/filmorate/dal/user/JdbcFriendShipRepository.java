package ru.yandex.practicum.filmorate.dal.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.dal.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
@Repository
@RequiredArgsConstructor
public class JdbcFriendShipRepository implements FriendShipRepository {
    private final NamedParameterJdbcOperations jdbc;
    private final UserRowMapper userRowMapper;
    @Override
    public void addFriend(int userId, int friendId) {
        String sql = "insert into friendship (user_id, friend_id) values (:userId, :friendId)";
        jdbc.update(sql, Map.of("userId", userId, "friendId", friendId));
    }

    @Override
    public void removeFriend(int userId, int friendId) {
        String sql = "delete from friendship where user_id = :userId AND friend_id = :friendId";
        jdbc.update(sql, Map.of("userId", userId, "friendId", friendId));
    }

    @Override
    public List<User> getFriends(int userId) {
        String sql = "select u.user_id, u.email, u.login, u.name, u.birthday " +
                "from users u " +
                "join friendship f on u.user_id = f.friend_id " +
                "where f.user_id = :userId";
        return jdbc.query(sql,Map.of("userId",userId), userRowMapper);
    }

    @Override
    public List<User> getCommonFriends(int userId, int anotherUserId) {
        String sql = "select * " +
                "    from (" +
                "        select u.USER_ID,u.EMAIL,u.LOGIN,u.NAME,u.BIRTHDAY,f.FRIEND_ID from USERS AS U " +
                "        join FRIENDSHIP AS F on U.USER_ID = F.FRIEND_ID " +
                "        where f.USER_ID = :userId) AS S " +
                "join FRIENDSHIP AS D on S.FRIEND_ID = D.FRIEND_ID " +
                "where D.USER_ID = :anotherUserId";
        return jdbc.query(sql,Map.of("userId",userId,"anotherUserId",anotherUserId), userRowMapper);
    }
}
