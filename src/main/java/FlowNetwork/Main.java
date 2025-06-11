package FlowNetwork;

import FlowNetwork.Visualization.VisualizationGenerator;
import com.google.gson.*;

import java.awt.geom.Point2D;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        Data data = readFile();
        ArrayList<Farmland> farmlands = new ArrayList<>();
        farmlands.addAll(data.farmlands);

        QuadrantManager manager = new QuadrantManager(farmlands);
        ArrayList<Quadrant> quadrants = manager.createQuadrants();

        Network S2 = createNetwork(data);
        barleyFlowBeforeDamage(S2, data, quadrants);
        beerFlowBeforeDamage(S2, data, quadrants);
        barleyFlowAfterDamage(S2, data, quadrants);
        beerFlowAfterDamage(S2, data, quadrants);

        for (int i = 0; i < quadrants.size(); i++) {
            Quadrant q = quadrants.get(i);
            System.out.println("Quadrant " + (i + 1));
            System.out.println("Production per plot: " + q.getProductionPerPlot());
            System.out.println("Hull points:");
            for (Point2D p : q.getHull()) {
                System.out.println("(" + p.getX() + ", " + p.getY() + ")");
            }
            System.out.println("Farmlands in this quadrant:");
            for (Farmland f : q.getFarmlands()) {
                System.out.println("(" + f.getX() + ", " + f.getY() + ")");
            }
            System.out.println();
        }
    }

    public static void barleyFlowBeforeDamage(Network S, Data data, ArrayList<Quadrant> quadrants) {
        Vertex src = S.addSourceVertex("Farmland");
        Vertex sink = S.addSinkVertex("Brewery");

        for (Road road : data.roads)
            S.setMaxFlow(road.getMaxBarleyFlow(), (int) road.x1, (int) road.y1, (int) road.x2, (int) road.y2);

        int maxBarleyFlowBeforeDamage = S.BFSMaxFlow(src, sink);
        S.printGraph();

        System.out.println("Max barley flow before damaging roads: " + maxBarleyFlowBeforeDamage + " with repair cost: " + S.getFlowRepairCost());
        //generateSVGfile(S, "BARLEY FLOW BEFORE DAMAGING", "barleyFlowBeforeDamage.svg", maxBarleyFlowBeforeDamage, quadrants);
        VisualizationGenerator.GenerateCytoscapeJSONfile(S, quadrants, "barleyFlowBeforeDamage", maxBarleyFlowBeforeDamage, S.getFlowRepairCost(), "data1");

        S.deleteSourceVertex(src);
        S.deleteSinkVertex(sink);
    }

    public static void beerFlowBeforeDamage(Network S, Data data, ArrayList<Quadrant> quadrants) {
        for (Road road : data.roads) {
            S.setMaxFlow(road.getMaxBeerFlow(), (int) road.x1, (int) road.y1, (int) road.x2, (int) road.y2);
            if (S.getVertex((int) road.x1, (int) road.y1).getType().equals("Brewery"))
                S.getVertex((int) road.x1, (int) road.y1).changeCapacity();
            if (S.getVertex((int) road.x2, (int) road.y2).getType().equals("Brewery"))
                S.getVertex((int) road.x2, (int) road.y2).changeCapacity();
        }

        Vertex src = S.addSourceVertex("Brewery");
        Vertex sink = S.addSinkVertex("Tavern");

        int maxBeerFlowBeforeDamage = S.BFSMaxFlow(src, sink);
        //S.printGraph();
        System.out.println("Max beer flow before damaging roads: " + maxBeerFlowBeforeDamage + " with repair cost: " + S.getFlowRepairCost());
        //generateSVGfile(S, "BEER FLOW BEFORE DAMAGING", "beerFlowBeforeDamage.svg", maxBeerFlowBeforeDamage, quadrants);
        VisualizationGenerator.GenerateCytoscapeJSONfile(S, quadrants, "beerFlowBeforeDamage", maxBeerFlowBeforeDamage, S.getFlowRepairCost(), "data3");

        S.deleteSourceVertex(src);
        S.deleteSinkVertex(sink);
    }

    public static void barleyFlowAfterDamage(Network S, Data data, ArrayList<Quadrant> quadrants) {
        Vertex src = S.addSourceVertex("Farmland");
        Vertex sink = S.addSinkVertex("Brewery");

        for (Road road : data.roads)
            S.setMaxFlow(road.getMaxBarleyFlow(), (int) road.x1, (int) road.y1, (int) road.x2, (int) road.y2);

        int maxBarleyFlowAfterDamage = S.minCostMaxFlow(src, sink);
        //S.printGraph();

        System.out.println("Max barley flow after damaging roads: " + maxBarleyFlowAfterDamage + " with repair cost: " + S.getFlowRepairCost());
        //generateSVGfile(S, "BARLEY FLOW AFTER DAMAGING", "barleyFlowAfterDamage.svg", maxBarleyFlowAfterDamage, quadrants);
        VisualizationGenerator.GenerateCytoscapeJSONfile(S, quadrants, "barleyFlowAfterDamage", maxBarleyFlowAfterDamage, S.getFlowRepairCost(), "data2");

        S.deleteSourceVertex(src);
        S.deleteSinkVertex(sink);
    }

    public static void beerFlowAfterDamage(Network S, Data data, ArrayList<Quadrant> quadrants) {
        for (Road road : data.roads) {
            S.setMaxFlow(road.getMaxBeerFlow(), (int) road.x1, (int) road.y1, (int) road.x2, (int) road.y2);
            if (S.getVertex((int) road.x1, (int) road.y1).getType().equals("Brewery"))
                S.getVertex((int) road.x1, (int) road.y1).changeCapacity();
            if (S.getVertex((int) road.x2, (int) road.y2).getType().equals("Brewery"))
                S.getVertex((int) road.x2, (int) road.y2).changeCapacity();
        }

        Vertex src = S.addSourceVertex("Brewery");
        Vertex sink = S.addSinkVertex("Tavern");

        int maxBeerFlowAfterDamage = S.minCostMaxFlow(src, sink);
        //S.printGraph();
        System.out.println("Max beer flow after damaging roads: " + maxBeerFlowAfterDamage + " with repair cost: " + S.getFlowRepairCost());
        //generateSVGfile(S, "BEER FLOW AFTER DAMAGING", "beerFlowAfterDamage.svg", maxBeerFlowAfterDamage, quadrants);
        VisualizationGenerator.GenerateCytoscapeJSONfile(S, quadrants, "beerFlowAfterDamage", maxBeerFlowAfterDamage, S.getFlowRepairCost(), "data4");

        S.deleteSourceVertex(src);
        S.deleteSinkVertex(sink);
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

    public static void generateSVGfile(Network network, String mapName, String fileName, int maxFlow, ArrayList<Quadrant> quadrants) {
        var roads = network.getGraph().values().stream()
                .flatMap(x -> x.values().stream())
                .filter(r -> r.getTo().getType() != "source"
                        && r.getTo().getType() != "sink"
                        && r.getFrom().getType() != "source"
                        && r.getFrom().getType() != "sink"
                )
                .filter(r -> r.getCurrentFlow() > 0
                                || (r.getCurrentFlow() == 0
                                && r.getReverseEdge().getCurrentFlow() == 0
                                && r.getFrom().getLocalId() > r.getTo().getLocalId()
                        )
                )
                .toList();
        var intersections = network.getGraph().keySet().stream().filter(v -> v.getType() == "Intersection").toList();
        var farmlands = network.getGraph().keySet().stream().filter(v -> v.getType() == "Farmland").toList();
        var breweries = network.getGraph().keySet().stream().filter(v -> v.getType() == "Brewery").toList();
        var taverns = network.getGraph().keySet().stream().filter(v -> v.getType() == "Tavern").toList();

        double margin = 20;
        int minXCoord = network.getGraph().keySet().stream().filter(v -> v.getType() != "source" && v.getType() != "sink").min(Comparator.comparingInt(Vertex::getX)).get().getX();
        int minYCoord = network.getGraph().keySet().stream().filter(v -> v.getType() != "source" && v.getType() != "sink").min(Comparator.comparingInt(Vertex::getY)).get().getY();
        int maxYCoord = network.getGraph().keySet().stream().filter(v -> v.getType() != "source" && v.getType() != "sink").max(Comparator.comparingInt(Vertex::getY)).get().getY();
        int maxXCoord = network.getGraph().keySet().stream().filter(v -> v.getType() != "source" && v.getType() != "sink").max(Comparator.comparingInt(Vertex::getX)).get().getX();

        double mapHeight = maxYCoord + (margin * 2);
        double mapWidth = maxXCoord + (margin * 2);

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.println("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<svg display=\"block\" style=\"background-color:black;\" height=\"" + mapHeight + "\" width=\"" + mapWidth + "\" xmlns=\"http://www.w3.org/2000/svg\">");
            writer.println("<text x=\"2\" y=\"" + (mapHeight - 2) + "\" font-family=\"Arial\" font-weight=\"600\" font-size=\"3.5px\" fill=\"white\">" + mapName + "  (max flow: " + maxFlow + "; flow cost: " + network.getFlowRepairCost() + ")</text>");

            for (Edge road : roads) {
                if (road.getCurrentFlow() != 0)
                    writer.println("<line x1=\"" + (road.getFrom().getX() - minXCoord + margin) +
                            "\" y1=\"" + (road.getFrom().getY() - minYCoord + margin) +
                            "\" x2=\"" + (road.getTo().getX() - minXCoord + margin) +
                            "\" y2=\"" + (road.getTo().getY() - minYCoord + margin) +
                            "\" style=\"stroke:#30271c; stroke-width:3; opacity:1;\" />");

                writer.println("<line x1=\"" + (road.getFrom().getX() - minXCoord + margin) + "\" y1=\"" + (road.getFrom().getY() - minYCoord + margin) + "\" x2=\"" + (road.getTo().getX() - minXCoord + margin) + "\" y2=\"" + (road.getTo().getY() - minYCoord + margin) + "\"  style=\"stroke:" + (road.getCurrentFlow() == 0 ? "#806f5b" : "#453828") + "; stroke-width:" + ((road.getCurrentFlow() == 0) ? "1" : "1.3") + "; opacity:" + ((road.getCurrentFlow() == 0) ? "0.6" : "1") + ";\" />");
            }

            for (Vertex intersection : intersections)
                writer.println("<circle cx=\"" + (intersection.getX() - minXCoord + margin) + "\" cy=\"" + (intersection.getY() - minYCoord + margin) + "\" r=\"2\" fill=\"#806f5b\" />");


            for (Edge road : roads) {
                writer.println("<text x=\"" + ((road.getFrom().getX() - minXCoord + margin) + (road.getTo().getX() - minXCoord + margin)) / 2 + "\" y=\"" + ((road.getFrom().getY() - minYCoord + margin) + (road.getTo().getY() - minYCoord + margin)) / 2 + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-weight=\"600\" font-size=\"3.1\" text-anchor=\"middle\" dominant-baseline=\"middle\">" + road.getCurrentFlow() + "/" + road.getMaxFlow() + "</text>");
                writer.println("<text x=\"" + ((road.getFrom().getX() - minXCoord + margin) + (road.getTo().getX() - minXCoord + margin)) / 2 + "\" y=\"" + ((((road.getFrom().getY() - minYCoord + margin) + (road.getTo().getY() - minYCoord + margin)) / 2) + 3.5) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-weight=\"600\" font-size=\"2.6\" text-anchor=\"middle\" dominant-baseline=\"middle\">" + road.getRepairCost() + "$</text>");
            }

            for (Vertex farmland : farmlands) {
                writer.println("<circle cx=\"" + (farmland.getX() - minXCoord + margin) + "\" cy=\"" + (farmland.getY() - minYCoord + margin) + "\" r=\"4.1\" fill=\"#FFC300\"  style=\"opacity:0.7; \"/>");
                writer.println("<text x=\"" + (farmland.getX() - minXCoord + margin) + "\" y=\"" + (farmland.getY() - minYCoord + margin) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-size=\"3.2\" font-weight=\"600\" text-anchor=\"middle\" dominant-baseline=\"end\">F</text>");
                writer.println("<text x=\"" + (farmland.getX() - minXCoord + margin) + "\" y=\"" + ((farmland.getY() - minYCoord + margin) + 2.5) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-size=\"2.5\" font-weight=\"600\" text-anchor=\"middle\" dominant-baseline=\"start\">" + farmland.getCapacity() + "</text>");
            }

            for (Vertex brewery : breweries) {
                int incomingFlow = roads.stream()
                        .filter(r -> r.getTo() == brewery)
                        .map(x -> x.getCurrentFlow())
                        .findFirst()
                        .orElse(0);
                writer.println("<circle cx=\"" + (brewery.getX() - minXCoord + margin) + "\" cy=\"" + (brewery.getY() - minYCoord + margin) + "\" r=\"4.1\" fill=\"#90EE90\"  style=\"opacity:0.7; \" />");
                writer.println("<text x=\"" + (brewery.getX() - minXCoord + margin) + "\" y=\"" + (brewery.getY() - minYCoord + margin) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-size=\"3.2\" font-weight=\"600\" text-anchor=\"middle\" dominant-baseline=\"end\">B</text>");
                writer.println("<text x=\"" + (brewery.getX() - minXCoord + margin) + "\" y=\"" + ((brewery.getY() - minYCoord + margin) + 2.5) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-size=\"2.5\" font-weight=\"600\" text-anchor=\"middle\" dominant-baseline=\"start\">" + incomingFlow + "/" + brewery.getCapacity() + "</text>");
            }

            for (Vertex tavern : taverns) {
                int incomingFlow = roads.stream()
                        .filter(r -> r.getTo() == tavern)
                        .map(x -> x.getCurrentFlow())
                        .findFirst()
                        .orElse(0);
                writer.println("<circle cx=\"" + (tavern.getX() - minXCoord + margin) + "\" cy=\"" + (tavern.getY() - minYCoord + margin) + "\" r=\"4.1\" fill=\"#8B5A2B\"   style=\"opacity:0.7; \"/>");
                writer.println("<text x=\"" + (tavern.getX() - minXCoord + margin) + "\" y=\"" + (tavern.getY() - minYCoord + margin) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-size=\"3.2\" font-weight=\"600\" text-anchor=\"middle\" dominant-baseline=\"end\">T</text>");
                writer.println("<text x=\"" + (tavern.getX() - minXCoord + margin) + "\" y=\"" + ((tavern.getY() - minYCoord + margin) + 2.5) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-size=\"2.5\" font-weight=\"600\" text-anchor=\"middle\" dominant-baseline=\"start\">" + incomingFlow + "/" + tavern.getCapacity() + "</text>");
            }

            if (quadrants != null) {
                for (var q : quadrants) {
                    StringBuilder polygonPoints = new StringBuilder();
                    for (Point2D p : q.getHull()) {
                        double x = p.getX() - minXCoord + margin;
                        double y = p.getY() - minYCoord + margin;
                        polygonPoints.append(x).append(",").append(y).append(" ");
                    }
                    writer.println("<polygon points=\"" + polygonPoints.toString().trim() + "\" " + "style=\"fill:none;stroke:#fcba03;stroke-width:0.7;\" />");
                }
            }

            writer.println("\n" +
                    "</svg>\n" +
                    "\n" +
                    "</body>\n" +
                    "</html>");
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisywania do pliku: " + e.getMessage());
        }
    }
}
