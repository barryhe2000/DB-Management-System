import java.io.BufferedReader;
import java.io.FileReader;

public class ScanOperator extends Operator {
	
    private BufferedReader in;

    public ScanOperator(String fileName) {
        super(fileName);
        reset(fileName);
    }

    @Override public Tuple getNextTuple() {
    	String strRow;
    	try {
    	    if ((strRow=in.readLine()) != null) return new Tuple(strRow.split(","), fileName);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
    }

    @Override public void reset(String file) {
        try {
        	in= new BufferedReader(new FileReader(Main.getTablePath().get(file)));
        } catch (Exception e) {
        	e.printStackTrace();
        } 
    }
}