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
    	double[]	randomValues
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
	}
	
    private void createRandomValues()
    {
    	int numberOfLeafs 	= (int)Math.pow(_branchingFactor, _depth);
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
    	return new RandomBoard(_branchingFactor, _depth, _player, _parent, _childNumber, _seed, _minValue, _maxValue);
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
		return new RandomBoard(_branchingFactor, _depth - 1, getNextPlayer(), this, ((RandomMove)move)._moveNumber, _seed, _minValue, _maxValue, _randomValues);
	}

	@Override
	public char getCurrentPlayer() 
	{
		return _player;
	}
	
	@Override
	public char getNextPlayer() 
	{
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
	
	

}
