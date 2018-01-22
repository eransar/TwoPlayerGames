import java.util.ArrayList;

public class WeightedAlphaBetaPruningNode implements INode
{
	public WeightedAlphaBetaPruningNode 		parent;
	public IBoard 								board;
	public char 								player;
	public double								pess;
	public double								opti;
	public double 								alpha;
	public double 								beta;
	//public double 								e;
	public double								probability; // for child of chance node
	public WeightedAlphaBetaPruning.NodeType 	nodeType;
	
	public boolean isTerminal()
	{
		return board.isTheGameOver(); //TODO: if depth is..
	}
	
	
	public ArrayList<WeightedAlphaBetaPruningNode> createChildren()
	{
		ArrayList<WeightedAlphaBetaPruningNode> children 		= new ArrayList<WeightedAlphaBetaPruningNode>();
		ArrayList<IMove> 						possibleMoves 	= board.getLegalMoves(player);
		for(IMove move: possibleMoves)
		{
			WeightedAlphaBetaPruningNode child = createOneChild(move);
			children.add(child);
		}
		return children;
	}
	
	
	private WeightedAlphaBetaPruningNode createOneChild(IMove move)
	{
		WeightedAlphaBetaPruningNode child = new WeightedAlphaBetaPruningNode();
		child.parent 		= this;
		IBoard childBoard 	= board.getNewChildBoard(move, player);
		child.board			= childBoard;
		child.player		= childBoard.getOtherPlayer(player);
		return child;	
	}
}
