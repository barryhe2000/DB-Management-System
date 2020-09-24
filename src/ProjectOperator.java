import java.util.ArrayList;
import java.util.List;
import net.sf.jsqlparser.statement.select.SelectItem;

public class ProjectOperator extends ScanOperator {
	
	protected List<SelectItem> sel;
	
    public ProjectOperator(String fileName, List<SelectItem> sel) {
        super(fileName);
        this.sel = sel;
    }

    @Override public Tuple getNextTuple() {
    	Tuple t = super.getNextTuple();
    	List<String> result = new ArrayList<String>();
    	while(t != null) {
    		SelectItemEvaluator e = new SelectItemEvaluator(t);
    		for(int i = 0; i < sel.size(); i++) {
    			sel.get(i).accept(e);
    			result.addAll(e.getStack().pop());
    		}
    		return new Tuple(result.toArray(new String[sel.size()]));
    	}
        return null;
    }
}
