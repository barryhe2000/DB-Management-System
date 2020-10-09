import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ScanOperator extends Operator {
	
    private List<String> lines;
	private int filePointer;
	private Path nioPath;
	private Charset charset = Charset.forName("UTF-8");

    public ScanOperator(String fileName) {
        super(fileName);
        reset(fileName);
    }

    @Override public Tuple getNextTuple() {
    	String strRow;
    	try {
    		if (filePointer < lines.size() && (strRow=lines.get(filePointer)) != null) {
    			filePointer++;
    			return new Tuple(strRow.split(","), fileName);
    		}
    		filePointer++;
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
    }

    @Override public void reset(String file) {
    	filePointer = 0;
    	nioPath = Paths.get(Main.getTablePath().get(file));
		try {
			this.lines = Files.readAllLines(nioPath, charset);
		} catch(Exception e) {
			System.out.println("Couldn't reset scan operator.");
			e.printStackTrace();
		}
    }
}