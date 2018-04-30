import java.util.ArrayList;
import java.util.List;

public class MCTS implements ISolver
{
	public IMove play
	(
		IBoard 	board
	)
	{
		List<IMove> legalMoves = board.getLegalMoves();
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
	public void writeGameToFile(String file, IBoard board, double score) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void writeSolverToFile(String file, IBoard board, int instanceID) {
		// TODO Auto-generated method stub
		
	}



}
