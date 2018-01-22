import java.util.ArrayList;

public class WeightedAlphaBetaPruning implements ISolver
{
	private IHeuristic _heuristic;
	
	public IMove play
	(
			IGame 		game,
			IHeuristic 	heuristic,
			IBoard 		board,
			char 		player,
			Object[]	parameters
	)
	{
		_heuristic = heuristic;
		ArrayList<IMove> legalMoves = board.getLegalMoves(player);
		IMove bestMove 				= legalMoves.get(0);
		return bestMove;
	}
	
	
	
	private Pair recursiveAlphaBeta
	(
			WeightedAlphaBetaPruningNode n,
			int alpha,
			int beta,
			int e
	)
	{
		IBoard currentBoard = n.board;
		char player = n.player;
		int h = _heuristic.getHeuristic(currentBoard, player);
		if (n.isTerminal())
			return new Pair(x, y)
	}
}
