import java.util.Random;

import org.omg.CORBA._PolicyStub;

public class OthelloHeuristic implements IHeuristic
{
	public boolean noise = false;
	
	public double getHeuristic
	(
			INode node
	)
	{
		IBoard board 			= node.getBoard();
		if (board instanceof OthelloBoard)
		{
			OthelloBoard 	othelloBoard = (OthelloBoard)board;
			int				cellsCount	 = othelloBoard._size * othelloBoard._size;
			
			Pair 	coins 			= getPlayersCoins(othelloBoard);
			double 	MaxPlayerCoins 	= (double)coins.x;
			double 	MinPlayerCoins 	= (double)coins.y;
			double 	CoinParityHeuristicValue =
						cellsCount * (MaxPlayerCoins - MinPlayerCoins) / (MaxPlayerCoins + MinPlayerCoins);
			
			Pair 	moves 			= getPlayersMoves(othelloBoard);
			double 	MaxPlayerMoves 	= (double)moves.x;
			double 	MinPlayerMoves 	= (double)moves.y; 
			double 	MobilityHeuristicValue;
			if (MaxPlayerMoves + MinPlayerMoves != 0)
				MobilityHeuristicValue =
						cellsCount * (MaxPlayerMoves - MinPlayerMoves) / (MaxPlayerMoves + MinPlayerMoves);
			else
				MobilityHeuristicValue = 0;
			
			Pair 	corners 		 = getPlayersCorners(othelloBoard);
			double 	MaxPlayerCorners = (double)corners.x;
			double 	MinPlayerCorners = (double)corners.y; 
			double 	CornerHeuristicValue;
			if (MaxPlayerCorners + MinPlayerCorners != 0)
				CornerHeuristicValue =
						cellsCount * (MaxPlayerCorners - MinPlayerCorners) / (MaxPlayerCorners + MinPlayerCorners);
			else
				CornerHeuristicValue = 0;
			
			double weight = 1.0 / 3.0;
			double heuristic = (weight * CoinParityHeuristicValue  +
								weight * MobilityHeuristicValue	+
								weight * CornerHeuristicValue);
			Random rand = new Random();
			double randomValue = cellsCount * (rand.nextDouble() * 2 - 1);
			weight = 1.0/4.0;
			/*if(noise)
				heuristic = (weight * CoinParityHeuristicValue  +
							weight * MobilityHeuristicValue	+
							weight * CornerHeuristicValue)+
							weight * randomValue;
			*/
			//if (nodeType == INode.NodeType.MIN)
			//	heuristic *= -1;
			return heuristic;
		}
	
		return 0;
	}

	private Pair getPlayersCoins
	(
		OthelloBoard othelloBoard
	) 
	{
		double maxCoins = 0;
		double minCoins = 0;
		char[][] board = othelloBoard._boardGame;
		for (int i = 0; i < othelloBoard._size; i++)
			for (int j = 0; j < othelloBoard._size; j++)
			{
				if (board[i][j] == '1')
					maxCoins++;
				else if (board[i][j] == '2')
					minCoins++;
			}
		return new Pair(maxCoins, minCoins);
	}
	
	private Pair getPlayersMoves
	(
		OthelloBoard othelloBoard
	) 
	{
		double maxMoves = othelloBoard.getLegalMoves().size();
		double minMoves = othelloBoard.getNextPlayerLegalMoves().size();
		if (othelloBoard.getCurrentPlayer() == '2')
		{
			double temp = maxMoves;
			maxMoves	= minMoves;
			minMoves	= temp;
		}
		return new Pair(maxMoves, minMoves);
	}


	private Pair getPlayersCorners
	(
		OthelloBoard othelloBoard
	) 
	{
		double 	 maxCorners = 0;
		double 	 minCorners = 0;
		char[][] board    	= othelloBoard._boardGame;
		int    	 size		= othelloBoard._size - 1;
		Pair[] 	 corners    = new Pair[] {	new Pair(0, 0),
											new Pair(0, size),
											new Pair(size, 0),
											new Pair(size, size)};
		for (Pair corner : corners) 
		{
			int x = (int)corner.x;
			int y = (int)corner.y;
			if 		(board[x][y] == '1')
				maxCorners++;
			else if (board[x][y] == '2')
				minCorners++;
		}
		return new Pair(maxCorners, minCorners);
	}

}
