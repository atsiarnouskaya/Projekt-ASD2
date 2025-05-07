package SiecPrzeplywowa;

import java.util.Objects;

public class Vertex {
    static int count = 0;
    private int localId;
    private int x;
    private int y;
    private String type;
    private int capacity;
    private int gottenFlow;


    public Vertex(int x, int y) {
        this.x = x;
        this.y = y;
        this.gottenFlow = 0;
        this.localId = count++;
    }

    public Vertex(int x, int y, String type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.gottenFlow = 0;
        this.localId = count++;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getLocalId() {
        return localId;
    }

    public int getGottenFlow() {
        return gottenFlow;
    }

    public void setGottenFlow(int gottenFlow) {
        this.gottenFlow = gottenFlow;
    }

    public void addGottenFlow(int n) {
        this.gottenFlow += n;
    }

    public void changeCapacity() {
        this.capacity = this.gottenFlow;
        this.gottenFlow = 0;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + "), type = " + type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return localId == vertex.localId && x == vertex.x && y == vertex.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(localId, x, y);
    }
}