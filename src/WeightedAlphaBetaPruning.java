import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class WeightedAlphaBetaPruning implements ISolver
{
	private 	IHeuristic 				_heuristic;
	private     ErrorPolicy 			_ePolicy;
	private 	double					_v_min;
	private		double					_v_max;
	public		double					_e;
	private   	long					_time;
	private		int 					_maxDepth;
	private		int						_expands;
	private		ChildSelectionMethod	_childSelection;
	public		WeightedAlphaBetaPruningNode _root;
	
	public enum ErrorPolicy				{NONE};
	public enum ChildSelectionMethod	{OPTI, PESS, AVERAGE};
	
	public WeightedAlphaBetaPruning
	(	
		IHeuristic 				heuristic,
		ErrorPolicy				ePolicy,
		double					lowerBound,
		double					upperBound,
		double					e,
		int						maxDepth,
		ChildSelectionMethod	childSelection
	)
	{
		_heuristic		= heuristic;
		_ePolicy		= ePolicy;
		_v_min			= lowerBound;
		_v_max			= upperBound;
		_e				= e;
		_maxDepth		= maxDepth;
		_expands  		= 0;
		_childSelection	= childSelection;
	}
	
	public WeightedAlphaBetaPruning
	(
		WeightedAlphaBetaPruning toCopy
	)
	{
		this(	toCopy._heuristic,
				toCopy._ePolicy,
				toCopy._v_min,
				toCopy._v_max,
				toCopy._e,
				toCopy._maxDepth,
				toCopy._childSelection);
	}
	
	public IMove play
	(
		IBoard 		board	
	)
	{
		long startTime = System.nanoTime();
		createRoot(board);

		IMove bestMove = rootAlphaBeta();

		return bestMove;
	}
	
	public Pair solve
	(
		IBoard 		board	
	)
	{
		long startTime = System.nanoTime();
		createRoot(board);

		rootAlphaBeta();

		return new Pair(_root.pess, _root.opti);
	}
	
	public ISolver copySolver()
	{
		return new WeightedAlphaBetaPruning(this);
	}
	
	private void createRoot
	(
		IBoard board
	)
	{
		WeightedAlphaBetaPruningNode root = new WeightedAlphaBetaPruningNode();
		root.board		= board;
		root.parent 	= null;
		root.alpha		= _v_min;
		root.beta		= _v_max;
		root.nodeType 	= INode.NodeType.MAX;
		root.pess 		= _v_min;
		root.opti 		= _v_max;
		_root			= root;
	}
	
	private IMove rootAlphaBeta()
	{
		double  bestOpti 	= _v_min; 
		_expands ++;
		
		ArrayList<WeightedAlphaBetaPruningNode> children = _root.createChildren();
		for (WeightedAlphaBetaPruningNode child : children)
		{
			child.alpha = _root.alpha;
			child.beta 	= _root.beta;
			//System.out.println(child.nodeType.name() + "    Node: " + child.hashCode() + ",[" + child.pess + " , " + child.opti + "]");
			Pair childValues 	= recursiveAlphaBeta(child, _e, 1);
			child.pess			= (double)childValues.x;
			child.opti			= (double)childValues.y;
			//System.out.println("Return Node: " + child.hashCode() + ",[" + child.pess + " , " + child.opti + "]");

			bestOpti 	= Double.max(bestOpti, child.opti);
			_root.pess 	= Double.max(_root.pess  , child.pess);
			
			_root.alpha = Double.max(_root.pess, _root.alpha);
			_root.beta  = Double.min(_root.opti, _root.beta);
		}

		_root.opti = bestOpti;
		
		return selectBestChild(children, _root.nodeType);
	}
	
	private IMove selectBestChild
	(
		ArrayList<WeightedAlphaBetaPruningNode> 	children,
		INode.NodeType 								nodeType
	)
	{
		double bestValue = 0;
		if 		(nodeType == INode.NodeType.MAX)
			bestValue = -Double.MAX_VALUE;
		else if	(nodeType == INode.NodeType.MIN)
			bestValue = Double.MAX_VALUE;
		if		(_childSelection == ChildSelectionMethod.OPTI)
			return selectBestOptiChild(children, nodeType, bestValue);
		else if	(_childSelection == ChildSelectionMethod.PESS)
			return selectBestPessChild(children, nodeType, bestValue);
		else 
			return selectBestOptiPessAvarageChild(children, nodeType, bestValue);
	}
	
	private IMove selectBestOptiChild
	(
		ArrayList<WeightedAlphaBetaPruningNode> children,
		INode.NodeType		 				 	nodeType,
		double 						 			bestValue
	)
	{
		IMove	bestMove = null;
		for (WeightedAlphaBetaPruningNode child : children) 
		{
			boolean a1 = nodeType == INode.NodeType.MAX;
			boolean a2 = child.opti > bestValue;
			boolean a3 = nodeType == INode.NodeType.MIN;
			boolean a4 = child.opti < bestValue;
					
			if (	(nodeType == INode.NodeType.MAX &&
					 	child.opti > bestValue		)	||
					(nodeType == INode.NodeType.MIN &&
						child.opti < bestValue		))
			{
				bestValue = child.opti;
				bestMove  = child.lastMove;
			}
		}
		return bestMove;
	}
	
	private IMove selectBestPessChild
	(
		ArrayList<WeightedAlphaBetaPruningNode> children,
		INode.NodeType		 				 	nodeType,
		double 						 			bestValue
	)
	{
		IMove	bestMove = null;
		for (WeightedAlphaBetaPruningNode child : children) 
		{
			if (	(nodeType == INode.NodeType.MAX &&
					 	child.pess > bestValue		)	||
					(nodeType == INode.NodeType.MIN &&
						child.pess < bestValue		))
			{
				bestValue = child.pess;
				bestMove  = child.lastMove;
			}
		}
		return bestMove;	
	}
	
	private IMove selectBestOptiPessAvarageChild
	(
		ArrayList<WeightedAlphaBetaPruningNode> children,
		INode.NodeType		 				 	nodeType,
		double 						 			bestValue
	)
	{
		IMove	bestMove = null;
		double  average;
		for (WeightedAlphaBetaPruningNode child : children) 
		{
			average = ((child.opti + child.pess) / 2);
			if (	(nodeType == INode.NodeType.MAX &&
					average > bestValue		)	||
					(nodeType == INode.NodeType.MIN &&
					average < bestValue		))
			{
				bestValue = average;
				bestMove  = child.lastMove;
			}
		}
		return bestMove;
	}
	
	private Pair recursiveAlphaBeta
	(
			WeightedAlphaBetaPruningNode 	n,
			double							e,
			int								depth
	)
	{
		double  bestOpti 	= 0; 
		double  bestPess 	= 0; 
		_expands ++;
		if (isTerminal(n, depth))
		{
			double 	h;
			if(n.board.isTheGameOver())
			{
				h		= n.board.getScore();
			}
			else
			{
				h 		= _heuristic.getHeuristic(n);
				h 		= Math.round(h * 100.0) / 100.0;
			}
			Pair 	toReturn 	= new Pair(h, h);
			//Main.sb.append(toReturn + " - TERMINAL NODE \n");
			return toReturn;
		}
		
		n.pess = _v_min;
		n.opti = _v_max;
		
		if 		(n.nodeType == INode.NodeType.MAX)
			bestOpti = _v_min;
		else if (n.nodeType == INode.NodeType.MIN)
			bestPess = _v_max;
		
		if (n.parent != null)
		{
			n.alpha = n.parent.alpha;
			n.beta  = n.parent.beta;
		}
		ArrayList<WeightedAlphaBetaPruningNode> children = n.createChildren();
		for (WeightedAlphaBetaPruningNode child : children)
		{
			if (	n.nodeType == INode.NodeType.MAX	||
					n.nodeType == INode.NodeType.MIN)
			{
				child.alpha = n.alpha;
				child.beta 	= n.beta;
			}
			else
			{
				child.alpha = Double.max(_v_min, (n.alpha - n.opti + child.probability * child.opti) / child.probability);
				child.beta  = Double.min(_v_max, (n.beta  - n.pess + child.probability * child.pess) / child.probability);
			}
			double zeta			= computeChildBound (e, n.opti, n.pess, child.probability);
			
			String s = new String();
			for ( int i = 0; i < depth; i++)
				s = "   " + s;
			
			//System.out.println(s + child.nodeType.name() + "    Node: " + child.hashCode() + ",[" + child.pess + " , " + child.opti + "]");
			Pair childValues 	= recursiveAlphaBeta(child, zeta, depth + 1);
			
			
			child.pess			= (double)childValues.x;
			child.opti			= (double)childValues.y;
			//System.out.println(s + "Return Node: " + child.hashCode() + ",[" + child.pess + " , " + child.opti + "]");
			
			if 		(n.nodeType == INode.NodeType.MAX)
			{
				bestOpti = Double.max(bestOpti, child.opti);
				n.pess 	 = Double.max(n.pess  , child.pess);
			}
			else if (n.nodeType == INode.NodeType.MIN)
			{
				bestPess = Double.min(bestPess, child.pess);
				n.opti   = Double.min(n.opti  , child.opti);
			}
			else
			{
				n.pess   = n.pess + child.probability * (child.pess - _v_min);
				n.opti   = n.opti - child.probability * (_v_max - child.opti);
			}
			n.alpha = Double.max(n.pess, n.alpha);
			n.beta  = Double.min(n.opti, n.beta);
			if (n.beta <= n.alpha + e)
			{
				Pair toReturn = new Pair(n.pess, n.opti);
				//Main.sb.append(toReturn + " - CUT! \n");
				return toReturn;
			}
		}
		
		if 		(n.nodeType == INode.NodeType.MAX)
			n.opti = bestOpti;
		else if (n.nodeType == INode.NodeType.MIN)
			n.pess = bestPess;
		
		Pair toReturn = new Pair(n.pess, n.opti);
		//Main.sb.append(toReturn + " \n");
		return toReturn;
	}
	
	private double computeChildBound
	(
		double e,
		double opti,
		double pess,
		double probability
	)
	{
		return e; //TODO: more sophisticated manner
	}
	
	private boolean isTerminal
	(
		WeightedAlphaBetaPruningNode 	n, 
		int 							depth
	)
	{
		return (	n.board.isTheGameOver() || 
					depth == _maxDepth); 
	}
	
	private void writeStatisticsToFile() 
	{
		String filePath = "C:\\Users\\USER\\Documents\\Results";
		String fileName = "Results.csv";
		
		try 
		{
			FileWriter pw = new FileWriter(filePath + "\\" + fileName, true);
	
			pw.append(Double.toString(_e));
			pw.append(",");
			pw.append(Integer.toString(_maxDepth));
			pw.append(",");
			pw.append(Integer.toString(_expands));
			pw.append(",");
			pw.append(Long.toString(TimeUnit.NANOSECONDS.toMillis(_time)));
			pw.append(",");
			pw.append(Double.toString(_root.pess));
			pw.append(",");
			pw.append(Double.toString(_root.opti));
			pw.append(",");
			pw.append(Double.toString(_v_min));
			pw.append(",");
			pw.append(Double.toString(_v_max));
			pw.append("\n");
	        pw.flush();
	        pw.close();
		}
		catch (Exception e) 
		{
			// TODO: handle exception
		}
	}
	
	public void writeGameToFile
	(
		String 	file, 
		double 	score
	)
	{
		try 
		{
			FileWriter pw = new FileWriter(file, true);
	
			pw.append(Double.toString(_e));
			pw.append(",");
			pw.append(Double.toString(score));
			pw.append(",");
			pw.append(_childSelection.name());
			pw.append(",");
			pw.append(Integer.toString(_maxDepth));
			pw.append(",");
			pw.append(Integer.toString(_expands));
			pw.append(",");
			pw.append(Double.toString(_v_min));
			pw.append(",");
			pw.append(Double.toString(_v_max));
			pw.append("\n");
			
	        pw.flush();
	        pw.close();
		}
		catch (Exception e) 
		{
			// TODO: handle exception
		}
	}
	
	public void writeSolverToFile
	(
		String 	file, 
		int 	instanceID
	)
	{
		try 
		{
			FileWriter pw = new FileWriter(file, true);
			pw.append(Integer.toString(instanceID));
			pw.append(",");
			pw.append(Double.toString(_e));
			pw.append(",");
			pw.append(Double.toString(_root.pess));
			pw.append(",");
			pw.append(Double.toString(_root.opti));
			pw.append(",");
			pw.append(_childSelection.name());
			pw.append(",");
			pw.append(Integer.toString(_maxDepth));
			pw.append(",");
			pw.append(Integer.toString(_expands));
			pw.append(",");
			pw.append(Double.toString(_v_min));
			pw.append(",");
			pw.append(Double.toString(_v_max));
			pw.append("\n");
			
	        pw.flush();
	        pw.close();
		}
		catch (Exception e) 
		{
			// TODO: handle exception
		}
	}

	@Override
	public String getSolverName() 
	{
		return "WAB - e = " + _e + ", ChildSelection = " + _childSelection.name() + ", MaxDepth = " +_maxDepth;  
	}
}
