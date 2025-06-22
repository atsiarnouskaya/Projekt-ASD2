package FlowNetwork;

import lombok.Getter;
import lombok.Setter;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/*
 Quadrant represents one convex area containing farmland plots.
 It stores the convex hull points that define the boundary,
 a list of farmland plots inside this quadrant,
 and the production capacity per plot in this area.
 */

@Getter
@Setter
public class Quadrant {
    private ArrayList<Point2D> hull = new ArrayList<>();
    private final ArrayList<Farmland> farmlands = new ArrayList<>();
    private int productionPerPlot;

    public void addFarmland(Farmland farmland) {
        farmlands.add(farmland);
    }

}