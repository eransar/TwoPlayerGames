
public interface INode 
{
	public enum NodeType 				{MAX, MIN, CHANCE};
	
	public  NodeType 	getNodeType();
	
	public	IBoard		getBoard();
}
