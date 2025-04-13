package SiecPrzeplywowa;

import java.util.ArrayList;

public class Data {
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
