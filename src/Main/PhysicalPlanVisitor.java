package Main;

import LogicalOperators.LogicalDuplicateElimination;
import LogicalOperators.LogicalJoin;
import LogicalOperators.LogicalProject;
import LogicalOperators.LogicalScan;
import LogicalOperators.LogicalSelect;
import LogicalOperators.LogicalSort;

public interface PhysicalPlanVisitor {
	
	public void visit(LogicalScan op);
	
	public void visit(LogicalSelect op);

	public void visit(LogicalProject op);
	
	public void visit(LogicalSort op);
	
	public void visit(LogicalJoin op);
	
	public void visit(LogicalDuplicateElimination op);
	
}
