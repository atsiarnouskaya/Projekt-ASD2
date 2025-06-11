package test.FlowNetwork;

import FlowNetwork.Network;
import FlowNetwork.Vertex;
import org.junit.jupiter.api.*;

public class NetworkTest {

    @Nested
    @DisplayName("Tests for updateEdge, addEdge and setMaxFlow methods")
    class settingUpNetworkTests {

        Network network;
        Vertex v1;
        Vertex v2;
        Vertex v3;

        @BeforeEach
        void setup() {
            network = new Network();
            v1 = new Vertex(1, 1);
            v2 = new Vertex(2, 2);
            v3 = new Vertex(3, 3);
            network.addVertex(1, 1);
            network.addVertex(2, 2);
        }

        @Test
        void updateEdgeTestNormalFlow() {
            network.addEdge(10, 3, 1, 1, 2, 2);
            network.updateEdge(4, v1, v2);
            Assertions.assertEquals(6, network.getEdge(v1, v2).getResidualFlow());
            Assertions.assertEquals(10, network.getEdge(v1, v2).getReverseEdge().getResidualFlow());
        }

        @Test
        void updateEdgeTestNegativeFlow() {
            network.addEdge(10, 3, 1, 1, 2, 2);
            network.updateEdge(-4, v1, v2);
            Assertions.assertEquals(0, network.getEdge(v1, v2).getCurrentFlow());
            Assertions.assertEquals(10, network.getEdge(v1, v2).getResidualFlow());
        }

        @Test
        void updateEdgeTestIncorrectVertexes() {
            network.addEdge(10, 3, 1, 1, 2, 2);
            Assertions.assertThrows(IllegalArgumentException.class, () -> network.updateEdge(4, v1, v3));
            Assertions.assertThrows(IllegalArgumentException.class, () -> network.updateEdge(4, v1, v1));
        }

        @Test
        void addEdgeTestNormalParams() {
            network.addEdge(10, 3, 1, 1, 2, 2);
            Assertions.assertEquals(10, network.getEdge(v1, v2).getMaxFlow());
            Assertions.assertEquals(0, network.getEdge(v1, v2).getCurrentFlow());
            Assertions.assertEquals(10, network.getEdge(v1, v2).getResidualFlow());
            Assertions.assertEquals(3, network.getEdge(v1, v2).getRepairCost());
        }

        @Test
        void addEdgeTestNegativeFlowAndRepairCostParams() {
            network.addEdge(-10, -3, 1, 1, 2, 2);
            Assertions.assertEquals(0, network.getEdge(v1, v2).getMaxFlow());
            Assertions.assertEquals(0, network.getEdge(v1, v2).getCurrentFlow());
            Assertions.assertEquals(0, network.getEdge(v1, v2).getResidualFlow());
            Assertions.assertEquals(0, network.getEdge(v1, v2).getRepairCost());
        }

        @Test
        void setMaxFlowTestNormalFlow() {
            network.addEdge(10, 3, 1, 1, 2, 2);
            network.setMaxFlow(3, 1, 1, 2, 2);
            Assertions.assertEquals(3, network.getEdge(v1, v2).getMaxFlow());
            Assertions.assertEquals(3, network.getEdge(v2, v1).getMaxFlow());
            Assertions.assertEquals(0, network.getEdge(v1, v2).getCurrentFlow());
            Assertions.assertEquals(0, network.getEdge(v2, v1).getCurrentFlow());
            Assertions.assertEquals(3, network.getEdge(v1, v2).getResidualFlow());
            Assertions.assertEquals(3, network.getEdge(v2, v1).getResidualFlow());
        }

        @Test
        void setMaxFlowTestNegativeFlow() {
            network.addEdge(10, 3, 1, 1, 2, 2);
            network.setMaxFlow(-3, 1, 1, 2, 2);
            Assertions.assertEquals(0, network.getEdge(v1, v2).getMaxFlow());
            Assertions.assertEquals(0, network.getEdge(v2, v1).getMaxFlow());
            Assertions.assertEquals(0, network.getEdge(v1, v2).getCurrentFlow());
            Assertions.assertEquals(0, network.getEdge(v2, v1).getCurrentFlow());
            Assertions.assertEquals(0, network.getEdge(v1, v2).getResidualFlow());
            Assertions.assertEquals(0, network.getEdge(v2, v1).getResidualFlow());
        }
    }

    @Nested
    @DisplayName("Adding source and sink vertexes tests")
    class SourceAndSinkAddingVertexesTests {
        Network network;
        Vertex v1;
        Vertex v2;
        Vertex v3;
        Vertex v4;
        Vertex v5;
        Vertex v6;

        @BeforeEach
        void setup() {
            network = new Network();
            v1 = network.addVertex(1, 1);
            v1.setType("Farm");
            v1.setCapacity(5);
            v2 = network.addVertex(2, 2);
            v2.setType("Farm");
            v2.setCapacity(10);
            v3 = network.addVertex(3, 3);
            v3.setType("Intersection");
            v4 = network.addVertex(4, 4);
            v4.setType("Tavern");
            v4.setCapacity(34);
            v5 = network.addVertex(5, 5);
            v5.setType("Brewery");
            v5.setCapacity(12);
            v6 = network.addVertex(6, 6);
            v6.setType("Brewery");
            v6.setCapacity(8);

            network.addEdge(2, 2, 1, 1, 3, 3);
            network.addEdge(2, 3, 2, 2, 3, 3);
            network.addEdge(2, 4, 4, 4, 3, 3);
            network.addEdge(2, 5, 5, 5, 3, 3);
            network.addEdge(2, 6, 6, 6, 3, 3);
        }

        @Test
        void addSourceVertexTest() {
            Vertex srcFarm = network.addSourceVertex("Farm");
            Assertions.assertEquals(2, network.getGraph().get(srcFarm).size());
            Assertions.assertEquals(5, network.getEdge(srcFarm, v1).getMaxFlow());
            for (var key : network.getGraph().get(srcFarm).keySet()) {
                Assertions.assertEquals("Farm", key.getType());
            }

            Vertex srcIntersection = network.addSourceVertex("Intersection");
            Assertions.assertEquals(1, network.getGraph().get(srcIntersection).size());
            for (var key : network.getGraph().get(srcIntersection).keySet()) {
                Assertions.assertEquals("Intersection", key.getType());
            }

            Vertex srcTavern = network.addSourceVertex("Tavern");
            Assertions.assertEquals(1, network.getGraph().get(srcTavern).size());
            Assertions.assertEquals(34, network.getEdge(srcTavern, v4).getMaxFlow());
            for (var key : network.getGraph().get(srcTavern).keySet()) {
                Assertions.assertEquals("Tavern", key.getType());
            }

            Vertex srcBrewery = network.addSourceVertex("Brewery");
            Assertions.assertEquals(2, network.getGraph().get(srcBrewery).size());
            Assertions.assertEquals(12, network.getEdge(srcBrewery, v5).getMaxFlow());
            Assertions.assertEquals(8, network.getEdge(srcBrewery, v6).getMaxFlow());
            for (var key : network.getGraph().get(srcBrewery).keySet()) {
                Assertions.assertEquals("Brewery", key.getType());
            }
        }

        @Test
        void addSinkVertexTest() {
            Vertex sinkFarm = network.addSinkVertex("Farm");
            Assertions.assertEquals(2, network.getGraph().get(sinkFarm).size());
            Assertions.assertEquals(5, network.getEdge(sinkFarm, v1).getMaxFlow());
            for (var key : network.getGraph().get(sinkFarm).keySet()) {
                Assertions.assertEquals("Farm", key.getType());
            }

            Vertex sinkIntersection = network.addSinkVertex("Intersection");
            Assertions.assertEquals(1, network.getGraph().get(sinkIntersection).size());
            for (var key : network.getGraph().get(sinkIntersection).keySet()) {
                Assertions.assertEquals("Intersection", key.getType());
            }

            Vertex sinkTavern = network.addSinkVertex("Tavern");
            Assertions.assertEquals(1, network.getGraph().get(sinkTavern).size());
            Assertions.assertEquals(34, network.getEdge(sinkTavern, v4).getMaxFlow());
            for (var key : network.getGraph().get(sinkTavern).keySet()) {
                Assertions.assertEquals("Tavern", key.getType());
            }

            Vertex sinkBrewery = network.addSinkVertex("Brewery");
            Assertions.assertEquals(2, network.getGraph().get(sinkBrewery).size());
            Assertions.assertEquals(12, network.getEdge(sinkBrewery, v5).getMaxFlow());
            Assertions.assertEquals(8, network.getEdge(sinkBrewery, v6).getMaxFlow());
            for (var key : network.getGraph().get(sinkBrewery).keySet()) {
                Assertions.assertEquals("Brewery", key.getType());
            }
        }
    }

    @Nested
    @DisplayName("Deleting source and sink vertexes tests")
    class SourceAndSinkDeletingVertexesTests {
        Network network;
        Vertex v1;
        Vertex v2;
        Vertex v3;
        Vertex v4;
        Vertex v5;
        Vertex v6;
        Vertex srcFarm;
        Vertex srcIntersection;
        Vertex sinkTavern;
        Vertex sinkBrewery;

        @BeforeEach
        void setup() {
            network = new Network();
            v1 = network.addVertex(1, 1);
            v1.setType("Farm");
            v2 = network.addVertex(2, 2);
            v2.setType("Farm");
            v3 = network.addVertex(3, 3);
            v3.setType("Intersection");
            v4 = network.addVertex(4, 4);
            v4.setType("Tavern");
            v5 = network.addVertex(5, 5);
            v5.setType("Brewery");
            v6 = network.addVertex(6, 6);
            v6.setType("Brewery");

            network.addEdge(2, 2, 1, 1, 3, 3);
            network.addEdge(2, 3, 2, 2, 3, 3);
            network.addEdge(2, 4, 4, 4, 3, 3);
            network.addEdge(2, 5, 5, 5, 3, 3);
            network.addEdge(2, 6, 6, 6, 3, 3);

            srcFarm = network.addSourceVertex("Farm");
            srcIntersection = network.addSourceVertex("Intersection");

            sinkTavern = network.addSinkVertex("Tavern");
            sinkBrewery = network.addSinkVertex("Brewery");
        }

        @Test
        void deleteSourceVertexTest() {
            network.deleteSourceVertex(srcFarm);
            Assertions.assertFalse(network.getGraph().containsKey(srcFarm));
            for (var e : network.getGraph().values()) {
                Assertions.assertFalse(e.containsKey(network
                        .getVertex(srcFarm.getX(), srcFarm.getY())));
            }
            Assertions.assertFalse(network.getVertexByCoord().containsKey(srcFarm));

            network.deleteSourceVertex(srcIntersection);
            Assertions.assertFalse(network.getGraph().containsKey(srcIntersection));
            for (var e : network.getGraph().values()) {
                Assertions.assertFalse(e.containsKey(network
                        .getVertex(srcIntersection.getX(), srcIntersection.getY())));
            }
            Assertions.assertFalse(network.getVertexByCoord().containsKey(srcIntersection));
        }

        @Test
        void deleteSinkVertexTest() {
            network.deleteSinkVertex(sinkBrewery);
            Assertions.assertFalse(network.getGraph().containsKey(sinkBrewery));
            for (var e : network.getGraph().values()) {
                Assertions.assertFalse(e.containsKey(network
                        .getVertex(sinkBrewery.getX(), sinkBrewery.getY())));
            }
            Assertions.assertFalse(network.getVertexByCoord().containsKey(sinkBrewery));

            network.deleteSinkVertex(sinkTavern);
            Assertions.assertFalse(network.getGraph().containsKey(sinkTavern));
            for (var e : network.getGraph().values()) {
                Assertions.assertFalse(e.containsKey(network
                        .getVertex(sinkTavern.getX(), sinkTavern.getY())));
            }
            Assertions.assertFalse(network.getVertexByCoord().containsKey(sinkTavern));
        }
    }

    @Nested
    @DisplayName("BFS and maxFlow tests")
    class BFSAndMaxFlowTests {
        Network network;
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
        void GivenSimplePathExists_WhenRunningBFS_ThenReturnsTrue() {
            /*
             Graph looks like 0 — 1 — 2 — 3 — 4 — 5 — 6 — 7
            */
            network.addEdge(10, 2, 0, 0, 1, 1);
            network.addEdge(10, 3, 1, 1, 2, 2);
            network.addEdge(10, 4, 2, 2, 3, 3);
            network.addEdge(10, 5, 3, 3, 4, 4);
            network.addEdge(10, 6, 4, 4, 5, 5);
            network.addEdge(10, 7, 5, 5, 6, 6);
            network.addEdge(10, 8, 6, 6, 7, 7);

            Assertions.assertTrue(network.BFS(src, dest));
        }

        @Test
        void GivenMultiplePathsExist_WhenRunningBFS_ThenReturnsTrue() {
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
            Assertions.assertTrue(network.BFS(src, dest));
        }

        @Test
        void GivenSourceEqualsDestination_WhenRunningBFS_ThenReturnsTrue() {
            Vertex destForTheTest = network.addVertex(0, 0);
            Assertions.assertTrue(network.BFS(src, destForTheTest));
        }

        @Test
        void GivenNoPathsExist_WhenRunningBFS_ThenReturnsFalse() {
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
            Assertions.assertFalse(network.BFS(src, dest));
        }

        @Test
        void GivenSimpleGraphWith0MaxFlow_WhenCallingBFS_ThenReturnsFalse() {
             /*
             Graph looks like 0 — 1 — 2 — 3 — 4 — 5 — 6 — 7
            */
            network.addEdge(0, 2, 0, 0, 1,1);
            network.addEdge(0, 3, 1,1,2,2);
            network.addEdge(0, 4, 2,2,3,3);
            network.addEdge(0, 5, 3,3,4,4);
            network.addEdge(0, 6,4,4, 5, 5);
            network.addEdge(0, 7, 5, 5, 6, 6);
            network.addEdge(0, 8, 6, 6, 7, 7);
            Assertions.assertFalse(network.BFS(src, dest));
        }

        @Test
        void GivenComplicatedGraphWith0MaxFlow_WhenCallingBFS_ThenReturnsFalse() {
             /*
                                    1 — 4 ———
                                  /   /       \
                Graph looks like 0 — 2         7
                                  \   \       /
                                    3 — 5 — 6
             */
            network.addEdge(0, 2, 0, 0, 1,1);
            network.addEdge(0, 3, 0,0,2,2);
            network.addEdge(0, 4, 0,0,3,3);
            network.addEdge(0, 6,3, 3, 5,5);
            network.addEdge(0, 5, 1,1,4,4);
            network.addEdge(0, 6,2, 2, 4,4);
            network.addEdge(0, 6,2, 2, 5,5);
            network.addEdge(0, 7, 5, 5, 6, 6);
            network.addEdge(0, 8, 4, 4, 7, 7);
            network.addEdge(0, 8, 6, 6, 7, 7);
            Assertions.assertFalse(network.BFS(src, dest));
        }

        @Test
        void GivenMultiplePathsExist_WhenRunningMaxFlow_ThenFlowIs10AndRepairCostIs48() {
            /*
                                    1 — 4 ———
                                  /   /       \
                Graph looks like 0 — 2         7
                                  \   \       /
                                    3 — 5 — 6
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
            Assertions.assertEquals(10, network.BFSMaxFlow(src, dest));
            Assertions.assertEquals(48, network.getFlowRepairCost());

        }

        @Test
        void GivenNoPathsExist_WhenRunningMaxFlow_ThenReturns0() {
            /*
                                    1 — 4
                                  /
                Graph looks like 0 — 2         7
                                  \           /
                                    3   5 — 6
             */
            network.addEdge(10, 2, 0, 0, 1, 1);
            network.addEdge(8, 3, 0, 0, 2, 2);
            network.addEdge(6, 4, 0, 0, 3, 3);
            network.addEdge(5, 5, 1, 1, 4, 4);
            network.addEdge(2, 7, 5, 5, 6, 6);
            network.addEdge(1, 8, 6, 6, 7, 7);
            Assertions.assertEquals(0, network.BFSMaxFlow(src, dest));
        }

        @Test
        void GivenBottleneckInGraph_WhenRunningMaxFlow_ThenReturns2() {
            /*
            0 — 1 — 2 — 7
            */
            network.addEdge(10, 1, 0, 0, 1, 1);
            network.addEdge(2, 2, 1, 1, 2, 2); //Bottleneck
            network.addEdge(10, 3, 2, 2, 7, 7);
            Assertions.assertEquals(2, network.BFSMaxFlow(src, dest));
        }
    }
}
