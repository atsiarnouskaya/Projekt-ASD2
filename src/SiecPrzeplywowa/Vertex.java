package SiecPrzeplywowa;

import java.util.Objects;

public class Vertex {
    private static int idCounter = 0;

    private int localId;
    private int x;
    private int y;


    public Vertex(int x, int y) {
        this.localId = idCounter++;
        this.x = x;
        this.y = y;
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

    public int getLocalId() {
        return localId;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ") via ";
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