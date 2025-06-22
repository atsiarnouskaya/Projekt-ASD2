package FlowNetwork;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Edge {
    private int maxFlow;
    private int currentFlow;
    private int residualFlow;
    private int repairCost;
    private Vertex from;
    private Vertex to;
    private Edge reverseEdge;

    public Edge(Vertex from, Vertex to, int maxFlow, int repairCost) {
        this.from = from;
        this.to = to;
        this.residualFlow = maxFlow;
        this.currentFlow = 0;
        this.maxFlow = maxFlow;
        this.repairCost = repairCost;
    }
    @Override
    public String toString() {
        return ("edge (from " + from + " to " + to + ". Properties: " +
                "maxFlow = " + maxFlow +
                ", currentFlow = " + currentFlow +
                ", residualFlow = " + residualFlow +
                ", repairCost = " + repairCost +
                ')');
    }
}
