package GeneratorDanych;

import SiecPrzeplywowa.Farmland;

import java.util.Random;

class FarmlandFactory implements IFactory<Farmland> {
    @Override
    public Farmland Create(Random random, int x, int y) {
        double prodCap = random.nextDouble(10, 10000);
        return new Farmland(x, y, prodCap);
    }
}
