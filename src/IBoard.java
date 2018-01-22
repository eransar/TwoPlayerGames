import java.util.ArrayList;

public interface IBoard 
{
	public ArrayList<IMove> getLegalMoves
	(
		char player
	);
	
	public boolean isTheGameOver();
	
	public IBoard getNewChildBoard
	(
		IMove move,
		char  player
	);
	
	public char getOtherPlayer
	(
		char player
	);
}
