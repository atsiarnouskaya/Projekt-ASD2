package GeneratorDanych;

import java.util.Random;

class FarmlandFactory implements IFactory<Farmland> {
    @Override
    public Farmland Create(Random random, double x, double y) {
        double prodCap = random.nextDouble(10, 10000);
        return new Farmland(x, y, prodCap);
    }
}
