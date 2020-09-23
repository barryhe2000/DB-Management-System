import java.util.ArrayList;
import java.util.List;

public abstract class Operator {

    protected String         fileName;
    protected List<Operator> opChildren= new ArrayList<>();

    public Operator(String fileName) {
        this.fileName= fileName;
    }

    public abstract Tuple getNextTuple();

    public abstract void reset();

    public void dump() {
        Tuple nextTuple= getNextTuple();
        while (nextTuple != null) {
            System.out.println(nextTuple.toString());
            nextTuple= getNextTuple();
        }
    }
}
