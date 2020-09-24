import java.util.ArrayList;
import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.statement.select.SelectItem;

public class ProjectOperator extends SelectOperator {
	
	protected List<SelectItem> sel;
	
    public ProjectOperator(String fileName, List<SelectItem> sel, Expression expression) {
        super(fileName, expression);
        this.sel = sel;
    }

    @Override public Tuple getNextTuple() {
    	Tuple t = super.getNextTuple();
    	while(t != null) {
    		List<String> result = new ArrayList<>();
    		SelectItemEvaluator e = new SelectItemEvaluator(t);
    		for(int i = 0; i < sel.size(); i++) {
    			sel.get(i).accept(e);
    			result.addAll(e.getStack().pop());
    		}
    		return new Tuple(result.toArray(new String[result.size()]));
    	}
    	return null;
    }
}
