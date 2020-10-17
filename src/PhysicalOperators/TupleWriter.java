package PhysicalOperators;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import Main.Main;

public class TupleWriter {
	private String fileName;
	private FileOutputStream fos;
	private FileChannel channel;
	private ByteBuffer bb = ByteBuffer.allocate(4096);
	private int idx = 0;
	private String[] table;
	
	public TupleWriter(String fileName) {
		this.fileName = fileName;
		try {
			fos = new FileOutputStream(new File(Main.getTablePath().get(fileName)));
			channel = fos.getChannel();
			bb.clear();
			try {
				// reads from the ByteBuffer and stores String content in strRow
				String strTable = "";
				while(channel.write(bb) > 0) {
					bb.flip();
//					while(bb.hasRemaining()) {
//						// need to change to getInt()
//						strTable += (char)bb.get();
//					}
//					table = strTable.split("(\r\n|\r|\n)");
				}
			} catch (IOException e) {
				System.out.println("readNextTuple IOException");
				e.printStackTrace();
			}
		} catch (Exception e) {
			
		}
	}
	
	
	public void writeNextTuple() {
//		Path outputPath = Paths.get(outputName);
//		Tuple nextTuple= getNextTuple();
//		Charset charset = Charset.forName("UTF-8");
//		String outputStr = "";
//    	try {
//    		while (nextTuple != null) {
//    			outputStr += nextTuple.toString() + "\n";
//                nextTuple= getNextTuple();
//            }
//    		Files.write(outputPath, outputStr.getBytes());
//    	} catch (Exception e) {
//    		System.err.println("Exception occurred during output dump");
//    		e.printStackTrace();
//    	}
	}

}
