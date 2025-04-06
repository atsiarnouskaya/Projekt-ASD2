package GeneratorDanych;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Stream;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Generator {
    private double minDistanceBetweenPoints = 3;
    private Random random;
    private int minRoadsCount = 4;
    private int maxRoadsCount = 10;
    private int minIntersectionCount = 5;
    private int maxIntersectionCount = 20;
    private int minCoordinates = -50;
    private int maxCoordinates = 50;

    public Generator(int seed) {
        random = new Random(seed);
    }

    public double calculateDistance(Point x, Point y) {
        return sqrt(pow(y.getX() - x.getX(), 2) + pow(y.getY() - x.getY(), 2));
    }

    public double calculateDistance(Point x, Point2D y) {
        return sqrt(pow(y.getX() - x.getX(), 2) + pow(y.getY() - x.getY(), 2));
    }

    public double calculateDistance(Point2D x, Point2D y) {
        return sqrt(pow(y.getX() - x.getX(), 2) + pow(y.getY() - x.getY(), 2));
    }

    public double calculateDistanceToClosestPoint(ArrayList<? extends Point> points, Point point) {
        double minDistance = calculateDistance(points.getFirst(), point);
        for (int i = 1; i < points.size(); i++) {
            double currentDistance = calculateDistance(points.get(i), point);
            if (calculateDistance(points.get(i), point) < minDistance)
                minDistance = currentDistance;
        }
        return minDistance;
    }

    public ArrayList<Line2D.Double> generate() {
        ArrayList<Line2D.Double> roads = new ArrayList<>();
        //ArrayList<Intersection> intersections= new ArrayList<>();
        int roadsCount = random.nextInt(minRoadsCount, maxRoadsCount + 1);

        Intersection intersection = new Intersection(
                (int)(random.nextDouble(minCoordinates, maxCoordinates) * 1000)/1000.0,
                (int)(random.nextDouble(minCoordinates, maxCoordinates) * 1000)/1000.0);
        Intersection intersection2 = new Intersection(
                (int)(random.nextDouble(minCoordinates, maxCoordinates) * 1000)/1000.0,
                (int)(random.nextDouble(minCoordinates, maxCoordinates) * 1000)/1000.0);

        Line2D.Double road = new Line2D.Double(intersection.getX(), intersection.getY(), intersection2.getX(), intersection2.getY());
        roads.add(road);
        while (roads.size() < roadsCount) {
            while (true) {
                Point p1 = new Point((int)(random.nextDouble(minCoordinates, maxCoordinates) * 1000)/1000.0, (int)(random.nextDouble(minCoordinates, maxCoordinates) * 1000)/1000.0);
                Point p2 = new Point((int)(random.nextDouble(minCoordinates, maxCoordinates) * 1000)/1000.0, (int)(random.nextDouble(minCoordinates, maxCoordinates) * 1000)/1000.0);
                Line2D.Double newRoad = new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
                if (!road.intersectsLine(newRoad))
                    break;
                roads.add(newRoad);
            }
        }

        Stack<Line2D.Double> roadsStack = new Stack<>();
        for (Line2D.Double r : roads)
            roadsStack.push(r);

        System.out.println(roadsStack.size());
        while (!roadsStack.isEmpty()) {
            Line2D.Double currentRoad = roadsStack.pop();
            System.out.println("Stack: " + roadsStack.size());

            for (int i = 0; i < roads.size(); i++) {
                if (currentRoad != roads.get(i)
                        && !linesEndsTouch(currentRoad, roads.get(i))
                        && currentRoad.intersectsLine(roads.get(i))) {

                    Point p = intersection(currentRoad, roads.get(i));
                    if(p != null){
                        System.out.println("Road 1: " + currentRoad.x1 + " " + currentRoad.y1 + " " + currentRoad.x2 + " " + currentRoad.y2);
                        System.out.println("Road 2: " + roads.get(i).x1 + " " + roads.get(i).y1 + " " + roads.get(i).x2 + " " + roads.get(i).y2);
                        System.out.println("P: " + p.getX() + " " + p.getY());
                        System.out.println("");
                    }

                    if (p != null && ((p.getX() != currentRoad.x1 && p.getY() != currentRoad.y1) || (p.getX() != currentRoad.x2 && p.getY() != currentRoad.y2))) {

                        var closestIntersection = roads
                                .stream()
                                .flatMap(r -> Stream.of(r.getP1(), r.getP2()))
                                .filter(p2 -> calculateDistance(p, p2) < 0.001)
                                .findAny();

                        var p2 = p;
                        if(closestIntersection.isPresent())
                            p2 = new Point(closestIntersection.get().getX(), closestIntersection.get().getY());

                        Line2D.Double newRoad1 = new Line2D.Double(roads.get(i).x1, roads.get(i).y1, p2.getX(), p2.getY());
                        Line2D.Double newRoad2 = new Line2D.Double(p2.getX(), p2.getY(), roads.get(i).x2, roads.get(i).y2);
                        roads.remove(i);
                        roadsStack.push(newRoad1);
                        roadsStack.push(newRoad2);
                        roads.add(newRoad1);
                        roads.add(newRoad2);
                        break;
                    }
                }
            }
        }
        return roads;
    }

    public boolean linesEndsTouch(Line2D.Double line1, Line2D.Double line2) {
        return calculateDistance(line1.getP1(), line2.getP1()) < 0.001
                || calculateDistance(line1.getP2(), line2.getP1()) < 0.001
                || calculateDistance(line1.getP1(), line2.getP2()) < 0.001
                || calculateDistance(line1.getP2(), line2.getP2()) < 0.001;
    }

    public Point intersection(Line2D a, Line2D b) {
        double x1 = a.getX1(), y1 = a.getY1(), x2 = a.getX2(), y2 = a.getY2(), x3 = b.getX1(), y3 = b.getY1(),
                x4 = b.getX2(), y4 = b.getY2();
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0)
            return null;

        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point(xi, yi);
    }
}

