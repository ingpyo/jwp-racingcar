package racingcar.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import racingcar.dao.GameDao;
import racingcar.dao.PlayerDao;
import racingcar.domain.NumberGenerator;
import racingcar.domain.RacingGame;
import racingcar.dto.GameRequestDto;
import racingcar.dto.GameResponseDto;
import racingcar.entity.Game;
import racingcar.entity.GameId;
import racingcar.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RacingGameService {

    private final NumberGenerator numberGenerator;
    private final GameDao gameDao;
    private final PlayerDao playerDao;

    public RacingGameService(final NumberGenerator numberGenerator, final GameDao gameDao, final PlayerDao playerDao) {
        this.numberGenerator = numberGenerator;
        this.gameDao = gameDao;
        this.playerDao = playerDao;
    }

    @Transactional
    public GameResponseDto play(final GameRequestDto gameRequest) {
        final RacingGame racingGame = playRacingGame(gameRequest);

        GameId saveGameId = gameDao.saveAndGetGame(new Game(racingGame.getCount()));

        final Set<String> winners = new HashSet<>(racingGame.findWinners());
        final List<Player> players = fromPlayers(racingGame, saveGameId.getId(), winners);

        playerDao.saveAll(players);

        return GameResponseDto.of(racingGame.findWinners(), racingGame.findCurrentCarPositions());
    }

    @Transactional(readOnly = true)
    public List<GameResponseDto> findAll() {
        List<Player> players = playerDao.findAll();

        return GameResponseDto.toGamePlayResponse(players);
    }

    private RacingGame playRacingGame(final GameRequestDto gameRequest) {
        final RacingGame racingGame = new RacingGame(numberGenerator, gameRequest.getNames(), gameRequest.getCount());

        racingGame.play();

        return racingGame;
    }

    private List<Player> fromPlayers(final RacingGame racingGame, final int gameId, final Set<String> winners) {
        return racingGame.findCurrentCarPositions().stream()
                .map(car -> new Player(car, winners.contains(car.getName()), gameId))
                .collect(Collectors.toList());
    }
}
