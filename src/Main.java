import java.io.FileReader;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;

public class Main {

    private static String queriesFile;

    public static void main(String[] args) {
        //args[0] should be inputdir and args[1] should be outputdir
        //pipe output to outputdir as files starting from query1, query2, etc.
        queriesFile= args[0] + "/queries.sql";

        try {
            CCJSqlParser parser= new CCJSqlParser(new FileReader(queriesFile));
            Statement statement;
            while ( (statement= parser.Statement()) != null) {
                Select select= (Select) statement;
                PlainSelect plainSelect= (PlainSelect) select.getSelectBody();
                List<Select> sel= plainSelect.getSelectItems();
                System.out.println(plainSelect.getJoins());
                Expression exp= plainSelect.getWhere();
                FromItem table= plainSelect.getFromItem();
                System.out.println(plainSelect.getFromItem());
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }

    }

}
