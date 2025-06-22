package FlowNetwork;

import lombok.Getter;

import java.awt.geom.Point2D;

@Getter
public class Brewery extends Point2D.Double{
    private final int productionCapacity;
    public Brewery(int x, int y, int productionCapacity) {
        super(x, y);

        this.productionCapacity=productionCapacity;
    }

}
