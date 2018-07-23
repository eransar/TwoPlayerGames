import java.util.ArrayList;
import java.util.Arrays;

public class OthelloBoard implements IBoard
{
	public int 		_size;
	public char[][] _boardGame;
	public int 		_cellsLeft;
	public char		_player;
	
	/**
	 * Othello Board Constructor
	 * <p>
	 * @param	size	The size of the rows/columns of the board 
	 */
	public OthelloBoard
	(
		int  size,
		char player
	)
	{
		_size 		= size;
		_cellsLeft 	= size * size - 4;
		_player		= player;
		createNewBoard();
	}
	
	
	/**
	 * Othello Board Copy Constructor
	 * <p>
	 * @param	size		The size of the rows/columns of the board 
	 * @param	boardGame	A board game to copy
	 * @param	cellsLeft	The number of empty cells
	 */
	public OthelloBoard
	(
		int 		size,
		char[][] 	boardGame,
		int			cellsLeft,
		char		player
	)
	{
		_size 		= size;
		_cellsLeft	= cellsLeft;
		_player		= player;
		_boardGame	= new char[size][size];
		for (int row = 0; row < _size; row++)
			for (int col = 0; col < _size; col++)
			{
				_boardGame[row][col] = boardGame[row][col];
			}
	}
	
	
	/**
	 * Othello Board Copy Constructor
	 * <p>
	 * @param	toCopy	The board that needed to be copied
	 */
	public OthelloBoard
	(
		OthelloBoard toCopy
	)
	{
		this(	toCopy._size,
				toCopy._boardGame,
				toCopy._cellsLeft,
				toCopy._player);
	}
	
	public IBoard copyBoard()
	{
		return new OthelloBoard(this);
	}
	
	/**
	 * Creating a new Othello board game
	 */
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
	
	public IBoard getNewChildBoard
	(
		IMove move
	)
	{
		if (move instanceof OthelloMove)
		{
			OthelloBoard childBoard = new OthelloBoard(this);
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
	
	
	/**
	 * Othello Board compare between this and other board
	 * <p>
	 * @param		otherBoard	Other board to compare to
	 * @return      true, if the two boards are equal. false, otherwise
	 */
	@Override
	public boolean equals
	(
		Object otherBoard
	)
	{
		// todo: check if otherBoard is othello
		return this.hashCode() == otherBoard.hashCode();
	}
	
	
	/**
	 * Othello Board hash code
	 * <p>
	 * @return      this board hash code
	 */
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
	
	/**
	 * Othello Board name
	 * <p>
	 * @return      this boarg game name
	 */
	@Override
	public String getBoardName()
	{
	   return "Othello " + _size + " x " + _size;
	}
	
	
	/**
	 * Othello Board to string
	 * <p>
	 * @return      this board as string
	 */
	@Override
	public String toString()
	{
	   return Arrays.deepToString(_boardGame);
	}
	
	/**
	 * Check if the game is over
	 * <p>
	 * @return      true, if the game is over. false, otherwise
	 */
	public boolean isTheGameOver()
	{
		if (_cellsLeft == 0)
			return true;
		if (_player == 'n')
            return true;
        return false;
	}
	
	/**
	 * Get the game score
	 * <p>
	 * @return      double of (player1score - player2score)
	 */
	public double getScore()
	{
		int player1score = 0;
		int player2score = 0;
		for (int i = 0; i < _size; i++)
			for (int j = 0; j < _size; j++)
				if 		(_boardGame[i][j] == '1')
					player1score++;
				else if (_boardGame[i][j] == '2')
					player2score++;
		return (double)(player1score - player2score);
	}
	
	/**
	 * get the legal available moves
	 * <p>
	 * @return      all the legal moves
	 */
	public ArrayList<IMove> getLegalMoves()
    {
        ArrayList<IMove> legalMoves = new ArrayList<IMove>();
        for (int i = 0; i < _size; i++)
            for (int j = 0; j < _size; j++)
            {
            	OthelloMove currentMove = new OthelloMove(i, j);
                if (isLegalMove(currentMove))
                    legalMoves.add(currentMove);
            }
        return legalMoves;
    }
	
	/**
	 * get the legal available moves of the other player
	 * <p>
	 * @return      all the legal moves of the other player
	 */
	public ArrayList<IMove> getNextPlayerLegalMoves()
    {
        _player 					= getNextPlayer();
        ArrayList<IMove> legalMoves = getLegalMoves();
        _player 					= getNextPlayer();
        return legalMoves;
    }
	
	
	/**
	 * is it a legal move
	 * <p>
	 * @param		move	the selected move
	 * @return      is it a legal move
	 */
	public boolean isLegalMove
	(
		IMove 	move
	)
	{
		if (move instanceof OthelloMove)
		{
			int row	= ((OthelloMove)move)._row;
			int col	= ((OthelloMove)move)._col;
			
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
	
	
	/**
	 * Execute a given move
	 * <p>
	 * @param		move		the selected move
	 * @return      is the execution succeed
	 */
	public boolean executePlayersMove
	(
		IMove 	move
	)
	{
		if (move instanceof OthelloMove)
		{
			int row	= ((OthelloMove)move)._row;
			int col	= ((OthelloMove)move)._col;
			
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
	
	/**
	 * get the corrunt player character
	 * <p>
	 * @return      the character of the other player
	 */
	public char getCurrentPlayer()
	{
		return _player;
	}
	
	/**
	 * get the other player character
	 * <p>
	 * @return      the character of the other player
	 */
	public char getNextPlayer()
	{
		if (_player == '1')
			return '2';
		if( _player == '2')
			return '1';
		return 'n';
	}
	
	
	/**
	 * is it a legal down move
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal down move
	 */
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
	
	
	/**
	 * is it a legal up move
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal up move
	 */
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
	
	
	/**
	 * is it a legal right move
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal right move
	 */
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
	
	
	/**
	 * is it a legal left move
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal left move
	 */
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
	
	
	/**
	 * is it a legal up-right move
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal up-right move
	 */
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
	
	
	/**
	 * is it a legal up-left move
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal up-left move
	 */
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
	
	
	/**
	 * is it a legal down-right move
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal down-right move
	 */
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
	
	
	/**
	 * is it a legal down-left move
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal down-left move
	 */
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
	
	
	/**
	 * execute a player move (down)
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
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
	
	
	/**
	 * execute a player move (up)
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
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
	
	
	/**
	 * execute a player move (right)
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
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
	
	
	/**
	 * execute a player move (left)
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
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
	
	
	/**
	 * execute a player move (up-right)
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
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
	
	
	/**
	 * execute a player move (up-left)
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
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
	
	
	/**
	 * execute a player move (down-right)
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
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
	
	
	/**
	 * execute a player move (down-left)
	 * <p>
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
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
	public void printBoard()  // not tested yet
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
	}


	@Override
	public double getChance() 
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
