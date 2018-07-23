import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DiceBoard implements IBoard
{
	public int 			_size;
	public char[][]	 	_boardGame;
	public int 			_cellsLeft;
	public char			_player;	// 1 -> row | 2 -> cols | 0 -> chance
	public int			_selectedRowOrCol;
	public int 			_winLength;
	public char			_winner;
	public DiceBoard	_parent;
	public int			_depth;
	public int			_seed;
	
	/**
	 * Dice Board Constructor
	 * <p>
	 * @param	size	The size of the rows/columns of the board 
	 */
	public DiceBoard
	(
		int  size,
		int  winLength
	)
	{
		this(size, '0', winLength);
	}
	
	/**
	 * Dice Board Constructor
	 * <p>
	 * @param	size	The size of the rows/columns of the board 
	 */
	public DiceBoard
	(
		int  size,
		int  winLength,
		int  depth,
		int  seed
	)
	{
		this(size, '0', winLength);
		_depth 	= depth;
		_seed	= seed;
		Random rand = new Random(seed);
		for(int d = 0; d < depth; d++)
		{
			for (int player = 1; player <= 2; player++)
			{
				boolean done = false;
				while(!done)
				{
					int row = rand.nextInt(_size);
					int col = rand.nextInt(_size);
					if (_boardGame[row][col] == '0')
					{
						if 		(player == 1)
							_boardGame[row][col] = '1';
						else if (player == 2)
							_boardGame[row][col] = '2';
						done = true;
					}
				}
			}
		}
		
	}
	
	/**
	 * Dice Board Constructor
	 * <p>
	 * @param	size	The size of the rows/columns of the board 
	 */
	public DiceBoard
	(
		int  size,
		char player,
		int  winLength
	)
	{
		_size 		= size;
		_cellsLeft 	= size * size;
		_player		= player;
		_winLength 	= winLength;
		createNewBoard();
	}

	/**
	 * Dice Board Copy Constructor
	 * <p>
	 * @param	size				The size of the rows/columns of the board 
	 * @param	boardGame			A board game to copy
	 * @param	cellsLeft			The number of empty cells
	 * @param	selectedRowOrCol	The selected row or col (by a dice)
	 */
	public DiceBoard
	(
		int 		size,
		char[][] 	boardGame,
		int			cellsLeft,
		char		player,
		int			selectedRowOrCol,
		int			winLength,
		int 		seed,
		int			depth
	)
	{
		_size 				= size;
		_cellsLeft			= cellsLeft;
		_player				= player;
		_selectedRowOrCol 	= selectedRowOrCol;
		_winLength			= winLength;
		_boardGame			= new char[size][size];
		_seed				= seed;
		_depth				= depth;
		for (int row = 0; row < _size; row++)
			for (int col = 0; col < _size; col++)
			{
				_boardGame[row][col] = boardGame[row][col];
			}
	}
	
	
	/**
	 * Dice Board Copy Constructor
	 * <p>
	 * @param	toCopy	The board that needed to be copied
	 */
	public DiceBoard
	(
		DiceBoard toCopy
	)
	{
		this(	toCopy._size,
				toCopy._boardGame,
				toCopy._cellsLeft,
				toCopy._player,
				toCopy._selectedRowOrCol,
				toCopy._winLength,
				toCopy._seed,
				toCopy._depth);
	}
	
	@Override
	public IBoard copyBoard() 
	{
		return new DiceBoard(this);
	}
	
	/**
	 * Creating a new Dice board game
	 */
	private void createNewBoard()
	{
		_boardGame = new char[_size][_size];
		for (int row = 0; row < _size; row++)
			for (int col = 0; col < _size; col++)
			{
				_boardGame[row][col] = '0';
			}
	}

	@Override
	public List<IMove> getLegalMoves() 
	{
		List<IMove> legalMoves 	= new ArrayList<IMove>();
		IMove		currentMove = null; 
		for (int i = 0; i < _size; i++)
		{
			currentMove = null; 
			if 		(_player == '1')
			{
				if (isLegalLocation(_selectedRowOrCol, i)	&&
					_boardGame[_selectedRowOrCol][i] == '0')
					currentMove = new DiceMove(_selectedRowOrCol, i);
			}
			else if (_player == '2')
			{
				if (isLegalLocation(i, _selectedRowOrCol)	&&
					_boardGame[_selectedRowOrCol][i] == '0')
					currentMove = new DiceMove(i, _selectedRowOrCol);
			}
			else if (_player == '0')
			{
				if 		(_parent == null)
					currentMove = new DiceMove(i);
				else if (_parent._player == '1'		&& checkCol(i))
					currentMove = new DiceMove(i);
				else if (_parent._player == '2'		&& checkRow(i))
					currentMove = new DiceMove(i);
					
			}
			if (currentMove != null)
				legalMoves.add(currentMove);
		}
		return legalMoves;
	}

	private boolean checkRow
	(
		int row
	) 
	{
		for (int i = 0; i < _size; i++)
		{
			if (_boardGame[row][i] == '0')
				return true;
		}
		return false;
	}
	
	private boolean checkCol
	(
		int col
	) 
	{
		for (int i = 0; i < _size; i++)
		{
			if (_boardGame[i][col] == '0')
				return true;
		}
		return false;
	}
	
	@Override
	public boolean isTheGameOver() 
	{
		if (_cellsLeft == 0)
			return true;
		for (int row = 0; row < _size; row++)
			for (int col = 0; col < _size; col++)
			{
				if (_boardGame[row][col] == '0')
					continue;
				if (checkVertical		(row, col)	||
					checkHorizontal		(row, col) 	||
					checkDiagonalRight	(row, col) 	||
					checkDiagonalLeft	(row, col))
					return true;
			}
		return false;
	}
	
	private boolean checkVertical
	(
		int row, 
		int col
	)
	{
		char potentialWinner = _boardGame[row][col];
		for (int i = 1; i < _winLength; i++)
		{
			if (!isLegalLocation(row + i, col))
				return false;
			if (_boardGame[row + i][col] != potentialWinner)
				return false;
		}
		_winner = potentialWinner;
		return true;
	}
	
	private boolean checkHorizontal
	(
		int row, 
		int col
	)
	{
		char potentialWinner = _boardGame[row][col];
		for (int i = 1; i < _winLength; i++)
		{
			if (!isLegalLocation(row, col + i))
				return false;
			if (_boardGame[row][col + i] != potentialWinner)
				return false;
		}
		_winner = potentialWinner;
		return true;
	}
	
	private boolean checkDiagonalRight
	(
		int row, 
		int col
	)
	{
		char potentialWinner = _boardGame[row][col];
		for (int i = 1; i < _winLength; i++)
		{
			if (!isLegalLocation(row + i, col + i))
				return false;
			if (_boardGame[row + i][col + i] != potentialWinner)
				return false;
		}
		_winner = potentialWinner;
		return true;
	}
	
	private boolean checkDiagonalLeft
	(
		int row, 
		int col
	)
	{
		char potentialWinner = _boardGame[row][col];
		for (int i = 1; i < _winLength; i++)
		{
			if (!isLegalLocation(row - i, col - i))
				return false;
			if (_boardGame[row - i][col - i] != potentialWinner)
				return false;
		}
		_winner = potentialWinner;
		return true;
	}

	public boolean isLegalLocation
	(
		int row, 
		int col
	)
	{
		if( row >= _size				||
			row <  0					||
			col >= _size				||
			col < 0)
			return false;
		return true;
	}
	
	@Override
	public IBoard getNewChildBoard(IMove move) 
	{
		DiceBoard newBoard 	= new DiceBoard(this);
		newBoard._parent 	= this;
		DiceMove diceMove	= (DiceMove)move;
		if (_player == '0')
		{
			newBoard._selectedRowOrCol = diceMove._selectedRowOrCol;
		}
		else
		{
			//System.out.println(diceMove);
			newBoard._boardGame[diceMove._row][diceMove._col] = _player;
			newBoard._cellsLeft--;
		}
		newBoard._player	= this.getNextPlayer();
		return newBoard;
	}

	@Override
	public char getCurrentPlayer() 
	{
		return _player;
	}

	@Override
	public char getNextPlayer() 
	{
		if (_player == '1' || _player == '2')
			return '0';
		if (_parent == null)
			return '1';
		if (_parent._player == '1')
			return '2';
		return '1';
	}

	@Override
	public double getScore() 
	{
		if (isTheGameOver())
		{
			if (_winner == '1')
				return 1;
			if (_winner == '2')
				return 0;
			return 1/2;
		}
		return 1/2;
	}

	@Override
	public String getBoardName() 
	{
		  return "Dice " + _size + " x " + _size + ",D=" + _depth + ",seed=" + _seed;
	}

	@Override
	public double getChance() 
	{
		return (1/(double)_size);
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
	}

}
