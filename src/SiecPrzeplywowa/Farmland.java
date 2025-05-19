package SiecPrzeplywowa;

import java.awt.geom.Point2D;

public class Farmland extends Point2D.Double {
    private int productionCapacity;

    public Farmland(int x, int y, int productionCapacity) {
        super(x, y);
        this.productionCapacity = productionCapacity; // productionCapacity will depend on quadrant
    }

    public int getProductionCapacity() {
        return productionCapacity;
    }
}
