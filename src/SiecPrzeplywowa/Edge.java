package SiecPrzeplywowa;

public class Edge {
    static private int id = 0;
    private int localId;
    private int maxFlow;
    private int currentFlow;
    private int residualFlow;
    private int from;
    private int to;
    private Edge reverseEdge;

    public Edge() {
    }

    public Edge(int to, int from, int maxFlow) {
        this.localId = id++;
        this.to = to;
        this.from = from;
        this.residualFlow = maxFlow;
        this.currentFlow = 0;
        this.maxFlow = maxFlow;
    }

    public Edge(int maxFlow, int currentFlow) {
        this.maxFlow = maxFlow;
        this.currentFlow = currentFlow;
        this.residualFlow = maxFlow - currentFlow;
        this.localId = id;
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

    public int getLocalId() {
        return localId;
    }

    public void setLocalId(int localId) {
        this.localId = localId;
    }

    public void setReverseEdge(Edge e) {
        this.reverseEdge = e;
    }

    public Edge getReverseEdge() {
        return this.reverseEdge;
    }

    @Override
    public String toString() {
        return ("edge (from " + from + " to " + to + " properties: " +
                "maxFlow = " + maxFlow +
                ", currentFlow = " + currentFlow +
                ", residualFlow = " + residualFlow +
                ')');
    }
}
