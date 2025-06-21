package FlowNetwork;

import java.awt.geom.Point2D;

public class Brewery extends Point2D.Double{
    private int productionCapacity;
    public Brewery(int x, int y, int productionCapacity) {
        super(x, y);

        this.productionCapacity=productionCapacity;
    }

    public int getProductionCapacity() {
        return productionCapacity;
    }
}
