import java.util.Arrays;

public class Pair 
{
	public Object x;
	public Object y;
	
	public Pair(Object x, Object y)
	{
		this.x = x;
		this.y = y;
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[ ").append(x).append(" , ").append(y).append(" ]");
		return sb.toString();	
	} 
}
