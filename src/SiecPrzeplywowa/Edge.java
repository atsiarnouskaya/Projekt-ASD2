package SiecPrzeplywowa;

public class Edge {
    private int maxFlow;
    private int currentFlow;
    private int residualFlow;

    public Edge() {
    }

    public Edge(int maxFlow, int currentFlow) {
        this.maxFlow = maxFlow;
        this.currentFlow = currentFlow;
        this.residualFlow = 0;
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
}
