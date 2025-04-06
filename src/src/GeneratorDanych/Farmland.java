package GeneratorDanych;

public class Farmland extends Point{
    private double productionCapacity;
    public Farmland(double x, double y) {
        super(x, y);
    }

    public double getProductionCapacity() {
        return productionCapacity;
    }
}
