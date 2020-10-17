package PhysicalOperators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.schema.Table;

/**
* Represents a Sort Operator (for ORDER BY clauses) in the Iterator model of SQL.
*/
public class SortOperator extends ProjectOperator {
	protected Expression expression;
	protected List<Tuple> tupleList = new ArrayList<Tuple>();
	protected int row;
	protected HashMap<String, Table> aliasMap;
	
	/**
	 * Constructor for SortOperator class.
	 * @param filename, String name of table to apply operator to
	 * @param sel, list of SelectItems representing columns to select
	 * @param children, child operators of the operator
	 * @param aliasMap, map of aliases to tables
	 * @param ord, ORDER BY clause expression
	 */
	public SortOperator(String fileName, List<SelectItem> sel, List<Operator> children, HashMap<String, Table> aliasMap, OrderByElement ord) {
		super(fileName, sel, children, aliasMap);
		Tuple nextTuple= super.getNextTuple();
		while (nextTuple != null) {
			tupleList.add(nextTuple);
            nextTuple= getNextTuple();
        }
		Expression expr = ord.getExpression();
		
		// TODO - Find out if there is a way to implement isAsc without the final keyword
		
		final boolean isAsc = ord.isAsc(); 
		
		// TODO - for Tuple, input a tuple of length with indices in it and use ord.getExpression()
		
		String[] ordList = new String[tupleList.get(0).getSize()];
		for(int i = 0; i < tupleList.get(0).getSize(); i++) {
			ordList[i] = String.valueOf(i);
		}
		Tuple ordTuple = new Tuple(ordList, nextTuple.getTableName());
		ExpressionEvaluator e = new ExpressionEvaluator(ordTuple, null, aliasMap); // probably want us to use the visitor
		expr.accept(e);
		final int colIndex = Integer.parseInt(e.getStack().pop().toString()); // i feel like this shouldn't be final
		Comparator<Tuple> tupleComparator = new Comparator<Tuple>() {
			public int compare(Tuple t1, Tuple t2) {
				int val = (Integer.parseInt(t1.getRowElement(colIndex))) - (Integer.parseInt(t2.getRowElement(colIndex)));
                if(val == 0){
                    return val;
                }
                if(isAsc) {
                	return val > 0 ? 1 : -1;
                } else {
                	return val < 0 ? 1 : -1;
                }
			}
		};
		Collections.sort(tupleList, tupleComparator);
		row = 0;
	}
	
	/**
	 * Returns the next tuple (table row) in table denoted by fileName
	 * and sorted according to ord
	 * @return tuple, next tuple in table denoted by fileName
	 * and sorted according to ord
	 */
	@Override public Tuple getNextTuple() {
		int temp = row;
		row++;
		return tupleList.get(temp);
	}
}
