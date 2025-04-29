package GeneratorDanych;

import SiecPrzeplywowa.Tavern;

import java.util.Random;

class TavernFactory implements IFactory<Tavern> {
    @Override
    public Tavern Create(Random random, int x, int y) {
        double consCap = random.nextDouble(10, 10000);
        return new Tavern(x, y, consCap);
    }
}
