import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
* Represents a Sort Operator (for ORDER BY clauses) in the Iterator model of SQL.
*/
public class SortOperator extends ProjectOperator {
	protected Expression expression;
	protected List<Tuple> tupleList = new ArrayList<Tuple>();
	protected int row;
	/**
	* Constructor for SortOperator class.
	* @param filename, String name of table to apply operator to
	* @param sel, list of SelectItems representing columns to select
	* @param expression, WHERE clause expression
	* @param ord, ORDER BY clause expression
	*/
	public SortOperator(String fileName, List<SelectItem> sel, List<Operator> children, OrderByElement ord) {
		super(fileName, sel, children);
		Tuple nextTuple= super.getNextTuple();
		while (nextTuple != null) {
			tupleList.add(nextTuple);
            nextTuple= getNextTuple();
        }
		Expression expr = ord.getExpression();
		final boolean isAsc = ord.isAsc(); // i feel like this also shouldn't be final
		// actually maybe do ord.getExpression, but for Tuple, input a tuple of length with indices in it
		String[] ordList = new String[tupleList.get(0).getSize()];
		for(int i = 0; i < tupleList.get(0).getSize(); i++) {
			ordList[i] = String.valueOf(i);
		}
		Tuple ordTuple = new Tuple(ordList, nextTuple.getTableName());
		ExpressionEvaluator e = new ExpressionEvaluator(ordTuple, null); // probably want us to use the visitor
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
	@Override
	/**
	* Returns the next tuple (table row) in table denoted by fileName
	* and sorted according to ord
	* @return tuple, next tuple in table denoted by fileName
	* and sorted according to ord
	*/
	public Tuple getNextTuple() {
		int temp = row;
		row++;
		return tupleList.get(temp);
	}
}
