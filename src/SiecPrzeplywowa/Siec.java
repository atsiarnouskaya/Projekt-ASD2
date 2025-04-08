package SiecPrzeplywowa;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Siec {
    private Node[][] graph;
    private int[] previousElements;

    public Siec() {}

    public Siec(Node[][] graph) {
        this.graph = graph;
    }

    public Siec(int a) {
        graph = new Node[a][a];
        previousElements = new int[a];
        for (int i = 0; i < a; i++) {
            for (int j = 0; j < a; j++) {
                graph[i][j] = new Node(0, 0);
            }
        }
    }

    //dodajemy krawedz a -> b
    public void addNode(int maxFlow, int a, int b) {
        graph[a][b] = new Node(maxFlow, 0);
    }

    public void updateNode(int currentFlow, int a, int b) {
        graph[a][b].setCurrentFlow(graph[a][b].getCurrentFlow() + currentFlow);
        graph[a][b].setMaxFlow(graph[a][b].getMaxFlow() - currentFlow);
        graph[a][b].setResidualFlow(graph[a][b].getResidualFlow() + currentFlow);
    }

    public Node getData(int a, int b) {
        return graph[a][b];
    }

    public void printGraph() {
        for (var row : graph) {
            for (var node : row) {
                System.out.print("(" + node.getMaxFlow() + " " + node.getCurrentFlow() + " " + node.getResidualFlow() + ") ");
            }
            System.out.println();
        }
    }

    private boolean BFS(int src, int dest) {
        int[] visited = new int[graph.length];
        Arrays.fill(visited, 0); //0 - nie odwiedzona, 1 - dodana do koleki, 2 - przetworzona
        Arrays.fill(previousElements, -1);
        Queue<Integer> queue = new LinkedList<>();
        queue.add(src);
        visited[src] = 1;
        while (!queue.isEmpty()) {
            int c = queue.poll();
            visited[c] = 2;
            if (c == dest) {
                return true;
            }
            for (int i = 0; i < graph[c].length; i++) {
                if (graph[c][i].getMaxFlow() == 0) continue;
                if (visited[i] == 0 && (graph[c][i].getMaxFlow() >= graph[c][i].getCurrentFlow())) {
                    visited[i] = 1;
                    previousElements[i] = c;
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
            while (previousElements[currentVertex] != -1) {
                int prevVert = previousElements[currentVertex];
                if (minFlow > graph[prevVert][currentVertex].getMaxFlow()) {
                    minFlow = graph[prevVert][currentVertex].getMaxFlow();
                }
                currentVertex = previousElements[currentVertex];
            }
            currentVertex = dest;
            while (previousElements[currentVertex] != -1) {
                int prevVert = previousElements[currentVertex];
                updateNode(minFlow, prevVert, currentVertex);
                currentVertex = previousElements[currentVertex];
            }
            maxFlow += minFlow;
        }
        return maxFlow;
    }
}
