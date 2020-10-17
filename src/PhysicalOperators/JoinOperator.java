package PhysicalOperators;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;

/**
 * Represents a Join Operator (for JOIN) in the Iterator model of SQL.
 */
public class JoinOperator extends ScanOperator {
	
	protected Expression expression;
	List<Joint> joinTables;
	Tuple[] arrTuple;
	Tuple t;
	
	/**
	 * Constructor for JoinOperator class.
	 * @param tuple, tuple (table row) to evaluate SelectItem on
	 * @param joinList, list of JOIN operator conditions
	 * @param expression, expression for JOIN operator
	 */
	public JoinOperator(String fileName, List<FromItem> joinList, Expression expression) {
		super(fileName);
		this.expression = expression;
		List<Joint> joinTables= new ArrayList<>();
		List<Tuple> joinTuples= new ArrayList<>();
		super.reset(fileName);
		t= super.getNextTuple();
		for (int i = 0; i < joinList.size(); i++) {
			String tName = ((Table) joinList.get(i)).getName();
			try {
				Joint j = new Joint(i, tName);
				joinTables.add(j);
				joinTuples.add(j.getNextTuple());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		Tuple[] arrTuple = new Tuple[joinTuples.size()];
		for(int i = 0; i < arrTuple.length; i++) {
			arrTuple[i] = joinTuples.get(i);
		}
		this.joinTables= joinTables;
		this.arrTuple = arrTuple;
	}
	
	/**
     * Resets class instance such that getNextTuple() starts back at the table head.
     */
	@Override public void reset(String file) {
		// TODO - Add reset for Join Operator or delete if same as scan
	}
	
	/**
	 * Combines tuples based on JOIN conditions
	 * @param head, Tuple to combine
	 * @return head, combined Tuple
	 */
	private Tuple combineJoin(Tuple head) {
		for (Tuple part : arrTuple) {
			head = head.combine(part);
		}
		return head;
	}
	
	/**
	 * Returns the next tuple in the JOIN query
	 * @return temp, next tuple in table that meets operator conditions
	 */
	@Override public Tuple getNextTuple() {
		Tuple temp = t;
		while (temp != null) {
			if (expression == null) {
				if (arrTuple[arrTuple.length-1] != null) {
					temp = combineJoin(temp);
					arrTuple[arrTuple.length-1] = joinTables.get(joinTables.size()-1).getNextTuple();
				} else {
					for (int i= arrTuple.length-1; i >= 0; i--) {
						if (arrTuple[i] != null) break;
						Joint currJoint = joinTables.get(i);
						try {
							currJoint.reset();
						} catch (IOException e) {
							e.printStackTrace();
						}
						arrTuple[i] = currJoint.getNextTuple();
						if (i > 0) {
							arrTuple[i-1] = joinTables.get(i-1).getNextTuple();
						}
						if (i == 0) {
							t = super.getNextTuple();
							if (t == null) return null;
							temp = t;
						}
					}
					temp = combineJoin(temp);
				}
				return temp;
			} else {
				if (arrTuple[arrTuple.length-1] == null) {
					for (int i= arrTuple.length-1; i >= 0; i--) {
						if (arrTuple[i] != null) break;
						Joint currJoint = joinTables.get(i);
						try {
							currJoint.reset();
						} catch (IOException e) {
							e.printStackTrace();
						}
						arrTuple[i] = currJoint.getNextTuple();
						if (i > 0) {
							arrTuple[i-1] = joinTables.get(i-1).getNextTuple();
						}
						if (i == 0) {
							t = super.getNextTuple();
							if (t == null) return null;
							temp = t;
						}
					}
				}
				
				JoinExpressionEvaluator j = new JoinExpressionEvaluator();
				expression.accept(j);
				Map<String, List<Expression>> requirements = j.getRequirements();
				boolean able = true;
				for (String names : requirements.keySet()) {
					ExpressionEvaluator ee;
					if (names.contains(" ")) {
						String[] twoTables = names.split("\\s+");
						Tuple one = null;
						Tuple two = null;
						for(Tuple findTuple : arrTuple) {
							if (findTuple == null) continue;
							if (findTuple.getTableName().equals(twoTables[0])) one = findTuple;
							else if (findTuple.getTableName().equals(twoTables[1])) two = findTuple;
						}
						if (t.getTableName().equals(twoTables[0])) one = t;
						else if (t.getTableName().equals(twoTables[1])) two = t;
						// TODO - add alias fix
						ee = new ExpressionEvaluator(one, two, null);
					} else {
						Tuple one = null;
						for(Tuple findTuple : arrTuple) {
							if (findTuple == null) continue;
							if (findTuple.getTableName().equals(names)) one = findTuple;
						}
						if (t.getTableName().equals(names)) one = t;
						// TODO - add alias fix
						ee = new ExpressionEvaluator(one, null, null);
					}
					List<Expression> reqs = requirements.get(names);
					for (Expression exp : reqs) {
						exp.accept(ee);
						if (ee.getStack().pop().equals(false)) able= false;
					}
				}
				if (able) {
					temp = combineJoin(temp);
					arrTuple[arrTuple.length-1] = joinTables.get(joinTables.size()-1).getNextTuple();
					return temp;
				} else {
					arrTuple[arrTuple.length-1] = joinTables.get(joinTables.size()-1).getNextTuple();
				}
			}
			temp= t;
		}
		return null;
	}
	
	/**
	 * Represents a Joined Table.
	 */
	class Joint {
		String tableName;
		private TupleReader tr;
		
		/**
	     * Constructor for Joint class
	     * @param tableName, the table name
	     * @throws IOException
	     */
		Joint(int order, String tableName) throws IOException {
			tr = new TupleReader(tableName);
			this.tableName = tableName;
		}
		
		/**
	     * Returns the table name.
	     * @return tableName, the String name of the table
	     */
		String getTableName() {
			return tableName;
		}
		
		/**
	     * Conditionally returns the next tuple (table row) 
	     * from table denoted by fileName.
	     * @return tuple, next tuple in table that meets operator conditions
	     */
		Tuple getNextTuple() {
			return tr.readNextTuple();
		}
		
		/**
	     * Resets class instance such that getNextTuple() starts back at the table head.
	     */
		void reset() throws IOException {
			tr = new TupleReader(fileName);
		}
	}
}
