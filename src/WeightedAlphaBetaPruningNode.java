import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


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
		child.probability   = child.board.getChance();
		
		if(board.getCurrentPlayer() == child.board.getCurrentPlayer())
			child.nodeType = nodeType;
		else
		{
			if(board.getChance() < 1 && board.getChance() > 0)
			{
				if (nodeType == NodeType.MAX || nodeType == NodeType.MIN)
					child.nodeType = NodeType.CHANCE;
				else
				{
					if(parent.nodeType == NodeType.MAX)
						child.nodeType = NodeType.MIN;
					else
						child.nodeType = NodeType.MAX;
				}
			}
			else
			{
				if(nodeType == NodeType.MAX)
					child.nodeType = NodeType.MIN; 
				else
					child.nodeType = NodeType.MAX;
			}
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
