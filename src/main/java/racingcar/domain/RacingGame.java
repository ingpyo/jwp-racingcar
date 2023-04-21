package racingcar.domain;

import java.util.List;

public class RacingGame {

    private final NumberGenerator numberGenerator;
    private final Cars cars;
    private final Count count;

    public RacingGame(final NumberGenerator numberGenerator, final List<String> names, final int count) {
        this.numberGenerator = numberGenerator;
        this.cars = new Cars(names);
        this.count = new Count(count);
    }

    public void play() {
        while (count.isPlayable()) {
            cars.race(numberGenerator);
            count.decrease();
        }
    }

    public List<Car> findCurrentCarPositions() {
        return cars.getCars();
    }

    public List<String> findWinners() {
        return cars.findWinners();
    }

    public int getCount() {
        return count.getValue();
    }
}
