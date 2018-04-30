import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomSolver implements ISolver 
{

	@Override
	public ISolver copySolver() 
	{
		return new RandomSolver();
	}

	@Override
	public IMove play(IBoard board) 
	{
		Random rand = new Random();
		List<IMove> legalMoves = board.getLegalMoves();
		int randomIndex = rand.nextInt(legalMoves.size());
		return legalMoves.get(randomIndex);
	}

	@Override
	public void writeGameToFile(String file, IBoard board, double score) 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSolverName() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Pair solve(IBoard board) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void writeSolverToFile(String file, IBoard board, int instanceID) {
		// TODO Auto-generated method stub
		
	}


}
