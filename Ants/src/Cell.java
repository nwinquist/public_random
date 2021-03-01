// Represents one position in the terrain grid
public class Cell
	{
	// 9 is the highest elevation there is
	public static final int MAX_CELL_HEIGHT = 9;

	private int v;

	private int visits;

	private int x;

	private int y;

	public Cell(int x, int y)
		{
		this(x, y, MAX_CELL_HEIGHT);
		}

	public Cell(int x, int y, int v)
		{
		super();
		this.x = x;
		this.y = y;
		this.v = v;
		this.visits = 0;
		}

	public void decr()
		{
		decr(1);
		}

	public void decr(int n)
		{
		setV(getV() - n);
		}

	@Override
	public boolean equals(Object obj)
		{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cell other = (Cell) obj;
		if (v != other.v)
			return false;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		return true;
		}

	public int getV()
		{
		return v;
		}

	public int getVisits()
		{
		return visits;
		}

	public int getX()
		{
		return x;
		}

	public int getY()
		{
		return y;
		}

	@Override
	public int hashCode()
		{
		final int prime = 31;
		int result = 1;
		result = prime * result + v;
		result = prime * result + x;
		result = prime * result + y;
		return result;
		}

	public boolean isMax()
		{
		return v == MAX_CELL_HEIGHT;
		}

	public void recordAntPayingAVisit(Ant a, Cell theOriginatingCell)
		{
		visits++;
		}

	public void setV(int v)
		{
		if (v < 0)
			v = 0;

		this.v = v;
		}

	public void setVisits(int visits)
		{
		this.visits = visits;
		}

	public void setX(int x)
		{
		if (x < 0)
			x = 0;
		this.x = x;
		}

	public void setY(int y)
		{
		if (y < 0)
			y = 0;
		this.y = y;
		}

	@Override
	public String toString()
		{
		return "[" + x + ", " + y + "]:" + v;
		}
	}
