import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableDB {

    private String                    name;
    private Map<String, List<String>> columns= new HashMap<>(); //maps column name to column elements
    private List<Tuple>               rows;

    TableDB(String name, List<List<String>> tableElements, List<Tuple> rows) {
        this.name= name;
        for (int i= 0; i < tableElements.size(); i++) {
            List<String> row= tableElements.get(i);
            for (int j= 0; j < row.size(); j++) {
                if (i == 0)
                    columns.put(row.get(j), new ArrayList<String>());
                else {
                    String columnTitle= tableElements.get(0).get(j);
                    List<String> currColumn= columns.get(columnTitle);
                    currColumn.add(row.get(j));
                    columns.put(columnTitle, currColumn);
                }
            }
        }
        this.rows= rows;
    }

    String getName() {
        return name;
    }

    Map<String, List<String>> getColumns() {
        return columns;
    }

    List<Tuple> getRows() {
        return rows;
    }
}
