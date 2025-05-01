package SiecPrzeplywowa;

public class Edge {
    private int maxFlow;
    private int currentFlow;
    private int residualFlow;
    private Vertex from;
    private Vertex to;
    private Edge reverseEdge;

    public Edge() {
    }

    public Edge(Vertex from, Vertex to, int maxFlow) {
        this.from = from;
        this.to = to;
        this.residualFlow = maxFlow;
        this.currentFlow = 0;
        this.maxFlow = maxFlow;
    }

    public Edge(int maxFlow, int currentFlow) {
        this.maxFlow = maxFlow;
        this.currentFlow = currentFlow;
        this.residualFlow = maxFlow - currentFlow;
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    public void setMaxFlow(int maxFlow) {
        this.maxFlow = maxFlow;
    }

    public int getCurrentFlow() {
        return currentFlow;
    }

    public void setCurrentFlow(int currentFlow) {
        this.currentFlow = currentFlow;
    }

    public int getResidualFlow() {
        return residualFlow;
    }

    public void setResidualFlow(int residualFlow) {
        this.residualFlow = residualFlow;
    }

    public void setReverseEdge(Edge e) {
        this.reverseEdge = e;
    }

    public Edge getReverseEdge() {
        return this.reverseEdge;
    }

    @Override
    public String toString() {
        return ("edge (from " + from + " to " + to + ". Properties: " +
                "maxFlow = " + maxFlow +
                ", currentFlow = " + currentFlow +
                ", residualFlow = " + residualFlow +
                ')');
    }
}
