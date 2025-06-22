package FlowNetwork;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
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
        return x == vertex.x && y == vertex.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}