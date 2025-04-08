package GeneratorDanych;

import java.util.Random;

interface IFactory<T> {
    T Create(Random random, double x, double y);
}
