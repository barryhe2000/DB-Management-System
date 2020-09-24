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

    private static String queriesFile; //path to queries.sql
    private static String schema; //path to schema.txt
    private static Map<String, String> tablePath; //name of table to path of table
    private static Map<String, List<String>> tableHeaders; //name of table to column names of table
    private static int queryNum; //number of query output
    
    public static String getQueriesFile() {
    	return queriesFile;
    }
    
    public static String getSchema() {
    	return schema;
    }
    
    public static Map<String, String> getTablePath() {
    	return tablePath;
    }
    
    public static Map<String, List<String>> getTableHeaders() {
    	return tableHeaders;
    }
    
    /*
     * Initialize database.
     */
    private static void initDB(String inputdir) throws FileNotFoundException {
        tablePath= new HashMap<>();
        tableHeaders= new HashMap<>();
        queryNum= 1;
        BufferedReader in= new BufferedReader(new FileReader(schema));
        String line= null;
        try {
            while ((line= in.readLine()) != null) {
                String[] lineArr= line.split("\\s+");
                String tableName= lineArr[0]; 
                List<String> columnNames= new ArrayList<>();
                for (int i= 1; i < lineArr.length; i++) {
                    columnNames.add(lineArr[i]);
                }
                tableHeaders.put(tableName, columnNames);
                tablePath.put(tableName, inputdir + "/db/data/" + tableName);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    //args[0] should be inputdir and args[1] should be outputdir
    //pipe output to outputdir as files starting from query1, query2, etc.
    public static void main(String[] args) throws FileNotFoundException {
        queriesFile= args[0] + "/queries.sql";
        schema= args[0] + "/db/schema.txt";
        initDB(args[0]);
        try {
            CCJSqlParser parser= new CCJSqlParser(new FileReader(queriesFile));
            Statement statement;
            while ((statement= parser.Statement()) != null) {
                Select select= (Select) statement;
                PlainSelect plainSelect= (PlainSelect) select.getSelectBody();
                List<SelectItem> sel= plainSelect.getSelectItems();
                Expression exp= plainSelect.getWhere();
                Table tableName= (Table) plainSelect.getFromItem();
                //super important: make sure to only create one operator per query
                //cannot have multiple operators created for the sake of parsing one query
                //so incorporate some logic below to choose which operator to create after
                //parsing the query
                if (sel.size() > 1 || sel.size() == 1 && !sel.get(0).toString().equals("*")) {
                	ProjectOperator project = new ProjectOperator(tableName.toString(), sel, exp);
                	project.dump(args[1] + "/query" + queryNum);
                	queryNum++;
                } else if (exp != null) {
                	SelectOperator so= new SelectOperator(tableName.toString(), exp);
                	so.dump(args[1] + "/query" + queryNum);
                	queryNum++;
                } else if (sel.size() == 1 && sel.get(0).toString().equals("*")) {
                	ScanOperator scan= new ScanOperator(tableName.toString());
                    scan.dump(args[1] + "/query" + queryNum);
                    queryNum++;
                }
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }
}