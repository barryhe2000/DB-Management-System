import java.util.List;

public class SelectOperator extends Operator {

    public SelectOperator(String fileName, List<Operator> opChildren) {
        super(fileName);
        this.opChildren= opChildren;
    }

    @Override public Tuple getNextTuple() {
        for (Operator op : opChildren) {
            Tuple t= op.getNextTuple();
            //if expression then return tuple;
            //if not match do nothing and go next
        }
        return null;
    }

    @Override public void reset() {

    }

}
