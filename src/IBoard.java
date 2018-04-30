import java.util.ArrayList;

public interface IBoard 
{
	public IBoard 			copyBoard();
	
	public ArrayList<IMove> getLegalMoves();
	
	public boolean 			isTheGameOver();
	
	public IBoard 			getNewChildBoard(IMove move);
	
	public char 			getCurrentPlayer();
	
	public char 			getNextPlayer();
	
	public double			getScore();
	
	public String 			getBoardName();
	
	public double 			getChance();
	
	public void 			printBoard();

}
