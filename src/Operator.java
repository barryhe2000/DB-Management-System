import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class Operator {

    protected String fileName;
    protected List<Operator> opChildren;

    public Operator(String fileName) {
        this.fileName= fileName;
        opChildren = new ArrayList<>();
    }

    public abstract Tuple getNextTuple();

    public abstract void reset();

    public void dump(String outputName) {
    	try {
    		FileWriter myWriter = new FileWriter(outputName);
    		Tuple nextTuple= getNextTuple();
    		while (nextTuple != null) {
    			myWriter.write(nextTuple.toString() + "\n");
                nextTuple= getNextTuple();
            }
    		myWriter.close();
    	} catch (Exception e) {
    		System.err.println("Exception occurred during output dump");
    		e.printStackTrace();
    	}
    }
}