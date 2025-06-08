package DataGenerator;

import FlowNetwork.*;

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

    private Road GenerateRandomRoad(double minLength, double maxLength) {
        Point2D.Double p1 = new Point2D.Double(random.nextInt(minCoordinates, maxCoordinates), random.nextInt(minCoordinates, maxCoordinates));
        Point2D.Double p2 = new Point2D.Double(random.nextInt(minCoordinates, maxCoordinates), random.nextInt(minCoordinates, maxCoordinates));
        while (p1.distance(p2) < minLength || p1.distance(p2) > maxLength)
            p2 = new Point2D.Double(random.nextInt(minCoordinates, maxCoordinates), random.nextInt(minCoordinates, maxCoordinates));
        return new Road(p1, p2);
    }

    public ArrayList<Road> addRoad(ArrayList<Road> _roads) {
        var roads = new ArrayList<>(_roads);
        ArrayList<Point2D> intersections = new ArrayList<>(findRoadEnds(roads, new ArrayList<>()));
        Road newRoad = GenerateRandomRoad(minRoadLength, maxRoadLength);
        if (roads.stream().anyMatch(r -> r.intersectsLine(newRoad))) {
            roads.add(newRoad);
            Stack<Road> roadsStack = new Stack<>();
            for (Road r : roads)
                roadsStack.push(r);
            while (!roadsStack.isEmpty()) {
                Road currentRoad = roadsStack.pop();
                for (int i = 0; i < roads.size(); i++) {
                    var intersectedRoad = roads.get(i);
                    if (currentRoad != intersectedRoad && !linesEndsTouch(currentRoad, intersectedRoad) && currentRoad.intersectsLine(intersectedRoad)) {
                        Point2D p = intersection(currentRoad, intersectedRoad);
                        if (p != null && currentRoad.getP1() != p && currentRoad.getP2() != p) {
                            //check if new intersections not too close to others
                            for (Point2D existing : intersections) {
                                if(existing.distance(p)<15)
                                    return _roads;
                            }
                            intersections.add(p);
                            Point2D p2 = p;
                            Road newRoad1 = new Road(currentRoad.getP1(), p2);
                            Road newRoad2 = new Road(p2, currentRoad.getP2());

                            Road newRoad3 = new Road(intersectedRoad.getP1(), p2);
                            Road newRoad4 = new Road(p2, intersectedRoad.getP2());

                            roads.remove(intersectedRoad);
                            roadsStack.remove(intersectedRoad);
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
        }
        return roads;
    }

    public ArrayList<Road> generateRoads() {
        ArrayList<Road> roads = new ArrayList<>();
        Road road = GenerateRandomRoad(minRoadLength, maxRoadLength);
        roads.add(road);
        int maxAttempts = 100000;
        int attempts = 0;
        while (roads.size() < roadsCount && attempts < maxAttempts) {
            var newRoads = addRoad(roads);
            if (newRoads.size() > roads.size()) {
                roads = newRoads;
                attempts = 0;
            } else
                attempts++;
            if (attempts >= maxAttempts)
                System.out.println("Warning: Reached maximum attempts to add roads.");
        }
        return new ArrayList(roads);
    }

    public <T extends Point2D> ArrayList<T> generateObjects(
            ArrayList<Road> roads,
            ArrayList<Point2D> existingObjects,
            IFactory<T> factory,
            int count) {

        var allObjects = new ArrayList<>(existingObjects);
        ArrayList<T> objects = new ArrayList<>();
        int currentCount = 0;
        int i = 0;

        int attempts = 0;
        int maxAttempts = 5000;

        while (currentCount != count && attempts < maxAttempts) {
            List<Point2D> intersections = findRoadEnds(roads, allObjects);
            Point2D currentIntersection = intersections.get(i++ % intersections.size());
            var x = random.nextInt(50) - 25;
            var y = random.nextInt(50) - 25;

            T object = factory.Create(random, (int) (currentIntersection.getX() + x), (int) (currentIntersection.getY() + y));
            if (roads.stream().anyMatch(r -> r.ptSegDist(object) < 10)) {
                attempts++;
                continue;
            }

            Road road = new Road(object, currentIntersection);
            if ((road.getP1().distance(road.getP2()) < minDistanceBetweenPoints)
                    || (roads.stream()
                    .filter(r -> !r.getP1().equals(currentIntersection) && !r.getP2().equals(currentIntersection))
                    .anyMatch(r -> r.intersectsLine(road)))
                    || objects.stream().anyMatch(o -> o.distance(object) < minDistanceBetweenPoints)
                    || existingObjects.stream().anyMatch(o -> o.distance(object) < minDistanceBetweenPoints)
            ){
                attempts++;
                continue;
            }

            objects.add(object);
            allObjects.add(object);
            roads.add(road);
            currentCount++;
            attempts=0;
        }
        if (currentCount < count) {
            System.out.println("Warning: Could only place " + currentCount + " out of " + count + " objects.");
        }
        return objects;
    }

    public boolean linesEndsTouch(Road line1, Road line2) {
        return line1.getP1().distance(line2.getP1()) < 0.001
                || line1.getP2().distance(line2.getP1()) < 0.001
                || line1.getP1().distance(line2.getP2()) < 0.001
                || line1.getP2().distance(line2.getP2()) < 0.001;
    }

    public Point2D intersection(Road a, Road b) {
        int x1 = (int) a.getX1(), y1 = (int) a.getY1(), x2 = (int) a.getX2(), y2 = (int) a.getY2(), x3 = (int) b.getX1(), y3 = (int) b.getY1(),
                x4 = (int) b.getX2(), y4 = (int) b.getY2();
        int d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0)
            return null;

        int xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        int yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point2D.Double(xi, yi);
    }

    public Data generate() {
        ArrayList<Road> roads = generateRoads();
        ArrayList<Point2D> existingObjects = new ArrayList<>();
        ArrayList<Farmland> farmlands = generateObjects(roads, existingObjects, new FarmlandFactory(), farmlandsCount);
        existingObjects.addAll(farmlands);
        ArrayList<Brewery> breweries = generateObjects(roads, existingObjects, new BreweryFactory(), breweriesCount);
        existingObjects.addAll(breweries);
        ArrayList<Tavern> taverns = generateObjects(roads, existingObjects, new TavernFactory(), tavernsCount);
        existingObjects.addAll(taverns);

        while (true)
            if (!roads.removeIf(r -> RoadWithoutObjects(roads, farmlands, breweries, taverns, r))) break;

        for (var r : roads) {
            r.setMaxBeerFlow(random.nextInt(0, 20));
            r.setMaxBarleyFlow(random.nextInt(0, 20));
            r.setRepairCost(random.nextInt(0, 30));
        }
        return new Data(roads, farmlands, breweries, taverns);
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

    public static List<Point2D> findRoadEnds(ArrayList<Road> roads, ArrayList<Point2D> existingObjects) {
        return roads.stream()
                .flatMap(r -> Stream.of(r.getP1(), r.getP2()))
                .distinct()
                .filter(p -> existingObjects.stream().noneMatch(p2 -> p.equals(p2)))
                .toList();
    }
}

