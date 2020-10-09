import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;

import java.io.IOException;
import java.util.HashMap;

public class SelectOperator extends ScanOperator {
	
	protected Expression expression;
	protected HashMap<String, Table> aliasMap;

    public SelectOperator(String fileName, Expression expression, HashMap<String, Table> aliasMap) {
        super(fileName);
        this.expression = expression;
        this.aliasMap = aliasMap;
    }

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