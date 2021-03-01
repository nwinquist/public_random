
// This is really a struct, not a class, so sue me deleting all the getters and setters
public class Pheromone
	{
	public static final int INITIAL = 5;
	public static final int MIN = 1;
	public static final int MAX = 9;
	public static final int GOOD = 8; // 8-9 inclussive
	public static final int BAD = 2; // 1-2 inclussive

	public Cell fromCell;
	public float goodness; // 1-9, 1=bad, 9=good
	public Cell toCell;

	public Pheromone(Cell fromCell, Cell toCell)
		{
		this(fromCell, toCell, INITIAL);
		}

	public Pheromone(Cell fromCell, Cell toCell, float goodness)
		{
		set(fromCell, toCell, goodness);
		}
 
	public void set(Cell fromCell, Cell toCell, float goodness)
		{
		this.fromCell = fromCell;
		this.toCell = toCell;
		this.goodness = goodness;
		}

	public String toString()
		{
		return "f:" + goodness;
		}

	// Is-functions for drawing assistance mainly
	public boolean isGood()
		{
		return goodness >= GOOD;
		}

	public boolean isBad()
		{
		return goodness <= BAD;
		}

	public boolean isMax()
		{
		return goodness == MAX;
		}

	public boolean isMin()
		{
		return goodness == MIN;
		}
	public boolean isSemi()
		{
		return goodness > INITIAL;
		}
	}
