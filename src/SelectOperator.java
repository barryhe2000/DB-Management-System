import net.sf.jsqlparser.expression.Expression;

public class SelectOperator extends ScanOperator {
	
	protected Expression expression;

    public SelectOperator(String fileName, Expression expression) {
        super(fileName);
        this.expression = expression;
    }

    @Override public Tuple getNextTuple() {
    	Tuple t = super.getNextTuple();
    	if (expression == null) return t;
    	while (t != null) {
    		ExpressionEvaluator e = new ExpressionEvaluator(t);
    		expression.accept(e);
    		if (e.getStack().pop().equals(true)) return t;	
    		t = super.getNextTuple();
    	}
        return null;
    }
}