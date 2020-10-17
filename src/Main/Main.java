package Main;
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

import LogicalOperators.LogicalOperator;
import LogicalOperators.LogicalQueryPlan;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;

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
            	LogicalQueryPlan lqp = new LogicalQueryPlan(statement, queryNum);
            	lqp.makeLogicalQueryPlan(outputPath);
            	LogicalOperator lo = lqp.makePlan(outputPath);
            	PhysicalPlanBuilder visitor = new PhysicalPlanBuilder(outputPath, queryNum);
            	lo.accept(visitor);
            	// idk why subtracting by 1 works here lol
            	queryNum = lqp.getQueryNum() - 1;
            	
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
    }
}