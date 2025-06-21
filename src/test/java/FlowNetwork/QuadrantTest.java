package FlowNetwork;
import org.junit.jupiter.api.*;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class QuadrantTest {
    @Nested
    @DisplayName("Tests for computing k")
    class ComputeKTests {
        @Test
        void GivenInputLessThan3_WhenComputeK_ThenReturns1() {
            Assertions.assertEquals(1, QuadrantManager.computeK(0));
            Assertions.assertEquals(1, QuadrantManager.computeK(1));
            Assertions.assertEquals(1, QuadrantManager.computeK(2));
        }
        @Test
        void  GivenInputExactly3_WhenComputeK_ThenReturns1() {
            Assertions.assertEquals(1, QuadrantManager.computeK(3));
        }
        @Test
        void GivenVariousInputs_WhenComputeK_ThenReturnsCorrectPowersOfTwo() {
            Assertions.assertEquals(2, QuadrantManager.computeK(6));
            Assertions.assertEquals(4, QuadrantManager.computeK(12));
            Assertions.assertEquals(2, QuadrantManager.computeK(10));
            Assertions.assertEquals(8, QuadrantManager.computeK(30)); // 30/3=10 and max power of two <= 10 is 8
        }
        @Test
        void GivenInputNotEnoughForTwoGroups_WhenComputeK_ThenReturns1() {
            Assertions.assertEquals(1, QuadrantManager.computeK(4)); // 4/3=1.33, max power of two <= 1.33 is 1
        }
    }
    @Nested
    @DisplayName("Tests for Graham Algorithm")
    class GrahamAlgorithmTests {
        ArrayList<Farmland> farmlands;
        QuadrantManager manager;
        @BeforeEach
        void setup() {
            farmlands = new ArrayList<>();
            manager = new QuadrantManager(farmlands);
        }
        @Test
        void GivenEmptyPointList_WhenRunningGrahamAlgorithm_ThenReturnsEmptyList() {
            ArrayList<Point2D> points = new ArrayList<>();
            ArrayList<Point2D> hull = manager.grahamAlgorithm(points);

            System.out.println("Input points:");
            points.forEach(System.out::println);
            System.out.println("Hull points:");
            points.forEach(System.out::println);
            System.out.println("\n");

            Assertions.assertTrue(hull.isEmpty());
        }
        @Test
        void GivenOnePoint_WhenRunningGrahamAlgorithm_ThenReturnsSamePoint() {
            ArrayList<Point2D> points = new ArrayList<>();
            points.add(new Point2D.Double(1, 1));
            ArrayList<Point2D> hull = manager.grahamAlgorithm(points);

            System.out.println("Input points:");
            points.forEach(System.out::println);
            System.out.println("Hull points:");
            points.forEach(System.out::println);
            System.out.println("\n");

            Assertions.assertEquals(1, hull.size());
            Assertions.assertEquals(new Point2D.Double(1, 1), hull.getFirst());
        }
        @Test
        void GivenTwoPoints_WhenRunningGrahamAlgorithm_ThenReturnsSamePoints() {
            ArrayList<Point2D> points = new ArrayList<>();
            points.add(new Point2D.Double(1, 1));
            points.add(new Point2D.Double(2, 2));
            ArrayList<Point2D> hull = manager.grahamAlgorithm(points);

            System.out.println("Input points:");
            points.forEach(System.out::println);
            System.out.println("Hull points:");
            points.forEach(System.out::println);
            System.out.println("\n");

            Assertions.assertEquals(2, hull.size());
            Assertions.assertTrue(hull.containsAll(points));
        }
        @Test
        void GivenColinearPoints_WhenRunningGrahamAlgorithm_ThenReturnsOnlyHullPoints() {
            ArrayList<Point2D> points = new ArrayList<>();
            points.add(new Point2D.Double(0, 0));
            points.add(new Point2D.Double(1, 1));
            points.add(new Point2D.Double(2, 2));
            points.add(new Point2D.Double(3, 3));
            ArrayList<Point2D> hull = manager.grahamAlgorithm(points);
            Assertions.assertEquals(2, hull.size());

            System.out.println("Input points:");
            points.forEach(System.out::println);
            System.out.println("Hull points:");
            points.forEach(System.out::println);
            System.out.println("\n");

            Assertions.assertTrue(hull.contains(new Point2D.Double(0, 0)));
            Assertions.assertTrue(hull.contains(new Point2D.Double(3, 3)));
        }
        @Test
        void GivenDuplicatePoints_WhenRunningGrahamAlgorithm_ThenReturnsCorrectHullWithoutDuplicates() {
            ArrayList<Point2D> points = new ArrayList<>();
            points.add(new Point2D.Double(0, 0));
            points.add(new Point2D.Double(0.5, 0.5));
            points.add(new Point2D.Double(1, 1));
            points.add(new Point2D.Double(1, 1));
            ArrayList<Point2D> hull = manager.grahamAlgorithm(points);

            System.out.println("Input points:");
            points.forEach(System.out::println);
            System.out.println("Hull points:");
            hull.forEach(System.out::println);

            Assertions.assertEquals(3, hull.size());
        }
    }
    @Nested
    @DisplayName("Tests for dividing points")
    class DividePointsTests {
        ArrayList<Farmland> farmlands;
        QuadrantManager manager;
        @BeforeEach
        void setup() {
            farmlands = new ArrayList<>();
            manager = new QuadrantManager(farmlands);
        }
        @Test
        void GivenKIsOne_WhenDividingPoints_ThenReturnsOriginalList() {
            farmlands.add(new Farmland(1, 2));
            farmlands.add(new Farmland(3, 4));
            ArrayList<ArrayList<Farmland>> result = manager.dividePoints(farmlands, 1, true);

            Assertions.assertEquals(1, result.size());
            Assertions.assertEquals(farmlands, result.getFirst());
        }
        @Test
        void GivenKIsTwo_WhenDividingPointsByX_ThenDividesIntoTwoGroups() {
            farmlands.add(new Farmland(1, 5));
            farmlands.add(new Farmland(3, 2));
            farmlands.add(new Farmland(2, 3));
            farmlands.add(new Farmland(4, 1));

            ArrayList<ArrayList<Farmland>> result = manager.dividePoints(farmlands, 2, true);

            Assertions.assertEquals(2, result.size());
            Assertions.assertTrue(result.get(0).stream().allMatch(f -> f.getX() <= 2));
            Assertions.assertTrue(result.get(1).stream().allMatch(f -> f.getX() >= 3));
        }
        @Test
        void GivenOddNumberOfPoints_WhenDividingPoints_ThenDividesCorrectly() {
            farmlands.add(new Farmland(1, 1));
            farmlands.add(new Farmland(2, 2));
            farmlands.add(new Farmland(3, 3));
            farmlands.add(new Farmland(4, 4));
            farmlands.add(new Farmland(5, 5));

            ArrayList<ArrayList<Farmland>> result = manager.dividePoints(farmlands, 2, true);

            Assertions.assertEquals(2, result.size());

            int totalPoints = result.stream().mapToInt(ArrayList::size).sum();
            Assertions.assertEquals(farmlands.size(), totalPoints);
        }
        @Test
        void GivenEmptyPoints_WhenDividingPoints_ThenReturnsOneEmptyGroup() {
            ArrayList<ArrayList<Farmland>> result = manager.dividePoints(farmlands, 1, true);

            Assertions.assertEquals(1, result.size());
            Assertions.assertTrue(result.getFirst().isEmpty());
        }
    }
    @Nested
    @DisplayName("Tests for creating quadrants")
    class CreateQuadrantsTests {
        ArrayList<Farmland> farmlands;
        QuadrantManager manager;
        @BeforeEach
        void setup() {
            farmlands = new ArrayList<>();
        }
        @Test
        void GivenEmptyFarmlands_WhenCreatingQuadrants_ThenReturnsEmptyQuadrants() {
            manager = new QuadrantManager(farmlands);
            ArrayList<Quadrant> quadrants = manager.createQuadrants();
            Assertions.assertTrue(quadrants.isEmpty());
        }
        @Test
        void GivenOneFarmland_WhenCreatingQuadrants_ThenReturnsOneQuadrantWithThatFarmland() {
            farmlands.add(new Farmland(0, 0));
            manager = new QuadrantManager(farmlands);
            ArrayList<Quadrant> quadrants = manager.createQuadrants();
            Assertions.assertEquals(1, quadrants.size());
            Quadrant q = quadrants.getFirst();
            Assertions.assertEquals(1, q.getFarmlands().size());
            Assertions.assertTrue(q.getHull().contains(new Point2D.Double(0, 0)));
        }
        @Test
        void GivenFourFarmlandsAndCenterPoint_WhenCreatingQuadrants_ThenAllBelongToOneQuadrantAndHullIsCorrect() {
            // Cztery punkty w rogach + punkt na środku
            farmlands.add(new Farmland(1, 1));    // I ćwiartka
            farmlands.add(new Farmland(-1, 1));   // II ćwiartka
            farmlands.add(new Farmland(-1, -1));  // III ćwiartka
            farmlands.add(new Farmland(1, -1));   // IV ćwiartka
            farmlands.add(new Farmland(0, 0));    // punkt w środku
            manager = new QuadrantManager(farmlands);
            ArrayList<Quadrant> quadrants = manager.createQuadrants();

            Assertions.assertEquals(1, quadrants.size());
            Quadrant q = quadrants.getFirst();

            Assertions.assertEquals(5, q.getFarmlands().size());

            ArrayList<Point2D> hull = q.getHull();

            Assertions.assertEquals(4, hull.size());
            Assertions.assertTrue(hull.contains(new Point2D.Double(1, 1)));
            Assertions.assertTrue(hull.contains(new Point2D.Double(-1, 1)));
            Assertions.assertTrue(hull.contains(new Point2D.Double(-1, -1)));
            Assertions.assertTrue(hull.contains(new Point2D.Double(1, -1)));

            Assertions.assertFalse(hull.contains(new Point2D.Double(0, 0)));
        }

        @Test
        void GivenColinearPoints_WhenCreatingQuadrants_ThenAllPointsBelongToQuadrantAndHullIsCorrect() {
            farmlands.add(new Farmland(0, 0));
            farmlands.add(new Farmland(1, 1));
            farmlands.add(new Farmland(2, 2));
            farmlands.add(new Farmland(3, 3));
            manager = new QuadrantManager(farmlands);
            ArrayList<Quadrant> quadrants = manager.createQuadrants();
            Assertions.assertEquals(1, quadrants.size());
            Quadrant q = quadrants.getFirst();
            ArrayList<Point2D> hull = q.getHull();
            Assertions.assertEquals(2, hull.size());
            Assertions.assertTrue(hull.contains(new Point2D.Double(0, 0)));
            Assertions.assertTrue(hull.contains(new Point2D.Double(3, 3)));
            for (Farmland f : farmlands) {
                Assertions.assertEquals(q, f.getQuadrant());
            }
        }
        @Test
        void GivenMultipleFarmlands_WhenCreatingQuadrants_ThenReturnsEightQuadrantsAndAssignsEachFarmland() {
            farmlands.add(new Farmland(10, 10));
            farmlands.add(new Farmland(12, 12));
            farmlands.add(new Farmland(14, 14));
            farmlands.add(new Farmland(110, 10));
            farmlands.add(new Farmland(112, 12));
            farmlands.add(new Farmland(114, 14));
            farmlands.add(new Farmland(210, 10));
            farmlands.add(new Farmland(212, 12));
            farmlands.add(new Farmland(214, 14));
            farmlands.add(new Farmland(310, 10));
            farmlands.add(new Farmland(312, 12));
            farmlands.add(new Farmland(314, 14));
            farmlands.add(new Farmland(10, 110));
            farmlands.add(new Farmland(12, 112));
            farmlands.add(new Farmland(14, 114));
            farmlands.add(new Farmland(110, 110));
            farmlands.add(new Farmland(112, 112));
            farmlands.add(new Farmland(114, 114));
            farmlands.add(new Farmland(210, 110));
            farmlands.add(new Farmland(212, 112));
            farmlands.add(new Farmland(214, 114));
            farmlands.add(new Farmland(310, 110));
            farmlands.add(new Farmland(312, 112));
            farmlands.add(new Farmland(314, 114));
            manager = new QuadrantManager(farmlands);
            ArrayList<Quadrant> quadrants = manager.createQuadrants();

            Assertions.assertEquals(8, quadrants.size());
            for (Farmland f : farmlands) {
                Assertions.assertNotNull(f.getQuadrant());
            }
        }
    }
}
