import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectItem;

/**
* Represents an Scan Operator in the Iterator model of SQL.
*/
public class ProjectOperator extends Operator {
	
	protected List<SelectItem> sel;
	private List<Operator> children;
	
	/**
	* Constructor for ScanOperator class.
	* @param filename, String name of table to apply operator to
	*/
    public ProjectOperator(String fileName, List<SelectItem> sel, List<Operator> children) {
        super(fileName);
        this.sel = sel;
        this.children = children;
    }

    @Override public Tuple getNextTuple() {
    	Tuple t = children.get(0).getNextTuple();
    	while(t != null) {
    		List<String> result = new ArrayList<>();
    		SelectItemEvaluator e = new SelectItemEvaluator(t, null);
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
