import java.util.ArrayList;


public class Main 
{
	public static StringBuilder sb  = new StringBuilder();
	public static StringBuilder sb2 = new StringBuilder();
	
	public static void main(String [ ] args)
	{
		//testWAlphaBeta1();
		GameController controller = new GameController();
		//controller.Run();
		System.out.println("Done!");
	}
	
	private static boolean checkSolutionsValidity
	(
		ArrayList<WeightedAlphaBetaPruning> solutions
	)
	{
		double Pess 		= Double.MIN_VALUE;
		double Opti 		= Double.MAX_VALUE;
		double smallestE	= Double.MAX_VALUE;
		for (WeightedAlphaBetaPruning solution : solutions)
		{
			if (solution._e < smallestE)
			{
				smallestE 	= solution._e;
				Pess		= solution._root.pess;
				Opti		= solution._root.opti;
			}
		}

		for (WeightedAlphaBetaPruning solution : solutions)
		{
			if (	(solution._root.pess > Pess)	||
					(solution._root.opti < Opti))
				return false;
		}
		return true;
	}
	
	private static void testWAlphaBeta1()
	{
		
		/*
		ArrayList<OthelloMove> SOLS;
		sb = new StringBuilder();
		do
		{
			for(int maxDepth = 11; maxDepth < 15; maxDepth++)
			{
				System.out.println(maxDepth);
				WeightedAlphaBetaPruning.MAX_DEPTH = maxDepth;
				SOLS = new ArrayList<OthelloMove>();
				//DummyHeuristic heuristic = new DummyHeuristic();
				OthelloHeuristic heuristic = new OthelloHeuristic();
				sb = new StringBuilder();
				sb2 = new StringBuilder();
	
				
				WeightedAlphaBetaPruning wab = new WeightedAlphaBetaPruning
						(heuristic, 
						 -100, 
						 100, 
						 64,
						 WeightedAlphaBetaPruning.ErrorPolicy.NONE,
						);
				SOLS.add((OthelloMove)wab.play(new OthelloBoard(16, '1')));
				
				
				wab = new WeightedAlphaBetaPruning
						(heuristic, 
						 -100, 
						 100, 
						 32,
						 WeightedAlphaBetaPruning.ErrorPolicy.NONE);
				SOLS.add((OthelloMove)wab.play(new OthelloBoard(16, '1')));
				
				
				wab = new WeightedAlphaBetaPruning
						(heuristic, 
						 -100, 
						 100, 
						 16,
						 WeightedAlphaBetaPruning.ErrorPolicy.NONE);
				SOLS.add((OthelloMove)wab.play(new OthelloBoard(16, '1')));
				
				
				wab = new WeightedAlphaBetaPruning
						(heuristic, 
						 -100, 
						 100, 
						 8,
						 WeightedAlphaBetaPruning.ErrorPolicy.NONE);
				SOLS.add((OthelloMove)wab.play(new OthelloBoard(16, '1')));
				
				
				wab = new WeightedAlphaBetaPruning
						(heuristic, 
						 -100, 
						 100, 
						 4,
						 WeightedAlphaBetaPruning.ErrorPolicy.NONE);
				SOLS.add((OthelloMove)wab.play(new OthelloBoard(16, '1')));
				
				
				wab = new WeightedAlphaBetaPruning
						(heuristic, 
						 -100, 
						 100, 
						 2,
						 WeightedAlphaBetaPruning.ErrorPolicy.NONE);
				SOLS.add((OthelloMove)wab.play(new OthelloBoard(16, '1')));
				
				
				wab = new WeightedAlphaBetaPruning
						(heuristic, 
						 -100, 
						 100, 
						 1,
						 WeightedAlphaBetaPruning.ErrorPolicy.NONE);
				SOLS.add((OthelloMove)wab.play(new OthelloBoard(16, '1')));
				
			
				wab = new WeightedAlphaBetaPruning
						(heuristic, 
						 -100, 
						 100, 
						 0,
						 WeightedAlphaBetaPruning.ErrorPolicy.NONE);
				SOLS.add((OthelloMove)wab.play(new OthelloBoard(16, '1')));
				
				
				wab = new WeightedAlphaBetaPruning
						(heuristic, 
						 -100, 
						 100, 
						 -Integer.MAX_VALUE,
						 WeightedAlphaBetaPruning.ErrorPolicy.NONE);
				SOLS.add((OthelloMove)wab.play(new OthelloBoard(16, '1')));
			}
		}while(checkSols(SOLS) && false);
		*/
		//System.out.println(sb);
		//System.out.println(sb2);
	}
	
	
}
