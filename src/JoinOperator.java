import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;

public class JoinOperator extends ScanOperator {
	
	protected Expression expression;

	public JoinOperator(String fileName, Expression expression) {
		super(fileName);
		this.expression = expression;
	}
	
	@Override
	public Tuple getNextTuple() {
		return super.getNextTuple();
	}
	
	

}
