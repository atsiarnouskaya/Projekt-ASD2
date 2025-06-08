package DataGenerator;

import FlowNetwork.Brewery;

import java.util.Random;

class BreweryFactory implements IFactory<Brewery> {
    @Override
    public Brewery Create(Random random, int x, int y) {
        int prodCap = random.nextInt(2, 30);
        return new Brewery(x, y, prodCap);
    }
}
