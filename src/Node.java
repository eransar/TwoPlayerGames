import java.util.ArrayList;
import java.util.List;

public class Node 
{
	public enum NodeType 	{MAX, MIN};
	
	private IBoard		_board;
	private NodeType	_nodeType;
	
	public Node
	(
		IBoard 		board,
		NodeType 	nodeType
	)
	{
		_nodeType 	= nodeType;
		_board		= board;
	}
	
	public  NodeType getNodeType()
	{
		return _nodeType;
	}
	
	public  NodeType getNodeOtherType()
	{
		if (_nodeType == NodeType.MAX)
			return NodeType.MIN;
		else
			return NodeType.MAX;
	}
	
	public	List<Node> getNodeChildren()
	{
		List<Node>	children	= new ArrayList<Node>();
		List<IMove> legalMoves 	= _board.getLegalMoves();
		for (IMove move : legalMoves)
		{
			IBoard 	newBoard 	= _board.getNewChildBoard(move);
			NodeType 	newNodeType;
			if (_board.getCurrentPlayer() == newBoard.getCurrentPlayer())
				newNodeType = _nodeType;
			else
				newNodeType = getNodeOtherType();
			
			Node	newNode		= new Node(newBoard, newNodeType);
			children.add(newNode);
		}
		return children;
	}
	
	public	IBoard	getBoard()
	{
		return _board;
	}
	
	public boolean isTerminalNode()
	{
		if (_board.isTheGameOver())
			return true;
		else
			return false;
	}
	
	public double getScore()
	{
		return _board.getScore();
	}
}
