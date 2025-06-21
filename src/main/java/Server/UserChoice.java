package Server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserChoice {
    private String choice;
    private int type; //1 - word in the text, 2 - word in logs, 3 - word in own file
}
