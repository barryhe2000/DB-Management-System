package PhysicalOperators;
import static org.junit.Assert.assertEquals;

import java.io.FileReader;
import java.util.List;
import java.util.Stack;

import org.junit.Test;

import Main.Main;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.parser.CCJSqlParser;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectItem;

public class ExpressionEvaluatorTest {

	@Test
	public void test() {
		try {
			Main.main(new String[] {"inputdir", "outputdir"});
            CCJSqlParser parser= new CCJSqlParser(new FileReader(Main.getQueriesFile()));
            Statement statement;
            while ((statement= parser.Statement()) != null) {
                Select select= (Select) statement;
                PlainSelect plainSelect= (PlainSelect) select.getSelectBody();
                List<SelectItem> sel= plainSelect.getSelectItems();
                Expression exp= plainSelect.getWhere();
                Table tableName= (Table) plainSelect.getFromItem();
            }
        } catch (Exception e) {
            System.err.println("Exception occurred during parsing");
            e.printStackTrace();
        }
	}

}
