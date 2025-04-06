package GeneratorDanych;

import java.awt.geom.Line2D;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args){
        Generator generator= new Generator(20);
        ArrayList<Line2D.Double> roads= generator.generate();
        for(Line2D.Double road: roads){
            System.out.println("(" + String.format("%.2f", road.x1) + ", " + String.format("%.2f", road.y1) + "), (" + String.format("%.2f", road.x2) + ", " + String.format("%.2f", road.y2) + ")" );
        }
    }
}
