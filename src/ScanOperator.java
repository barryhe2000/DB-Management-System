public class ScanOperator extends Operator {

	private TupleReader tr = new TupleReader(fileName);

    public ScanOperator(String fileName) {
        super(fileName);
        reset(fileName);
    }

    /**
     * Returns the next tuple (table row) 
     * from table denoted by fileName.
     * @return tuple, next tuple in table
     */
    @Override public Tuple getNextTuple() {
    	return tr.readNextTuple();
    }

    /**
     * Resets class instance such that getNextTuple() starts back at the table head.
     */
    @Override public void reset(String file) {
    	tr = new TupleReader(fileName);
    }
}