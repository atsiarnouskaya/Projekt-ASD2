package SiecPrzeplywowa;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Road extends Line2D.Double {
    private int maxBeerFlow;
    private int maxBarleyFlow;
    private int repairCost;

    public Road(Point2D x1, Point2D x2){
        super(x1, x2);
    }

    public void setMaxBeerFlow(int maxBeerFlow) {
        this.maxBeerFlow = maxBeerFlow;
    }

    public void setMaxBarleyFlow(int maxBarleyFlow) {
        this.maxBarleyFlow = maxBarleyFlow;
    }

    public int getMaxBeerFlow() {
        return maxBeerFlow;
    }

    public int getMaxBarleyFlow() {
        return maxBarleyFlow;
    }

    public int getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(int repairCost) {
        this.repairCost = repairCost;
    }

}

