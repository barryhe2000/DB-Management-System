import java.util.List;
import net.sf.jsqlparser.expression.Expression;

public class SelectOperator extends Operator {
	Expression whereItem;

    public SelectOperator(String fileName, List<Operator> opChildren, Expression whereItem) {
        super(fileName);
        this.opChildren= opChildren;
        this.whereItem = whereItem;
    }
    
    boolean evaluate(Expression expr, Tuple tup) {
    	ExpressionEvaluator exprEval = new ExpressionEvaluator(tup);
    	expr.accept(exprEval);
    	
//    	System.out.println("Stack is " + exprEval.getStack().toString());
    	boolean result = (boolean)exprEval.getStack().pop();
    	
//    	System.out.println(result);
    	return result;
    }

    @Override public Tuple getNextTuple() {
        for (Operator op : opChildren) {
            Tuple t= op.getNextTuple();
            if(t != null && whereItem != null && evaluate(whereItem, t)) {
            	return t;
            } else if (t != null) {
            	op.getNextTuple();
            } else {
            	return null;
            }
        }
        return null;
    }

    @Override public void reset() {

    }

}