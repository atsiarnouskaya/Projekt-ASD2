package SiecPrzeplywowa;

public class Main {
    public static void main(String[] args) {
        Siec S = new Siec(4);
        S.addNode(10, 0, 1);
        S.addNode(8, 0, 2);
        S.addNode(5, 1, 3);
        S.addNode(8, 2, 3);
        S.printGraph();
        System.out.println();
        System.out.println(S.maxFlow(0, 3));

        //System.out.println(S.getData(2, 1).getCurrentFlow() + " " + S.getData(2, 1).getMaxFlow());
    }
}