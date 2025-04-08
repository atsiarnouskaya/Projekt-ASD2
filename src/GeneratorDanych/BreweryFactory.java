package GeneratorDanych;

import SiecPrzeplywowa.Brewery;

import java.util.Random;

class BreweryFactory implements IFactory<Brewery> {
    @Override
    public Brewery Create(Random random, int x, int y) {
        double prodCap = random.nextDouble(10, 10000);
        return new Brewery(x, y, prodCap);
    }
}
