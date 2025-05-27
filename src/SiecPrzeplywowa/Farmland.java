package SiecPrzeplywowa;

import java.awt.geom.Point2D;

public class Farmland extends Point2D.Double {
    private Quadrant quadrant;
    private double productionCapacity;
    public Farmland(double x, double y) {
        super(x, y);
    }
    public void setQuadrant(Quadrant quadrant) {
        this.quadrant = quadrant;
    }
    public Quadrant getQuadrant() {
        return quadrant;
    }
    public void setProductionCapacity(double productionCapacity) {
        this.productionCapacity = productionCapacity;
    }
    public double getProductionCapacity() {
        return productionCapacity;
    }
}