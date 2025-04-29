package SiecPrzeplywowa;

import java.awt.geom.Point2D;

public class Tavern extends Point2D.Double{
    private double consumptionCapacity;

    public Tavern(int x, int y, double consumptionCapacity) {
        super(x, y);
        this.consumptionCapacity=consumptionCapacity;
    }

    public double getConsumptionCapacity() {
        return consumptionCapacity;
    }

}
