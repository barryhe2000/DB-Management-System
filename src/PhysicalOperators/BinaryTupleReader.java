package PhysicalOperators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import Main.Main;

public class BinaryTupleReader {
	private String fileName;
	private FileInputStream fis;
	FileChannel channel;
	final int pageSize = 4096;
	private ByteBuffer bb = ByteBuffer.allocate(pageSize);
	private int idx = 0;
	private String[] table;
	
	/**
	 * Constructor for TupleReader class.
	 * @param filename, String name of table to read
	 */
	public BinaryTupleReader(String fileName) {
		this.fileName = fileName;
		
		// creates a channel from file fileName to ByteBuffer bb
		try {
			fis = new FileInputStream(new File(Main.getTablePath().get(fileName)));
			channel = fis.getChannel();
			bb.clear();
			try {
				// reads from the ByteBuffer and stores String content in strRow
//				String strTable = "";
				List<String> strList = new ArrayList<String>();
				while(channel.read(bb) > 0) {
					int count = 8;
					bb.flip();
					while(bb.hasRemaining() && count < bb.limit()) {
						// TODO - change from bb.get() to bb.getInt(key)
						int b = bb.getInt(count);
						System.out.println(b);
						strList.add(Integer.toString(b));
						count += 4;
					}
					table = (String[])strList.toArray();
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
