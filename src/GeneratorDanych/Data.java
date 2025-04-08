package GeneratorDanych;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

class Data {
    public ArrayList<Road> roads;
    public ArrayList<Farmland> farmlands;
    public ArrayList<Brewery> breweries;
    public ArrayList<Tavern> taverns;

    public Data(ArrayList<Road> roads, ArrayList<Farmland> farmlands, ArrayList<Brewery> breweries, ArrayList<Tavern> taverns) {
        this.roads = roads;
        this.farmlands = farmlands;
        this.breweries = breweries;
        this.taverns = taverns;
    }
}
