package FlowNetwork;

import lombok.Getter;
import lombok.Setter;

import java.awt.geom.Point2D;

@Setter
@Getter
public class Farmland extends Point2D.Double {
    private Quadrant quadrant;
    private int productionCapacity;
    public Farmland(double x, double y) {
        super(x, y);
    }
}