package PhysicalOperators;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.HashMap;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.AllColumns;
import net.sf.jsqlparser.statement.select.AllTableColumns;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectItemVisitor;
import net.sf.jsqlparser.schema.Table;

/**
 * Represents a Select Item Evaluator with the visitor pattern
 */
public class SelectItemEvaluator implements SelectItemVisitor {
	
	Stack<List<String>> stack;
	Tuple tuple;
	Tuple tuple2;
	HashMap<String, Table> aliasMap;
	
	/**
	* Constructor for SelectItemEvaluator class.
	* @param tuple, tuple (table row) to evaluate SelectItem on
	*/
	public SelectItemEvaluator(Tuple tuple, Tuple tuple2, HashMap<String, Table> aliasMap) {
		stack = new Stack<>();
		this.tuple = tuple;
		this.tuple2 = tuple2;
		this.aliasMap = aliasMap;
	}
	
	/**
	 * Returns stack of query output rows
	 * @return stack, stack of query output rows
	 */
	public Stack<List<String>> getStack() {
		return stack;
	}

	@Override
	public void visit(AllColumns arg0) {
		stack.push(tuple.toStringList());
	}

	@Override
	public void visit(AllTableColumns arg0) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void visit(SelectExpressionItem arg0) {
		Expression expr = arg0.getExpression();
		ExpressionEvaluator e = new ExpressionEvaluator(tuple, null, aliasMap);
		expr.accept(e);
		List<String> strList = new ArrayList<>();
		strList.add(e.getStack().pop().toString());
		stack.push(strList);
	}

}
