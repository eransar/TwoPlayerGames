import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WeightedOthelloBoard implements IBoard
{
	public int 		_size;
	public char[][] _boardGame;
	public int[][]	_boardRewards;
	public int 		_cellsLeft;
	public char		_player;
	
	
	public WeightedOthelloBoard
	(
		String problem
	)
	{
		importInstance(problem);
		_player = '1';
	}

	public WeightedOthelloBoard
	(
		int  size,
		char player
	)
	{
		_size 		= size;
		_cellsLeft 	= size * size - 4;
		_player		= player;
		createNewBoard();
		createNewRewardsBoard();
	}
	
	
	public WeightedOthelloBoard
	(
		int 		size,
		char[][] 	boardGame,
		int[][]		boardRewards,
		int			cellsLeft,
		char		player
	)
	{
		_size 			= size;
		_cellsLeft		= cellsLeft;
		_player			= player;
		_boardGame		= new char	[size][size];
		_boardRewards	= new int	[size][size];
		for (int row = 0; row < _size; row++)
			for (int col = 0; col < _size; col++)
			{
				_boardGame		[row][col] = boardGame		[row][col];
				_boardRewards	[row][col] = boardRewards	[row][col];
			}
	}
	
	
	public WeightedOthelloBoard
	(
		WeightedOthelloBoard toCopy
	)
	{
		this(	toCopy._size,
				toCopy._boardGame,
				toCopy._boardRewards,
				toCopy._cellsLeft,
				toCopy._player);
	}
	
	
	private void importInstance
	(
		String problemName
	)
	{
		// Get the file
		File file 	= new File(problemName); 
	    Scanner sc;
		try 
		{
			sc = new Scanner(file);
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			return;
		} 
	    
		// Read the file
	    while (sc.hasNextLine()) 
	    {
	    	String cuurentLine = sc.nextLine();
	    	if (cuurentLine.contains("Size:"))					// Board size
	    	{
	    		cuurentLine 	= sc.nextLine();
	    		_size 			= Integer.parseInt(cuurentLine);
	    		_boardGame 		= new char [_size][_size];
	    		_boardRewards 	= new int  [_size][_size];
	    	}
	    	else if (cuurentLine.contains("Board:"))			// Board instance
	    	{
	    		for (int row = 0; row < _size; row ++)
	    		{
	    			cuurentLine 	= sc.nextLine();
	    			String[] tokens = cuurentLine.split("\\|");
	    			for (int col = 0; col < _size; col ++)
	    			{
	    				_boardGame[row][col] = tokens[col].charAt(0);
	    				if (_boardGame[row][col] == '0')
	    					_cellsLeft++;
	    			}
	    		}
	    	}
	    	else if (cuurentLine.contains("Rewards:"))			// Board rewards
	    	{
	    		for (int row = 0; row < _size; row ++)
	    		{
	    			cuurentLine 	= sc.nextLine();
	    			String[] tokens = cuurentLine.split("\\|");
	    			for (int col = 0; col < _size; col ++)
	    			{
	    				_boardRewards[row][col] = Integer.parseInt(tokens[col]);
	    			}
	    		}
	    	}
	    }
	    sc.close();
	}
	
	
	public IBoard copyBoard()
	{
		return new WeightedOthelloBoard(this);
	}
	

	private void createNewBoard()
	{
		_boardGame = new char[_size][_size];
		for (int row = 0; row < _size; row++)
			for (int col = 0; col < _size; col++)
			{
				_boardGame[row][col] = '0';
			}
		_boardGame[(_size / 2) - 1 ][ (_size / 2) - 1  ] = '1';
        _boardGame[(_size / 2)     ][ (_size / 2)      ] = '1';
        _boardGame[(_size / 2) - 1 ][ (_size / 2)      ] = '2';
        _boardGame[(_size / 2)     ][ (_size / 2) - 1  ] = '2';
	}
	
	
	private void createNewRewardsBoard()
	{
		_boardRewards				= new int[_size][_size];
		Random 	random 				= new Random();
		int[] 	randomNumbersArray 	= new int[_size * _size];
		int		randomIndex;
		for (int i = 0; i < _size * _size; i ++)
			randomNumbersArray[i] = i;
		int		counter = 0;
		for (int row = 0; row < _size; row++)
			for (int col = 0; col < _size; col++)
			{
				randomIndex				= random.nextInt(_size * _size - counter);
				_boardRewards[row][col] = randomNumbersArray[randomIndex];
				randomNumbersArray[randomIndex] = randomNumbersArray[_size * _size - counter - 1];
				counter ++;
			}
	}
	
	public IBoard getNewChildBoard
	(
		IMove move
	)
	{
		if (move instanceof WeightedOthelloMove)
		{
			WeightedOthelloBoard childBoard = new WeightedOthelloBoard(this);
			childBoard.executePlayersMove(move);
			childBoard._player = this.getNextPlayer();
			if (childBoard.getLegalMoves().size() == 0)
			{
				childBoard._player = childBoard.getNextPlayer();
			}
			if (childBoard.getLegalMoves().size() == 0)
			{
				childBoard._player = 'n';
			}
			return childBoard;
		}
		return null;
	}
	
	
	@Override
	public boolean equals
	(
		Object otherBoard
	)
	{
		return this.hashCode() == otherBoard.hashCode();
	}
	
	
	@Override
	public int hashCode()
	{
		int hashCode = 0;
		for (int i = 0; i < _size; i++)
			for (int j = 0; j < _size; j++)
			{
				if (_boardGame[i][j] == '1')
					hashCode += (i + 1) * 2 + (j + 1) * 3; 
				else if (_boardGame[i][j] == '2')
					hashCode += (i + 1) * 5 + (j + 1) * 7; 
			}
		
		return hashCode;
	}
	
	@Override
	public String getBoardName()
	{
	   return "Othello " + _size + " x " + _size;
	}
	
	
	@Override
	public String toString()
	{
	   return Arrays.deepToString(_boardGame);
	}
	

	public boolean isTheGameOver()
	{
		if (_cellsLeft == 0)
			return true;
		if (_player == 'n')
            return true;
        return false;
	}
	

	public double getScore()
	{
		int player1score = 0;
		int player2score = 0;
		for (int i = 0; i < _size; i++)
			for (int j = 0; j < _size; j++)
				if 		(_boardGame[i][j] == '1')
					player1score += _boardRewards[i][j];
				else if (_boardGame[i][j] == '2')
					player2score += _boardRewards[i][j];
		return (double)(player1score - player2score);
	}
	

	public ArrayList<IMove> getLegalMoves()
    {
        ArrayList<IMove> legalMoves = new ArrayList<IMove>();
        for (int i = 0; i < _size; i++)
            for (int j = 0; j < _size; j++)
            {
            	WeightedOthelloMove currentMove = new WeightedOthelloMove(i, j);
                if (isLegalMove(currentMove))
                    legalMoves.add(currentMove);
            }
        return legalMoves;
    }
	
	
	
	public boolean isLegalMove
	(
		IMove 	move
	)
	{
		if (move instanceof WeightedOthelloMove)
		{
			int row	= ((WeightedOthelloMove)move)._row;
			int col	= ((WeightedOthelloMove)move)._col;
			
			if (row > _size - 1    	||
				col > _size - 1    	||
				row < 0         	||
				col < 0         	||
				_boardGame[row][col] != '0')
                return false;
			
            if (isLegalDown (row, col)              ||
                isLegalUp   (row, col)              ||
                isLegalRight(row, col)              ||
                isLegalLeft (row, col)              ||
                isLegalDiagonalDownLeft (row, col)  ||
                isLegalDiagonalDownRight(row, col)  ||
                isLegalDiagonalUpLeft   (row, col)  ||
                isLegalDiagonalUpRight  (row, col))
                return true;
		}
        return false;
	}
	
	
	public boolean executePlayersMove
	(
		IMove 	move
	)
	{
		if (move instanceof WeightedOthelloMove)
		{
			int row	= ((WeightedOthelloMove)move)._row;
			int col	= ((WeightedOthelloMove)move)._col;
			
			if (!isLegalMove(move))
	            return false;
	
	        _boardGame[row][col] = _player;
	
	        if (isLegalDown(row, col))
	            executeDown(row, col);
	
	        if (isLegalUp(row, col))
	        	executeUp(row, col);
	
	        if (isLegalRight(row, col))
	        	executeRight(row, col);
	
	        if (isLegalLeft(row, col))
	        	executeLeft(row, col);
	        
	        if (isLegalDiagonalDownLeft(row, col))
	        	executeDiagonalDownLeft(row, col);
	        
	        if (isLegalDiagonalDownRight(row, col))
	        	executeDiagonalDownRight(row, col);
	        
	        if (isLegalDiagonalUpLeft(row, col))
	        	executeDiagonalUpLeft(row, col);
	        
	        if (isLegalDiagonalUpRight(row, col))
	        	executeDiagonalUpRight(row, col);
	        
	        _cellsLeft--;
	        return true;
		}
		return false;
	}
	
	
	public char getCurrentPlayer()
	{
		return _player;
	}
	

	public char getNextPlayer()
	{
		if (_player == '1')
			return '2';
		if( _player == '2')
			return '1';
		return 'n';
	}
	
	
	private boolean isLegalDown
	(
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int i = row + 1; i < _size; i++) 
        {
            if (_boardGame[i][col] == '0')
                return false;
            if (_boardGame[i][col] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[i][col] == _player)
                return true;
            else if (_boardGame[i][col] == _player)
                return false;
        }
        return false;
	}
	
	
	private boolean isLegalUp
	(
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int i = row - 1; i >= 0; i--) 
        {
            if (_boardGame[i][col] == '0')
                return false;
            if (_boardGame[i][col] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[i][col] == _player)
                return true;
            else if (_boardGame[i][col] == _player)
                return false;
        }
        return false;
	}
	

	private boolean isLegalRight
	(
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int j = col + 1; j < _size; j++) 
        {
            if (_boardGame[row][j] == '0')
                return false;
            if (_boardGame[row][j] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[row][j] == _player)
                return true;
            else if (_boardGame[row][j] == _player)
                return false;
        }
        return false;
	}
	

	private boolean isLegalLeft
	(
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int j = col - 1; j >= 0; j--) 
        {
            if (_boardGame[row][j] == '0')
                return false;
            if (_boardGame[row][j] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[row][j] == _player)
                return true;
            else if (_boardGame[row][j] == _player)
                return false;
        }
        return false;
	}
	
	
	private boolean isLegalDiagonalUpRight
	(
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int i = row - 1, j = col + 1; i >= 0 && j < _size; i--, j++)
        {
            if (_boardGame[i][j] == '0')
                return false;
            if (_boardGame[i][j] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[i][j] == _player)
                return true;
            else if (_boardGame[i][j] == _player)
                return false;
        }
        return false;
	}

	
	private boolean isLegalDiagonalUpLeft
	(
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
        {
            if (_boardGame[i][j] == '0')
                return false;
            if (_boardGame[i][j] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[i][j] == _player)
                return true;
            else if (_boardGame[i][j] == _player)
                return false;
        }
        return false;
	}
	
	
	private boolean isLegalDiagonalDownRight
	(
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int i = row + 1, j = col + 1; i < _size && j < _size; i++, j++)
        {
            if (_boardGame[i][j] == '0')
                return false;
            if (_boardGame[i][j] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[i][j] == _player)
                return true;
            else if (_boardGame[i][j] == _player)
                return false;
        }
        return false;
	}
	
	
	private boolean isLegalDiagonalDownLeft
	(
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int i = row + 1, j = col - 1; i < _size && j >= 0; i++, j--)
        {
            if (_boardGame[i][j] == '0')
                return false;
            if (_boardGame[i][j] == getNextPlayer())
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[i][j] == _player)
                return true;
            else if (_boardGame[i][j] == _player)
                return false;
        }
        return false;
	}
	
	
	private boolean executeDown
    (
        int     row,
        int     col
    )
    {
        for (int i = row + 1; i < _size; i++) 
        {
            if (_boardGame[i][col] == getNextPlayer())
                _boardGame[i][col] = _player;
            else if (_boardGame[i][col] == _player)
                return true;
        }
        return false;
    }
	
	
	private boolean executeUp
    (
        int     row,
        int     col
    )
    {
        for (int i = row - 1; i >= 0; i--)
        {
            if (_boardGame[i][col] == getNextPlayer())
                _boardGame[i][col] = _player;
            else if (_boardGame[i][col] == _player)
                return true;
        }
        return false;
    }
	
	
	private boolean executeRight
    (
        int     row,
        int     col
    )
    {
		for (int j = col + 1; j < _size; j++)
		{
            if (_boardGame[row][j] == getNextPlayer())
                _boardGame[row][j] = _player;
            else if (_boardGame[row][j] == _player)
                return true;
        }
        return false;
    }
	
	
	private boolean executeLeft
    (
        int     row,
        int     col
    )
    {
		for (int j = col - 1; j >= 0; j--)
		{
            if (_boardGame[row][j] == getNextPlayer())
                _boardGame[row][j] = _player;
            else if (_boardGame[row][j] == _player)
                return true;
        }
        return false;
    }
	
	
	private boolean executeDiagonalUpRight
    (
        int     row,
        int     col
    )
    {
		for (int i = row - 1, j = col + 1; i >= 0 && j < _size; i--, j++)
		{
            if (_boardGame[i][j] == getNextPlayer())
                _boardGame[i][j] = _player;
            else if (_boardGame[i][j] == _player)
                return true;
        }
        return false;
    }
	
	
	
	private boolean executeDiagonalUpLeft
    (
        int     row,
        int     col
    )
    {
		for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
		{
            if (_boardGame[i][j] == getNextPlayer())
                _boardGame[i][j] = _player;
            else if (_boardGame[i][j] == _player)
                return true;
        }
        return false;
    }
	
	
	private boolean executeDiagonalDownRight
    (
        int     row,
        int     col
    )
    {
		for (int i = row + 1, j = col + 1; i < _size && j < _size; i++, j++)
		{
            if (_boardGame[i][j] == getNextPlayer())
                _boardGame[i][j] = _player;
            else if (_boardGame[i][j] == _player)
                return true;
        }
        return false;
    }
	
	
	private boolean executeDiagonalDownLeft
    (
        int     row,
        int     col
    )
    {
		for (int i = row + 1, j = col - 1; i < _size && j >= 0; i++, j--)
		{
            if (_boardGame[i][j] == getNextPlayer())
                _boardGame[i][j] = _player;
            else if (_boardGame[i][j] == _player)
                return true;
        }
        return false;
    }


	@Override
	public void printBoard()  
	{
		String s = "";
		int rows = _size;
		int cols = _size;
		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col< cols; col++)
			{
				if 		(_boardGame[row][col] == '1')
					s += " | 1";
				else if (_boardGame[row][col] == '2')
					s += " | 2";
				else
					s += " |  ";
			}
			s += " |\n";
		}
		System.out.println(s);
		printRewards();
	}
	
	private void printRewards()  
	{
		String s = "";
		int rows = _size;
		int cols = _size;
		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col< cols; col++)
			{
				s += "|" + _boardRewards[row][col];

			}
			s += "|\n";
		}
		System.out.println(s);
	}


}
