package SiecPrzeplywowa;

import java.awt.geom.Point2D;

public class Brewery extends Point2D.Double{
    private double productionCapacity;
    public Brewery(int x, int y, double productionCapacity) {
        super(x, y);
        this.productionCapacity=productionCapacity;
    }

    public double getProductionCapacity() {
        return productionCapacity;
    }
}
