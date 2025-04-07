package GeneratorDanych;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Road extends Line2D.Double {
    private int throughput;
    public Road(Point2D x1, Point2D x2){
        super(x1, x2);
    }
}
