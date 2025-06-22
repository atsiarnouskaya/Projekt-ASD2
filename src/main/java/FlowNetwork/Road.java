package FlowNetwork;

import lombok.Getter;
import lombok.Setter;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

@Setter
@Getter
public class Road extends Line2D.Double {
    private int maxBeerFlow;
    private int maxBarleyFlow;
    private int repairCost;

    public Road(Point2D x1, Point2D x2){
        super(x1, x2);
    }

}

