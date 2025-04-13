package SiecPrzeplywowa;

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
        return "Vertex{" +
                "localId=" + localId +
                ", x=" + x +
                ", y=" + y +
                '}';
    }
}
