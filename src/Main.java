import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
 * Represents the Main class for the Database Management System.
 */
public class Main {

	/** Path to queries.sql file */
    private static String queriesFile;
    /** Path to schema.txt file */
    private static String schema;
    /** Map of table names to table paths */
    private static Map<String, String> tablePath;
    /** Map from table names to column names of table */
    private static Map<String, List<String>> tableHeaders;
    /** Index of query output */
    private static int queryNum; //number of query output
    /** Path to outputdir directory */
    private static String outputPath;
    
    /**
     * Returns the path to the "queries.sql" file. 
     * @return queriesFile, the String path to the "queries.sql" file.
     */
    public static String getQueriesFile() {
    	return queriesFile;
    }
    
    /**
     * Returns the path to the "schema.txt" file. 
     * @return schema, the String path to the "schema.txt" file.
     */
    public static String getSchema() {
    	return schema;
    }
    
    /**
     * Returns map of table names to the paths of the table. 
     * @return tablePath, a Map that maps String table names to the String paths of the table.
     */
    public static Map<String, String> getTablePath() {
    	return tablePath;
    }
    
    /**
     * Returns map of table names to the paths of the table. 
     * @return tableHeaders, maps table name to column headers
     */
    public static Map<String, List<String>> getTableHeaders() {
    	return tableHeaders;
    }
    
    /**
     * Initialize database to track schema and table headers.
     * @param inputdir, path to input directory
     * @throws IOException
     */    
    private static void initDB(String inputdir) throws IOException {
    	tablePath= new HashMap<>();
        tableHeaders= new HashMap<>();
        queryNum= 1;
        
        // TODO - Use NIO ByteBuffer instead of NIO Path
        
        Path dbPath = Paths.get(schema);
        Charset charset = Charset.forName("UTF-8");
        try {
        	List<String> lines = Files.readAllLines(dbPath, charset);
            for (String line : lines) {
	        	String[] lineArr= line.split("\\s+");
	            String tableName= lineArr[0]; 
	            List<String> columnNames= new ArrayList<>();
	            for (int i= 1; i < lineArr.length; i++) {
	                columnNames.add(lineArr[i]);
	            }
	            tableHeaders.put(tableName, columnNames);
	            tablePath.put(tableName, inputdir + "/db/data/" + tableName);
            }
            
        } catch (Exception e) {
        	e.printStackTrace();
        }    
    }
    
    /**
     * Main method of main class. Parses and interprets SQL commands.
     * @param args, command line arguments where args[0] is the inputdir and args[1] is the outputdir
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        queriesFile= args[0] + "/queries.sql";
        schema= args[0] + "/db/schema.txt";
        outputPath = args[1] + "/query";
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
                
                // TODO - Add Alias for JoinOperator, Columns
                
                // gets and maps aliases to table names
                String alias = tableName.getAlias();
                HashMap <String, Table> aliasMap = new HashMap<String, Table>();
                String fileName = tableName.getName();
                if(aliasMap.containsKey(fileName)) {
                	fileName = aliasMap.get(fileName).toString();
                }
                if(alias != null && !alias.isEmpty()) {
                	aliasMap.put(alias,  tableName);
                }
                
                // gets list of JOIN conditions, ORDER BY sorting information, and DISTINCT status
                List<Join> joinList = plainSelect.getJoins();
                List<OrderByElement> orderElements = plainSelect.getOrderByElements();
                Distinct d = plainSelect.getDistinct();
                
                // if the query has the DISTINCT keyword
                if (d != null) {
                	List<Operator> lst = new ArrayList<>();
                	lst.add(new SelectOperator(fileName, exp, aliasMap));
                	DuplicateEliminationOperator de = new DuplicateEliminationOperator(fileName, sel, lst, aliasMap, orderElements.get(0));
                	de.dump(outputPath + queryNum);
                	queryNum++;
                } 
                // if the query has the ORDER BY clause
                else if (orderElements != null) {
                	List<Operator> lst = new ArrayList<>();
                	lst.add(new SelectOperator(fileName, exp, aliasMap));
                	SortOperator so = new SortOperator(fileName, sel, lst, aliasMap, orderElements.get(0));
                	so.dump(outputPath + queryNum);
                	queryNum++;
                } 
                // if the query has the JOIN clause
                else if (joinList != null) {
                	List<FromItem> joining = new ArrayList<>();
                	for (Join j : joinList) {
                		joining.add(j.getRightItem());
                	}
                	JoinOperator jo = new JoinOperator(fileName, joining, exp);
                	jo.dump(outputPath + queryNum);
                	queryNum++;
                } 
                // if the query selects specific columns
                else if (sel.size() > 1 || sel.size() == 1 && !sel.get(0).toString().equals("*")) {
                	List<Operator> lst = new ArrayList<>();
                	lst.add(new SelectOperator(fileName, exp, aliasMap));
                	ProjectOperator project = new ProjectOperator(fileName, sel, lst, aliasMap);
                	project.dump(outputPath + queryNum);
                	queryNum++;
                }
                // if the query has WHERE conditions
                else if (exp != null) {
                	SelectOperator so= new SelectOperator(fileName, exp, aliasMap);
                	so.dump(outputPath + queryNum);
                	queryNum++;
                }
                // if the query is just a full-table scan
                else if (sel.size() == 1 && sel.get(0).toString().equals("*")) {
                	ScanOperator scan= new ScanOperator(fileName);
                    scan.dump(outputPath + queryNum);
                    queryNum++;
                }
                // clears aliasMap after each query
                aliasMap.clear();
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }
}