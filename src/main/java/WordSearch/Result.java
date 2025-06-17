package WordSearch;

public class Result {
    private String BMResult;
    private String KMPResult;

    public Result() {
        BMResult = "";
        KMPResult = "";
    }
    public void addBMResult(String result) {
        BMResult += result;
        BMResult += "\n";
    }

    public void addKMPResult(String result) {
        KMPResult += result;
        KMPResult += "\n";
    }

    public String getResult(String option) {
        if (option.equals("1")) {
            return "[KMP]\n" + KMPResult;
        } else if (option.equals("2")) {
            return "[BM]\n" + BMResult;
        }
        return "[KMP and BM]\n" + KMPResult + BMResult;
    }
}
