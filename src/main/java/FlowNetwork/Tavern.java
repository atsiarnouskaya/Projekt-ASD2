package FlowNetwork;

import lombok.Getter;

import java.awt.geom.Point2D;

@Getter
public class Tavern extends Point2D.Double{
    private int consumptionCapacity;

    public Tavern(int x, int y, int consumptionCapacity) {
        super(x, y);
        this.consumptionCapacity=consumptionCapacity;
    }

}
