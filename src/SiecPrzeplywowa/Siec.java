package SiecPrzeplywowa;


import java.awt.geom.Point2D;
import java.beans.VetoableChangeListener;
import java.security.KeyPair;
import java.sql.SQLOutput;
import java.util.*;
import java.lang.Object;


public class Siec {
    private ArrayList<HashMap<Vertex, Edge>> graph;
    private Map<String, Vertex> vertexByCoord;
    private ArrayList<Vertex> previousElements;

    public Siec() {
        this.graph = new ArrayList<>();
        this.vertexByCoord = new HashMap<>();
        this.previousElements = new ArrayList<>();
    }

    public void addVertex(int x, int y) {
        Vertex v = new Vertex(x, y);
        vertexByCoord.put(x + "," + y, v);
        graph.add(new HashMap<>());
    }

    //dodajemy krawedz a -> b
    public void addEdge(int maxFlow, int x1, int y1, int x2, int y2) {    //a - id wierzcholka #1, v2 - x2 i y2 wierzcholka 2
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

        Edge forwardEdge = new Edge(to.getLocalId(), from.getLocalId(), maxFlow);
        Edge backwardEdge = new Edge(from.getLocalId(), to.getLocalId(), 0);
        graph.get(from.getLocalId()).put(to, forwardEdge);
        graph.get(to.getLocalId()).put(from, backwardEdge);
        forwardEdge.setReverseEdge(backwardEdge);
        backwardEdge.setReverseEdge(forwardEdge);
    }

    public void updateEdge(int currentFlow, Vertex from, Vertex to) {
        Edge currentEdge = graph.get(from.getLocalId()).get(to);
        Edge currentBackwardEdge = currentEdge.getReverseEdge();
        currentEdge.setCurrentFlow(currentEdge.getCurrentFlow() + currentFlow);
        currentEdge.setResidualFlow(currentEdge.getResidualFlow() - currentFlow);
        currentBackwardEdge.setResidualFlow(currentBackwardEdge.getResidualFlow() + currentFlow);
    }

    public Edge getData(Vertex from, Vertex to) {
        return graph.get(from.getLocalId()).get(to);
    }

    public void printGraph() {
        int i = 0;
        for (var row : graph) {
            System.out.println("Vertex number " + i++ + " is connected to: ");
            System.out.println(row.toString());
            System.out.println();
        }
    }

    public boolean BFS(Vertex src, Vertex dest) {
        int[] visited = new int[vertexByCoord.size() + 1];
        previousElements.clear();
        src = vertexByCoord.get(src.getX() + "," + src.getY());
        dest = vertexByCoord.get(dest.getX() + "," + dest.getY());
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
            graph.get(c.getLocalId()).forEach((v, e) -> {
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
        src = vertexByCoord.get(src.getX() + "," + src.getY());
        dest = vertexByCoord.get(dest.getX() + "," + dest.getY());
        while (BFS(src, dest)) {
            minFlow = Integer.MAX_VALUE;
            Vertex currentVertex = dest;
            while (previousElements.get(currentVertex.getLocalId()) != null) {
                Vertex prevVert = previousElements.get(currentVertex.getLocalId());
                if (minFlow > graph.get(prevVert.getLocalId()).get(currentVertex).getResidualFlow()) {
                    minFlow = graph.get(prevVert.getLocalId()).get(currentVertex).getResidualFlow();
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
