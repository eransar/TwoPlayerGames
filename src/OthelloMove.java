import java.util.Objects;

public class OthelloMove implements IMove
{
	public int _row;
	public int _col;
	
	
	/**
	 * Othello Move Constructor
	 * <p>
	 * @param	row		The selected row
	 * @param	col		The selected column
	 */
	public OthelloMove
	(
		int row,
		int col
	)
	{
		_row	= row;
		_col	= col;
	}
	
	
	/**
	 * Othello Move Copy Constructor
	 * <p>
	 * @param	toCopy	The move that needed to be copied
	 */
	public OthelloMove
	(
		OthelloMove toCopy
	)
	{
		this(	toCopy._row,
				toCopy._col);
	}
	
	
	/**
	 * Othello Move compare between this and other move
	 * <p>
	 * @param		otherMove	Other move to compare to
	 * @return      true, if the two moves are equal. false, otherwise
	 */
	@Override
	public boolean equals
	(
		Object otherMove
	)
	{
		if(otherMove instanceof OthelloMove)
		{
			if(		this._row == ((OthelloMove)otherMove)._row	&&
					this._col == ((OthelloMove)otherMove)._col)
				return true;
		}
		return false;
	}
	
	
	/**
	 * Othello Move hash code
	 * <p>
	 * @return      this move hash code
	 */
	@Override
	public int hashCode()
	{
		int hashCode = Objects.hash(_row, _col);
		return hashCode;
	}
	
	
	/**
	 * Othello Move to string
	 * <p>
	 * @return      this move as string
	 */
	@Override
	public String toString()
	{
	   StringBuilder sb = new StringBuilder();
	   sb.append("[ ").append(_row).append(" , ").append(_col).append(" ]");
	   return sb.toString();
	}
}
