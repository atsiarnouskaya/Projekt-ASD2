package GeneratorDanych;

import java.util.Random;

class TavernFactory implements IFactory<Tavern> {
    @Override
    public Tavern Create(Random random, double x, double y) {
        return new Tavern(x, y);
    }
}
