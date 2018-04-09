import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class GameController 
{
	private ArrayList<IBoard> 	_boards;
	private	ArrayList<ISolver>	_solvers;
	
	public GameController()
	{
		//compareDepthRandomBoard();
		//compareChildPoliciesRandomBoard();
		comparePlayingChildPoliciesRandomBoard();
		//comparePlayingEOthello();
		//compareNodeExpantionEOthello();
	}
	
	public void RunAll()
	{
		for (IBoard board : _boards)
		{
		
			for (ISolver solver1 : _solvers) 
				for (ISolver solver2 : _solvers) 
				{
					if(solver1 == solver2)
						continue;
					IBoard 	currentBoard 	= board.copyBoard();
					ISolver currentSolver1 	= solver1.copySolver();
					ISolver currentSolver2 	= solver2.copySolver();
					currentBoard 			= RunOneGame (currentBoard, currentSolver1, currentSolver2);
					WriteGameToFile (currentBoard, currentSolver1, currentSolver2);
				}
		}
	}
	
	public void SolveAll()
	{
		int instanceID = 0;
		for (IBoard board : _boards)
		{
			for (ISolver solver : _solvers) 
			{
				IBoard 	currentBoard 	= board.copyBoard();
				ISolver currentSolver 	= solver.copySolver();
				SolveOneGame (currentBoard, currentSolver);
				WriteSolverToFile (currentBoard, currentSolver, instanceID);
			}
			instanceID++;
		}
	}
	

	private IBoard RunOneGame
	(
		IBoard 	board,
		ISolver solver1,
		ISolver solver2
	)
	{
		printGameInfo(board, solver1, solver2);
		while (!board.isTheGameOver())
		{
			IMove move = null;
			if 		(board.getCurrentPlayer() == '1')
				move = solver1.play(board);
			else if (board.getCurrentPlayer() == '2')		
				move = solver2.play(board);
			board = board.getNewChildBoard(move);
		}
		return board;
	}
	
	private Pair SolveOneGame
	(
		IBoard 	board,
		ISolver solver
	)
	{
		printSolveInfo(board, solver);
		return solver.solve(board);
	}
	
	private void printGameInfo
	(
			IBoard 	board,
			ISolver solver1,
			ISolver solver2
	)
	{
		System.out.println(board.getBoardName() + "| Player1 - " + solver1.getSolverName() + "| Player2 - " + solver2.getSolverName());
	}
	
	private void printSolveInfo
	(
			IBoard 	board,
			ISolver solver1
	)
	{
		System.out.println(board.getBoardName() + "| Solver - " + solver1.getSolverName());
	}
	
	private void WriteGameToFile
	(
		IBoard 		board,
		ISolver 	solver1,
		ISolver 	solver2
	)
	{
		String 		filePath = "C:\\Users\\USER\\Documents\\Results";
		String 		fileName = "Results.csv";
		
		String 		file		= filePath + "\\" + fileName;
		
		WriteGameHeaderToFile(file);
		double 		score = board.getScore();
		solver1.writeGameToFile(file, board, score);
		solver2.writeGameToFile(file, board, - score);
	}
	
	private void WriteGameHeaderToFile
	(
		String file
	)
	{
		try 
		{
			File tmpDir = new File(file);
			if(tmpDir.exists())
				return;
			
			FileWriter pw = new FileWriter(file, true);
	
			pw.append("e");
			pw.append(",");
			pw.append("score");
			pw.append(",");
			pw.append("ChildSelectionPolicy");
			pw.append(",");
			pw.append("maxDepth");
			pw.append(",");
			pw.append("expands");
			pw.append(",");
			pw.append("maxExpands");
			pw.append(",");
			pw.append("vmin");
			pw.append(",");
			pw.append("vmax");
			pw.append(",");
			pw.append("board");
			pw.append("\n");
			
	        pw.flush();
	        pw.close();
		}
		catch (Exception e) 
		{
			// TODO: handle exception
		}
	}

	private void WriteSolverToFile
	(
		IBoard 		board,
		ISolver 	solver,
		int			instanceID
	)
	{
		String 		filePath = "C:\\Users\\USER\\Documents\\Results";
		String 		fileName = "Results.csv";
		
		String 		file		= filePath + "\\" + fileName;
		
		WriteSolverHeaderToFile(file);
		solver.writeSolverToFile(file, board, instanceID);
	}
	
	private void WriteSolverHeaderToFile
	(
		String file
	)
	{
		try 
		{
			File tmpDir = new File(file);
			if(tmpDir.exists())
				return;
			
			FileWriter pw = new FileWriter(file, true);
	
			pw.append("instance");
			pw.append(",");
			pw.append("e");
			pw.append(",");
			pw.append("pess");
			pw.append(",");
			pw.append("opti");
			pw.append(",");
			pw.append("maxDepth");
			pw.append(",");
			pw.append("expands");
			pw.append(",");
			pw.append("maxExpands");
			pw.append(",");
			pw.append("vmin");
			pw.append(",");
			pw.append("vmax");
			pw.append(",");
			pw.append("board");
			pw.append("\n");
			
	        pw.flush();
	        pw.close();
		}
		catch (Exception e) 
		{
			// TODO: handle exception
		}
	}

	private void compareSelectingChildMethods(int depth, double ee)
	{
		_boards.add(new OthelloBoard(6, '1'));
		
		IHeuristic 								othelloHeuristic 	= new OthelloHeuristic();
		WeightedAlphaBetaPruning.ErrorPolicy 	ePolicy 			= WeightedAlphaBetaPruning.ErrorPolicy.NONE;
		double 									lowerBound			= -100;
		double									upperBound			= 100;
		double									e					= ee;
		int 									maxDepth			= depth;
		
		
			_solvers.add(new RandomSolver());
			//_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.OPTI));
			//_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.PESS));
			_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, ee, depth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
	}
	
	
	private void comparePlayingChildPoliciesRandomBoard()
	{
		_boards			= new ArrayList<IBoard>();
		_solvers		= new ArrayList<ISolver>();
		int 	branchingFactor	= 3;
		int 	depth			= 7;
		double 	lowerBound		= 0;
		double 	upperBound		= 10000;
		
		for(int seed = 0; seed < 10000; seed++)
			_boards.add(new RandomBoard(branchingFactor, depth, seed, lowerBound, upperBound));
		
		
		IHeuristic 								othelloHeuristic 	= new OthelloHeuristic();
		WeightedAlphaBetaPruning.ErrorPolicy 	ePolicy 			= WeightedAlphaBetaPruning.ErrorPolicy.NONE;
		double									e					= 0;
		int 									maxDepth			= Integer.MAX_VALUE;
			
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 1000, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 1000, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.OPTI));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 1000, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.PESS));

		RunAll();
	}
	
	
	private void compareERandomBoard()
	{
		_boards			= new ArrayList<IBoard>();
		_solvers		= new ArrayList<ISolver>();
		int 	branchingFactor	= 3;
		int 	depth			= 6;
		double 	lowerBound		= 0;
		double 	upperBound		= 10000;
		
		for(int seed = 0; seed < 10; seed++)
			_boards.add(new RandomBoard(branchingFactor, depth, seed, lowerBound, upperBound));
		
		
		IHeuristic 								othelloHeuristic 	= new OthelloHeuristic();
		WeightedAlphaBetaPruning.ErrorPolicy 	ePolicy 			= WeightedAlphaBetaPruning.ErrorPolicy.NONE;
		double									e					= 0;
		int 									maxDepth			= Integer.MAX_VALUE;
			
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 10, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 100, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 1000, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));

		SolveAll();
	}
	
	private void compareChildPoliciesOthello()
	{
		_boards			= new ArrayList<IBoard>();
		_solvers		= new ArrayList<ISolver>();
		
		int 	boardSize		= 4;
		double 	lowerBound		= - (boardSize * boardSize);
		double 	upperBound		= boardSize * boardSize;
		
		_boards.add(new OthelloBoard(boardSize,'1'));
		
		
		IHeuristic 								othelloHeuristic 	= new OthelloHeuristic();
		WeightedAlphaBetaPruning.ErrorPolicy 	ePolicy 			= WeightedAlphaBetaPruning.ErrorPolicy.NONE;
		double									e					= 0;
		int 									maxDepth			= Integer.MAX_VALUE;
			
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 1, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 2, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 4, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 8, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 16, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));

		RunAll();
	}
	
	private void comparePlayingEOthello()
	{
		_boards			= new ArrayList<IBoard>();
		_solvers		= new ArrayList<ISolver>();
		
		int 	boardSize		= 6;
		double 	lowerBound		= - (boardSize * boardSize);
		double 	upperBound		= boardSize * boardSize;
		
		_boards.add(new OthelloBoard(boardSize,'1'));
		
		
		IHeuristic 								othelloHeuristic 	= new OthelloHeuristic();
		WeightedAlphaBetaPruning.ErrorPolicy 	ePolicy 			= WeightedAlphaBetaPruning.ErrorPolicy.NONE;
		double									e					= 0;
		int 									maxDepth			= 0;
			
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE, 5000));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 4, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE, 5000));
	
		RunAll();
	}
	
	private void compareNodeExpantionEOthello()
	{
		_boards			= new ArrayList<IBoard>();
		_solvers		= new ArrayList<ISolver>();
		
		int 	boardSize		= 6;
		double 	lowerBound		= - (boardSize * boardSize);
		double 	upperBound		= boardSize * boardSize;
		
		_boards.add(new OthelloBoard(boardSize,'1'));
		
		
		IHeuristic 								othelloHeuristic 	= new OthelloHeuristic();
		WeightedAlphaBetaPruning.ErrorPolicy 	ePolicy 			= WeightedAlphaBetaPruning.ErrorPolicy.NONE;
		double									e					= 0;
		int 									maxDepth			= 5;
			
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 1, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 2, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 4, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 8, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 16, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 32, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
		_solvers.add(new WeightedAlphaBetaPruning(othelloHeuristic, ePolicy, lowerBound, upperBound, e + 64, maxDepth, WeightedAlphaBetaPruning.ChildSelectionMethod.AVERAGE));
	
		SolveAll();
	}
}
