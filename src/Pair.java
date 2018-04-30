import java.util.Arrays;

public class Pair 
{
	public Object x;
	public Object y;
	
	public Pair
	(
		Object x, 
		Object y
	)
	{
		this.x = x;
		this.y = y;
	}
	
	public Pair
	(
		Pair otherPair
	)
	{
		this.x = otherPair.x;
		this.y = otherPair.y;
	}
	
	public Pair copyPair()
	{
		return new Pair(this);
	}
	
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("[ ").append(x).append(" , ").append(y).append(" ]");
		return sb.toString();	
	} 
	
	@Override
	public boolean equals
	(
		Object other
	)
	{
		if (other instanceof Pair)
		{
			Pair otherPair = (Pair)other;
			if(x == otherPair.x && y == otherPair.y)
				return true;
		}
		return false;
	}
	
	@Override
	public int hashCode()
	{
		int hc = (int)Math.pow(3, (int)x) + (int)Math.pow(5, (int)y);
		return hc;
	}
}
