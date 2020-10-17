DB-Management-System
P2
- Main/Main.java
- We weren't able to implement SMJ in time, but we planned on handling the partition reset during SMJ by computing the number of the page the tuple is on based on the index, fetching the specific page, and then calling getNextTuple (index - 4 bytes * elements per page * page number) times.
- Known Bugs - Binary NIO read/write, physical operators make assumptions about order (instead of leaving that to the logical operators), no BNLJ, no SMJ, no external sort, no performance benchmarking

P1
- Main.java
- We extracted the join conditions from the WHERE clause by 
creating an implementation of the ExpressionVisitor subclass. 
We then used that implementation to visit/evaluate the expression
and get the left and right components of the expression which we then
put into a map of table names and lists of expressions that apply to those
table names.
- Known Bugs...
Implementation was rushed throughout Join, OrderBy, and Distinct. May not have self-join implementation.
