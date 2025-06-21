package FlowNetwork;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Stack;
/*
 - QuadrantManager handles dividing farmland points into k convex quadrants.
 - It uses Graham's scan algorithm to compute the convex hull of each quadrant.
 - Each quadrant is assigned a random production capacity multiplier.
 - Each Farmland stores a reference to its quadrant and the production capacity.
 */
public class QuadrantManager {
    int k; // total number of quadrants
    Point2D p0;
    private final ArrayList<Farmland> farmlands; //farmlands points that will be divided

    public QuadrantManager(ArrayList<Farmland> farmlands) {
        this.farmlands = farmlands;
        this.k = computeK(farmlands.size());
    }
    /*
    Calculates how many(k) groups we can make from farmlands
    Each groups must have at least 3 points. k is the biggest power of 2 that fits this rule
     */
    public static int computeK(int numberOfFarmlands) {
        int k = 1;
        while (k * 2 <= numberOfFarmlands / 3) { //at least 3 points for a quadrant
            k *= 2;
        }
        return k;
    }
    //calculating the determinant (cross product) to determine turn orientation of 3 points
    private int calculateDeterminant(Point2D p1, Point2D p2, Point2D p3){
        return (int)((p2.getX() - p1.getX())*(p3.getY()-p1.getY()) - (p3.getX() - p1.getX())*(p2.getY() - p1.getY()));
    }
    //graham algorithm to make a hull from a set of points
    public ArrayList<Point2D> grahamAlgorithm(ArrayList<Point2D> points) {
        LinkedHashSet<Point2D> uniquePoints = new LinkedHashSet<>(points); // dont want any duplicates
        points = new ArrayList<>(uniquePoints);
        if (points.size() < 3) {
            return new ArrayList<>(points); //return all points if there is less than 3 points (otherwise hull wont build)
        }
        p0 = points.getFirst();
        //finding the lowest point(with smallest y, x if y are the same)
        for (Point2D p : points) {
            if (p.getY() < p0.getY() || (p.getY() == p0.getY() && p.getX() < p0.getX())) {
                p0 = p;
            }
        }
        points.remove(p0);
        //sorting remaining points by polar angle with respect to starting point (p0)
        points.sort((p1, p2) -> {
            int det = calculateDeterminant(p0, p1, p2);
            if (det > 0) return -1;
            else if (det < 0) return 1;
            else {
                //if points ale colinear, the further one should come first
                double dist1 = p0.distanceSq(p1);
                double dist2 = p0.distanceSq(p2);
                return Double.compare(dist1, dist2);
            }
        });
        //building the hull by using a stack
        Stack<Point2D> stack = new Stack<>();
        stack.push(p0);
        stack.push(points.get(0));
        stack.push(points.get(1));
        for (int i = 2; i < points.size(); i++) {
            Point2D p = points.get(i);
            while (stack.size() > 1 && calculateDeterminant(stack.get(stack.size() - 2), stack.peek(), p) <= 0) {
                stack.pop(); //removing nonleft turns
            }
            stack.push(p);
        }
        return new ArrayList<>(stack);
    }
    /*
     * Recursively divides a list of farmlands points into k "areas" (divide and conquer)
     * division work by sorting X and Y coordinates at each recursion levels.
     * k must is power of 2!!! to ensure balanced splitting
     * */
    public ArrayList<ArrayList<Farmland>> dividePoints(ArrayList<Farmland> points, int k, boolean divideByX) {
        ArrayList<ArrayList<Farmland>> result = new ArrayList<>();
        if (k == 1) { //no further division needed
            result.add(points);
            return result;
        }
        //sort by x coordinates or y coordinate (based on dividebyX flag)
        points.sort((a, b) -> divideByX ?
                Double.compare(a.getX(), b.getX()) :
                Double.compare(a.getY(), b.getY()));

        int mid = points.size() / 2;
        ArrayList<Farmland> left = new ArrayList<>(points.subList(0, mid));
        ArrayList<Farmland> right = new ArrayList<>(points.subList(mid, points.size()));

        //divide each half to k/2 areas, flip divide by x flag
        result.addAll(dividePoints(left, k / 2, !divideByX));
        result.addAll(dividePoints(right, k / 2, !divideByX));
        return result;
    }
    // creates quadrant by farmland points and making a hull for each area. Also assings random production capacity to each quadrant
    //so farmlands in each quadrant produce the same amount of barley
    public ArrayList<Quadrant> createQuadrants() {
        if(farmlands.isEmpty()) return new ArrayList<>();
        ArrayList<ArrayList<Farmland>> clusters = dividePoints(farmlands, k, true);
        ArrayList<Quadrant> quadrants = new ArrayList<>();
        Random rand = new Random();
        for (ArrayList<Farmland> cluster : clusters) {
            Quadrant q = new Quadrant();
            ArrayList<Point2D> points = new ArrayList<>();
            for (Farmland f : cluster) {
                points.add(new Point2D.Double(f.getX(), f.getY()));
            }
            ArrayList<Point2D> hull = grahamAlgorithm(new ArrayList<>(points));
            q.setHull(hull);
            int randomProduction = 5 + rand.nextInt(20); //deciding production for quadrant
            q.setProductionPerPlot(randomProduction);
            for (Farmland f : cluster) {
                f.setQuadrant(q);
                f.setProductionCapacity(randomProduction);
                q.addFarmland(f);
            }
            quadrants.add(q);
        }
        return quadrants;
    }
}