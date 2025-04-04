package SiecPrzeplywowa;

public class Node {
    private int maxFlow;
    private int currentFlow;
    private int residualFlow;

    public Node() {
    }

    public Node(int maxFlow, int currentFlow) {
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
}
