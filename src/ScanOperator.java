import java.util.ArrayList;
import java.util.List;

public class ScanOperator extends Operator {

    private List<Operator> opChildren= new ArrayList<>();
    private TableDB        table;
    private int            row;

    public ScanOperator(String fileName) {
        super(fileName);
        table= Main.getDatabase().get(fileName);
        row= 0;
    }

    @Override public Tuple getNextTuple() {
        if (row < table.getRows().size()) {
            int temp= row;
            row++;
            return table.getRows().get(temp);
        }
        return null;
    }

    @Override public void reset() {
        row= 0;
    }
}