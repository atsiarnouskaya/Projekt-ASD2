package GeneratorDanych;

public class Brewery extends Point{
    private double productionCapacity;
    public Brewery(double x, double y) {
        super(x, y);
    }

    public double getProductionCapacity() {
        return productionCapacity;
    }
}
