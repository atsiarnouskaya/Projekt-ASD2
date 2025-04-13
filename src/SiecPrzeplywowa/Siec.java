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
    private ArrayList<Integer> previousElements;

    public Siec() {
        this.graph = new ArrayList<>();
        this.vertexByCoord = new HashMap<>();
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

        Edge edge = new Edge(to.getLocalId(), from.getLocalId(), maxFlow);
        graph.get(from.getLocalId()).put(to, edge);
    }

    public void updateEdge(int currentFlow, Vertex from, Vertex to) {
        Edge currentEdge = graph.get(from.getLocalId()).get(to);
        currentEdge.setCurrentFlow(currentEdge.getCurrentFlow() + currentFlow);
        currentEdge.setMaxFlow(currentEdge.getMaxFlow() - currentFlow);
        currentEdge.setResidualFlow(currentEdge.getResidualFlow() + currentFlow);
    }

    public Edge getData(Vertex from, Vertex to) {
        return graph.get(from.getLocalId()).get(to);
    }

    public void printGraph() {
        int i = 0;
        for (var row : graph) {
            System.out.println(i++ + ":");
            System.out.println(row.toString());
            System.out.println();
        }
    }

//    private boolean BFS(int src, int dest) {
//        int[] visited = new int[graph.size()];
//        Arrays.fill(visited, 0); //0 - nie odwiedzona, 1 - dodana do koleki, 2 - przetworzona
//        for (int i = 0; i < graph.size(); i++) {
//            previousElements.add(-1);
//        }
//        Queue<Integer> queue = new LinkedList<>();
//        queue.add(src);
//        visited[src] = 1;
//        while (!queue.isEmpty()) {
//            int c = queue.poll();
//            visited[c] = 2;
//            if (c == dest) {
//                return true;
//            }
//            for (int i = 0; i < graph.get(c).size(); i++) {
//                if (graph.get(c).get(i).getMaxFlow() == 0) continue;
//                if (visited[i] == 0 && (graph.get(c).get(i).getMaxFlow() >= graph.get(c).get(i).getCurrentFlow())) {
//                    visited[i] = 1;
//                    previousElements.set(i, c);
//                    queue.add(i);
//                }
//            }
//        }
//        return false;
//    }
//
//    public int maxFlow(int src, int dest) {
//        int maxFlow = 0;
//        int minFlow;
//        while (BFS(src, dest)) {
//            minFlow = Integer.MAX_VALUE;
//            int currentVertex = dest;
//            while (previousElements.get(currentVertex) != -1) {
//                int prevVert = previousElements.get(currentVertex);
//                if (minFlow > graph.get(prevVert).get(currentVertex).getMaxFlow()) {
//                    minFlow =  graph.get(prevVert).get(currentVertex).getMaxFlow();
//                }
//                currentVertex = previousElements.get(currentVertex);
//            }
//            currentVertex = dest;
//            while (previousElements.get(currentVertex) != -1) {
//                int prevVert = previousElements.get(currentVertex);
//                updateEdge(minFlow, prevVert, currentVertex);
//                currentVertex = previousElements.get(currentVertex);
//            }
//            maxFlow += minFlow;
//        }
//        return maxFlow;
//    }
}
