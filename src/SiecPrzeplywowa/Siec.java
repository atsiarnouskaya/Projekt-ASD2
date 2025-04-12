package SiecPrzeplywowa;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Siec {
    private ArrayList<ArrayList<Edge>> graph;
    private ArrayList<Integer> previousElements;

    public Siec() {}

    public Siec(ArrayList<ArrayList<Edge>> graph) {
        this.graph = graph;
    }

    public Siec(int a) {
        graph = new ArrayList<>();
        previousElements = new ArrayList<>();
        for (int i = 0; i < a; i++) {
            graph.add(new ArrayList<Edge>());
            for (int j = 0; j < a; j++) {
                graph.get(i).add(new Edge(0, 0));
            }
        }
    }

    //dodajemy krawedz a -> b
    public void addEdge(int maxFlow, int a, int b) {
        graph.get(a).get(b).setMaxFlow(maxFlow);

    }

    public void updateEdge(int currentFlow, int a, int b) {
        graph.get(a).get(b).setCurrentFlow(graph.get(a).get(b).getCurrentFlow() + currentFlow);
        graph.get(a).get(b).setMaxFlow(graph.get(a).get(b).getMaxFlow() - currentFlow);
        graph.get(a).get(b).setResidualFlow(graph.get(a).get(b).getResidualFlow() + currentFlow);
    }

    public Edge getData(int a, int b) {
        return graph.get(a).get(b);
    }

    public void printGraph() {
        for (var row : graph) {
            for (var Edge : row) {
                System.out.print("(" + Edge.getMaxFlow() + " " + Edge.getCurrentFlow() + " " + Edge.getResidualFlow() + ") ");
            }
            System.out.println();
        }
    }

    private boolean BFS(int src, int dest) {
        int[] visited = new int[graph.size()];
        Arrays.fill(visited, 0); //0 - nie odwiedzona, 1 - dodana do koleki, 2 - przetworzona
        for (int i = 0; i < graph.size(); i++) {
            previousElements.add(-1);
        }
        Queue<Integer> queue = new LinkedList<>();
        queue.add(src);
        visited[src] = 1;
        while (!queue.isEmpty()) {
            int c = queue.poll();
            visited[c] = 2;
            if (c == dest) {
                return true;
            }
            for (int i = 0; i < graph.get(c).size(); i++) {
                if (graph.get(c).get(i).getMaxFlow() == 0) continue;
                if (visited[i] == 0 && (graph.get(c).get(i).getMaxFlow() >= graph.get(c).get(i).getCurrentFlow())) {
                    visited[i] = 1;
                    previousElements.set(i, c);
                    queue.add(i);
                }
            }
        }
        return false;
    }

    public int maxFlow(int src, int dest) {
        int maxFlow = 0;
        int minFlow;
        while (BFS(src, dest)) {
            minFlow = Integer.MAX_VALUE;
            int currentVertex = dest;
            while (previousElements.get(currentVertex) != -1) {
                int prevVert = previousElements.get(currentVertex);
                if (minFlow > graph.get(prevVert).get(currentVertex).getMaxFlow()) {
                    minFlow =  graph.get(prevVert).get(currentVertex).getMaxFlow();
                }
                currentVertex = previousElements.get(currentVertex);
            }
            currentVertex = dest;
            while (previousElements.get(currentVertex) != -1) {
                int prevVert = previousElements.get(currentVertex);
                updateEdge(minFlow, prevVert, currentVertex);
                currentVertex = previousElements.get(currentVertex);
            }
            maxFlow += minFlow;
        }
        return maxFlow;
    }
}
