package SiecPrzeplywowa;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Stack;
/*
done:
- Graham's scan algorithm has been implemented to compute the hull from a set of 2D points.
- The hull defines the boundary of a quadrant, representing a group of farmland plots.

remaining:
- Connect the algorithm with the Farmland class. Each farmland point should be linked to a specific quadrant.
- Add a method in the Farmland class to calculate the amount of barley produced, based on a multiplier and the area.
- Implement functionality to divide the full set of points into multiple disjoint quadrants.
    This involves repeatedly constructing convex hulls from the remaining unused points
    and removing them from the available pool after each iteration.
- Decide how to assign the barley production multiplier for each quadrant.
    (e.g., production multiplier = 0.1 * number of hull points).
- Store this multiplier in the Quadrant class and apply it when calculating the production in Farmland.
 */
public class Quadrant {
    private ArrayList<Point2D> hull; // hull of this quadrant
    Point2D p0;
    private ArrayList<Farmland> farmlands; //farmlands belonging to this quadrant
    //calculating the determinant (cross product) to determine orientation of 3 points
    private int calculateDeterminant(Point2D p1, Point2D p2, Point2D p3){
        return (int)((p2.getX() - p1.getX())*(p3.getY()-p1.getY()) - (p3.getX() - p1.getX())*(p2.getY() - p1.getY()));
    }

    public void grahamAlgorithm(ArrayList<Point2D> points) {
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
                //if points ale colinear, the farther one should come first
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
        hull = new ArrayList<>(stack);
    }
    //geter for the hull
    public ArrayList<Point2D> getHull() {
        return hull;
    }

    //simple test (will remove it later)
    public static void main(String[] args) {
        ArrayList<Point2D> testPoints = new ArrayList<>();
        testPoints.add(new Point2D.Double(0, 0));
        testPoints.add(new Point2D.Double(1, 0));
        testPoints.add(new Point2D.Double(2, 0));
        testPoints.add(new Point2D.Double(2, 1));
        testPoints.add(new Point2D.Double(4, 2));
        testPoints.add(new Point2D.Double(3, 4));
        testPoints.add(new Point2D.Double(1, 3));
        testPoints.add(new Point2D.Double(5, 1));
        testPoints.add(new Point2D.Double(3, 0));
        testPoints.add(new Point2D.Double(2, 2));

        Quadrant q = new Quadrant();
        q.grahamAlgorithm(testPoints);

        System.out.println("Punkty otoczki:");
        for (Point2D p : q.getHull()) {
            System.out.println(p);
        }
    }
}
//test