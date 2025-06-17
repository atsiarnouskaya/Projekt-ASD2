package Server;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.File;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWordToSearch {
    private String word;
    private String text;
    private File file;
    private String algorithmChoice; //1 - kmp, 2 - bm, 3 - both
    private int type; //1 - word in the text, 2 - word in logs, 3 - word in own file

}
