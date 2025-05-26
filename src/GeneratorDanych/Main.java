package GeneratorDanych;

import java.awt.geom.Point2D;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import SiecPrzeplywowa.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {

    public static void main(String[] args) {
        Generator generator = new Generator(21, 7, 13, 4, 6);
        Data data = generator.generate();
        //Data data = readFile();
        generateSVGfile(data.roads, data.farmlands, data.breweries, data.taverns);
        generateJSONfile(data);
    }

    public static void generateJSONfile(Data data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data.json"))) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();
            String json = gson.toJson(data);
            writer.write(json);
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisywania do pliku: " + e.getMessage());
        }
    }

    public static void generateSVGfile(ArrayList<Road> roads, ArrayList<Farmland> farmlands, ArrayList<Brewery> breweries, ArrayList<Tavern> taverns) {
        List<Point2D> coords = roads.stream().flatMap(x -> Stream.of(x.getP1(), x.getP2())).toList();
        double margin = 20;
        double minXCoord = coords.stream().min(Comparator.comparingDouble(Point2D::getX)).get().getX();
        double minYCoord = coords.stream().min(Comparator.comparingDouble(Point2D::getY)).get().getY();

        for (var r : roads)
            r.setLine(r.x1 - minXCoord + margin, r.y1 - minYCoord + margin, r.x2 - minXCoord + margin, r.y2 - minYCoord + margin);
        for (var f : farmlands) {
            f.setLocation(f.getX() - minXCoord + margin, f.getY() - minYCoord + margin);
        }
        for (var b : breweries)
            b.setLocation(b.getX() - minXCoord + margin, b.getY() - minYCoord + margin);
        for (var t : taverns)
            t.setLocation(t.getX() - minXCoord + margin, t.getY() - minYCoord + margin);

        coords = roads.stream().flatMap(x -> Stream.of(x.getP1(), x.getP2())).toList();
        Point2D maxXCoord = coords.stream().max(Comparator.comparingDouble(Point2D::getX)).orElse(null);
        Point2D maxYCoord = coords.stream().max(Comparator.comparingDouble(Point2D::getY)).orElse(null);

        double mapHeight = maxYCoord.getY() + (margin * 2);
        double mapWidth = maxXCoord.getX() + (margin * 2);

        var intersections = Generator.findIntersections(roads);
        try (PrintWriter writer = new PrintWriter(new FileWriter("map.svg"))) {
            writer.println("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<svg display=\"block\" style=\"background-color:black;\" height=\"" + mapHeight + "\" width=\"" + mapWidth + "\" xmlns=\"http://www.w3.org/2000/svg\">");

            for (Road road : roads) {
                writer.println("<line x1=\"" + String.format("%.0f", road.x1) + "\" y1=\"" + String.format("%.0f", road.y1) + "\" x2=\"" + String.format("%.0f", road.x2) + "\" y2=\"" + String.format("%.0f", road.y2) + "\"  style=\"stroke:#806f5b; stroke-width:1; opacity:0.6; \" />");
            }

            for (Point2D intersection : intersections) {
                writer.println("<circle cx=\"" + String.format("%.0f", intersection.getX()) + "\" cy=\"" + String.format("%.0f", intersection.getY()) + "\" r=\"2\" fill=\"#806f5b\" />");
            }

            for (Road road : roads) {
                writer.println("<text x=\"" + String.format("%.0f", (road.x1 + road.x2) / 2) + "\" y=\"" + String.format("%.0f", (road.y1 + road.y2) / 2) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-weight=\"600\" font-size=\"3.1\" text-anchor=\"middle\" dominant-baseline=\"middle\">" + road.getMaxBeerFlow() + "/" + road.getMaxBarleyFlow() + "</text>");
                writer.println("<text x=\"" + String.format("%.0f", (road.x1 + road.x2) / 2) + "\" y=\"" + String.format("%.0f", (road.y1 + road.y2+7) / 2) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-weight=\"600\" font-size=\"2.6\" text-anchor=\"middle\" dominant-baseline=\"middle\">" + road.getRepairCost() + "$</text>");
            }

            for (Farmland farmland : farmlands) {
                writer.println("<circle cx=\"" + String.format("%.0f", farmland.getX()) + "\" cy=\"" + String.format("%.0f", farmland.getY()) + "\" r=\"4.1\" fill=\"#FFC300\"  style=\"opacity:0.7; \"/>");
                writer.println("<text x=\"" + String.format("%.0f", farmland.getX()) + "\" y=\"" + String.format("%.0f", farmland.getY()) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-size=\"3.2\" font-weight=\"600\" text-anchor=\"middle\" dominant-baseline=\"end\">F</text>");
                writer.println("<text x=\"" + String.format("%.0f", farmland.getX()) + "\" y=\"" + String.format("%.0f", (farmland.getY() + 2.5)) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-size=\"2.5\" font-weight=\"600\" text-anchor=\"middle\" dominant-baseline=\"start\">" + farmland.getProductionCapacity() + "</text>");

            }

            for (Brewery brewery : breweries) {
                writer.println("<circle cx=\"" + String.format("%.0f", brewery.getX()) + "\" cy=\"" + String.format("%.0f", brewery.getY()) + "\" r=\"4.1\" fill=\"#90EE90\"  style=\"opacity:0.7; \" />");
                writer.println("<text x=\"" + String.format("%.0f", brewery.getX()) + "\" y=\"" + String.format("%.0f", brewery.getY()) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-size=\"3.2\" font-weight=\"600\" text-anchor=\"middle\" dominant-baseline=\"end\">B</text>");
                writer.println("<text x=\"" + String.format("%.0f", brewery.getX()) + "\" y=\"" + String.format("%.0f", (brewery.getY() + 2.5)) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-size=\"2.5\" font-weight=\"600\" text-anchor=\"middle\" dominant-baseline=\"start\">" + brewery.getProductionCapacity() + "</text>");
            }

            for (Tavern tavern : taverns) {
                writer.println("<circle cx=\"" + String.format("%.0f", tavern.getX()) + "\" cy=\"" + String.format("%.0f", tavern.getY()) + "\" r=\"4.1\" fill=\"#8B5A2B\" style=\"opacity:0.7; \"/>");
                writer.println("<text x=\"" + String.format("%.0f", tavern.getX()) + "\" y=\"" + String.format("%.0f", tavern.getY()) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-size=\"3.2\" font-weight=\"600\" text-anchor=\"middle\" dominant-baseline=\"end\">T</text>");
                writer.println("<text x=\"" + String.format("%.0f", tavern.getX()) + "\" y=\"" + String.format("%.0f", (tavern.getY() + 2.5)) + "\" fill=\"white\" font-family=\"Arial, sans-serif\" font-size=\"2.5\" font-weight=\"600\" text-anchor=\"middle\" dominant-baseline=\"start\">" + tavern.getConsumptionCapacity() + "</text>");
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

    public static Data readFile() {
        try (FileReader reader = new FileReader("gutData.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            Data data = gson.fromJson(reader, Data.class);
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
