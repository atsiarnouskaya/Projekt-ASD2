package GeneratorDanych;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Main {

    public static void main(String[] args) {
        Generator generator = new Generator(666);
        Data data = generator.generate();
        generateSVGfile(data.roads, data.farmlands, data.breweries, data.taverns);
        generateJSONfile(data);
    }

    public static void generateJSONfile(Data data) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("data.json"))) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(data);
            writer.write(json);
        } catch (IOException e) {
            System.err.println("Wystąpił błąd podczas zapisywania do pliku: " + e.getMessage());
        }
    }

    public static void generateSVGfile(ArrayList<Road> roads, ArrayList<Farmland> farmlands, ArrayList<Brewery> breweries, ArrayList<Tavern> taverns) {
        var intersections = Generator.findIntersections(roads);
        try (PrintWriter writer = new PrintWriter(new FileWriter("map.svg"))) {
            writer.println("<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<body>\n" +
                    "\n" +
                    "<svg height=\"1000\" width=\"1000\" xmlns=\"http://www.w3.org/2000/svg\">");

            for (Line2D.Double road : roads) {
                writer.println("<line x1=\"" + String.format("%.2f", road.x1) + "\" y1=\"" + String.format("%.2f", road.y1) + "\" x2=\"" + String.format("%.2f", road.x2) + "\" y2=\"" + String.format("%.2f", road.y2) + "\"  style=\"stroke:brown; stroke-width:1; opacity:0.6; \" />");
            }
            for (Point2D intersection : intersections) {
                writer.println("<circle cx=\"" + String.format("%.2f", intersection.getX()) + "\" cy=\"" + String.format("%.2f", intersection.getY()) + "\" r=\"2\" fill=\"brown\" />");
            }

            for (Point2D farmland : farmlands) {
                writer.println("<circle cx=\"" + String.format("%.2f", farmland.getX()) + "\" cy=\"" + String.format("%.2f", farmland.getY()) + "\" r=\"3\" fill=\"lightGreen\"  style=\"opacity:0.6; \"/>");
            }

            for (Point2D brewery : breweries) {
                writer.println("<circle cx=\"" + String.format("%.2f", brewery.getX()) + "\" cy=\"" + String.format("%.2f", brewery.getY()) + "\" r=\"3\" fill=\"gold\"  style=\"opacity:0.6; \" />");
            }

            for (Point2D tavern : taverns) {
                writer.println("<circle cx=\"" + String.format("%.2f", tavern.getX()) + "\" cy=\"" + String.format("%.2f", tavern.getY()) + "\" r=\"3\" fill=\"PaleVioletRed\"   style=\"opacity:0.6; \"/>");
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
