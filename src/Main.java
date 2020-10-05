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
import net.sf.jsqlparser.statement.select.Distinct;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

public class Main {

    private static String queriesFile; //path to queries.sql
    private static String schema; //path to schema.txt
    private static Map<String, String> tablePath; //name of table to path of table
    private static Map<String, List<String>> tableHeaders; //name of table to column names of table
    private static int queryNum; //number of query output
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
    
    /**
    * Main method of main class. Parses and interprets SQL commands.
    * @param args, command line arguments
    */
    //args[0] should be inputdir and args[1] should be outputdir
    //pipe output to outputdir as files starting from query1, query2, etc.
    public static void main(String[] args) throws FileNotFoundException {
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
                // added alias, need to map to table name, need to do for joins too...
                // does this work for for tableName given that it's casted?
                // alias works now, but needs to work for columns
                String alias = tableName.getAlias();
//                System.out.println("alias is " + alias);
                HashMap <String, Table> aliasMap = new HashMap<String, Table>();
                String fileName = tableName.getName();
//                System.out.println("file name before is " + fileName);
                if(aliasMap.containsKey(fileName)) {
                	fileName = aliasMap.get(fileName).toString();
                }
//                System.out.println("file name after is " + fileName);
                
                if(alias != null && !alias.isEmpty()) {
                	aliasMap.put(alias,  tableName);
                }
                List<Join> joinList = plainSelect.getJoins();
                List<OrderByElement> orderElements = plainSelect.getOrderByElements();
                Distinct d = plainSelect.getDistinct();
                if (d != null) {
                	List<Operator> lst = new ArrayList<>();
                	lst.add(new SelectOperator(fileName, exp, aliasMap));
                	DuplicateEliminationOperator de = new DuplicateEliminationOperator(fileName, sel, lst, aliasMap, orderElements.get(0));
                	de.dump(outputPath + queryNum);
                	queryNum++;
                } else if (orderElements != null) {
                	List<Operator> lst = new ArrayList<>();
                	lst.add(new SelectOperator(fileName, exp, aliasMap));
                	SortOperator so = new SortOperator(fileName, sel, lst, aliasMap, orderElements.get(0));
                	so.dump(outputPath + queryNum);
                	queryNum++;
                } else if (joinList != null) {
                	List<FromItem> joining = new ArrayList<>();
                	for (Join j : joinList) {
                		joining.add(j.getRightItem());
                	}
                	JoinOperator jo = new JoinOperator(fileName, joining, exp);
                	jo.dump(outputPath + queryNum);
                	queryNum++;
                } else if (sel.size() > 1 || sel.size() == 1 && !sel.get(0).toString().equals("*")) {
                	List<Operator> lst = new ArrayList<>();
                	lst.add(new SelectOperator(fileName, exp, aliasMap));
                	ProjectOperator project = new ProjectOperator(fileName, sel, lst, aliasMap);
                	project.dump(outputPath + queryNum);
                	queryNum++;
                } else if (exp != null) {
                	SelectOperator so= new SelectOperator(fileName, exp, aliasMap);
                	so.dump(outputPath + queryNum);
                	queryNum++;
                } else if (sel.size() == 1 && sel.get(0).toString().equals("*")) {
                	ScanOperator scan= new ScanOperator(fileName);
                    scan.dump(outputPath + queryNum);
                    queryNum++;
                }
                // clear aliasMap after each query
                aliasMap.clear();
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }
}