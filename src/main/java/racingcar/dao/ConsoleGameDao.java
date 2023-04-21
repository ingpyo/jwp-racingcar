package racingcar.dao;

import racingcar.entity.GameEntity;
import racingcar.entity.GameId;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsoleGameDao implements GameDao {

    private final List<GameEntity> games = new ArrayList<>();
    private final AtomicInteger id = new AtomicInteger(0);

    @Override
    public GameId saveAndGetGameId(final GameEntity game) {
        GameEntity savedGame = new GameEntity(id.incrementAndGet(), game.getTrial(), game.getCreatedAt());
        games.add(savedGame);
        return new GameId(id.get());
    }
}
