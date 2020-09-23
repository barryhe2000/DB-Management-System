import java.util.ArrayList;
import java.util.List;

public abstract class Operator {

    protected String       fileName;
    private List<Operator> opChildren= new ArrayList<>();

    public Operator(String fileName) {
        this.fileName= fileName;
    }

    public Tuple getNextTuple() {
        if (opChildren.isEmpty()) {
            return null;
        } else {
            return new Tuple(new String[0]);
            // do something to the child
        }
    }

    public abstract void reset();

    public void dump() {
        Tuple nextTuple= getNextTuple();
        while (nextTuple != null) {
            System.out.println(nextTuple.toString());
            nextTuple= getNextTuple();
        }
    }
}
