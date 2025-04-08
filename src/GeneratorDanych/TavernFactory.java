package GeneratorDanych;

import SiecPrzeplywowa.Tavern;

import java.util.Random;

class TavernFactory implements IFactory<Tavern> {
    @Override
    public Tavern Create(Random random, int x, int y) {
        return new Tavern(x, y);
    }
}
