package FlowNetwork;

import FlowNetwork.Visualization.VisualizationGenerator;
import com.google.gson.*;

import java.awt.geom.Point2D;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class FlowNetworkMain {
    private static final FileWriter writer;

    static {
        try {
            writer = new FileWriter("src/logs/output.txt", true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void run() throws IOException {
        Data data = readFile();
        ArrayList<Farmland> farmlands = new ArrayList<>();
        farmlands.addAll(data.farmlands);

        QuadrantManager manager = new QuadrantManager(farmlands);
        ArrayList<Quadrant> quadrants = manager.createQuadrants();

        writer.write("[OK] Quadrants: " + quadrants.size() + " were created\n");
        writer.flush();

        for (int i = 0; i < quadrants.size(); i++) {

            Quadrant q = quadrants.get(i);

            writer.write("Quadrant " + (i + 1) + "\n");
            writer.write("Production per plot: " + q.getProductionPerPlot() + "\n");
            writer.write("Hull points: " + "\n");
            writer.flush();

            for (Point2D p : q.getHull()) {
                writer.write("(" + p.getX() + ", " + p.getY() + ")" + "\n");
                writer.flush();
            }

            writer.write("Farmlands in this quadrant:" + "\n");
            writer.flush();

            for (Farmland f : q.getFarmlands()) {
                writer.write("(" + f.getX() + ", " + f.getY() + ")" + "\n");
                writer.flush();
            }
        }

        Network S2 = createNetwork(data);

        writer.write("[OK] The network was created without errors\n");
        writer.flush();

        barleyFlowBeforeDamage(S2, data, quadrants);
        beerFlowBeforeDamage(S2, data, quadrants);
        barleyFlowAfterDamage(S2, data, quadrants);
        beerFlowAfterDamage(S2, data, quadrants);
    }

    public static void barleyFlowBeforeDamage(Network S, Data data, ArrayList<Quadrant> quadrants) throws IOException {
        Vertex src = S.addSourceVertex("Farmland");

        writer.write("[OK]  Source vertex for farmlands was created\n");
        writer.flush();

        Vertex sink = S.addSinkVertex("Brewery");

        writer.write("[OK]  Sink vertex for breweries was created\n");
        writer.flush();

        for (Road road : data.roads)
            S.setMaxFlow(road.getMaxBarleyFlow(), (int) road.x1, (int) road.y1, (int) road.x2, (int) road.y2);

        int maxBarleyFlowBeforeDamage = S.BFSMaxFlow(src, sink);

        writer.write("[BARLEY FLOW BEFORE DAMAGING] " + maxBarleyFlowBeforeDamage + "\n");
        writer.flush();

        VisualizationGenerator.GenerateCytoscapeJSONfile(S, quadrants, "barleyFlowBeforeDamage", maxBarleyFlowBeforeDamage, S.getFlowRepairCost(), "data1");

        S.deleteSourceVertex(src);

        writer.write("[OK]  Source vertex for farmlands was deleted\n");
        writer.flush();

        S.deleteSinkVertex(sink);

        writer.write("[OK]  Sink vertex for breweries was deleted\n");
        writer.flush();
    }

    public static void beerFlowBeforeDamage(Network S, Data data, ArrayList<Quadrant> quadrants) throws IOException {
        for (Road road : data.roads) {
            S.setMaxFlow(road.getMaxBeerFlow(), (int) road.x1, (int) road.y1, (int) road.x2, (int) road.y2);
            if (S.getVertex((int) road.x1, (int) road.y1).getType().equals("Brewery"))
                S.getVertex((int) road.x1, (int) road.y1).changeCapacity();
            if (S.getVertex((int) road.x2, (int) road.y2).getType().equals("Brewery"))
                S.getVertex((int) road.x2, (int) road.y2).changeCapacity();
        }

        Vertex src = S.addSourceVertex("Brewery");

        writer.write("[OK]  Source vertex for breweries was created\n");
        writer.flush();

        Vertex sink = S.addSinkVertex("Tavern");

        writer.write("[OK]  Sink vertex for taverns was created\n");
        writer.flush();

        int maxBeerFlowBeforeDamage = S.BFSMaxFlow(src, sink);

        writer.write("[BEER FLOW BEFORE DAMAGING] " + maxBeerFlowBeforeDamage + "\n");
        writer.flush();

        VisualizationGenerator.GenerateCytoscapeJSONfile(S, quadrants, "beerFlowBeforeDamage", maxBeerFlowBeforeDamage, S.getFlowRepairCost(), "data3");

        S.deleteSourceVertex(src);

        writer.write("[OK]  Source vertex for breweries was deleted\n");
        writer.flush();

        S.deleteSinkVertex(sink);

        writer.write("[OK]  Sink vertex for taverns was deleted\n");
        writer.flush();
    }

    public static void barleyFlowAfterDamage(Network S, Data data, ArrayList<Quadrant> quadrants) throws IOException {
        Vertex src = S.addSourceVertex("Farmland");

        writer.write("[OK]  Source vertex for farmlands was created\n");
        writer.flush();

        Vertex sink = S.addSinkVertex("Brewery");

        writer.write("[OK]  Sink vertex for breweries was created\n");
        writer.flush();

        for (Road road : data.roads)
            S.setMaxFlow(road.getMaxBarleyFlow(), (int) road.x1, (int) road.y1, (int) road.x2, (int) road.y2);

        int maxBarleyFlowAfterDamage = S.minCostMaxFlow(src, sink);

        writer.write("[BARLEY FLOW AFTER DAMAGING] " + maxBarleyFlowAfterDamage + "\n");
        writer.flush();

        VisualizationGenerator.GenerateCytoscapeJSONfile(S, quadrants, "barleyFlowAfterDamage", maxBarleyFlowAfterDamage, S.getFlowRepairCost(), "data2");

        S.deleteSourceVertex(src);

        writer.write("[OK]  Source vertex for farmlands was deleted\n");
        writer.flush();

        S.deleteSinkVertex(sink);

        writer.write("[OK]  Sink vertex for breweries was deleted\n");
        writer.flush();
    }

    public static void beerFlowAfterDamage(Network S, Data data, ArrayList<Quadrant> quadrants) throws IOException {
        for (Road road : data.roads) {
            S.setMaxFlow(road.getMaxBeerFlow(), (int) road.x1, (int) road.y1, (int) road.x2, (int) road.y2);
            if (S.getVertex((int) road.x1, (int) road.y1).getType().equals("Brewery"))
                S.getVertex((int) road.x1, (int) road.y1).changeCapacity();
            if (S.getVertex((int) road.x2, (int) road.y2).getType().equals("Brewery"))
                S.getVertex((int) road.x2, (int) road.y2).changeCapacity();
        }

        Vertex src = S.addSourceVertex("Brewery");

        writer.write("[OK]  Source vertex for breweries was created\n");
        writer.flush();

        Vertex sink = S.addSinkVertex("Tavern");

        writer.write("[OK]  Sink vertex for taverns was created\n");
        writer.flush();

        int maxBeerFlowAfterDamage = S.minCostMaxFlow(src, sink);

        writer.write("[BEER FLOW AFTER DAMAGING] " + maxBeerFlowAfterDamage + "\n");

        VisualizationGenerator.GenerateCytoscapeJSONfile(S, quadrants, "beerFlowAfterDamage", maxBeerFlowAfterDamage, S.getFlowRepairCost(), "data4");

        S.deleteSourceVertex(src);

        writer.write("[OK]  Source vertex for breweries was deleted\n");
        writer.flush();

        S.deleteSinkVertex(sink);

        writer.write("[OK]  Sink vertex for taverns was deleted\n");
        writer.flush();
    }

    public static Network createNetwork(Data data) {
        Network S2 = new Network();
        List<Point2D> intersections = data.getIntersections();

        for (var point : intersections) {
            Vertex v = S2.addVertex((int) point.getX(), (int) point.getY());
            v.setType("Intersection");
        }

        for (var farmland : data.farmlands) {
            Vertex v = S2.addVertex((int) farmland.getX(), (int) farmland.getY());
            v.setType("Farmland");
            v.setCapacity(farmland.getQuadrant().getProductionPerPlot());
        }

        for (var breweries : data.breweries) {
            Vertex v = S2.addVertex((int) breweries.getX(), (int) breweries.getY());
            v.setType("Brewery");
            v.setCapacity(breweries.getProductionCapacity());
        }

        for (var tavern : data.taverns) {
            Vertex v = S2.addVertex((int) tavern.getX(), (int) tavern.getY());
            v.setType("Tavern");
            v.setCapacity(tavern.getConsumptionCapacity());
        }

        for (Road road : data.roads)
            S2.addEdge(road.getMaxBarleyFlow(), road.getRepairCost(), (int) road.x1, (int) road.y1, (int) road.x2, (int) road.y2);
        return S2;
    }

    public static Data readFile() {
        try (FileReader reader = new FileReader("data.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            return gson.fromJson(reader, Data.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
