package SiecPrzeplywowa;

public class Edge {
    private int maxFlow;
    private int currentFlow;
    private int residualFlow;
    private int repairCost;
    private Vertex from;
    private Vertex to;
    private Edge reverseEdge;

    public Edge() {
    }

    public Edge(Vertex from, Vertex to, int maxFlow, int repairCost) {
        this.from = from;
        this.to = to;
        this.residualFlow = maxFlow;
        this.currentFlow = 0;
        this.maxFlow = maxFlow;
        this.repairCost= repairCost;
    }

    public Edge(int maxFlow, int currentFlow, int repairCost) {
        this.maxFlow = maxFlow;
        this.currentFlow = currentFlow;
        this.residualFlow = maxFlow - currentFlow;
        this.repairCost= repairCost;
    }

    public int getMaxFlow() {
        return maxFlow;
    }

    public void setMaxFlow(int maxFlow) {
        this.maxFlow = maxFlow;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
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

    public int getRepairCost() {
        return repairCost;
    }

    public void setRepairCost(int repairCost) {
        this.repairCost = repairCost;
    }
}
