public class Tuple {
    private String[] row;
    private int size;

    public Tuple(String[] row) {
        this.row= row;
        size= row.length;
    }

    public String getRowElement(int index) {
        assert index > 0 && index < size;
        return row[index];
    }

    public int getSize() {
        return size;
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