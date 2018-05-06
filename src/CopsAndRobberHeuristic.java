import java.util.ArrayList;
import java.util.List;

public class CopsAndRobberHeuristic implements IHeuristic
{

	@Override
	public double getHeuristic(INode node) 
	{
		IBoard board 			= node.getBoard();
		if (board instanceof CopsAndRobberBoard)
		{
			CopsAndRobberBoard 	copsAndRobberBoard 	= (CopsAndRobberBoard)board;
			Pair 				robberLocation 		= copsAndRobberBoard._robberLocation;
			//System.out.println("time: " + copsAndRobberBoard._time);
			//System.out.println("robber: " + robberLocation);
			List<Pair> 	copsLocations 		= copsAndRobberBoard._copsLocations;
			//for (Pair cop : copsLocations)
			//	System.out.println("cop " + copsLocations.indexOf(cop) + ": " + cop);
			double 				heuristic 			= 0;
			for (Pair cop : copsLocations)
			{
				heuristic += manhattanDistance(robberLocation, cop);
			}
			heuristic /= copsAndRobberBoard._cops;
			return heuristic + copsAndRobberBoard._time;
		}
		return 0;
	}
	
	private double manhattanDistance
	(
		Pair location1,
		Pair location2
	)
	{
		int x1 = (int)location1.x;
		int y1 = (int)location1.y;
		int x2 = (int)location2.x;
		int y2 = (int)location2.y;
		double distanceX = Math.abs((double)x1 - (double)x2);
		double distanceY = Math.abs((double)y1 - (double)y2);
		double result	 = distanceX + distanceY;
		return result;
	}
}
