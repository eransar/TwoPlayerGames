import java.util.Objects;

public class WeightedOthelloMove implements IMove
{
	public int _row;
	public int _col;
	
	
	public WeightedOthelloMove
	(
		int row,
		int col
	)
	{
		_row	= row;
		_col	= col;
	}
	
	
	public WeightedOthelloMove
	(
			WeightedOthelloMove toCopy
	)
	{
		this(	toCopy._row,
				toCopy._col);
	}
	
	
	@Override
	public boolean equals
	(
		Object otherMove
	)
	{
		if(otherMove instanceof WeightedOthelloMove)
		{
			if(		this._row == ((WeightedOthelloMove)otherMove)._row	&&
					this._col == ((WeightedOthelloMove)otherMove)._col)
				return true;
		}
		return false;
	}
	
	
	@Override
	public int hashCode()
	{
		int hashCode = Objects.hash(_row, _col);
		return hashCode;
	}
	
	
	@Override
	public String toString()
	{
	   StringBuilder sb = new StringBuilder();
	   sb.append("[ ").append(_row).append(" , ").append(_col).append(" ]");
	   return sb.toString();
	}
}
