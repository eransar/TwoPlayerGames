import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Grid 
{
	public int 				_rows;
	public int				_cols;
	public ArrayList<Pair> 	_obstacles;

	
	public Grid
	(	
		int 	size
	)
	{
		this(size, size);
	}
	
	public Grid
	(	
		int 	rows,
		int 	cols

	)
	{
		_rows 		= rows;
		_cols		= cols;
		_obstacles 	= new ArrayList<Pair>();
	}
	
	public Grid
	(	
		int 	size,
		double 	obstacles
	)
	{
		this(size, size, obstacles);
	}
	
	public Grid
	(	
		int 	rows,
		int 	cols,
		double 	obstacles
	)
	{
		this(rows, cols);
		boolean done = false;
		while (!done)
		{
			try
			{
				createObstacles(obstacles);
				done = true;
			}
			catch (Exception e)
			{
				
			}
		}
	}
	
	public Grid
	(	
		int 	rows,
		int 	cols,
		double 	obstacles,
		int 	seed
	)
	{
		this(rows, cols);
		boolean done = false;
		while (!done)
		{
			try
			{
				createObstacles(obstacles, seed);
				done = true;
			}
			catch (Exception e)
			{
				
			}
		}
	}
	
	public Grid
	(
		int 				rows,
		int 				cols,
		 ArrayList<Pair> 	obstacles
	)
	{
		this(rows, cols);
		_obstacles.addAll(obstacles);
	}
	
	public Grid
	(
		Grid otherGrid
	)
	{
		this(otherGrid._rows, otherGrid._cols, otherGrid._obstacles);
	}
	
	public Grid copyGrid() 
	{
		return new Grid(this);
	}
	
	
	public ArrayList<Pair> getAvailableLocations()
	{
		ArrayList<Pair> availableLocations = new ArrayList<Pair>();
		for (int row = 0; row < _rows; row++)
			for(int col = 0; col < _cols; col++)
			{
				Pair currentLocation = new Pair(row, col);
				if (!_obstacles.contains(currentLocation))
					availableLocations.add(currentLocation);
			}
		return availableLocations;
	}
	
	public boolean isLegalLocation
	(
		Pair location
	)
	{
		int x = (int)location.x;
		int y = (int)location.y;
		if (_obstacles.contains(location))
			return false;
		if (x < 0		||
			x >= _rows	||
			y < 0		||
			y >= _cols)
			return false;
		return true;
	}
	
	public List<Pair> getNeighborsLocations 	// for one robber or cop
	(
		Pair location
	)
	{
		int x = (int)location.x;
		int y = (int)location.y;
		List<Pair> moves 			= new ArrayList<Pair>();
		List<Pair> movesToRemove 	= new ArrayList<Pair>();
		Pair moveUp 	= new Pair(x, y - 1);
		Pair moveDown 	= new Pair(x, y + 1);
		Pair moveRight  = new Pair(x + 1, y);
		Pair moveLeft	= new Pair(x - 1, y);
		moves.add(moveUp);
		moves.add(moveDown);
		moves.add(moveRight);
		moves.add(moveLeft);
		for (Pair move : moves)
		{
			if (!isLegalLocation(move))
				movesToRemove.add(move);
		}
		moves.removeAll(movesToRemove);
		moves.add(location);
		return moves;
	}
	
	private void createObstacles
	(
		double 	obstacles,
		int		seed
	)
	{
		Random 	rand 						= new Random(seed);
		ArrayList<Pair> availableObstacles	= new ArrayList<Pair>();
		int numberOfObstacles 				= (int)((double)_rows * (double)_cols * obstacles);
		for (int row = 0; row < _rows; row++)
			for (int col = 0; col < _cols; col++)
			{
				availableObstacles.add(new Pair(row, col));
			}
		
		for (int obs = 0; obs < numberOfObstacles; obs++)
		{
			int 	randomIndex;
			Pair 	randomObstacle;
			do 
			{
			randomIndex 	= rand.nextInt(availableObstacles.size());
			randomObstacle 	= availableObstacles.remove(randomIndex);
			} while(!validObstacle(randomObstacle));
			_obstacles.add(randomObstacle);
		}
	}
	
	private void createObstacles
	(
		double 	obstacles
	)
	{
		Random 	rand 						= new Random();
		int		seed						= rand.nextInt();
		createObstacles(obstacles, seed);
	}
	
	private boolean validObstacle
	(
		Pair newObstacle
	)
	{
		int[][] tempGrid = new int[_rows][_cols];
		for (Pair obstacle:_obstacles)
		{
			int x = (int)obstacle.x;
			int y = (int)obstacle.y;
			tempGrid[x][y] = -1;
		}
		int x = (int)newObstacle.x;
		int y = (int)newObstacle.y;
		tempGrid[x][y] = -1;
		
		Pair start = null;
		for (int row = 0; row < _rows; row++)
		{
			for (int col = 0; col < _cols; col++)
			{
				if (tempGrid[row][col] == 0)
				{
					start = new Pair(row, col);
					break;
				}
			}
			if (start != null)
				break;
		}
		
		ArrayList<Pair> openList = new ArrayList<Pair>();
		openList.add(start);
		Pair currentLocation;
		while (openList.size() > 0)
		{
			currentLocation = openList.remove(0);
			int currentX = (int)currentLocation.x;
			int currentY = (int)currentLocation.y;
			tempGrid[currentX][currentY] = 1;
			openList.addAll(validNeighbors(tempGrid, currentLocation));
		}
		return allLocationsAreAvailable(tempGrid);
	}
	
	private ArrayList<Pair> validNeighbors		// for obstacles
	(
		int[][] tempGrid,
		Pair	location
	)
	{
		int x = (int)location.x;
		int y = (int)location.y;
		ArrayList<Pair> neighbors 			= new ArrayList<Pair>();
		ArrayList<Pair> neighborsToRemove 	= new ArrayList<Pair>();
		neighbors.add(new Pair(x + 1, y));
		neighbors.add(new Pair(x - 1, y));
		neighbors.add(new Pair(x, y + 1));
		neighbors.add(new Pair(x, y - 1));
		for (Pair neighbor : neighbors)
		{
			if (!validLocation(tempGrid, neighbor))
				neighborsToRemove.add(neighbor);
		}
		neighbors.removeAll(neighborsToRemove);
		return neighbors;
	}
	
	
	private boolean validLocation
	(
		int[][] tempGrid,
		Pair 	location
	)
	{
		int x = (int)location.x;
		int y = (int)location.y;
		if (x < 0 		||
			x >= _rows 	||
			y < 0 		||
			y >= _cols)
			return false;
		if (tempGrid[x][y] == 0)
			return true;
		return false;
	}
	
	private boolean allLocationsAreAvailable
	(
		int[][] tempGrid
	)
	{
		for (int row = 0; row < _rows; row++)
			for (int col = 0; col < _cols; col++)
			{
				if (tempGrid[row][col] == 0)
					return false;
			}
		return true;
	}
}
