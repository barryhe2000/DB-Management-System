import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Represents a NIO object that reads tuples from a table
 */
public class TupleReader {
	private String fileName;
	private FileInputStream fis;
	FileChannel channel;
	private ByteBuffer bb = ByteBuffer.allocate(4096);
	private int idx = 0;
	private String[] table;
	
	/**
	 * Constructor for TupleReader class.
	 * @param filename, String name of table to read
	 */
	public TupleReader(String fileName) {
		this.fileName = fileName;
		
		// creates a channel from file fileName to ByteBuffer bb
		try {
			fis = new FileInputStream(new File(Main.getTablePath().get(fileName)));
			channel = fis.getChannel();
			bb.clear();
			try {
				// reads from the ByteBuffer and stores String content in strRow
				String strTable = "";
				while(channel.read(bb) > 0) {
					bb.flip();
					while(bb.hasRemaining()) {
						// TODO - change from bb.get() to bb.getInt(key)
						strTable += (char)bb.get();
					}
					table = strTable.split("(\r\n|\r|\n)");
				}
			} catch (IOException e) {
				System.out.println("readNextTuple IOException");
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			System.out.println("readNextTuple FileNotFoundException");
			e.printStackTrace();
		}
	}
	
	/**
     * Reads the next tuple in the query output
     * from table denoted by fileName.
     * @return tuple, next tuple in table
     */
	public Tuple readNextTuple() {
		if(idx < table.length) {
			String[] row = table[idx].split(",");
			idx++;
			return new Tuple(row, fileName);
		}
		return null;
	}
	
	
}
