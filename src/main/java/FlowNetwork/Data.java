package FlowNetwork;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Data {
    public ArrayList<Road> roads;
    public ArrayList<Farmland> farmlands;
    public ArrayList<Brewery> breweries;
    public ArrayList<Tavern> taverns;

    public List<Point2D> getIntersections(){
        List<Point2D.Double> existingObjects=  Stream.concat(Stream.concat(farmlands.stream(), breweries.stream()),taverns.stream()).toList();
        return roads.stream()
                .flatMap(r -> Stream.of(r.getP1(), r.getP2()))
                .distinct()
                .filter(p -> existingObjects.stream().noneMatch(p2 -> p.equals(p2)))
                .toList();
    }

    public Data(ArrayList<Road> roads, ArrayList<Farmland> farmlands, ArrayList<Brewery> breweries, ArrayList<Tavern> taverns) {
        this.roads = roads;
        this.farmlands = farmlands;
        this.breweries = breweries;
        this.taverns = taverns;
    }
}
