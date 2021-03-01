import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

/*

 4 4 7 8 9 8 7 5 3 5
 5 4 5 7 8 8 8 4 3 3
 7 7 6 5 7 9 9 7 4 5
 7 7 7 6 7 9 9 8 7 6
 6 5 5 5 7 9 8 8 7 8
 6 4 3 3 7 7 7 4 6 7
 7 4 3 3 5 5 3 2 1 6
 8 5 5 3 3 2 1 0 3 5
 7 6 5 4 4 1 0 1 5 8
 7 7 7 6 6 4 2 5 8 9
 
*/

// The terrain, consisting of a grid of cells (that is: places, or positions), all with 
// different elevations
public class Grid
	{

	private Cell[][] cells;

	private int height;

	private Random rand;

	private Cell startingCell;

	private final int steps;

	private int width;

	public Grid(Random rand, int w, int h, int s)
		{
		this.rand = rand;
		steps = s;
		width = w;
		height = h;
		cells = new Cell[height][width]; // NOTE the "xy" ORDER is inverted [y][x]!!
		// Last index is x, so the coordinates are (y, x)

		int x = 0;
		int y = 0;

		// Init all grid cells with value 9
		for (Cell[] row : cells)
			{
			x = 0;
			for (Cell cell : row)
				{
				cells[y][x] = new Cell(x, y);
				x++;
				}
			y++;
			}
		setStartingCell(null);
		mouldTheGround();
		// p(toString());
		}

	// Potential cells to go to next
	public List<Cell> get3CellsOnTheRight(Cell centerCell)
		{
		List<Cell> trio = new ArrayList<Cell>(3);
		if (isOnLastCol(centerCell)) // Not necessary needed, but is a performance
																	// optimization
			return null;

		int x = centerCell.getX() + 1;
		int startY = centerCell.getY() - 1;
		int endY = centerCell.getY() + 1;
		if (startY < 0)
			startY = 0;
		if (endY >= height)
			endY = height - 1;
		for (int y = startY; y <= endY; y++)
			{
			trio.add(cells[y][x]);
			}

		return trio;
		}

	public Cell getCell(int x, int y)
		{
		// Add x, y checks?
		return cells[y][x];
		}

	public Cell[][] getCells()
		{
		return cells;
		}

	public int getHeight()
		{
		return height;
		}

	public Cell getStartingCell()
		{
		return this.startingCell;
		}

	private List<Cell> getSurroundingCells(Cell centerCell)
		{
		List<Cell> ret = new ArrayList<Cell>();
		int startX = centerCell.getX() - 1;
		int startY = centerCell.getY() - 1;
		int endX = centerCell.getX() + 1;
		int endY = centerCell.getY() + 1;

		if (startX < 0)
			startX = 0;
		if (startY < 0)
			startY = 0;

		if (endX >= width)
			endX = width - 1;
		if (endY >= height)
			endY = height - 1;

		for (int y = startY; y <= endY; y++)
			for (int x = startX; x <= endX; x++)
				if (x == centerCell.getX() && y == centerCell.getY())
					continue; // Skip center cell
				else
					ret.add(cells[y][x]);

		return ret;
		}

	public int getWidth()
		{
		return width;
		}

	public boolean isOnLastCol(Cell cell)
		{
		return (cell.getX() == this.getWidth() - 1);
		}

	public void loopAllCells(Consumer<Cell> consumer)
		{
		for (Cell[] row : getCells())
			for (Cell cell : row)
				consumer.accept(cell);
		}

	private void mouldTheGround()
		{
		for (int i = 0; i < steps; i++)
			{
			step();
			}
		}

	void p(String s)
		{
		System.out.println(s);
		}

	public void setCells(Cell[][] cells)
		{
		this.cells = cells;
		}

	public void setGrid(int[][] table)
		{
		height = table.length;
		width = table[0].length;
		cells = new Cell[height][width];
		int x = 0;
		int y = 0;
		for (Cell[] row : cells)
			{
			x = 0;
			for (Cell cell : row)
				{
				cells[y][x] = new Cell(x, y, table[y][x]);
				x++;
				}
			y++;
			}
		setStartingCell(null);
		}

	public void setStartingCell(Cell startingCell)
		{
		if (startingCell == null)
			this.startingCell = cells[height / 2][0]; // In the first column, at the
																								// very middle
		else
			this.startingCell = startingCell;
		}

	public void step()
		{
		step(rand.nextInt(width), rand.nextInt(height));
		}

	// Kick the ground. Surrounding cells will drop -1, hit point -2.
	public void step(int x, int y)
		{
		Cell centerCell = getCell(x, y);
		getSurroundingCells(centerCell).stream().forEach(c -> c.decr());
		centerCell.decr(2);
		}

	
	public String toString()
		{
		String ret = "";
		for (Cell[] row : cells)
			{
			ret += "\n";
			for (Cell cell : row)
				ret += " " + cell.getV();
			}
		return ret;// "CreateGridMap [grid=" + Arrays.toString(grid) + ", rand=" +
		// rand + ", width=" + width + ", height=" + height + "]";

		}

	public float getAverageHeight()
		{
		float ret = 0;
		float sum = 0;
		int n = 0;
		for (Cell[] row : cells)
			for (Cell cell : row)
				{
				sum += cell.getV();
				n++;
				}
		ret = sum / n;
		return ret;
		}
	}