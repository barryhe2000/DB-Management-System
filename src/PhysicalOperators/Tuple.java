package PhysicalOperators;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a tuple (table row)
 */
public class Tuple {
    private String[] row;
    private int size;
    private String tableName;

    /**
	 * Constructor for Tuple class.
	 * @param row, String contents of row
	 * @param tableName, String table name
	 */
    public Tuple(String[] row, String tableName) {
        this.row= row;
        size= row.length;
        this.tableName= tableName;
    }
    
    /**
	 * Returns the row element at index [index]
	 * @param index, the index to get the row element from
	 * @return rowElement, the element of the row at index [index]
	 */
    public String getRowElement(int index) {
        assert index >= 0 && index < size;
        return row[index];
    }
    
    /**
	 * Returns the size of the tuple
	 * @return size, the number of elements in the tuple
	 */
    public int getSize() {
        return size;
    }
    
    /**
	 * Returns the name of the table the tuple is from
	 * @return tableName, the name of the table the tuple is from
	 */
    public String getTableName() {
    	return tableName;
    }
    
    /**
	 * Returns a string list representation of the tuple
	 * @return tupleStrList, the string list representation of the tuple
	 */
    public List<String> toStringList() {
    	return Arrays.asList(row);
    }
    
    /**
	 * Combines tuple [t] with this tuple
	 * @return combined, the combined tuple
	 */
    public Tuple combine(Tuple t) {
    	String[] tup = new String[size + t.getSize()];
    	for(int i = 0; i < size; i++) {
    		tup[i] = row[i];
    	}
    	for(int i = size; i < size + t.getSize(); i++) {
    		tup[i] = t.getRowElement(i-size);
    	}
    	return new Tuple(tup, tableName + " " + t.getTableName());
    }
    
    /**
	 * Returns a string representation of the tuple
	 * @return tupleStr, the string representation of the tuple
	 */
    @Override public String toString() {
        String result= "";
        for (int i= 0; i < row.length; i++) {
            result += row[i];
            if (i < row.length - 1) result += ",";
        }
        return result;
    }
}