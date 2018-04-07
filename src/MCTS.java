import java.util.ArrayList;

public class MCTS implements ISolver
{
	public IMove play
	(
		IBoard 	board
	)
	{
		ArrayList<IMove> legalMoves = board.getLegalMoves();
		IMove bestMove 				= legalMoves.get(0);
		return bestMove;
	}
	
	public ISolver copySolver()
	{
		return new MCTS();
	}

	public void writeToFile(String file, double score) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSolverName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair solve(IBoard board) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeGameToFile(String file, double score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeSolverToFile(String file, int instanceID) {
		// TODO Auto-generated method stub
		
	}

}
