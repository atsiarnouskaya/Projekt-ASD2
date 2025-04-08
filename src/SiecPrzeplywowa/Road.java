package SiecPrzeplywowa;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Road extends Line2D.Double {
    private int maxBeerFlow;
    private int maxBarleyFlow;
    public Road(Point2D x1, Point2D x2){
        super(x1, x2);
    }

    public void setMaxBeerFlow(int maxBeerFlow) {
        this.maxBeerFlow = maxBeerFlow;
    }

    public void setMaxBarleyFlow(int maxBarleyFlow) {
        this.maxBarleyFlow = maxBarleyFlow;
    }
}

