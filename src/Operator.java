import java.io.FileWriter;

/**
* Represents an Operator in the Iterator model of SQL.
*/
public abstract class Operator {

    protected String fileName;
    
    /**
	* Constructor for Operator class.
	* @param filename, String name of table to apply operator to
	*/
    public Operator(String fileName) {
        this.fileName= fileName;
    }
    
    /**
    * Conditionally returns the next tuple (table row) 
    * from table denoted by fileName.
    * @return tuple, next tuple in table that meets operator conditions
    */
    public abstract Tuple getNextTuple();
    
    /**
    * Resets class instance such that getNextTuple() starts back at the table head.
    */
    public abstract void reset(String file);
    
    /**
    * Writes all tuples that meet operator conditions in order to file with path outputName.
    * @param outputName, String path of file to write to
    */
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