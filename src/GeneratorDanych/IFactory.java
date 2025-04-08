package GeneratorDanych;

import java.util.Random;

interface IFactory<T> {
    T Create(Random random, int x, int y);
}
