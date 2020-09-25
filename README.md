DB-Management-System

- Main.java
- We extracted the join conditions from the WHERE clause by 
creating an implementation of the ExpressionVisitor subclass. 
We then used that implementation to visit/evaluate the expression
and get the left and right components of the expression which we then
put into a map of table names and lists of expressions that apply to those
table names.
- Known Bugs...
Implementation was rushed throughout Join, OrderBy, and Distinct. May not have self-join implementation.
