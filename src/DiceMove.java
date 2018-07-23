import java.util.Objects;

public class DiceMove implements IMove
{
	public int _row;
	public int _col;
	public int _selectedRowOrCol;
	
	
	/**
	 * Dice Move Constructor
	 * <p>
	 * @param	row		The selected row
	 * @param	col		The selected column
	 */
	public DiceMove
	(
		int row,
		int col
	)
	{
		_row	= row;
		_col	= col;
	}
	
	/**
	 * Dice Move Constructor
	 * <p>
	 * @param	selectedRowOrCol	The selected row or col by random
	 */
	public DiceMove
	(
		int selectedRowOrCol
	)
	{
		_selectedRowOrCol = selectedRowOrCol;
	}

	/**
	 * Dice Move Constructor
	 * <p>
	 * @param	row					The selected row
	 * @param	col					The selected column
	 * @param	selectedRowOrCol	The selected row or col by random
	 */
	public DiceMove
	(
		int row,
		int col,
		int selectedRowOrCol
	)
	{
		_row 				= row;
		_col				= col;
		_selectedRowOrCol 	= selectedRowOrCol;
	}
	
	
	
	/**
	 * Dice Move Copy Constructor
	 * <p>
	 * @param	toCopy	The move that needed to be copied
	 */
	public DiceMove
	(
			DiceMove toCopy
	)
	{
		this(toCopy._row,
			 toCopy._col,
			 toCopy._selectedRowOrCol);
	}
	
	
	/**
	 * Dice Move compare between this and other move
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
		if(otherMove instanceof DiceMove)
		{
			if(		this._row 				== ((DiceMove)otherMove)._row		&&
					this._col 				== ((DiceMove)otherMove)._col 		&&
					this._selectedRowOrCol 	== ((DiceMove)otherMove)._selectedRowOrCol)
				return true;
		}
		return false;
	}
	
	
	/**
	 * Dice Move hash code
	 * <p>
	 * @return      this move hash code
	 */
	@Override
	public int hashCode()
	{
		int hashCode = Objects.hash(_row, _col, _selectedRowOrCol);
		return hashCode;
	}
	
	
	/**
	 * Dice Move to string
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
