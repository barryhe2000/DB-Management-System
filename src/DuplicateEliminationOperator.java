import java.util.HashSet;
import java.util.List;
import java.util.HashMap;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.schema.Table;
/**
* Represents a Duplicate Elimination Operator (for DISTINCT) in the Iterator model of SQL.
*/
public class DuplicateEliminationOperator extends SortOperator {
	protected HashSet<Tuple> tupleSet = new HashSet<Tuple>();
	protected HashMap<String, Table> aliasMap;
	/**
	* Constructor for DuplicateEliminationOperator class.
	* @param filename, String name of table to apply operator to
	* @param sel, list of SelectItems representing columns to select
	* @param expression, WHERE clause expression
	* @param ord, ORDER BY clause expression
	*/
	public DuplicateEliminationOperator(String fileName, List<SelectItem> sel, List<Operator> children, HashMap<String, Table> aliasMap,
			OrderByElement ord) {
		super(fileName, sel, children, aliasMap, ord);
		// idk if this is redundant
		this.aliasMap = aliasMap;
	}
	@Override
	/**
	* Returns the next unique tuple (table row) in table denoted by fileName
	* @return tuple, next unique tuple in table denoted by fileName
	*/
	public Tuple getNextTuple() {
		Tuple nextTuple = super.getNextTuple();
		if(tupleSet.contains(nextTuple)) {
			getNextTuple();
		}
		tupleSet.add(nextTuple);
		return nextTuple;
	}
}