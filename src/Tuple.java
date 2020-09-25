import java.util.Arrays;
import java.util.List;

public class Tuple {
    private String[] row;
    private int size;
    private String tableName;

    public Tuple(String[] row, String tableName) {
        this.row= row;
        size= row.length;
        this.tableName= tableName;
    }

    public String getRowElement(int index) {
        assert index >= 0 && index < size;
        return row[index];
    }

    public int getSize() {
        return size;
    }
    
    public String getTableName() {
    	return tableName;
    }
    
    public List<String> toStringList() {
    	return Arrays.asList(row);
    }
    
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

    @Override public String toString() {
        String result= "";
        for (int i= 0; i < row.length; i++) {
            result += row[i];
            if (i < row.length - 1) result += ",";
        }
        return result;
    }
}