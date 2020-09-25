import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;

public class JoinOperator extends ScanOperator {
	
	protected Expression expression;
	List<Joint> joinTables;
	Tuple[] arrTuple;
	Tuple t;
	
	/**
	* Constructor for SelectItemEvaluator class.
	* @param tuple, tuple (table row) to evaluate SelectItem on
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
				Joint j = new Joint(Main.getTablePath().get(tName), i, tName);
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
	
	@Override
	public void reset(String file) {
		
	}
	
	private Tuple combineJoin(Tuple head) {
		for (Tuple part : arrTuple) {
			head = head.combine(part);
		}
		return head;
	}
	
	@Override
	public Tuple getNextTuple() {
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
						ee = new ExpressionEvaluator(one, two);
					} else {
						Tuple one = null;
						for(Tuple findTuple : arrTuple) {
							if (findTuple == null) continue;
							if (findTuple.getTableName().equals(names)) one = findTuple;
						}
						if (t.getTableName().equals(names)) one = t;
						ee = new ExpressionEvaluator(one, null);
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
	
	class Joint {
		
		FileReader f;
		BufferedReader in;
		int order;
		String tableName;
		String path;
		
		Joint(String path, int order, String tableName) throws IOException {
			this.path = path;
			f = new FileReader(path);
			in = new BufferedReader(f);
			this.order = order;
			this.tableName = tableName;
		}
		
		String getTableName() {
			return tableName;
		}
		
		Tuple getNextTuple() {
			String strRow;
	    	try {
	    	    if ((strRow=in.readLine()) != null) return new Tuple(strRow.split(","), tableName);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			return null;
		}
		
		void reset() throws IOException {
			f = new FileReader(path);
			in = new BufferedReader(f);
		}
	}
}
