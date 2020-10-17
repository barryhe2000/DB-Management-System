package PhysicalOperators;
import java.util.Stack;

import Main.*;
import java.util.HashMap;

import net.sf.jsqlparser.expression.AllComparisonExpression;
import net.sf.jsqlparser.expression.AnyComparisonExpression;
import net.sf.jsqlparser.expression.CaseExpression;
import net.sf.jsqlparser.expression.DateValue;
import net.sf.jsqlparser.expression.DoubleValue;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.expression.InverseExpression;
import net.sf.jsqlparser.expression.JdbcParameter;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.NullValue;
import net.sf.jsqlparser.expression.Parenthesis;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.TimeValue;
import net.sf.jsqlparser.expression.TimestampValue;
import net.sf.jsqlparser.expression.WhenClause;
import net.sf.jsqlparser.expression.operators.arithmetic.Addition;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseAnd;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseOr;
import net.sf.jsqlparser.expression.operators.arithmetic.BitwiseXor;
import net.sf.jsqlparser.expression.operators.arithmetic.Concat;
import net.sf.jsqlparser.expression.operators.arithmetic.Division;
import net.sf.jsqlparser.expression.operators.arithmetic.Multiplication;
import net.sf.jsqlparser.expression.operators.arithmetic.Subtraction;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.expression.operators.conditional.OrExpression;
import net.sf.jsqlparser.expression.operators.relational.Between;
import net.sf.jsqlparser.expression.operators.relational.EqualsTo;
import net.sf.jsqlparser.expression.operators.relational.ExistsExpression;
import net.sf.jsqlparser.expression.operators.relational.GreaterThan;
import net.sf.jsqlparser.expression.operators.relational.GreaterThanEquals;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.IsNullExpression;
import net.sf.jsqlparser.expression.operators.relational.LikeExpression;
import net.sf.jsqlparser.expression.operators.relational.Matches;
import net.sf.jsqlparser.expression.operators.relational.MinorThan;
import net.sf.jsqlparser.expression.operators.relational.MinorThanEquals;
import net.sf.jsqlparser.expression.operators.relational.NotEqualsTo;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.SubSelect;

/**
 * Represents an Expression Evaluator with the visitor pattern
 */
public class ExpressionEvaluator implements ExpressionVisitor {
	
	private Stack<Object> stack;
	private Tuple tuple;
	private Tuple tuple2;
	private HashMap<String, Table> aliasMap;
	
    /**
	 * Constructor for ExpressionEvaluator class
	 * @param tuple, first tuple to evaluate
	 * @param tuple2, second tuple to evaluate
	 * @param aliasMap, map of aliases to table names
	 */
	public ExpressionEvaluator(Tuple tuple, Tuple tuple2, HashMap<String, Table> aliasMap) {
		stack = new Stack<>();
		this.tuple = tuple;
		this.tuple2 = tuple2;
		this.aliasMap = aliasMap;
	}
	
	/**
	 * Returns stack of query output rows
	 * @return stack, stack of query output rows
	 */
	public Stack<Object> getStack() {
		return stack;
	}

	@Override
	public void visit(NullValue arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(Function arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(InverseExpression arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(JdbcParameter arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(DoubleValue arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(LongValue arg0) {
		stack.push(arg0.getValue());
	}

	@Override
	public void visit(DateValue arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(TimeValue arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(TimestampValue arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(Parenthesis arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(StringValue arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(Addition arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(Division arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(Multiplication arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(Subtraction arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(AndExpression arg0) {
		arg0.getLeftExpression().accept(this);
		arg0.getRightExpression().accept(this);
		Object right = stack.pop();
		Object left = stack.pop();
		//may be unnecessary to evaluate numeric conditionals such as 5 AND 4
		if (right.getClass() == Long.class && left.getClass() == Long.class)
			stack.push(((Long) right != 0) && ((Long) left != 0));
		else if (right.getClass() == Long.class) stack.push(left);
		else if (left.getClass() == Long.class) stack.push(((Long) left != 0) && (Boolean) right); 
		else stack.push((Boolean) left && (Boolean) right);
	}

	@Override
	public void visit(OrExpression arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(Between arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(EqualsTo arg0) { 
		arg0.getLeftExpression().accept(this);
		arg0.getRightExpression().accept(this);
		stack.push(stack.pop().equals(stack.pop()));
	}

	@Override
	public void visit(GreaterThan arg0) {
		arg0.getLeftExpression().accept(this);
		arg0.getRightExpression().accept(this);
		stack.push((Long) stack.pop() < (Long) stack.pop());
	}

	@Override
	public void visit(GreaterThanEquals arg0) {
		arg0.getLeftExpression().accept(this);
		arg0.getRightExpression().accept(this);
		stack.push((Long) stack.pop() <= (Long) stack.pop());
	}

	@Override
	public void visit(InExpression arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(IsNullExpression arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(LikeExpression arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(MinorThan arg0) {
		arg0.getLeftExpression().accept(this);
		arg0.getRightExpression().accept(this);
		stack.push((Long) stack.pop() > (Long) stack.pop());
	}

	@Override
	public void visit(MinorThanEquals arg0) {
		arg0.getLeftExpression().accept(this);
		arg0.getRightExpression().accept(this);
		stack.push((Long) stack.pop() >= (Long) stack.pop());
	}

	@Override
	public void visit(NotEqualsTo arg0) {
		arg0.getLeftExpression().accept(this);
		arg0.getRightExpression().accept(this);
		stack.push(!stack.pop().equals(stack.pop()));
	}

	@Override
	public void visit(Column arg0) {
		String tableName = arg0.getTable().getName();
		if(aliasMap != null && aliasMap.containsKey(tableName)) {
			tableName = aliasMap.get(tableName).getName();
		}
    	String colName = arg0.getColumnName();
    	int colIndex = Main.getTableHeaders().get(tableName).indexOf(colName);
    	if (tuple.getTableName().equals(tableName)) stack.push(Long.parseLong(tuple.getRowElement(colIndex)));
    	else stack.push(Long.parseLong(tuple2.getRowElement(colIndex)));
	}

	@Override
	public void visit(SubSelect arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(CaseExpression arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(WhenClause arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(ExistsExpression arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(AllComparisonExpression arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(AnyComparisonExpression arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(Concat arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(Matches arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(BitwiseAnd arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(BitwiseOr arg0) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public void visit(BitwiseXor arg0) {
		throw new UnsupportedOperationException();
		
	}
}