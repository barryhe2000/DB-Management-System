import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

public class Main {

    private static String               queriesFile;
    private static String               schema;
    private static Map<String, TableDB> database; //name of table to a Table object

    public static Map<String, TableDB> getDatabase() {
        return database;
    }

    private static void initDB(String inputdir) throws FileNotFoundException {
        database= new HashMap<>();
        BufferedReader in= new BufferedReader(new FileReader(schema));
        String line= null;
        try {
            while ( (line= in.readLine()) != null) {
                String[] lineArr= line.split("\\s+"); //lineArr: [Sailor, A, B, C]
                String tableName= lineArr[0]; //Sailor
                List<List<String>> tableElements= new ArrayList<>();
                List<String> columnNames= new ArrayList<>();
                for (int i= 1; i < lineArr.length; i++) {
                    columnNames.add(lineArr[i]);
                }
                tableElements.add(columnNames);

                String dataTablePath= inputdir + "/db/data/" + tableName;
                BufferedReader tableIn= new BufferedReader(new FileReader(dataTablePath));
                String strRow;
                List<Tuple> entireTable= new ArrayList<>();
                try {
                    while ( (strRow= tableIn.readLine()) != null) {
                        String[] row= strRow.split(",");
                        List<String> rowList= new ArrayList<>();
                        for (String s : row) {
                            rowList.add(s);
                        }
                        tableElements.add(rowList);
                        Tuple newRow= new Tuple(row);
                        entireTable.add(newRow);
                    }
                    database.put(tableName, new TableDB(tableName, tableElements, entireTable));
                    tableIn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        //args[0] should be inputdir and args[1] should be outputdir
        //pipe output to outputdir as files starting from query1, query2, etc.
        queriesFile= args[0] + "/queries.sql";
        schema= args[0] + "/db/schema.txt";
        initDB(args[0]);
        try {
            CCJSqlParser parser= new CCJSqlParser(new FileReader(queriesFile));
            Statement statement;
            while ( (statement= parser.Statement()) != null) {
                Select select= (Select) statement;
                PlainSelect plainSelect= (PlainSelect) select.getSelectBody();
                List<SelectItem> sel= plainSelect.getSelectItems();
                Expression exp= plainSelect.getWhere();
                Table tableName= (Table) plainSelect.getFromItem();
                if (sel.size() == 1 && sel.get(0).toString().equals("*")) { //fix if * is not the only selectItem
                    ScanOperator scan= new ScanOperator(tableName.toString());
                    scan.dump();
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }
}
