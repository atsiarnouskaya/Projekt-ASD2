package SiecPrzeplywowa;

import java.awt.geom.Point2D;

public class Farmland extends Point2D.Double {
    private double productionCapacity;

    public Farmland(int x, int y, double productionCapacity) {
        super(x, y);
        this.productionCapacity = productionCapacity;
    }

    public double getProductionCapacity() {
        return productionCapacity;
    }
}
