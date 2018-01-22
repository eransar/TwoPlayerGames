import java.util.ArrayList;

public class MCTS implements ISolver
{
	public IMove play
	(
			IGame 		game,
			IHeuristic 	heuristic,
			IBoard 		board,
			char 		player,
			Object[]	parameters
	)
	{
		ArrayList<IMove> legalMoves = board.getLegalMoves(player);
		IMove bestMove 				= legalMoves.get(0);
		return bestMove;
	}
}
