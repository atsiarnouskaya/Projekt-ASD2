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
        Vertex v = new Vertex(x, y);
        vertexByCoord.put(x + "," + y, v);
        graph.put(v, new HashMap<>());
        return v;
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
    public void addEdge(int maxFlow, int x1, int y1, int x2, int y2) {
        Vertex from = vertexByCoord.get(x1 + "," + y1);
        Vertex to = vertexByCoord.get(x2 + "," + y2);

        if (from == null) {
            Vertex v = addVertex(x1, y1);
            from = vertexByCoord.get(x1 + "," + y1);
        }
        if (to == null) {
            Vertex v = addVertex(x2, y2);
            to = vertexByCoord.get(x2 + "," + y2);
        }

        Edge forwardEdge = new Edge(from, to, maxFlow);
        Edge backwardEdge = new Edge(to, from, maxFlow);
        forwardEdge.setReverseEdge(backwardEdge);
        backwardEdge.setReverseEdge(forwardEdge);
        graph.get(from).put(to, forwardEdge);
        graph.get(to).put(from, backwardEdge);

    }

    public void updateEdge(int currentFlow, Vertex from, Vertex to) {
        Edge currentEdge = graph.get(from).get(to);
        currentEdge.setCurrentFlow(currentEdge.getCurrentFlow() + currentFlow);
        currentEdge.setResidualFlow(currentEdge.getResidualFlow() - currentFlow);

        // Wydaje mi się, że poniższy kod powinien zostać usunięty.
        // Residual flow wyraża pozostałą przepustowość na danej krawędzi
        // Ta krawędź reprezentuje przepływ towarów w innym kierunku.
        // Zwiększenie residual flow na krawędzi w przeciwnym kierunku powoduje dziwną sytuację:
        // Przewiezienie 2 zboża z farmy do browaru zwiększa możliwość przewozu piwa o 2 tą samą drogą w przeciwnym kierunku
        // Drogi o przeciwnych kierunkach mają swoje rozdzielne flow i residual flow i nie powinny być łączone

        // Alternatywnie, jeśli droga miałaby mieć wspólną przepustowość dla obu kierunków* to prawdopodobnie
        // powinno być tam `- currentFlow` zamiast `+ currentFlow`
        //
        // * Wspólną przepustowość dla obu kierunków, czyli:
        // Drogą o wspólnej przepustowości 2 można:
        // A. Przewieźć 2 od A do B i 0 od B do A
        // B. Przewieźć 0 od A do B i 2 od B do A
        // C. Przewieźć 1 od A do B i 1 od B do A
        // * Rozdzielną przepustowość dla obu kierunków, czyli:
        // Drogą o rozdzielnej przepustowości 2 można:
        // A. Przewieźć 2 od A do B i 2 od B do A

//        Edge currentBackwardEdge = currentEdge.getReverseEdge();
//        currentBackwardEdge.setResidualFlow(currentBackwardEdge.getResidualFlow() + currentFlow);
    }

    public Vertex addSourceVertex(String sourceName) {
        Vertex source = new Vertex(Integer.MIN_VALUE, Integer.MIN_VALUE, "source");
        vertexByCoord.put(Integer.MIN_VALUE + "," + Integer.MIN_VALUE, source);
        graph.put(source, new HashMap<>());
        vertexByCoord.forEach((key, vertex) -> {
            if (sourceName.equals(vertex.getType())){
                addEdge(vertex.getCapacity(), source.getX(), source.getY(), vertex.getX(), vertex.getY());
            }
        });
        return source;
    }

    public Vertex addSinkVertex(String sinkName) {
        Vertex sink = new Vertex(Integer.MAX_VALUE, Integer.MAX_VALUE, "sink");
        vertexByCoord.put(Integer.MAX_VALUE + "," + Integer.MAX_VALUE, sink);
        graph.put(sink, new HashMap<>());
        vertexByCoord.forEach((key, vertex) -> {
            if (sinkName.equals(vertex.getType())){
                addEdge(vertex.getCapacity(), vertex.getX(), vertex.getY(), sink.getX(), sink.getY());
            }
        });
        return sink;
    }

    public Edge getData(Vertex from, Vertex to) {
        return graph.get(from).get(to);
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
        visited.put(src, 0);
        while (!queue.isEmpty()) {
            Vertex c = queue.poll();
            visited.put(c, 2);
            if (c.equals(dest)) {
                return true;
            }

            graph.get(c).forEach((v, e) -> {
                if (visited.get(v) == 0 && (e.getResidualFlow() > 0)) {
                    visited.put(c, 1);
                    previousElements.put(v, c);
                    queue.add(v);
                }
            });
        }
        return false;
    }

    public int maxFlow(Vertex src, Vertex dest) {
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
                Edge currentEdge = graph.get(prevVert).get(currentVertex);
                currentVertex = previousElements.get(currentVertex);
            }
            maxFlow += minFlow;
        }
        return maxFlow;
    }
}
