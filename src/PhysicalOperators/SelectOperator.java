package PhysicalOperators;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import java.util.HashMap;

/**
 * Represents a Select Operator (for WHERE) in the Iterator model of SQL.
 */
public class SelectOperator extends ScanOperator {
	
	protected Expression expression;
	protected HashMap<String, Table> aliasMap;

	/**
	 * Constructor for SelectOperator class.
	 * @param filename, String name of table to apply operator to
	 * @param expression, WHERE clause expression
	 * @param aliasMap, map of aliases to tables
	 */
    public SelectOperator(String fileName, Expression expression, HashMap<String, Table> aliasMap) {
        super(fileName);
        this.expression = expression;
        this.aliasMap = aliasMap;
    }
    
    /**
	 * Returns the next tuple (table row) in table denoted by fileName
	 * filtered by expression
	 * @return tuple, next tuple in table denoted by fileName
	 * filtered by expression
	 */
    @Override public Tuple getNextTuple() {
    	Tuple t = super.getNextTuple();
    	if (expression == null) return t;
    	while (t != null) {
    		ExpressionEvaluator e = new ExpressionEvaluator(t, null, aliasMap);
    		expression.accept(e);
    		if (e.getStack().pop().equals(true)) return t;	
    		t = super.getNextTuple();
    	}
        return null;
    }
}