import java.util.Stack;
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
import net.sf.jsqlparser.statement.select.SubSelect;

public class ExpressionEvaluator implements ExpressionVisitor {

    private Stack<Object> stack= new Stack<>();
    private Tuple tup;

    Stack<Object> getStack() {
        return stack;
    }
    
//    void pushStack(Long i) {
//    	stack.push(i);
//    }
    
    public ExpressionEvaluator(Tuple tup) {
    	this.tup = tup;
    }

    @Override public void visit(NullValue arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(InverseExpression arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(JdbcParameter arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(DoubleValue arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(LongValue arg0) {
//    	System.out.println("long is " + arg0.toString());

        stack.push(arg0.getValue());
    }

    @Override public void visit(DateValue arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(TimeValue arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(TimestampValue arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(Parenthesis arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(StringValue arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(Addition arg0) {

    }

    @Override public void visit(Division arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(Multiplication arg0) {
    	arg0.getLeftExpression().accept(this);
    	arg0.getRightExpression().accept(this);
    	
        long fac1= (long)stack.pop();
        long fac2= (long)stack.pop();
        
        stack.push(fac1 * fac2);
    }

    @Override public void visit(Subtraction arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(AndExpression arg0) {
    	arg0.getLeftExpression().accept(this);
    	arg0.getRightExpression().accept(this);
    	
    	boolean c1= (boolean)stack.pop();
        boolean c2= (boolean)stack.pop();
    	
    	stack.push(c1 && c2);
    	
//        while (arg0.getLeftExpression() != null) {
//
//        }
    }

    @Override public void visit(OrExpression arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(Between arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(EqualsTo arg0) {
    	arg0.getLeftExpression().accept(this);
    	arg0.getRightExpression().accept(this);
//    	System.out.println("HIIII");
    	
    	Long o1 = Long.parseLong((stack.pop().toString()));
    	Long o2 = Long.parseLong((stack.pop().toString()));
    	
//    	System.out.println("BAIII");
    	
//    	System.out.println(o1 + " hi " + o2);
    	
    	stack.push(o1 == o2);
    }

    @Override public void visit(GreaterThan arg0) {
    	arg0.getLeftExpression().accept(this);
    	arg0.getRightExpression().accept(this);
    	
    	Long o1 = (long)stack.pop();
    	Long o2 = (long)stack.pop();
    	
    	stack.push(o1 > o2);
    }

    @Override public void visit(GreaterThanEquals arg0) {
    	arg0.getLeftExpression().accept(this);
    	arg0.getRightExpression().accept(this);
    	
    	Long o1 = (long)stack.pop();
    	Long o2 = (long)stack.pop();
    	
    	stack.push(o1 >= o2);
    }

    @Override public void visit(InExpression arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(IsNullExpression arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(LikeExpression arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(MinorThan arg0) {
    	arg0.getLeftExpression().accept(this);
    	arg0.getRightExpression().accept(this);
    	
    	Long o1 = (long)stack.pop();
    	Long o2 = (long)stack.pop();
    	
    	stack.push(o1 < o2);
    }

    @Override public void visit(MinorThanEquals arg0) {
    	arg0.getLeftExpression().accept(this);
    	arg0.getRightExpression().accept(this);
    	
    	Long o1 = (long)stack.pop();
    	Long o2 = (long)stack.pop();
    	
    	stack.push(o1 <= o2);
    }

    @Override public void visit(NotEqualsTo arg0) {
    	arg0.getLeftExpression().accept(this);
    	arg0.getRightExpression().accept(this);
    	
    	Object o1 = (long)stack.pop();
    	Object o2 = (long)stack.pop();
    	
    	stack.push(o1 != o2 && !o1.equals(o2));
    }

    @Override public void visit(Column arg0) {
    	String tableName = arg0.getTable().getName();
    	String colName = arg0.getColumnName();
    	// get the column name and compare to schema
    	
    	int colIndex = Main.getTableHeaders().get(tableName).indexOf(colName);
    	
//    	System.out.println("Column is " + tup.getRowElement(colIndex));
    	
    	stack.push(Long.parseLong(tup.getRowElement(colIndex)));
    }

    @Override public void visit(SubSelect arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(CaseExpression arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(WhenClause arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(ExistsExpression arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(AllComparisonExpression arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(AnyComparisonExpression arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(Concat arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(Matches arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(BitwiseAnd arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(BitwiseOr arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(BitwiseXor arg0) {
        throw new UnsupportedOperationException();
    }

    @Override public void visit(Function arg0) {
        throw new UnsupportedOperationException();
    }

}