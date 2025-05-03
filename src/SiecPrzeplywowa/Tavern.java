package SiecPrzeplywowa;

import java.awt.geom.Point2D;

public class Tavern extends Point2D.Double{
    private int consumptionCapacity;

    public Tavern(int x, int y, int consumptionCapacity) {
        super(x, y);
        this.consumptionCapacity=consumptionCapacity;
    }

    public int getConsumptionCapacity() {
        return consumptionCapacity;
    }

}
