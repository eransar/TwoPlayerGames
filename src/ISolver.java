
public interface ISolver 
{
	public ISolver 	copySolver();
	
	public IMove 	play(IBoard board);
			
	public Pair 	solve(IBoard board);
	
	public void		writeGameToFile(String file, IBoard board, double score);
	
	public void		writeSolverToFile(String file, IBoard board, int instanceID);
	
	public String	getSolverName();
}
