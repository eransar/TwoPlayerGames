import java.util.ArrayList;
import java.util.Arrays;

public class OthelloBoard implements IBoard
{
	public int 		_size;
	public char[][] _boardGame;
	public int 		_cellsLeft;
	
	/**
	 * Othello Board Constructor
	 * <p>
	 * @param	size	The size of the rows/columns of the board 
	 */
	public OthelloBoard
	(
		int size
	)
	{
		_size 		= size;
		_cellsLeft 	= size * size - 4;
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
		int			cellsLeft
	)
	{
		_size 		= size;
		_cellsLeft	= cellsLeft;
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
				toCopy._cellsLeft);
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
		IMove move,
		char  player
	)
	{
		if(move instanceof OthelloMove)
		{
			OthelloBoard childBoard = new OthelloBoard(this);
			childBoard.executePlayersMove(player, move);
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
		int hashCode = Arrays.hashCode(_boardGame);
		return hashCode;
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
		if (	getLegalMoves('1').size() == 0	&&
				getLegalMoves('2').size() == 0)
                return true;
        return false;
	}
	
	
	/**
	 * get the legal available moves for the given player
	 * <p>
	 * @param		player		the current player
	 * @return      all the legal moves
	 */
	public ArrayList<IMove> getLegalMoves(char player)
    {
        ArrayList<IMove> legalMoves = new ArrayList<IMove>();
        for (int i = 0; i < _size; i++)
            for (int j = 0; j < _size; j++)
            {
            	OthelloMove currentMove = new OthelloMove(i, j);
                if (isLegalMove(player, currentMove))
                    legalMoves.add(currentMove);
            }
        return legalMoves;
    }
	
	
	/**
	 * is it a legal move
	 * <p>
	 * @param		player		the current player
	 * @param		move		the selected move
	 * @return      is it a legal move
	 */
	public boolean isLegalMove
	(
		char 	player,
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
			
            if (isLegalDown (player, row, col)              ||
                isLegalUp   (player, row, col)              ||
                isLegalRight(player, row, col)              ||
                isLegalLeft (player, row, col)              ||
                isLegalDiagonalDownLeft (player, row, col)  ||
                isLegalDiagonalDownRight(player, row, col)  ||
                isLegalDiagonalUpLeft   (player, row, col)  ||
                isLegalDiagonalUpRight  (player, row, col))
                return true;
		}
        return false;
	}
	
	
	/**
	 * Execute a given player's move
	 * <p>
	 * @param		player		the current player
	 * @param		move		the selected move
	 * @return      is the execution succeed
	 */
	public boolean executePlayersMove
	(
		char 	player,
		IMove 	move
	)
	{
		if (move instanceof OthelloMove)
		{
			int row	= ((OthelloMove)move)._row;
			int col	= ((OthelloMove)move)._col;
			
			if (!isLegalMove(player, move))
	            return false;
	
	        _boardGame[row][col] = player;
	
	        if (isLegalDown(player, row, col))
	            executeDown(player, row, col);
	
	        if (isLegalUp(player, row, col))
	        	executeUp(player, row, col);
	
	        if (isLegalRight(player, row, col))
	        	executeRight(player, row, col);
	
	        if (isLegalLeft(player, row, col))
	        	executeLeft(player, row, col);
	        
	        if (isLegalDiagonalDownLeft(player, row, col))
	        	executeDiagonalDownLeft(player, row, col);
	        
	        if (isLegalDiagonalDownRight(player, row, col))
	        	executeDiagonalDownRight(player, row, col);
	        
	        if (isLegalDiagonalUpLeft(player, row, col))
	        	executeDiagonalUpLeft(player, row, col);
	        
	        if (isLegalDiagonalUpRight(player, row, col))
	        	executeDiagonalUpRight(player, row, col);
	        
	        _cellsLeft--;
	        return true;
		}
		return false;
	}
	
	
	/**
	 * get the other player character
	 * <p>
	 * @param		player		the current player
	 * @return      the character of the other player
	 */
	public char getOtherPlayer
	(
		char player
	)
	{
		if (player == '1')
			return '2';
		if( player == '2')
			return '1';
		return '0';
	}
	
	
	/**
	 * is it a legal down move
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal down move
	 */
	private boolean isLegalDown
	(
		char	player,
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int i = row + 1; i < _size; i++) 
        {
            if (_boardGame[i][col] == '0')
                return false;
            if (_boardGame[i][col] == getOtherPlayer(player))
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[i][col] == player)
                return true;
            else if (_boardGame[i][col] == player)
                return false;
        }
        return false;
	}
	
	
	/**
	 * is it a legal up move
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal up move
	 */
	private boolean isLegalUp
	(
		char	player,
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int i = row - 1; i >= 0; i--) 
        {
            if (_boardGame[i][col] == '0')
                return false;
            if (_boardGame[i][col] == getOtherPlayer(player))
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[i][col] == player)
                return true;
            else if (_boardGame[i][col] == player)
                return false;
        }
        return false;
	}
	
	
	/**
	 * is it a legal right move
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal right move
	 */
	private boolean isLegalRight
	(
		char	player,
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int j = col + 1; j < _size; j++) 
        {
            if (_boardGame[row][j] == '0')
                return false;
            if (_boardGame[row][j] == getOtherPlayer(player))
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[row][j] == player)
                return true;
            else if (_boardGame[row][j] == player)
                return false;
        }
        return false;
	}
	
	
	/**
	 * is it a legal left move
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal left move
	 */
	private boolean isLegalLeft
	(
		char	player,
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int j = col - 1; j >= 0; j--) 
        {
            if (_boardGame[row][j] == '0')
                return false;
            if (_boardGame[row][j] == getOtherPlayer(player))
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[row][j] == player)
                return true;
            else if (_boardGame[row][j] == player)
                return false;
        }
        return false;
	}
	
	
	/**
	 * is it a legal up-right move
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal up-right move
	 */
	private boolean isLegalDiagonalUpRight
	(
		char	player,
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int i = row - 1, j = col + 1; i >= 0 && j < _size; i--, j++)
        {
            if (_boardGame[i][j] == '0')
                return false;
            if (_boardGame[i][j] == getOtherPlayer(player))
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[i][j] == player)
                return true;
            else if (_boardGame[i][j] == player)
                return false;
        }
        return false;
	}
	
	
	/**
	 * is it a legal up-left move
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal up-left move
	 */
	private boolean isLegalDiagonalUpLeft
	(
		char	player,
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
        {
            if (_boardGame[i][j] == '0')
                return false;
            if (_boardGame[i][j] == getOtherPlayer(player))
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[i][j] == player)
                return true;
            else if (_boardGame[i][j] == player)
                return false;
        }
        return false;
	}
	
	
	/**
	 * is it a legal down-right move
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal down-right move
	 */
	private boolean isLegalDiagonalDownRight
	(
		char	player,
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int i = row + 1, j = col + 1; i < _size && j < _size; i++, j++)
        {
            if (_boardGame[i][j] == '0')
                return false;
            if (_boardGame[i][j] == getOtherPlayer(player))
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[i][j] == player)
                return true;
            else if (_boardGame[i][j] == player)
                return false;
        }
        return false;
	}
	
	
	/**
	 * is it a legal down-left move
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is it a legal down-left move
	 */
	private boolean isLegalDiagonalDownLeft
	(
		char	player,
		int		row,
		int		col
	)
	{
		boolean otherPlayerFlag = false;
        for (int i = row + 1, j = col - 1; i < _size && j >= 0; i++, j--)
        {
            if (_boardGame[i][j] == '0')
                return false;
            if (_boardGame[i][j] == getOtherPlayer(player))
                otherPlayerFlag = true;
            else if (otherPlayerFlag && _boardGame[i][j] == player)
                return true;
            else if (_boardGame[i][j] == player)
                return false;
        }
        return false;
	}
	
	
	/**
	 * execute a player move (down)
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
	private boolean executeDown
    (
        char    player,
        int     row,
        int     col
    )
    {
        for (int i = row + 1; i < _size; i++) 
        {
            if (_boardGame[i][col] == getOtherPlayer(player))
                _boardGame[i][col] = player;
            else if (_boardGame[i][col] == player)
                return true;
        }
        return false;
    }
	
	
	/**
	 * execute a player move (up)
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
	private boolean executeUp
    (
        char    player,
        int     row,
        int     col
    )
    {
        for (int i = row - 1; i >= 0; i--)
        {
            if (_boardGame[i][col] == getOtherPlayer(player))
                _boardGame[i][col] = player;
            else if (_boardGame[i][col] == player)
                return true;
        }
        return false;
    }
	
	
	/**
	 * execute a player move (right)
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
	private boolean executeRight
    (
        char    player,
        int     row,
        int     col
    )
    {
		for (int j = col + 1; j < _size; j++)
		{
            if (_boardGame[row][j] == getOtherPlayer(player))
                _boardGame[row][j] = player;
            else if (_boardGame[row][j] == player)
                return true;
        }
        return false;
    }
	
	
	/**
	 * execute a player move (left)
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
	private boolean executeLeft
    (
        char    player,
        int     row,
        int     col
    )
    {
		for (int j = col - 1; j >= 0; j--)
		{
            if (_boardGame[row][j] == getOtherPlayer(player))
                _boardGame[row][j] = player;
            else if (_boardGame[row][j] == player)
                return true;
        }
        return false;
    }
	
	
	/**
	 * execute a player move (up-right)
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
	private boolean executeDiagonalUpRight
    (
        char    player,
        int     row,
        int     col
    )
    {
		for (int i = row - 1, j = col + 1; i >= 0 && j < _size; i--, j++)
		{
            if (_boardGame[i][j] == getOtherPlayer(player))
                _boardGame[i][j] = player;
            else if (_boardGame[i][j] == player)
                return true;
        }
        return false;
    }
	
	
	/**
	 * execute a player move (up-left)
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
	private boolean executeDiagonalUpLeft
    (
        char    player,
        int     row,
        int     col
    )
    {
		for (int i = row - 1, j = col - 1; i >= 0 && j >= 0; i--, j--)
		{
            if (_boardGame[i][j] == getOtherPlayer(player))
                _boardGame[i][j] = player;
            else if (_boardGame[i][j] == player)
                return true;
        }
        return false;
    }
	
	
	/**
	 * execute a player move (down-right)
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
	private boolean executeDiagonalDownRight
    (
        char    player,
        int     row,
        int     col
    )
    {
		for (int i = row + 1, j = col + 1; i < _size && j < _size; i++, j++)
		{
            if (_boardGame[i][j] == getOtherPlayer(player))
                _boardGame[i][j] = player;
            else if (_boardGame[i][j] == player)
                return true;
        }
        return false;
    }
	
	
	/**
	 * execute a player move (down-left)
	 * <p>
	 * @param		player		the current player
	 * @param		row			the selected row
	 * @param		col			the selected column
	 * @return      is the execution succeed
	 */
	private boolean executeDiagonalDownLeft
    (
        char    player,
        int     row,
        int     col
    )
    {
		for (int i = row + 1, j = col - 1; i < _size && j >= 0; i++, j--)
		{
            if (_boardGame[i][j] == getOtherPlayer(player))
                _boardGame[i][j] = player;
            else if (_boardGame[i][j] == player)
                return true;
        }
        return false;
    }
}
