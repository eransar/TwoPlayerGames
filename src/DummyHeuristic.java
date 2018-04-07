
public class DummyHeuristic implements IHeuristic
{
	
	
	public DummyHeuristic()
	{
	}
	

	public double getHeuristic
	(
		INode node
	)
	{
		IBoard board = node.getBoard();
		double toReturn = board.hashCode() % 100;
		if (((OthelloBoard)board)._player == '2')
			toReturn *= -1;
		return toReturn;
	}
	
	
}
