import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Random;

public class CopsAndRobberBoard implements IBoard
{
	Grid 			_grid;
	List<Pair> _copsLocations;
	Pair			_robberLocation;
	int				_cops;
	char			_player;   // 1 - robber, 2 - cops
	int				_time;	   // timeSteps
	double			_chance;
	double			_currentChance;
	CopsAndRobberBoard _parent;
	int				TIME_OUT = 2000;

	
	public CopsAndRobberBoard
	(
		int 	rows,
		int 	cols,
		double 	obstacles,
		int		cops
	)
	{
		_time	= 0;
		_cops	= cops;
		_grid 	= new Grid(rows, cols, obstacles);
		_player = '1';
		initializeLocations();
		_chance = 1;
		_parent = null;
	}
	
	public CopsAndRobberBoard
	(
		int 	rows,
		int 	cols,
		double 	obstacles,
		int		cops,
		CopsAndRobberBoard parent,
		double  chance
	)
	{
		this(rows, cols, obstacles, cops);
		_chance 		= chance;
		_currentChance 	= chance;
		_parent 		= parent;
	}
	
	public CopsAndRobberBoard
	(	
		IBoard otherBoard
	)
	{
		CopsAndRobberBoard otherCABBoard 	= (CopsAndRobberBoard)otherBoard;
		_grid 								= otherCABBoard._grid.copyGrid();
		_copsLocations						= new ArrayList<Pair>();
		_copsLocations.addAll(otherCABBoard._copsLocations);
		_robberLocation = new Pair(otherCABBoard._robberLocation);
		_cops 			= otherCABBoard._cops;
		_player 		= '1';
		_time			= otherCABBoard._time;
		_chance			= otherCABBoard._chance;
		_currentChance  = otherCABBoard._currentChance;
		_parent			= otherCABBoard._parent;
	}
	

	private void initializeLocations()
	{
		Random rand = new Random();
		ArrayList<Pair> availableLocations = _grid.getAvailableLocations();
		int randomIndex 		= rand.nextInt(availableLocations.size());
		Pair randomLocation 	= availableLocations.remove(randomIndex);
		_robberLocation 		= randomLocation;
		_copsLocations			= new ArrayList<Pair>();
		for (int cop = 0; cop < _cops; cop++)
		{
			randomIndex 	= rand.nextInt(availableLocations.size());
			randomLocation 	= availableLocations.remove(randomIndex); 
			_copsLocations.add(cop, randomLocation);
		}
	}
	
	@Override
	public IBoard copyBoard() 
	{
		return new CopsAndRobberBoard(this);
	}

	@Override
	public List<IMove> getLegalMoves() 
	{
 		if 		(_player == '1')
			return getRobberMoves();
		else if (_player == '2')
			return getCopsMoves();
		else if (_player == '0')	// Chance node
		{
			return _parent.getLegalMoves();
		}
		return null;
	}

	private List<IMove> getRobberMoves()
	{
		List<Pair>		neighbors 	= _grid.getNeighborsLocations(_robberLocation);
		List<IMove> 	moves 		= new ArrayList<IMove>();

		for (Pair neighbor : neighbors)
		{
			ArrayList<Pair> currentList	= new ArrayList<>();
			currentList.add(neighbor);
			CopsAndRobberMove currentMove = new CopsAndRobberMove(currentList);
			moves.add(currentMove);
		}
		return moves;
	}
	
	private List<IMove> getCopsMoves()
	{
		List<List<Pair>> 	allCopsNeighbors 	= new ArrayList<List<Pair>>();
		List<IMove> 			moves 				= new ArrayList<IMove>();

		for (int cop = 0; cop < _copsLocations.size(); cop++)
		{
			Pair 			currentCopLocation 	= _copsLocations.get(cop);
			List<Pair>	neighbors 			= _grid.getNeighborsLocations(currentCopLocation);
			allCopsNeighbors.add(cop, neighbors);
		}
		
		List<IMove> allMoves = getCopsMovesRecursively(allCopsNeighbors, null, 0, null);
		List<IMove> movesToRemove = new ArrayList<IMove>();
		if(allMoves==null)
			throw new IllegalStateException("No more moved");
		for (IMove move : allMoves)
		{
			CopsAndRobberMove CABMove = (CopsAndRobberMove)move;
			List<Pair> allCops = CABMove._moves;
			for (int cop1 = 0; cop1 < allCops.size() - 1; cop1++)
				for (int cop2 = cop1 + 1; cop2 < allCops.size(); cop2++)
				{
					if (allCops.get(cop1).equals(allCops.get(cop2)) ||																	//vertex conflic
						(allCops.get(cop1).equals(_copsLocations.get(cop2)) && allCops.get(cop2).equals(_copsLocations.get(cop1))))		//edge conflict
					{
						movesToRemove.add(move);
						break;
					}
							
				}
		}
		allMoves.removeAll(movesToRemove);
		return allMoves;
	}
	
	private List<IMove> getCopsMovesRecursively
	(
		List<List<Pair>> 	allCopsNeighbors,
		List<Pair> 			currentLocations,
		int							currentCop,
		List<IMove>			allCombinations
	)
	{
		if (currentCop == allCopsNeighbors.size())
		{
			CopsAndRobberMove currentMoves = new CopsAndRobberMove(currentLocations);
			if (allCombinations == null)
				allCombinations = new ArrayList<IMove>();
			allCombinations.add(currentMoves);
			return allCombinations;
		}
		for (Pair currentCopMove : allCopsNeighbors.get(currentCop))
		{
			ArrayList<Pair> currentCopsMove = new ArrayList<Pair>();
			if (currentLocations != null)
				currentCopsMove.addAll(currentLocations);
			currentCopsMove.add(currentCop, currentCopMove);
			allCombinations = getCopsMovesRecursively(allCopsNeighbors, currentCopsMove, currentCop + 1, allCombinations);
		}
		return allCombinations;
	}
	
	@Override
	public boolean isTheGameOver() 
	{
		if (_copsLocations.contains(_robberLocation))
			return true;
		if (TIME_OUT ==  _time)
			return true;
		return false;
	}

	@Override
	public IBoard getNewChildBoard(IMove move) 		// TODO: fix time and movemext - regular node or in chance node (just in one)
	{
		CopsAndRobberBoard newBoard = new CopsAndRobberBoard(this);
		newBoard._parent = this;
		if (_chance < 1)
		{
			if (_player == '0')
			{
				newBoard._time++;
				if 		(_parent._player == '1')
					newBoard._robberLocation = ((CopsAndRobberMove)move)._moves.get(0);
				else if (_parent._player == '2')
					newBoard._copsLocations = ((CopsAndRobberMove)move)._moves;
				else
					newBoard = null;
				newBoard._player = getNextPlayer();
				int moves = _parent.getLegalMoves().size();
				if 		((	_parent._player == '1'	&& newBoard._robberLocation.equals(_robberLocation))	||
							_parent._player == '2'	&& compareCops(newBoard._copsLocations))
				{
					newBoard._currentChance = _chance;
					if (moves == 1)
						newBoard._currentChance = 1;
				}
				else
				{
					BigDecimal mone1 	= new BigDecimal(1);
					BigDecimal mone2 	= new BigDecimal(String.valueOf(-_chance));
					mone1 = mone1.add(mone2);
					BigDecimal mehane	= new BigDecimal(moves - 1);
					//BigDecimal result	= mone / mehane;
					newBoard._currentChance = mone1.doubleValue() / mehane.doubleValue();
						
				}
				return newBoard;
			}
			if 		(_player == '1')
				newBoard._robberLocation = ((CopsAndRobberMove)move)._moves.get(0);
			else if (_player == '2')
				newBoard._copsLocations = ((CopsAndRobberMove)move)._moves;
			
			newBoard._player 		= getNextPlayer();
			newBoard._currentChance	= _chance;
			return newBoard;	
		}
		else
		{
			newBoard._time++;
			if 		(_player == '1')
				newBoard._robberLocation = ((CopsAndRobberMove)move)._moves.get(0);
			else if (_player == '2')
				newBoard._copsLocations = ((CopsAndRobberMove)move)._moves;
			else
				newBoard = null;
			newBoard._player 		= getNextPlayer();
			newBoard._currentChance	= _currentChance;
		}
		return newBoard;
	}

	
	private boolean compareCops(List<Pair> otherCops)
	{
		for (Pair cop : _copsLocations)
		{
			if (!otherCops.contains(cop))
				return false;
		}
		for (Pair otherCop : otherCops)
		{
			if (!_copsLocations.contains(otherCop))
				return false;
		}
		return true;
	}
	
	@Override
	public char getCurrentPlayer() 
	{
		return _player;
	}

	@Override
	public char getNextPlayer() 
	{
		if (_chance < 1)		// With chance
		{
			if (_player == '1' || _player == '2')
				return '0';
			if (_parent._player == '1')
				return '2';
			return '1';
		}
		if (_player == '1')
			return '2';
		if( _player == '2')
			return '1';
		return 'n';
	}

	@Override
	public double getScore() 
	{
		return _time;
	}

	@Override
	public String getBoardName() 
	{
		return ("CopsAndRobber " + _grid._rows + " x " + _grid._cols + ", obs:" + _grid._obstacles.size() + ", cops:" + _cops);
	}

	@Override
	public void printBoard() 
	{
		String s = "";
		int rows = _grid._rows;
		int cols = _grid._cols;
		for (int row = 0; row < rows; row++)
		{
			for (int col = 0; col< cols; col++)
			{
				Pair currentLocation = new Pair(row, col);
				if (_grid._obstacles.contains(currentLocation))
					s += " | @";
				else if (_copsLocations.contains(currentLocation))
				{
					int cop = _copsLocations.indexOf(currentLocation);
					s += " | " + cop;
				}
				else if (_robberLocation.equals(currentLocation))
					s += " | $";
				else
					s += " |  ";
			}
			s += " |\n";
		}
		System.out.println(s);
	}

	@Override
	public double getChance() {
		return _currentChance;
	}
	
	
	
}
