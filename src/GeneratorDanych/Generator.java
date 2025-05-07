package GeneratorDanych;

import SiecPrzeplywowa.*;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Generator {
    private int minDistanceBetweenPoints = 20;
    private Random random;
    private int roadsCount;
    private int minRoadLength = 50;
    private int maxRoadLength = 150;
    private int minCoordinates = 0;
    private int maxCoordinates = 1000;
    private int farmlandsCount;
    private int breweriesCount;
    private int tavernsCount;

    public Generator(int seed) {
        random = new Random(seed);
        farmlandsCount = 2;
        breweriesCount = 2;
        tavernsCount = 2;
        roadsCount = 2;
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

    private Road GenerateRandomRoad(double minLength, double maxLength) {
        Point2D.Double p1 = new Point2D.Double(random.nextInt(minCoordinates, maxCoordinates), random.nextInt(minCoordinates, maxCoordinates));
        Point2D.Double p2 = new Point2D.Double(random.nextInt(minCoordinates, maxCoordinates), random.nextInt(minCoordinates, maxCoordinates));
        while (calculateDistance(p1, p2) < minLength || calculateDistance(p1, p2) > maxLength)
            p2 = new Point2D.Double(random.nextInt(minCoordinates, maxCoordinates), random.nextInt(minCoordinates, maxCoordinates));
        return new Road(p1, p2);
    }

    public ArrayList<Road> generateRoads() {
        ArrayList<Road> roads = new ArrayList<>();
        Road road = GenerateRandomRoad(minRoadLength, maxRoadLength);
        roads.add(road);
        while (roads.size() < roadsCount) {
            while (true) {
                Road newRoad = GenerateRandomRoad(minRoadLength, maxRoadLength);
                if (roads.stream().noneMatch(r -> r.intersectsLine(newRoad)))
                    break;
                roads.add(newRoad);
            }
        }

        Stack<Road> roadsStack = new Stack<>();
        for (Road r : roads)
            roadsStack.push(r);

        while (!roadsStack.isEmpty()) {
            Road currentRoad = roadsStack.pop();

            for (int i = 0; i < roads.size(); i++) {
                var przecinana = roads.get(i);

                if (currentRoad != przecinana && !linesEndsTouch(currentRoad, przecinana) && currentRoad.intersectsLine(przecinana)) {
                    Point2D p = intersection(currentRoad, przecinana);

                    if (p != null && currentRoad.getP1() != p && currentRoad.getP2() != p) {
                        Point2D p2 = p;
                        Road newRoad1 = new Road(currentRoad.getP1(), p2);
                        Road newRoad2 = new Road(p2, currentRoad.getP2());

                        Road newRoad3 = new Road(przecinana.getP1(), p2);
                        Road newRoad4 = new Road(p2, przecinana.getP2());

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

        return roads;
    }

    public boolean RoadWithoutObjects(ArrayList<Road> roads,
                                      ArrayList<Farmland> farmlands,
                                      ArrayList<Brewery> breweries,
                                      ArrayList<Tavern> taverns,
                                      Road r) {

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

        return !twoIntersections
                        && !(farmlands.stream().anyMatch(p -> p.equals(r.getP1()) || p.equals(r.getP2()))
                        || breweries.stream().anyMatch(p -> p.equals(r.getP1()) || p.equals(r.getP2()))
                        || taverns.stream().anyMatch(p -> p.equals(r.getP1()) || p.equals(r.getP2()))
                );
    }

    public <T extends Point2D> ArrayList<T> generateObjects(
            ArrayList<Road> roads,
            ArrayList<Point2D> existingObjects,
            IFactory<T> factory,
            int count) {
        List<Point2D> intersections = findRoadEnds(roads, existingObjects);
        ArrayList<T> objects = new ArrayList<>();
        int currentCount = 0;
        while (currentCount != count) {
            int rand = random.nextInt(0, intersections.size());
            Point2D currentIntersection = intersections.get(rand);
            T object = factory.Create(random, (int)(currentIntersection.getX() + random.nextInt(-30, 30)), (int)(currentIntersection.getY() + random.nextInt(-30, 30)));

            if(roads.stream().anyMatch(r -> r.ptLineDist(object)<8))
                continue;

            Road road = new Road(object, currentIntersection);
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

    public boolean linesEndsTouch(Road line1, Road line2) {
        return calculateDistance(line1.getP1(), line2.getP1()) < 0.001
                || calculateDistance(line1.getP2(), line2.getP1()) < 0.001
                || calculateDistance(line1.getP1(), line2.getP2()) < 0.001
                || calculateDistance(line1.getP2(), line2.getP2()) < 0.001;
    }

    public Point2D intersection(Road a, Road b) {
        int x1 = (int)a.getX1(), y1 = (int)a.getY1(), x2 = (int)a.getX2(), y2 = (int)a.getY2(), x3 = (int)b.getX1(), y3 = (int)b.getY1(),
                x4 = (int)b.getX2(), y4 = (int)b.getY2();
        int d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0)
            return null;

        int xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        int yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point2D.Double(xi, yi);
    }

    public Data generate() {
        ArrayList<Road> roads = generateRoads2();
        ArrayList<Point2D> existingObjects = new ArrayList<>();
        ArrayList<Farmland> farmlands = generateObjects(roads, existingObjects, new FarmlandFactory(), farmlandsCount);
        existingObjects.addAll(farmlands);
        ArrayList<Brewery> breweries = generateObjects(roads, existingObjects, new BreweryFactory(), breweriesCount);
        existingObjects.addAll(breweries);
        ArrayList<Tavern> taverns = generateObjects(roads, existingObjects, new TavernFactory(), tavernsCount);
        existingObjects.addAll(taverns);

        RemoveRoadsWithoutObjects(roads, farmlands, breweries, taverns);
        for(var r: roads){
            r.setMaxBeerFlow(random.nextInt(0, 20));
            r.setMaxBarleyFlow(random.nextInt(0, 20));
            r.setRepairCost(random.nextInt(0, 30));
        }
        return new Data(roads, farmlands, breweries, taverns);
    }

    public void RemoveRoadsWithoutObjects(ArrayList<Road> roads, ArrayList<Farmland> farmlands, ArrayList<Brewery> breweries, ArrayList<Tavern> taverns) {
        while (true) {
            var roadsToRemove = roads.stream().filter(r -> RoadWithoutObjects(roads, farmlands, breweries, taverns, r)).findFirst();
            if (roadsToRemove.isEmpty())
                break;
            roads.remove(roadsToRemove.get());
        }
    }

    public static List<Point2D> findRoadEnds(ArrayList<Road> roads, ArrayList<Point2D> existingObjects) {
        return roads.stream()
                .flatMap(r -> Stream.of(r.getP1(), r.getP2()))
                .distinct()
                .filter(p -> existingObjects.stream().noneMatch(p2 -> p.equals(p2)))
                .toList();
    }

    public static List<Point2D> findIntersections(ArrayList<Road> roads) {
        List<Point2D> points = roads.stream()
                .flatMap(r -> Stream.of(r.getP1(), r.getP2()))
                .toList();

        var intersections = points.stream()
                .filter(i -> Collections.frequency(points, i) > 1)
                .collect(Collectors.toSet())
                .stream().toList();

        return intersections;
    }


    private Road GenerateRandomRoad2(Road road) {
        var p1 = road.getP2();
        var p2 = road.getP2();
        while (calculateDistance(p1, p2) < minRoadLength || calculateDistance(p1, p2) > maxRoadLength) {
            p2 = new Point2D.Double(road.getP2().getX() + random.nextInt(-maxRoadLength, maxRoadLength),
                    road.getP2().getY() + random.nextInt(-maxRoadLength, maxRoadLength));
        }

        return new Road(p1, p2);
    }

    public ArrayList<Road> generateRoads2() {
        ArrayList<Road> roads = new ArrayList<>();
        roads.add(GenerateRandomRoad(minRoadLength, maxRoadLength));

        int k = 0;
        while (roads.size() < roadsCount) {
            var existingRoad = roads.get(k++ % roads.size());
            int z =Math.min(random.nextInt(1, 3),  roadsCount - roads.size());
            for(int j = 0; j < z; j++){
                Road newRoad = GenerateRandomRoad2(existingRoad);
                while (true) {
                    if (!roads.stream().noneMatch(r -> r.intersectsLine(newRoad))) {
                        roads.add(newRoad);
                        break;
                    }
                }
            }
        }

        Stack<Road> roadsStack = new Stack<>();
        for (Road r : roads)
            roadsStack.push(r);

        while (!roadsStack.isEmpty()) {
            Road currentRoad = roadsStack.pop();

            for (int i = 0; i < roads.size(); i++) {
                var przecinana = roads.get(i);

                if (currentRoad != przecinana && !linesEndsTouch(currentRoad, przecinana) && currentRoad.intersectsLine(przecinana)) {
                    Point2D p = intersection(currentRoad, przecinana);

                    if (p != null && currentRoad.getP1() != p && currentRoad.getP2() != p) {
                        var p2 = p;
                        Road newRoad1 = new Road(currentRoad.getP1(), p2);
                        Road newRoad2 = new Road(p2, currentRoad.getP2());

                        Road newRoad3 = new Road(przecinana.getP1(), p2);
                        Road newRoad4 = new Road(p2, przecinana.getP2());

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

        return roads;
    }
}

