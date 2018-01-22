
public interface ISolver 
{
	public IMove play
	(
			IGame 		game,
			IHeuristic 	heuristic,
			IBoard 		board,
			char 		player,
			Object[]	parameters
	);
}
