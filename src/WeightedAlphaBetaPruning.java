import java.util.ArrayList;

public class WeightedAlphaBetaPruning implements ISolver
{
	private 	IHeuristic 	_heuristic;
	private     ErrorPolicy _errorPolicy;
	public enum NodeType 	{MAX, MIN, CHANCE};
	public enum ErrorPolicy	{NONE};
	
	public IMove play
	(
			IGame 		game,
			IHeuristic 	heuristic,
			IBoard 		board,
			char 		player,
			Object[]	parameters
	)
	{
		_heuristic 					= heuristic;
		ArrayList<IMove> legalMoves = board.getLegalMoves(player);
		IMove bestMove 				= legalMoves.get(0); //TODO: return the best child move
		return bestMove;
	}
	
	private WeightedAlphaBetaPruningNode createRoot
	(
		IBoard board,
		char   player
	)
	{
		
	}
	
	
	private Pair recursiveAlphaBeta
	(
			WeightedAlphaBetaPruningNode 	n,
			double							e
	)
	{
		char player 		= n.player;
		int  v_min			= Integer.MAX_VALUE;
		int  v_max			= Integer.MIN_VALUE;
		double  bestOpti 	= 0; // TODO: init
		double  bestPess 	= 0; // TODO: init
		if 		(n.nodeType == NodeType.MAX)
		{
			bestOpti = Double.MIN_VALUE;
			bestPess = Double.MIN_VALUE;
		}
		else if (n.nodeType == NodeType.MIN)
		{
			bestOpti = Double.MAX_VALUE;
			bestPess = Double.MAX_VALUE;
		}
		if (n.isTerminal())
		{
			int h = _heuristic.getHeuristic(n.board, n.player);
			return new Pair(h, h);
		}
		n.pess = v_min;
		n.opti = v_max;
		if 		(n.nodeType == NodeType.MAX)
			bestOpti = v_min;
		else if (n.nodeType == NodeType.MIN)
			bestPess = v_max;
		n.alpha = n.parent.alpha;
		n.beta  = n.parent.beta;
		ArrayList<WeightedAlphaBetaPruningNode> children = n.createChildren();
		for (WeightedAlphaBetaPruningNode child : children)
		{
			if (	n.nodeType == NodeType.MAX	||
					n.nodeType == NodeType.MIN)
			{
				child.alpha = n.alpha;
				child.beta 	= n.beta;
			}
			else
			{
				child.alpha = Double.max(v_min, (n.alpha - n.opti + child.probability * child.opti) / child.probability);
				child.beta  = Double.min(v_max, (n.beta  - n.pess + child.probability * child.pess) / child.probability);
			}
			double zeta			= computeChildBound (e, n.opti, n.pess, child.probability);
			Pair childValues 	= recursiveAlphaBeta(child, zeta);
			child.pess			= (double)childValues.x;
			child.opti			= (double)childValues.y;
			if 		(n.nodeType == NodeType.MAX)
			{
				bestOpti = Double.max(bestOpti, child.opti);
				bestPess = Double.max(n.pess  , child.pess);
			}
			else if (n.nodeType == NodeType.MIN)
			{
				bestOpti = Double.min(bestPess, child.pess);
				bestPess = Double.min(n.opti  , child.opti);
			}
			else
			{
				n.pess   = n.pess + child.probability * (child.pess - v_min);
				n.opti   = n.opti - child.probability * (v_max - child.opti);
			}
			n.alpha = Double.max(n.pess, n.alpha);
			n.beta  = Double.min(n.opti, n.beta);
			if (n.beta < n.alpha + e)
				return new Pair(n.pess, n.opti);
		}
		if 		(n.nodeType == NodeType.MAX)
			n.opti = bestOpti;
		else if (n.nodeType == NodeType.MIN)
			n.pess = bestPess;
		return new Pair(n.pess, n.opti);
	}
	
	private double computeChildBound
	(
		double e,
		double opti,
		double pess,
		double probability
	)
	{
		return e; //TODO: more sophisticated manner
	}

}
