import java.util.List;

public class AlphaBetaPruning implements ISolver
{
	@Override
	public String getSolverName() 
	{
		return "Alpha-Beta Pruning";
	}
	
	@Override
	public double solve(IBoard board)
	{
		Node root = new Node(board, Node.NodeType.MAX);
		return AlphaBetaPruningAlgorithm(root, - Double.MAX_VALUE, Double.MAX_VALUE);
	}

	
	private double AlphaBetaPruningAlgorithm(Node 	node, double 	p_alpha, double 	p_beta)
	{
		double value;
		if (node.getNodeType() == Node.NodeType.MAX)
			value = -Double.MAX_VALUE;
		else
			value = Double.MAX_VALUE;

		// Explore Child Nodes
		List<Node> children = node.getNodeChildren();
		for (Node child : children) {
			double childValue;
			if (child.isTerminalNode())
				childValue = child.getScore();
			else
				childValue = AlphaBetaPruningAlgorithm(child,p_alpha,p_beta);
			if(!(p_beta<=p_alpha)) {
				if (node.getNodeType() == Node.NodeType.MAX) {
					if (childValue <= p_alpha)
						return p_alpha;
					value = Double.max(value, childValue);
				} else {
					if (childValue >= p_beta)
						return p_beta;
					value = Double.min(value, childValue);
				}
			}
		}

		return value;
	}
	
}
