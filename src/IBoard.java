import java.util.ArrayList;

public interface IBoard 
{
	public ArrayList<IMove> getLegalMoves
	(
		char player
	);
	
	public boolean isTheGameOver();
}
