package FlowNetwork.Visualization;

import FlowNetwork.Network;
import FlowNetwork.Quadrant;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class VisualizationGenerator {
    public static void GenerateCytoscapeJSONfile(
            Network network,
            ArrayList<Quadrant> quadrants,
            String fileName,
            int maxFlow,
            int roadsRepairCost,
            String jsName
    ) {

        ResultData resultData = new ResultData();
        resultData.maxFlow=maxFlow;
        resultData.roadsRepairCost=roadsRepairCost;

        resultData.nodes = network.getGraph()
                .keySet()
                .stream()
                .filter(n->!n.getType().equals("source") && !n.getType().equals("sink"))
                .map(v -> {

            int incomingFlow = network
                    .getRoads()
                    .stream()
                    .filter(r -> r.getTo() == v)
                    .filter(r -> r.getFrom().getType() != "source" && r.getTo().getType() != "source")
                    .filter(r -> r.getFrom().getType() != "sink" && r.getTo().getType() != "sink")
                    .filter(r -> r.getCurrentFlow() > 0)
                    .map(x -> x.getCurrentFlow())
                    .findFirst()
                    .orElse(0);

            var vv = new VisualizationVertex();
            vv.position.x = v.getX() * 10;
            vv.position.y = v.getY() * 10;
            vv.data.id = "" + v.getLocalId();
            vv.data.type = "" + v.getType();
            vv.data.label = "" + v.getType().charAt(0);
            vv.data.productionCapacity = v.getCapacity();
            vv.data.incomingFlow=incomingFlow;
            return vv;
        }).toList();

        var edges = network.getGraph().values().stream()
                .flatMap(x -> x.values().stream())
                .filter(r -> r.getTo().getType() != "source"
                        && r.getTo().getType() != "sink"
                        && r.getFrom().getType() != "source"
                        && r.getFrom().getType() != "sink"
                )
                .filter(r -> r.getCurrentFlow() > 0
                                || (r.getCurrentFlow() == 0
                                && r.getReverseEdge().getCurrentFlow() == 0
                                && r.getFrom().getLocalId() > r.getTo().getLocalId()
                        )
                );

        resultData.edges = new ArrayList<>(edges.map(e -> {
            var ee = new VisualizationEdge();
            ee.data.label = e.getCurrentFlow() + "/" + e.getMaxFlow();
            ee.data.source = "" + e.getFrom().getLocalId();
            ee.data.target = "" + e.getTo().getLocalId();
            ee.data.currentFlow =  e.getCurrentFlow();
            ee.data.repairCost =  e.getRepairCost();
            return ee;
        }).toList());

        var pedges = quadrants
                .stream()
                .flatMap(q -> {
                    var points = q.getHull();
                    var polygonEdges = new ArrayList<VisualizationEdge>();
                    for(int i = 0; i < points.size(); i++){
                        var from = points.get(i);
                        var to = points.get((i + 1) % points.size());
                        var edge = new VisualizationEdge();
                        edge.data.label = "P";
                        edge.data.source = "" + network.getVertex((int)from.getX(),(int) from.getY()).getLocalId();
                        edge.data.target = "" + network.getVertex((int)to.getX(),(int) to.getY()).getLocalId();
                        edge.data.currentFlow = 0;
                        edge.data.repairCost = 0;
                        edge.data.type = "Polygon";
                        polygonEdges.add(edge);
                    }

                    return polygonEdges.stream();
                })
                .toList();

        resultData.edges.addAll(pedges);

        try (PrintWriter writer = new PrintWriter(new FileWriter("src/generatedGraph/" + fileName + ".js"))) {
            Gson gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .create();

            String json = gson.toJson(resultData);
            writer.write("const " + jsName + " = " + json + ";");
        } catch (IOException e) {
            System.err.println("An error occurred while saving to the file: " + e.getMessage());
        }
    }
}
