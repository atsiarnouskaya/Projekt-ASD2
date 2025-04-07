package GeneratorDanych;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.stream.Collectors;
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

    public Generator(int seed) {
        random = new Random(seed);
        farmlandsCount = 15;
        breweriesCount = 6;
        tavernsCount = 8;
        roadsCount = 20;
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

    private Road GenerateRandomRoad(double minLength, double maxLength) {
        var p1 = new Point2D.Double(random.nextDouble(minCoordinates, maxCoordinates), random.nextDouble(minCoordinates, maxCoordinates));
        var p2 = new Point2D.Double(random.nextDouble(minCoordinates, maxCoordinates), random.nextDouble(minCoordinates, maxCoordinates));
        while (calculateDistance(p1, p2) < minLength || calculateDistance(p1, p2) > maxLength)
            p2 = new Point2D.Double(random.nextDouble(minCoordinates, maxCoordinates), random.nextDouble(minCoordinates, maxCoordinates));
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

    public boolean DrogaLuzna(ArrayList<Road> roads,
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

        return // calculateDistance(r.getP1(), r.getP2()) < minRoadLength &&
                !twoIntersections
                && !(farmlands.stream().anyMatch(p -> p.equals(r.getP1()) || p.equals(r.getP2()))
                || breweries.stream().anyMatch(p -> p.equals(r.getP1()) || p.equals(r.getP2()))
                || taverns.stream().anyMatch(p -> p.equals(r.getP1()) || p.equals(r.getP2()))
                );
    }

    public <T extends Point2D> ArrayList<T> generateObjects(
            ArrayList<Road> roads,
            IFactory<T> factory,
            int count) {
        List<Point2D> intersections = findRoadEnds(roads);
        ArrayList<T> objects = new ArrayList<>();
        int currentCount = 0;
        while (currentCount != count) {
            int rand = random.nextInt(0, intersections.size());
            Point2D currentIntersection = intersections.get(rand);
            T object = factory.Create(random, currentIntersection.getX() + random.nextInt(-30, 30), currentIntersection.getY() + random.nextInt(-30, 30));

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
        double x1 = a.getX1(), y1 = a.getY1(), x2 = a.getX2(), y2 = a.getY2(), x3 = b.getX1(), y3 = b.getY1(),
                x4 = b.getX2(), y4 = b.getY2();
        double d = (x1 - x2) * (y3 - y4) - (y1 - y2) * (x3 - x4);
        if (d == 0)
            return null;

        double xi = ((x3 - x4) * (x1 * y2 - y1 * x2) - (x1 - x2) * (x3 * y4 - y3 * x4)) / d;
        double yi = ((y3 - y4) * (x1 * y2 - y1 * x2) - (y1 - y2) * (x3 * y4 - y3 * x4)) / d;

        return new Point2D.Double(xi, yi);
    }

    public Data generate() {
        ArrayList<Road> roads = generateRoads();
        ArrayList<Farmland> farmlands = generateObjects(roads, new FarmlandFactory(), farmlandsCount);
        ArrayList<Brewery> breweries = generateObjects(roads, new BreweryFactory(), breweriesCount);
        ArrayList<Tavern> taverns = generateObjects(roads, new TavernFactory(), tavernsCount);

        RemoveLuzneDrogi(roads, farmlands, breweries,taverns);
        return new Data(roads, farmlands, breweries, taverns);
    }

    public void RemoveLuzneDrogi(ArrayList<Road> roads, ArrayList<Farmland> farmlands, ArrayList<Brewery> breweries, ArrayList<Tavern> taverns) {
        while (true) {
            var roadsToRemove = roads.stream().filter(r -> DrogaLuzna(roads, farmlands, breweries, taverns, r)).findFirst();
            if (roadsToRemove.isEmpty())
                break;
            roads.remove(roadsToRemove.get());
        }
    }

    public static List<Point2D> findRoadEnds(ArrayList<Road> roads) {
        return roads.stream()
                .flatMap(r -> Stream.of(r.getP1(), r.getP2()))
                .distinct()
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
}

