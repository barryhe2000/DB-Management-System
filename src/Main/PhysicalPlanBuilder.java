package Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import LogicalOperators.LogicalDuplicateElimination;
import LogicalOperators.LogicalJoin;
import LogicalOperators.LogicalProject;
import LogicalOperators.LogicalScan;
import LogicalOperators.LogicalSelect;
import LogicalOperators.LogicalSort;
import PhysicalOperators.DuplicateEliminationOperator;
import PhysicalOperators.JoinOperator;
import PhysicalOperators.Operator;
import PhysicalOperators.ProjectOperator;
import PhysicalOperators.ScanOperator;
import PhysicalOperators.SelectOperator;
import PhysicalOperators.SortOperator;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.FromItem;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.OrderByElement;
import net.sf.jsqlparser.statement.select.SelectItem;

public class PhysicalPlanBuilder implements PhysicalPlanVisitor {
	
	private String outputPath;
	private int queryNum;
	
	public PhysicalPlanBuilder(String outputPath, int queryNum) {
		this.outputPath = outputPath;
		this.queryNum = queryNum;
	}

	@Override
	public void visit(LogicalScan op) {
		String fileName = op.getFileName();
		ScanOperator scan= new ScanOperator(fileName);
    	System.currentTimeMillis();
    	scan.dump(outputPath + queryNum);
    	System.currentTimeMillis();
	}

	@Override
	public void visit(LogicalSelect op) {
		String fileName = op.getFileName();
		Expression exp = op.getExpression();
		HashMap<String, Table> aliasMap = op.getAliasMap();
		SelectOperator so= new SelectOperator(fileName, exp, aliasMap);
    	System.currentTimeMillis(); 
    	so.dump(outputPath + queryNum);
    	System.currentTimeMillis();
	}

	@Override
	public void visit(LogicalProject op) {
		String fileName = op.getFileName();
		List<SelectItem> sel = op.getSelectItems();
		LogicalSelect selectOp = (LogicalSelect)op.getChildren().get(0);
		Expression exp = selectOp.getExpression();
		
		HashMap<String, Table> aliasMap = op.getAliasMap();
		
		List<Operator> lst = new ArrayList<>();
    	lst.add(new SelectOperator(fileName, exp, aliasMap));
    	
    	ProjectOperator project = new ProjectOperator(fileName, sel, lst, aliasMap);
    	System.currentTimeMillis();
    	project.dump(outputPath + queryNum);
    	System.currentTimeMillis();
	}

	@Override
	public void visit(LogicalSort op) {
		String fileName = op.getFileName();
		List<SelectItem> sel = op.getSelectItems();
		LogicalSelect selectOp = (LogicalSelect)op.getChildren().get(0);
		Expression exp = selectOp.getExpression();
		
		HashMap<String, Table> aliasMap = op.getAliasMap();
		OrderByElement ord = op.getOrd();
		
		List<Operator> lst = new ArrayList<>();
		lst.add(new SelectOperator(fileName, exp, aliasMap));
    	
    	SortOperator so = new SortOperator(fileName, sel, lst, aliasMap, ord);
    	System.currentTimeMillis();
    	so.dump(outputPath + queryNum);
    	System.currentTimeMillis();
	}

	@Override
	public void visit(LogicalJoin op) {
		String fileName = op.getFileName();
		Expression exp = op.getExpression();
		
		List<Join> joinList = op.getJoinList();
		List<FromItem> joining = op.getJoining();
		
    	JoinOperator jo = new JoinOperator(fileName, joining, exp);
    	System.currentTimeMillis();
    	jo.dump(outputPath + queryNum);
    	System.currentTimeMillis();
	}

	@Override
	public void visit(LogicalDuplicateElimination op) {
		String fileName = op.getFileName();
		List<SelectItem> sel = op.getSelectItems();
		LogicalSelect selectOp = (LogicalSelect)op.getChildren().get(0);
		Expression exp = selectOp.getExpression();
		
		HashMap<String, Table> aliasMap = op.getAliasMap();
		OrderByElement ord = op.getOrd();
		
		List<Operator> lst = new ArrayList<Operator>();
		lst.add(new SelectOperator(fileName, exp, aliasMap));
		
		DuplicateEliminationOperator de = new DuplicateEliminationOperator(fileName, sel, lst, aliasMap, ord);
    	System.currentTimeMillis();
    	de.dump(outputPath + queryNum);
    	System.currentTimeMillis();
	}

}
