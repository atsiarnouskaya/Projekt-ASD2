package DataGenerator;

import FlowNetwork.Farmland;

import java.util.Random;

class FarmlandFactory implements IFactory<Farmland> {
    @Override
    public Farmland Create(Random random, int x, int y) {
        //int prodCap = random.nextInt(2, 30);
        return new Farmland(x, y);
    }
}
