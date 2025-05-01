package SiecPrzeplywowa;

import java.awt.geom.Point2D;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class Siec {
    private Map<Vertex, HashMap<Vertex, Edge>> graph;
    private Map<String, Vertex> vertexByCoord;
    private ArrayList<Vertex> previousElements;

    public Siec() {
        this.graph = new ConcurrentHashMap<>();
        this.vertexByCoord = new ConcurrentHashMap<>();
        this.previousElements = new ArrayList<>();
    }

    public Vertex addVertex(int x, int y) {
        Vertex v = new Vertex(x, y);
        vertexByCoord.put(x + "," + y, v);
        graph.put(v, new HashMap<>());
        return v;
    }

    //dodajemy krawedz a -> b i a <- b
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

        Edge forwardEdge = new Edge(to.getLocalId(), from.getLocalId(), maxFlow);
        Edge backwardEdge = new Edge(from.getLocalId(), to.getLocalId(), maxFlow);
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
            System.out.println("Vertex " + from.getLocalId() + " (" + from.getType() + ") [" + (int)from.getX() + "," + (int)from.getY() + "]:");

            Map<Vertex, Edge> edges = graph.get(from);
            if (edges.isEmpty()) {
                System.out.println("   No outgoing edges.");
            } else {
                for (Map.Entry<Vertex, Edge> entry : edges.entrySet()) {
                    Vertex to = entry.getKey();
                    Edge edge = entry.getValue();
                    System.out.printf("   --> to Vertex %d (%s) [%d,%d]: flow = %d / %d, residual = %d%n",
                            to.getLocalId(),
                            to.getType(),
                            (int)to.getX(),
                            (int)to.getY(),
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
        int[] visited = new int[vertexByCoord.size() + 1];
        previousElements.clear();
        src = vertexByCoord.get((int)src.getX() + "," + (int)src.getY());
        dest = vertexByCoord.get((int)dest.getX() + "," + (int)dest.getY());
        Arrays.fill(visited, 0); //0 - nie odwiedzona, 1 - dodana do koleki, 2 - przetworzona
        for (int i = 0; i <= vertexByCoord.size(); i++) {
            previousElements.add(null);
        }
        Queue<Vertex> queue = new LinkedList<>();
        queue.add(src);
        visited[src.getLocalId()] = 1;
        while (!queue.isEmpty()) {
            Vertex c = queue.poll();
            visited[c.getLocalId()] = 2;
            if (c.equals(dest)) {
                return true;
            }
            graph.get(c).forEach((v, e) -> {
                if (visited[v.getLocalId()] == 0 && (e.getResidualFlow() > 0)) {
                    visited[v.getLocalId()] = 1;
                    previousElements.set(v.getLocalId(), c);
                    queue.add(v);
                }
            });
        }
        return false;
    }

    public int maxFlow(Vertex src, Vertex dest) {
        int maxFlow = 0;
        int minFlow;
        src = vertexByCoord.get((int)src.getX() + "," + (int)src.getY());
        dest = vertexByCoord.get((int)dest.getX() + "," + (int)dest.getY());
        while (BFS(src, dest)) {
            minFlow = Integer.MAX_VALUE;
            Vertex currentVertex = dest;
            while (previousElements.get(currentVertex.getLocalId()) != null) {
                Vertex prevVert = previousElements.get(currentVertex.getLocalId());
                if (minFlow > graph.get(prevVert).get(currentVertex).getResidualFlow()) {
                    minFlow = graph.get(prevVert).get(currentVertex).getResidualFlow();
                }
                currentVertex = previousElements.get(currentVertex.getLocalId());
            }
            currentVertex = dest;
            while (previousElements.get(currentVertex.getLocalId()) != null) {
                Vertex prevVert = previousElements.get(currentVertex.getLocalId());
                updateEdge(minFlow, prevVert, currentVertex);
                currentVertex = previousElements.get(currentVertex.getLocalId());
            }
            maxFlow += minFlow;
        }
        return maxFlow;
    }
}
