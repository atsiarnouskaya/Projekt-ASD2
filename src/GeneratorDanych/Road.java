package GeneratorDanych;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class Road extends Line2D.Double {
    private int beerThroughput;
    private int barleyThroughput;
    public Road(Point2D x1, Point2D x2){
        super(x1, x2);
    }

    public void setThroughputs(int beerThroughput, int barleyThroughput)
    {
        this.beerThroughput = beerThroughput;
        this.barleyThroughput= barleyThroughput;
    }
}
