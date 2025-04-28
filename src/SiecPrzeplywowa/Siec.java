package SiecPrzeplywowa;

import java.awt.geom.Point2D;
import java.util.*;


public class Siec {
    private HashMap<Vertex, HashMap<Vertex, Edge>> graph;
    private Map<String, Vertex> vertexByCoord;
    private ArrayList<Vertex> previousElements;

    public Siec() {
        this.graph = new HashMap<>();
        this.vertexByCoord = new HashMap<>();
        this.previousElements = new ArrayList<>();
    }

    public Vertex addVertex(int x, int y) {
        Vertex v = new Vertex(x, y);
        vertexByCoord.put(x + "," + y, v);
        // Ciężko było mi zrozumieć dlaczego przy dodawaniu nowego wierzchołka dodajemy nowy element w grafie
        // Dopiero po pewnym czasie zrozumiałam, że wartości locationId w Vertex są rosnące od zera
        // co pokyrwa się z indeksami arraylisty. Wydaje mi się, że łatwiej pracować na kluczu, który jest referencją
        // do wierzchołka. Wydaje mi się, że tak jest czytelniej
        graph.put(v, new HashMap<>());
        return v;
    }

    //dodajemy krawedz a -> b i a <- b
    public void addEdge(int maxFlow, int x1, int y1, int x2, int y2) {
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
        // Wydaje mi się, że max flow w drugą stronę powinien być taki sam celem umożliwienia
        // transportu towarów w obie strony tą samą drogą.
        // Uzgodniłyśmy, że narazie droga ma taką samą i rozdzielną przepustowość w każdym kierunku
        // Czyli droga o przepustowości 2 pozwala przewieźć 2 zboża od A do B oraz 2 zboża od B do A
        //
        // W przeciwnym wypadku, wydaje mi się, że nie powinnyśmy mieć dwóch oddzielnych krawędzi
        // na dwa oddzielne kierunki drogi
        Edge backwardEdge = new Edge(from.getLocalId(), to.getLocalId(), maxFlow);
        graph.get(from).put(to, forwardEdge);
        graph.get(to).put(from, backwardEdge);
        forwardEdge.setReverseEdge(backwardEdge);
        backwardEdge.setReverseEdge(forwardEdge);
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

        // Edge currentBackwardEdge = currentEdge.getReverseEdge();
        // currentBackwardEdge.setResidualFlow(currentBackwardEdge.getResidualFlow() + currentFlow);
    }

    public Edge getData(Vertex from, Vertex to) {
        return graph.get(from).get(to);
    }

    public void printGraph() {
        for (Vertex row : graph.keySet()) {
            System.out.println("Vertex number " + row.getLocalId() + " is connected to: ");
            System.out.println(graph.get(row).toString());
            System.out.println();
        }
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

    public int maxFlow(Point2D.Double srcPoint, Point2D.Double destPoint) {
        int maxFlow = 0;
        int minFlow;
        Vertex src = vertexByCoord.get((int)srcPoint.getX() + "," + (int)srcPoint.getY());
        Vertex dest = vertexByCoord.get((int)destPoint.getX() + "," + (int)destPoint.getY());
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
