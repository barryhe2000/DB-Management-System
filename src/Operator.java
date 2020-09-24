import java.io.FileWriter;

public abstract class Operator {

    protected String fileName;

    public Operator(String fileName) {
        this.fileName= fileName;
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