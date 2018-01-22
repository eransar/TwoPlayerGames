
public class WeightedAlphaBetaPruningNode implements INode
{
	public WeightedAlphaBetaPruningNode prev;
	public IBoard board;
	public int h;
	public char player;
	
	public boolean isTerminal()
	{
		return board.isTheGameOver();
	}
	
	
}
