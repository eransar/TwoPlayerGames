import java.util.ArrayList;


public class WeightedAlphaBetaPruningNode implements INode
{
	public WeightedAlphaBetaPruningNode 		parent;
	public IBoard 								board;
	public double								pess;
	public double								opti;
	public double 								alpha;
	public double 								beta;
	public double								probability; // for child of chance node
	public NodeType 							nodeType;
	public IMove								lastMove;
	

	
	
	public ArrayList<WeightedAlphaBetaPruningNode> createChildren()
	{
		ArrayList<WeightedAlphaBetaPruningNode> children 		= new ArrayList<WeightedAlphaBetaPruningNode>();
		ArrayList<IMove> 						possibleMoves 	= board.getLegalMoves();
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
		child.board		 	= board.getNewChildBoard(move);
		child.lastMove		= move;
		
		if(board.getCurrentPlayer() == child.board.getCurrentPlayer())
			child.nodeType = nodeType;
		else
		{
			if(nodeType == NodeType.MAX)
				child.nodeType = NodeType.MIN; //TODO: chance nodes??
			else
				child.nodeType = NodeType.MAX;
		}
		return child;	
	}
	
	public NodeType getNodeType()
	{
		return nodeType;
	}
	
	public IBoard getBoard()
	{
		return board;
	}
	
}
