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
            Vertex v = addVertex(x1, y1);
            from = vertexByCoord.get(x1 + "," + y1);
        }
        if (to == null) {
            Vertex v = addVertex(x2, y2);
            to = vertexByCoord.get(x2 + "," + y2);
        }

        Edge forwardEdge = new Edge(from, to, maxFlow, repairCost);
        Edge backwardEdge = new Edge(to, from, maxFlow, repairCost);
        forwardEdge.setReverseEdge(backwardEdge);
        backwardEdge.setReverseEdge(forwardEdge);
        graph.get(from).put(to, forwardEdge);
        graph.get(to).put(from, backwardEdge);

    }

    public void setFlow(int flow, int x1, int y1, int x2, int y2) {
        Vertex from = vertexByCoord.get(x1 + "," + y1);
        Vertex to = vertexByCoord.get(x2 + "," + y2);
        if (from == null)
            from = addVertex(x1, y1);
        if (to == null)
            from = addVertex(x2, y2);

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

        Edge currentBackwardEdge = currentEdge.getReverseEdge();
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


    /// Te dwie metody robią to samo
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
        visited.get(dest);
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
                currentVertex = previousElements.get(currentVertex);
            }
            maxFlow += minFlow;
            previousElements.get(dest).addGottenFlow(minFlow);
        }
        return maxFlow;
    }

    public void refactorRoads(Vertex src, Vertex sink) {
        deleteSinkVertex(sink);
        deleteSourceVertex(src);
        while (true) {
            var cycle = findCycleWithNegativeCost();
            if (cycle.isEmpty()) break;
            /// dwa razy to obliczam.. nie da się inaczej?
            int minVal = getMinFlowInCycle(cycle);
            System.out.println("___________");
            System.out.println(cycle);
            System.out.println("min val: " + minVal);
            for (int i = 0; i < cycle.size(); i++) {
                Vertex from = cycle.get(i);
                Vertex to = cycle.get((i + 1) % cycle.size());
                Edge e = graph.get(from).get(to);
                System.out.println("Przed: "+e.getCurrentFlow()+", res: "+e.getResidualFlow());
                updateEdge(minVal, e.getFrom(), e.getTo());

                System.out.println("Po: "+e.getCurrentFlow()+", res: "+e.getResidualFlow());
            }
        }
    }

    public List<Vertex> findCycleWithNegativeCost() {
        for (Vertex vertex : graph.keySet()) {
            List<List<Vertex>> cycles = new ArrayList<>();
            DFS(vertex, new HashSet<>(), new HashSet<>(), new HashMap<>(), cycles);
            for (List<Vertex> cycle : cycles) {
                if (countCycleCost(cycle) < 0 && getMinFlowInCycle(cycle) > 0)
                    return cycle;
            }
        }
        return new ArrayList<>();
    }


    private void DFS(Vertex current, Set<Vertex> visited, Set<Vertex> stack, Map<Vertex, Vertex> parent, List<List<Vertex>> cycles) {
        visited.add(current);
        stack.add(current);
        Map<Vertex, Edge> neighbors = graph.getOrDefault(current, new HashMap<>());
        for (Vertex neighbor : neighbors.keySet()) {
            if (!visited.contains(neighbor)) {
                parent.put(neighbor, current);
                DFS(neighbor, visited, stack, parent, cycles);
            } else if (stack.contains(neighbor)) {
                List<Vertex> cycle = new ArrayList<>();
                Vertex temp = current;
                cycle.add(temp);
                while (!temp.equals(neighbor)) {
                    temp = parent.get(temp);
                    if (temp == null) break;
                    cycle.add(temp);
                }
                Collections.reverse(cycle);
                if (cycle.size() >= 3) {
                    cycles.add(cycle);
                }
            }
        }
        stack.remove(current);
    }


    public int getMinFlowInCycle(List<Vertex> cycle) {
        int minVal = Integer.MAX_VALUE;
        for (int i = 0; i < cycle.size(); i++) {
            Vertex from = cycle.get(i);
            Vertex to = cycle.get((i + 1) % cycle.size());
            Edge e = graph.get(from).get(to);
            if (e.getResidualFlow() < minVal)
                minVal = e.getResidualFlow();
        }
        return minVal;
    }

    public int countCycleCost(List<Vertex> cycle) {
        int cost = 0;
        for (int i = 0; i < cycle.size(); i++) {
            Vertex from = cycle.get(i);
            Vertex to = cycle.get((i + 1) % cycle.size());
            Edge e = this.getGraph().get(from).get(to);
            cost += (e.getRepairCost() * e.getCurrentFlow());
        }
        return cost;
    }
}