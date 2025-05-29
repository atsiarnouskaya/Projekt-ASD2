package SiecPrzeplywowa;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Siec {
    private final Map<Vertex, HashMap<Vertex, Edge>> graph;
    private final Map<String, Vertex> vertexByCoord;
    private final Map<Vertex, Vertex> previousElements;

    public Siec() {
        this.graph = new ConcurrentHashMap<>();
        this.vertexByCoord = new ConcurrentHashMap<>();
        this.previousElements = new HashMap<>();
    }

    public Map<Vertex, HashMap<Vertex, Edge>> getGraph() {
        return this.graph;
    }

    public Vertex addVertex(int x, int y) {
        String key = x + "," + y;
        if (vertexByCoord.containsKey(key)) return vertexByCoord.get(key);
        Vertex v = new Vertex(x, y);
        vertexByCoord.put(key, v);
        graph.put(v, new HashMap<>());
        return v;
    }

    public Vertex getVertex(int x, int y) {
        return vertexByCoord.get(x + "," + y);
    }

    // Dodajemy krawędź od a do b oraz od b do a.
    // Żeby krawędź była dwukierunkowa, maksymalny przepływ (maxFlow) w obie strony musi być taki sam.
    // Na przykład, jeśli z a do b maxFlow = 5, to z b do a też będzie 5.
    // Gdy puścimy przepływ np. currentFlow = 3, to w sieci rezydualnej:
    //    z a do b zostanie 2 (czyli 5 - 3),
    //    a z b do a będzie można „cofnąć” 3 (czyli to, co już przeszło).
    // Ale jeśli te dwie krawędzie są zsynchronizowane i mają wspólną przepustowość,
    //    to zostaje tylko 2 w którąkolwiek stronę.
    // Czyli całkowita przepustowość to 5 — i ona się nigdy nie zmienia.
    public void addEdge(int maxFlow, int repairCost, int x1, int y1, int x2, int y2) {
        Vertex from = vertexByCoord.get(x1 + "," + y1);
        Vertex to = vertexByCoord.get(x2 + "," + y2);

        if (from == null) {
            addVertex(x1, y1);
            from = vertexByCoord.get(x1 + "," + y1);
        }
        if (to == null) {
            addVertex(x2, y2);
            to = vertexByCoord.get(x2 + "," + y2);
        }

        Edge forwardEdge = new Edge(from, to, maxFlow, repairCost);
        Edge backwardEdge = new Edge(to, from, maxFlow, repairCost);
        forwardEdge.setReverseEdge(backwardEdge);
        backwardEdge.setReverseEdge(forwardEdge);
        graph.get(from).put(to, forwardEdge);
        graph.get(to).put(from, backwardEdge);
    }

    public Edge getEdge(Vertex from, Vertex to) {
        return graph.get(from).get(to);
    }

    public void setMaxFlow(int flow, int x1, int y1, int x2, int y2) {
        Vertex from = vertexByCoord.get(x1 + "," + y1);
        Vertex to = vertexByCoord.get(x2 + "," + y2);
        if (from == null)
            from = addVertex(x1, y1);
        if (to == null)
            to = addVertex(x2, y2);

        graph.get(from).get(to).setMaxFlow(flow);
        graph.get(from).get(to).setResidualFlow(flow);
        graph.get(from).get(to).setCurrentFlow(0);
        graph.get(to).get(from).setMaxFlow(flow);
        graph.get(to).get(from).setResidualFlow(flow);
        graph.get(to).get(from).setCurrentFlow(0);
    }

    public void updateEdge(int flow, Vertex from, Vertex to) {
        Edge currentEdge = graph.get(from).get(to);
        currentEdge.setCurrentFlow(currentEdge.getCurrentFlow() + flow);
        currentEdge.setResidualFlow(currentEdge.getResidualFlow() - flow);

        Edge currentBackwardEdge = graph.get(to).get(from);
        currentBackwardEdge.setCurrentFlow(currentBackwardEdge.getCurrentFlow() - flow);
        currentBackwardEdge.setResidualFlow(currentBackwardEdge.getResidualFlow() + flow);
    }


    public Vertex addSourceVertex(String sourceName) {
        Vertex source = new Vertex(Integer.MIN_VALUE, Integer.MIN_VALUE, "source");
        vertexByCoord.put(Integer.MIN_VALUE + "," + Integer.MIN_VALUE, source);
        graph.put(source, new HashMap<>());
        vertexByCoord.forEach((key, vertex) -> {
            if (sourceName.equals(vertex.getType())) {
                addEdge(vertex.getCapacity(), 0, source.getX(), source.getY(), vertex.getX(), vertex.getY());
            }
        });
        return source;
    }

    public Vertex addSinkVertex(String sinkName) {
        Vertex sink = new Vertex(Integer.MAX_VALUE, Integer.MAX_VALUE, "sink");
        vertexByCoord.put(Integer.MAX_VALUE + "," + Integer.MAX_VALUE, sink);
        graph.put(sink, new HashMap<>());
        vertexByCoord.forEach((key, vertex) -> {
            if (sinkName.equals(vertex.getType())) {
                addEdge(vertex.getCapacity(), 0, vertex.getX(), vertex.getY(), sink.getX(), sink.getY());
            }
        });
        return sink;
    }

    public void deleteSourceVertex(Vertex sourceVert) {
        graph.forEach((key, vertex) -> {
            graph.get(key).remove(sourceVert);
        });
        graph.remove(sourceVert);
        vertexByCoord.remove(sourceVert.getX() + "," + sourceVert.getY());
    }

    public void deleteSinkVertex(Vertex sinkVert) {
        graph.forEach((key, vertex) -> {
            graph.get(key).remove(sinkVert);
        });
        graph.remove(sinkVert);
        vertexByCoord.remove(sinkVert.getX() + "," + sinkVert.getY());
    }

    public void printGraph() {
        System.out.println("=== Flow Network Graph ===");

        for (Vertex from : graph.keySet()) {
            System.out.println("Vertex " + "[" + from.getX() + "," + from.getY() + "]" + " (" + from.getType() + "): ");

            Map<Vertex, Edge> edges = graph.get(from);
            if (edges.isEmpty()) {
                System.out.println("   No outgoing edges.");
            } else {
                for (Map.Entry<Vertex, Edge> entry : edges.entrySet()) {
                    Vertex to = entry.getKey();
                    Edge edge = entry.getValue();
                    System.out.printf("   --> to Vertex [%d,%d] (%s): flow = %d / %d, residual = %d%n",
                            to.getX(),
                            to.getY(),
                            to.getType(),
                            edge.getCurrentFlow(),
                            edge.getMaxFlow(),
                            edge.getResidualFlow()
                    );
                }
            }
            System.out.println();
        }

        System.out.println("=== End of Graph ===\n");
    }

    public boolean BFS(Vertex src, Vertex dest) {
        Map<Vertex, Integer> visited = new HashMap<>();  //0 - nie odwiedzona, 1 - dodana do koleki, 2 - przetworzona
        previousElements.clear();
        src = vertexByCoord.get(src.getX() + "," + src.getY());
        dest = vertexByCoord.get(dest.getX() + "," + dest.getY());
        vertexByCoord.forEach((key, vertex) -> {
            previousElements.put(vertexByCoord.get(key), null);
            visited.put(vertexByCoord.get(key), 0);
        });
        Queue<Vertex> queue = new LinkedList<>();
        queue.add(src);
        visited.put(src, 1);
        while (!queue.isEmpty()) {
            Vertex c = queue.poll();
            visited.put(c, 2);
            if (c.equals(dest)) {
                return true;
            }

            graph.get(c).forEach((v, e) -> {
                if ((visited.get(v) == 0) && (e.getResidualFlow() > 0)) {
                    visited.put(c, 1);
                    previousElements.put(v, c);
                    queue.add(v);
                }
            });
        }
        return false;
    }

    public int BFSMaxFlow(Vertex src, Vertex dest) {
        int maxFlow = 0;
        int minFlow;
        src = vertexByCoord.get(src.getX() + "," + src.getY());
        dest = vertexByCoord.get(dest.getX() + "," + dest.getY());

        while (BFS(src, dest)) {
            minFlow = Integer.MAX_VALUE;
            Vertex currentVertex = dest;
            while (previousElements.get(currentVertex) != null) {
                Vertex prevVert = previousElements.get(currentVertex);
                if (minFlow > graph.get(prevVert).get(currentVertex).getResidualFlow()) {
                    minFlow = graph.get(prevVert).get(currentVertex).getResidualFlow();
                }
                currentVertex = previousElements.get(currentVertex);
            }

            currentVertex = dest;

            while (previousElements.get(currentVertex) != null) {
                Vertex prevVert = previousElements.get(currentVertex);
                updateEdge(minFlow, prevVert, currentVertex);
                currentVertex = previousElements.get(currentVertex);
            }
            maxFlow += minFlow;
            previousElements.get(dest).addGottenFlow(minFlow);
        }
        return maxFlow;
    }

    public boolean dijkstra(Vertex source, Vertex dest, Map<Vertex, Vertex> previousVertices) {
        Map<Vertex, Integer> costToReach = new HashMap<>();
        Set<Vertex> visited = new HashSet<>();
        PriorityQueue<Vertex> pq = new PriorityQueue<>(Comparator.comparingInt(costToReach::get));

        graph.keySet().forEach(v -> costToReach.put(v, Integer.MAX_VALUE));
        costToReach.put(source, 0);
        pq.add(source);
        previousVertices.clear();

        while (!pq.isEmpty()) {
            Vertex c = pq.poll();
            if (visited.contains(c)) continue;
            visited.add(c);
            graph.get(c).forEach((v, e) -> {
                if (e.getResidualFlow() > 0) {
                    //relaxation
                    int newCost = costToReach.get(c) + e.getRepairCost();
                    if (costToReach.get(v) > newCost) {
                        costToReach.put(v, newCost);
                        previousVertices.put(v, c);
                        pq.add(v);
                    }
                }
            });
        }
        return previousVertices.containsKey(dest);
    }

    public int minCostMaxFlow(Vertex src, Vertex dest) {
        int maxFlow = 0;
        int minFlow;
        Map<Vertex, Vertex> previousVertices = new HashMap<>();
        while (dijkstra(src, dest, previousVertices)) {
            minFlow = Integer.MAX_VALUE;
            Vertex currentVertex = dest;
            while (previousVertices.get(currentVertex) != null) {
                Vertex prevVertex = previousVertices.get(currentVertex);
                minFlow = Math.min(minFlow, graph.get(prevVertex).get(currentVertex).getResidualFlow());
                currentVertex = prevVertex;
            }
            currentVertex = dest;

            while (previousVertices.get(currentVertex) != null) {
                Vertex prevVertex = previousVertices.get(currentVertex);
                updateEdge(minFlow, prevVertex, currentVertex);
                currentVertex = prevVertex;
            }
            maxFlow += minFlow;
            previousVertices.get(dest).addGottenFlow(minFlow);
        }
        return maxFlow;
    }
}