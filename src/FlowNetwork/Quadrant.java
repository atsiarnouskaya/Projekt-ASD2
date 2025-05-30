package FlowNetwork;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/*
 Quadrant represents one convex area containing farmland plots.
 It stores the convex hull points that define the boundary,
 a list of farmland plots inside this quadrant,
 and the production capacity per plot in this area.
 */

public class Quadrant {
    private ArrayList<Point2D> hull = new ArrayList<>();
    private final ArrayList<Farmland> farmlands = new ArrayList<>();
    private int productionPerPlot;
    public void setHull(ArrayList<Point2D> hull) {
        this.hull = hull;
    }

    public ArrayList<Point2D> getHull() {
        return hull;
    }

    public void addFarmland(Farmland farmland) {
        farmlands.add(farmland);
    }

    public ArrayList<Farmland> getFarmlands() {
        return farmlands;
    }

    public void setProductionPerPlot(int productionPerPlot) {
        this.productionPerPlot = productionPerPlot;
    }

    public int getProductionPerPlot() {
        return productionPerPlot;
    }
}