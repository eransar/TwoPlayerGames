import java.util.ArrayList;
import java.util.Random;

public class RandomBoard implements IBoard
{
	int 		_branchingFactor;
	int 		_depth;
	char 		_player;
	RandomBoard _parent;
	int			_childNumber;
	int			_seed;
	double[]	_randomValues;
	double		_minValue;
	double		_maxValue;
	double 		_chance;
	double 		_currentChance;
	
    public RandomBoard()
    {
    	
	}
    
    public RandomBoard
    (
    	int 		branchingFactor, 
    	int 		depth, 
    	int			seed,
    	double		minValue,
    	double		maxVluae
    )
    {
    	this(branchingFactor, depth, '1', null, 1, seed, minValue, maxVluae);
    }
    
    public RandomBoard			// With chance
    (
    	int 		branchingFactor, 
    	int 		depth, 
    	int			seed,
    	char		player,
    	double		minValue,
    	double		maxVluae,
    	double		chance
    )
    {
    	this(branchingFactor, depth, player, null, 1, seed, minValue, maxVluae);
    	_chance	= chance;
    }
    
    public RandomBoard
    (
    	int 		branchingFactor, 
    	int 		depth, 
    	char 		player,
    	RandomBoard parent,
    	int			childNumber,
    	int			seed,
    	double		minValue,
    	double		maxValue
    )
    {
    	_branchingFactor = branchingFactor;
    	_depth			 = depth;
    	_player			 = player;
    	_parent			 = parent;
    	_childNumber	 = childNumber;
    	_seed			 = seed;
    	_minValue		 = minValue;
    	_maxValue		 = maxValue;	
    	createRandomValues();
    	_chance			 = 1;
	}
    
    public RandomBoard
    (
    	int 		branchingFactor, 
    	int 		depth, 
    	char 		player,
    	RandomBoard parent,
    	int			childNumber,
    	int			seed,
    	double		minValue,
    	double		maxValue,
    	double[]	randomValues,
    	double		chance
    )
    {
    	_branchingFactor = branchingFactor;
    	_depth			 = depth;
    	_player			 = player;
    	_parent			 = parent;
    	_childNumber	 = childNumber;
    	_seed			 = seed;
    	_minValue		 = minValue;
    	_maxValue		 = maxValue;
    	_randomValues	 = randomValues;
    	_chance			 = chance;
    	_currentChance	 = chance;
	}
	
    private void createRandomValues()
    {
    	int numberOfLeafs 	= (int)Math.pow(_branchingFactor, _depth);
    	//if (_chance > 0)
    	//	numberOfLeafs 	= (int)Math.pow(_branchingFactor, _depth * 2);
    	_randomValues 		= new double[numberOfLeafs];
    	Random rand 		= new Random(_seed);
    	for (int i = 0; i < numberOfLeafs; i++)
    	{
    		double currentRandomNumber 	= rand.nextDouble() * (_maxValue - _minValue) + _minValue;
    		_randomValues[i] 			= currentRandomNumber;
    	}
    	
    }
    
    public IBoard copyBoard()
    {
    	return new RandomBoard(_branchingFactor, _depth, _player, _parent, _childNumber, _seed, _minValue, _maxValue, _randomValues ,_chance);
    }
    
	@Override
	public ArrayList<IMove> getLegalMoves() 
	{
		ArrayList<IMove> array = new ArrayList<IMove>();
		for (int i = 0; i < _branchingFactor; i++)
			array.add(new RandomMove(i + 1));
		return array;
	}

	@Override
	public boolean isTheGameOver() 
	{
		if (_depth == 0)
			return true;
		return false;
	}

	@Override
	public IBoard getNewChildBoard(IMove move) 
	{
		RandomBoard newBoard;
		if (_chance < 1)
		{
			if (getCurrentPlayer() == '0')
			{
				newBoard = new RandomBoard(_branchingFactor, _depth - 1, getNextPlayer(), this, ((RandomMove)move)._moveNumber, _seed, _minValue, _maxValue, _randomValues, _chance);
				if (newBoard._childNumber == _childNumber)
					newBoard._currentChance = _chance;
				else
					newBoard._currentChance = ((1 - _chance) / (double)(_branchingFactor - 1));
				return newBoard;
			}
			newBoard = new RandomBoard(_branchingFactor, _depth, getNextPlayer(), this, ((RandomMove)move)._moveNumber, _seed, _minValue, _maxValue, _randomValues, _chance);
			newBoard._currentChance = _currentChance;
			return newBoard;
		}
		return new RandomBoard(_branchingFactor, _depth - 1, getNextPlayer(), this, ((RandomMove)move)._moveNumber, _seed, _minValue, _maxValue, _randomValues, _chance);
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
		return '1';
	}
	
	@Override
	public double getScore() 
	{
		Pair leafNumber = getLeafNumber();
		return _randomValues[(int)leafNumber.x - 1];
	}
	
	private Pair getLeafNumber()
	{
		int lowerBound	= 0;
		int upperBound	= 0;
		Pair bounds		= null;
		if (this._parent != null)
			bounds = this._parent.getLeafNumber();
		else
		{
			lowerBound = 1;
			upperBound = (int)Math.pow(_branchingFactor, _depth);
			return new Pair(lowerBound, upperBound);
		}
		if (_chance < 1)
		{
			if (_player == '0')
			{
				int childRange 	= ((int)bounds.y - (int)bounds.x + 1) / _branchingFactor;
				upperBound		= childRange * _childNumber + (int)bounds.x - 1; 
				lowerBound 		= upperBound - childRange + 1;
			}
			else
			{
				lowerBound = (int)bounds.x;
				upperBound = (int)bounds.y;
			}
			return new Pair(lowerBound, upperBound);
		}
		int childRange 	= ((int)bounds.y - (int)bounds.x + 1) / _branchingFactor;
		upperBound		= childRange * _childNumber + (int)bounds.x - 1; 
		lowerBound 		= upperBound - childRange + 1;
		return new Pair(lowerBound, upperBound);
	}

	@Override
	public String getBoardName() 
	{
		return "RandomBaord, B = " + _branchingFactor + ", D = " + _depth;
	}

	@Override
	public void printBoard() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public double getChance() 
	{
		return _currentChance;
	}
	
	

}
