import java.util.ArrayList;

public class DiceHeuristic implements IHeuristic
{

	@Override
	public double getHeuristic
	(
		INode node
	) 
	{
		IBoard board 			= node.getBoard();
		if (board instanceof DiceBoard)
		{
			DiceBoard diceBoard = (DiceBoard)board;
			int player1pairs = countPairsForPlayer(diceBoard, '1');
			int player2pairs = countPairsForPlayer(diceBoard, '2');
			return ((double)player1pairs) / ((double)player1pairs + (double)player2pairs);
		}
		return (1/2);
	}

	private int countPairsForPlayer
	(
		DiceBoard 	diceBoard,
		char 		player
	)
	{
		int pairsCount = 1;
		ArrayList<Pair> possiblePairs = new ArrayList<Pair>();
		possiblePairs.add(new Pair(0, 1));
		possiblePairs.add(new Pair(0, 2));
		possiblePairs.add(new Pair(1, 0));
		possiblePairs.add(new Pair(2, 0));
		possiblePairs.add(new Pair(1, 1));
		possiblePairs.add(new Pair(2, 2));
		possiblePairs.add(new Pair(-1, -1));
		possiblePairs.add(new Pair(-2, -2));
		int size = diceBoard._size;
		for (int row = 0; row < size; row++)
			for (int col = 0; col < size; col++)
			{
				for (Pair distance : possiblePairs)
				{
					Pair currentLocation = new Pair(row, col);
					if (checkPair(diceBoard, player, currentLocation, distance))
						pairsCount++;
				}
			}
		return pairsCount;
	}
	
	private boolean checkPair
	(
		DiceBoard 	diceBoard,
		char 		player,
		Pair		location,
		Pair		distanceFromLocation
	)
	{
		char[][] board = diceBoard._boardGame;
		int x1 = (int)location.x;
		int y1 = (int)location.y;
		int x2 = (int)location.x + (int)distanceFromLocation.x;
		int y2 = (int)location.y + (int)distanceFromLocation.y;
		if (!diceBoard.isLegalLocation(x2, y2))
			return false;
		int x3 = (x1 + x2) / 2;
		int y3 = (y1 + y2) / 2;
		boolean distanceOfTwo = (int)distanceFromLocation.x == 2 || (int)distanceFromLocation.y == 2;
		if (board[x1][y1] != player 	|| 
			board[x2][y2] != player)
			return false;
		if (distanceOfTwo && board[x3][y3] != '0')
			return false;
		return true;
	}
}
