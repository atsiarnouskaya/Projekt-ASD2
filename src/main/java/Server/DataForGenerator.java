package Server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DataForGenerator {
    private int seed;
    private int roadsCount;
    private int farmlandsCount;
    private int breweriesCount;
    private int tavernsCount;
}
