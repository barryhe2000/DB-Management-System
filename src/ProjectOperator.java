import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectItem;
import net.sf.jsqlparser.schema.Table;

/**
* Represents a Project Operator in the Iterator model of SQL.
*/
public class ProjectOperator extends Operator {
	
	protected List<SelectItem> sel;
	private List<Operator> children;
	private HashMap<String, Table> aliasMap;
	
	/**
	* Constructor for ProjectOperator class.
	* @param filename, String name of table to apply operator to
	* @param sel, list of SelectItems representing columns to select
	* @param children, child operators of the operator
	* @param aliasMap, map of aliases to tables
	*/
    public ProjectOperator(String fileName, List<SelectItem> sel, List<Operator> children, HashMap<String, Table> aliasMap) {
        super(fileName);
        this.sel = sel;
        this.children = children;
        
        // TODO - add aliasing support for Columns
        
        this.aliasMap = aliasMap;
    }

    /**
	* Returns the next tuple (table row) in table denoted by fileName
	* and sorted according to ord
	* @return tuple, next tuple in table denoted by fileName
	* with columns specified by sel
	*/
    @Override public Tuple getNextTuple() {
    	Tuple t = children.get(0).getNextTuple();
    	while(t != null) {
    		List<String> result = new ArrayList<>();
    		SelectItemEvaluator e = new SelectItemEvaluator(t, null, aliasMap);
    		for(int i = 0; i < sel.size(); i++) {
    			sel.get(i).accept(e);
    			result.addAll(e.getStack().pop());
    		}
    		return new Tuple(result.toArray(new String[result.size()]), t.getTableName());
    	}
    	return null;
    }

    // TODO - fix reset
	@Override
	public void reset(String file) {
	}
}
