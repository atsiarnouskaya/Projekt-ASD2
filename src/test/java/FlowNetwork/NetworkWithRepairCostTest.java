package FlowNetwork;

import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

public class NetworkWithRepairCostTest {
    @Nested
    @DisplayName("Dijkstra and minCostMaxFlow tests")
    class DijkstraAndMinCostMaxFlowTests {
        Network network;
        Map<Vertex, Vertex> previousVertices;
        Vertex v1;
        Vertex v2;
        Vertex v3;
        Vertex v4;
        Vertex v5;
        Vertex v6;
        Vertex src;
        Vertex dest;

        @BeforeEach
        void setup() {
            network = new Network();
            previousVertices = new HashMap<>();
            v1 = network.addVertex(1, 1);
            v2 = network.addVertex(2, 2);
            v3 = network.addVertex(3, 3);
            v4 = network.addVertex(4, 4);
            v5 = network.addVertex(5, 5);
            v6 = network.addVertex(6, 6);
            src = network.addVertex(0, 0);
            dest = network.addVertex(7, 7);
        }

        @Test
        void GivenSimplePathExist_WhenRunningDijkstra_ThenReturnsTrue() {
            /*
             Graph looks like  0 —— 1 —— 2 —— 3 —— 4 —— 5 —— 6 —— 7
            */
            network.addEdge(10, 2, 0, 0, 1, 1);
            network.addEdge(10, 3, 1, 1, 2, 2);
            network.addEdge(10, 4, 2, 2, 3, 3);
            network.addEdge(10, 5, 3, 3, 4, 4);
            network.addEdge(10, 6, 4, 4, 5, 5);
            network.addEdge(10, 7, 5, 5, 6, 6);
            network.addEdge(10, 8, 6, 6, 7, 7);
            Assertions.assertTrue(network.dijkstra(src, dest, previousVertices));
        }

        @Test
        void GivenMultiplePathsExist_WhenRunningDijkstra_ThenReturnsTrue() {
            /*
                                    1 — 4 ———
                                  /   /       \
                Graph looks like 0 — 2         7
                                  \   \       /
                                   3 — 5  —  6
            */
            network.addEdge(10, 2, 0, 0, 1, 1);
            network.addEdge(10, 3, 0, 0, 2, 2);
            network.addEdge(10, 4, 0, 0, 3, 3);
            network.addEdge(10, 5, 1, 1, 4, 4);
            network.addEdge(10, 6, 2, 2, 4, 4);
            network.addEdge(10, 6, 2, 2, 5, 5);
            network.addEdge(10, 6, 3, 3, 5, 5);
            network.addEdge(10, 7, 5, 5, 6, 6);
            network.addEdge(10, 8, 4, 4, 7, 7);
            network.addEdge(10, 8, 6, 6, 7, 7);
            Assertions.assertTrue(network.dijkstra(src, dest, previousVertices));
        }

        @Test
        void GivenPathsExistButNoAvailableFlow_WhenRunningDijkstra_ThenReturnsFalse() {
             /*
                                       1 ——6—— 4 ——————
                                      /       /         \
                                     /       /           0
                                    2       9             \
                                   /       /               \
                                  /       /                 \
                Graph looks like 0 ——4—— 2                  7
                                  \       \                 /
                                   \       \               /
                                    4       0            12
                                     \       \           /
                                      \       \         /
                                       3 ——0—— 5 ——3—— 6
            */
            network.addEdge(2, 2, 0, 0, 1, 1);
            network.addEdge(4, 3, 0, 0, 2, 2);
            network.addEdge(4, 4, 0, 0, 3, 3);
            network.addEdge(6, 5, 1, 1, 4, 4);
            network.addEdge(9, 6, 2, 2, 4, 4);
            network.addEdge(0, 6, 2, 2, 5, 5);
            network.addEdge(0, 6, 3, 3, 5, 5);
            network.addEdge(3, 7, 5, 5, 6, 6);
            network.addEdge(0, 8, 4, 4, 7, 7);
            network.addEdge(12, 8, 6, 6, 7, 7);
            Assertions.assertFalse(network.dijkstra(src, dest, previousVertices));
        }

        @Test
        void GivenNoPathsExist_WhenRunningDijkstra_ThenReturnsFalse() {
            /*
             Graph looks like 0 - 1 - 2 - 3
                              4 - 5 - 6 - 7
            */
            network.addEdge(10, 2, 0, 0, 1, 1);
            network.addEdge(10, 3, 1, 1, 2, 2);
            network.addEdge(10, 4, 2, 2, 3, 3);
            network.addEdge(10, 6, 4, 4, 5, 5);
            network.addEdge(10, 7, 5, 5, 6, 6);
            network.addEdge(10, 8, 6, 6, 7, 7);
            Assertions.assertFalse(network.dijkstra(src, dest, previousVertices));
        }

        @Test
        void GivenSourceEqualsDestination_WhenRunningDijkstra_ThenReturnsTrue() {
            Vertex destForTheTest = network.addVertex(0, 0);
            Assertions.assertTrue(network.dijkstra(src, destForTheTest, previousVertices));
        }

        @Test
        void GivenMultiplePathsExist_WhenRunningMinCostMaxFlow_ThenFlowIs10AndRepairCostIs39() {
             /* maxFlow/RepairCost
                                       1 ——5/6—— 4  ———————
                                      /         /           \
                                     /         /           10/5
                                  10/6       4/9              \
                                   /         /                 \
                                  /         /                   \
                Graph looks like 0 ——8/4—— 2                     7
                                  \         \                   /
                                   \         \                 /
                                   6/4       3/0             1/6
                                     \         \             /
                                      \         \           /
                                       3 ——3/5—— 5 ——2/3—— 6
            */
            network.addEdge(10, 6, 0, 0, 1, 1);
            network.addEdge(8, 4, 0, 0, 2, 2);
            network.addEdge(6, 4, 0, 0, 3, 3);
            network.addEdge(5, 6, 1, 1, 4, 4);
            network.addEdge(4, 9, 2, 2, 4, 4);
            network.addEdge(3, 0, 2, 2, 5, 5);
            network.addEdge(3, 5, 3, 3, 5, 5);
            network.addEdge(2, 3, 5, 5, 6, 6);
            network.addEdge(10, 5, 4, 4, 7, 7);
            network.addEdge(1, 6, 6, 6, 7, 7);
            Assertions.assertEquals(10, network.minCostMaxFlow(src, dest));
            Assertions.assertEquals(39, network.getFlowRepairCost());
        }

        @Test
        void GivenZeroRepairCostPathExists_WhenRunningMinCostMaxFlow_ThenChoosesIt() {
            /* maxFlow/RepairCost

                 Graph looks like  0 ————————10/100—————————
                                    \                        \
                                     \                        \
                                    10/0                       5 ——10/0—— 7
                                       \                        \
                                        \                       10/0
                                         \                        \
                                          1  ——10/0——  2  ——10/0—— 3
            */
            network.addEdge(10, 100, 0, 0, 5, 5);
            network.addEdge(10, 0, 0, 0, 1, 1);
            network.addEdge(10, 0, 1, 1, 2, 2);
            network.addEdge(10, 0, 2, 2, 3, 3);
            network.addEdge(10, 0, 3, 3, 5, 5);
            network.addEdge(10, 0, 5, 5, 7, 7);

            Assertions.assertEquals(10, network.minCostMaxFlow(src, dest));
            Assertions.assertEquals(0, network.getFlowRepairCost());
        }

        @Test
        void GivenNoPathsExist_WhenRunningMinCostMaxFlow_ThenReturns0() {
            /*
                                     1 — 4
                                   /
                Graph looks like  0 — 2         7
                                   \           /
                                     3   5 — 6
            */
            network.addEdge(10, 2, 0, 0, 1, 1);
            network.addEdge(8, 3, 0, 0, 2, 2);
            network.addEdge(6, 4, 0, 0, 3, 3);
            network.addEdge(5, 5, 1, 1, 4, 4);
            network.addEdge(2, 7, 5, 5, 6, 6);
            network.addEdge(1, 8, 6, 6, 7, 7);
            Assertions.assertEquals(0, network.minCostMaxFlow(src, dest));
        }

        @Test
        void GivenBottleneckInGraph_WhenRunningMinCostMaxFlow_ThenReturns2() {
            /*
                Graph looks like  0 ——10/1—— 1 ——2/2—— 2 ——10/3—— 7
            */
            network.addEdge(10, 1, 0, 0, 1, 1);
            network.addEdge(2, 2, 1, 1, 2, 2); //Bottleneck
            network.addEdge(10, 3, 2, 2, 7, 7);
            Assertions.assertEquals(2, network.minCostMaxFlow(src, dest));
        }

        @Test
        void GivenCycleInGraph_WhenRunningMinCostMaxFlow_ThenHandlesItCorrectly() {
            /*
                Graph looks like  0  ——10/1——  2
                                   \          /
                                    \      10/1
                                   10/1     /
                                       \   /
                                         1 ——10/1—— 7
            */
            network.addEdge(10, 1, 0, 0, 1, 1);
            network.addEdge(10, 1, 1, 1, 2, 2);
            network.addEdge(10, 1, 2, 2, 0, 0); //Cycle
            network.addEdge(10, 1, 2, 2, 7, 7);
            Assertions.assertEquals(10, network.minCostMaxFlow(src, dest));
            Assertions.assertEquals(2, network.getFlowRepairCost());
        }
    }
}
