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
		if(node.isTerminalNode())
			return node.getScore();
		if(node.getNodeType() == Node.NodeType.MAX){
			double value = - Double.MAX_VALUE;
			for(Node n : node.getNodeChildren()){
				value = Double.max(value,AlphaBetaPruningAlgorithm(n,p_alpha,p_beta));
				p_alpha = Double.max(p_alpha,value);
				if(p_alpha>=p_beta)
					break;
			}
			return value;
		}
		else{
			double value = Double.MAX_VALUE;
			for(Node n : node.getNodeChildren()){
				value = Double.min(value,AlphaBetaPruningAlgorithm(n,p_alpha,p_beta));
				p_beta = Double.min(p_beta,value);
				if(p_alpha>=p_beta)
					break;
			}
			return value;
		}
	}
	
}
