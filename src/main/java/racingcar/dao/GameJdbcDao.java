package racingcar.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import racingcar.Entity.Game;

import java.sql.PreparedStatement;

@Component
public class GameJdbcDao implements GameDao {
    private final JdbcTemplate jdbcTemplate;

    public GameJdbcDao(final JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public int save(final Game game) {
        final String sql = "insert into game (trial, winners) values (?,?)";

        final KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, new String[]{"id"});
            ps.setInt(1, game.getTrial());
            ps.setString(2, game.getWinners());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().intValue();
    }
}
