package GeneratorDanych;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;
import java.util.stream.Stream;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Generator {
    private double minDistanceBetweenPoints = 3;
    private Random random;
    private int roadsCount;
    private int minRoadLength = 50;
    private int maxRoadLength = 150;
    private int minCoordinates = 0;
    private int maxCoordinates = 1000;
    private int farmlandsCount;
    private int breweriesCount;
    private int tavernsCount;

    public Generator() {
        random = new Random(System.nanoTime());
        farmlandsCount = 15;
        breweriesCount = 6;
        tavernsCount = 8;
        roadsCount = 6;
    }

    public Generator(int seed) {
        random = new Random(seed);
        farmlandsCount = 15;
        breweriesCount = 6;
        tavernsCount = 8;
        roadsCount = 6;
    }

    public Generator(int seed, int roadsCount, int farmlandsCount, int breweriesCount, int tavernsCount) {
        random = new Random(seed);
        this.roadsCount = roadsCount;
        this.farmlandsCount = farmlandsCount;
        this.breweriesCount = breweriesCount;
        this.tavernsCount = tavernsCount;
    }

    public double calculateDistance(Point2D x, Point2D y) {
        return sqrt(pow(y.getX() - x.getX(), 2) + pow(y.getY() - x.getY(), 2));
    }

//    public double calculateDistanceToClosestPoint(ArrayList<? extends Point2D> points, Point2D point) {
//        double minDistance = calculateDistance(points.getFirst(), point);
//        for (int i = 1; i < points.size(); i++) {
//            double currentDistance = calculateDistance(points.get(i), point);
//            if (calculateDistance(points.get(i), point) < minDistance)
//                minDistance = currentDistance;
//        }
//        return minDistance;
//    }

    private Line2D.Double GenerateRandomRoad(double minLength, double maxLength) {
        var p1 = new Point2D.Double(random.nextDouble(minCoordinates, maxCoordinates), random.nextDouble(minCoordinates, maxCoordinates));
        var p2 = new Point2D.Double(random.nextDouble(minCoordinates, maxCoordinates), random.nextDouble(minCoordinates, maxCoordinates));
        while (calculateDistance(p1, p2) < minLength || calculateDistance(p1, p2) > maxLength)
            p2 = new Point2D.Double(random.nextDouble(minCoordinates, maxCoordinates), random.nextDouble(minCoordinates, maxCoordinates));
        return new Line2D.Double(p1, p2);
    }


    public ArrayList<Line2D.Double> generateRoads() {
        ArrayList<Line2D.Double> roads = new ArrayList<>();
        var road = GenerateRandomRoad(minRoadLength, maxRoadLength);
        roads.add(road);
        while (roads.size() < roadsCount) {
            while (true) {
                Line2D.Double newRoad = GenerateRandomRoad(minRoadLength, maxRoadLength);
                if (roads.stream().noneMatch(r -> r.intersectsLine(newRoad)))
                    break;
                roads.add(newRoad);
            }
        }

        Stack<Line2D.Double> roadsStack = new Stack<>();
        for (Line2D.Double r : roads)
            roadsStack.push(r);

        while (!roadsStack.isEmpty()) {
            Line2D.Double currentRoad = roadsStack.pop();

            for (int i = 0; i < roads.size(); i++) {
                var przecinana = roads.get(i);

                if (currentRoad != przecinana && !linesEndsTouch(currentRoad, przecinana) && currentRoad.intersectsLine(przecinana)) {
                    Point2D p = intersection(currentRoad, przecinana);

                    if (p != null && currentRoad.getP1() != p && currentRoad.getP2() != p) {
                        var p2 = p;
                        Line2D.Double newRoad1 = new Line2D.Double(currentRoad.getP1(), p2);
                        Line2D.Double newRoad2 = new Line2D.Double(p2, currentRoad.getP2());

                        Line2D.Double newRoad3 = new Line2D.Double(przecinana.getP1(), p2);
                        Line2D.Double newRoad4 = new Line2D.Double(p2, przecinana.getP2());

                        roads.remove(przecinana);
                        roadsStack.remove(przecinana);
                        roads.remove(currentRoad);

                        roadsStack.push(newRoad4);
                        roadsStack.push(newRoad3);
                        roadsStack.push(newRoad2);
                        roadsStack.push(newRoad1);

                        roads.add(newRoad1);
                        roads.add(newRoad2);
                        roads.add(newRoad3);
                        roads.add(newRoad4);
                        break;
                    }
                }
            }
        }

        while (true) {
            var roadsToRemove = roads.stream().filter(r -> DrogaLuzna(roads, r)).findFirst();
            if (roadsToRemove.isEmpty())
                break;
            roads.remove(roadsToRemove.get());
        }

        return roads;
    }

    public boolean DrogaLuzna(ArrayList<Line2D.Double> roads, Line2D r) {
        var twoIntersections = roads
                .stream()
                .flatMap(r2 -> Stream.of(r2.getP1(), r2.getP2()))
                .filter(p -> p.getX() == r.getP1().getX() && p.getY() == r.getP1().getY())
                .count() > 1

                && roads
                .stream()
                .flatMap(r2 -> Stream.of(r2.getP1(), r2.getP2()))
                .filter(p -> p.getX() == r.getP2().getX() && p.getY() == r.getP2().getY())
                .count() > 1;

        return calculateDistance(r.getP1(), r.getP2()) < minRoadLength && !twoIntersections;
    }

    public ArrayList<Intersection> generateIntersections(ArrayList<Line2D.Double> roads) {
        ArrayList<Intersection> intersections = new ArrayList<>();
        for (Line2D.Double road : roads) {
            if (!intersections.contains(road.getP1()))
                intersections.add(new Intersection(road.getP1()));

            if (!intersections.contains(road.getP2()))
                intersections.add(new Intersection(road.getP2()));
        }
        return intersections;
    }

    public <T extends Point2D> ArrayList<T> generateObjects(
            ArrayList<Intersection> intersections,
            ArrayList<Line2D.Double> roads,
            IFactory<T> factory,
            int count) {
        ArrayList<T> objects = new ArrayList<>();
        int currentCount = 0;
        while (currentCount != count) {
            int rand = random.nextInt(0, intersections.size());
            Point2D currentIntersection = intersections.get(rand);
            T object = factory.Create(random, currentIntersection.getX() + random.nextInt(-30, 30), currentIntersection.getY() + random.nextInt(-30, 30));

            Line2D.Double road = new Line2D.Double(object, currentIntersection);
            if ((calculateDistance(road.getP1(), road.getP2()) < minDistanceBetweenPoints) || (roads.stream()
                    .filter(r -> !r.getP1().equals(currentIntersection) && !r.getP2().equals(currentIntersection))
                    .anyMatch(r -> r.intersectsLine(road))))
                continue;

            objects.add(object);
            roads.add(road);
            currentCount++;
        }
        return objects;
    }

    public boolean linesEndsTouch(Line2D.Double line1, Line2D.Double line2) {
        return calculateDistance(line1.getP1(), line2.getP1()) < 0.001
                || calculateDistance(line1.getP2(), line2.getP1()) < 0.001
                || calculateDistance(line1.getP1(), line2.getP2()) < 0.001
                || calculateDistance(line1.getP2(), line2.getP2()) < 0.001;
    }

    public Point2D intersection(Line2D a, Line2D b) {
        double x1 = a.getX1(), y1 = a.getY1(), x2 = a.getX2(), y2 = a.getY2(), x3 = b.getX1(), y3 = b.getY1(),
                x4 = b.getX2(), y4 = b.getY2();
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0)
            return null;

        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point2D.Double(xi, yi);
    }

    public int getFarmlandsCount() {
        return farmlandsCount;
    }

    public int getBreweriesCount() {
        return breweriesCount;
    }

    public int getTavernsCount() {
        return tavernsCount;
    }

    public Data generate() {

        ArrayList<Line2D.Double> roads = generateRoads();
        ArrayList<Intersection> intersections = generateIntersections(roads);
        ArrayList<Farmland> farmlands = generateObjects(intersections, roads, new FarmlandFactory(), getFarmlandsCount());
        ArrayList<Brewery> breweries = generateObjects(intersections, roads, new BreweryFactory(), getBreweriesCount());
        ArrayList<Tavern> taverns = generateObjects(intersections, roads, new TavernFactory(), getTavernsCount());

        return new Data(roads, farmlands, breweries, taverns);
    }
}

class Data {
    public ArrayList<Line2D.Double> roads;
    public ArrayList<Point2D> intersections;
    public ArrayList<Farmland> farmlands;
    public ArrayList<Brewery> breweries;
    public ArrayList<Tavern> taverns;

    public Data(ArrayList<Line2D.Double> roads, ArrayList<Farmland> farmlands, ArrayList<Brewery> breweries, ArrayList<Tavern> taverns) {
        this.roads = roads;
        this.farmlands = farmlands;
        this.breweries = breweries;
        this.taverns = taverns;
    }
}

interface IFactory<T> {
    T Create(Random random, double x, double y);
}

class FarmlandFactory implements IFactory<Farmland> {
    @Override
    public Farmland Create(Random random, double x, double y) {
        double prodCap = random.nextDouble(10, 10000);
        return new Farmland(x, y, prodCap);
    }
}

class BreweryFactory implements IFactory<Brewery> {
    @Override
    public Brewery Create(Random random, double x, double y) {
        double prodCap = random.nextDouble(10, 10000);
        return new Brewery(x, y, prodCap);
    }
}

class TavernFactory implements IFactory<Tavern> {
    @Override
    public Tavern Create(Random random, double x, double y) {
        return new Tavern(x, y);
    }
}