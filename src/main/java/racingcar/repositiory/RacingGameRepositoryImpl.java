package racingcar.repositiory;

import org.springframework.stereotype.Repository;
import racingcar.dao.GameDao;
import racingcar.dao.PlayerDao;
import racingcar.domain.RacingGame;
import racingcar.entity.Game;
import racingcar.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public class RacingGameRepositoryImpl implements RacingGameRepository{
    private final GameDao gameDao;
    private final PlayerDao playerDao;

    public RacingGameRepositoryImpl(final GameDao gameDao, final PlayerDao playerDao) {
        this.gameDao = gameDao;
        this.playerDao = playerDao;
    }

    @Override
    public void save(final RacingGame racingGame) {
        int gameId = gameDao.saveAndGetId(new Game(racingGame.getCount()))
                .orElseThrow(() -> new IllegalArgumentException("게임 저장에 실패하였습니다."));

        final Set<String> winners = new HashSet<>(racingGame.findWinners());
        final List<Player> players = fromPlayers(racingGame, gameId, winners);

        playerDao.saveAll(players);
    }

    @Override
    public List<Player> findAll() {
        return playerDao.findAll();
    }

    private List<Player> fromPlayers(final RacingGame racingGame, final int gameId, final Set<String> winners) {
        return racingGame.findCurrentCarPositions().stream()
                .map(car -> new Player(car, winners.contains(car.getName()), gameId))
                .collect(Collectors.toList());
    }
}
