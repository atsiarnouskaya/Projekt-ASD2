package GeneratorDanych;

import java.awt.geom.Point2D;

public class Intersection extends Point2D.Double{
    public Intersection(double x, double y) {
        super(x, y);
    }
    public Intersection(Point2D P) {
        super(P.getX(), P.getY());
    }
}
