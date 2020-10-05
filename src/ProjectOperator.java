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
	*/
    public ProjectOperator(String fileName, List<SelectItem> sel, List<Operator> children, HashMap<String, Table> aliasMap) {
        super(fileName);
        this.sel = sel;
        this.children = children;
        // problem here is that an alias could also be for a column...
        this.aliasMap = aliasMap;
    }

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

	@Override
	public void reset(String file) {
		
		
	}
}
