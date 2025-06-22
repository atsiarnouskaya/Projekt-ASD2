package DataGenerator;

import FlowNetwork.Farmland;

import java.util.Random;

class FarmlandFactory implements IFactory<Farmland> {
    @Override
    public Farmland Create(Random random, int x, int y) {
        return new Farmland(x, y);
    }
}
