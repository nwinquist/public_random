import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.*;

// The colony class owns most of the resources, especially feromones. This class is the handle to all data for the main class Simulator.
public class Colony extends Canvas
	{
	// Height, default number of rows in grid
	public static final int H = 30;

	// Steps, how many kicks on the terrain to mould it
	public static final int S = 30;
	// Evaporation, the pheromone scent evaporation over time
	public static final float V = 0.03f;
	// Width, the horizontal size (x-axis) of the grid
	public static final int W = 30;

	// Scales a value v in scale a1->a2 to another scale b1->b2
	public static int scale(int v1, int a1, int a2, int b1, int b2)
		{
		int v2 = b1 + (int) (v1 - a1) * (b2 - b1) / (a2 - a1);
		return v2;
		}

	private List<Ant> ants = null;
	private Path bestPathSoFar;
	private Font boldFont = new Font("default", Font.BOLD, 12);
	private float evaporation;
	private List<Pheromone> feromons;
	private Grid grid;
	private Path lastPath; // Just for painting
	private long lastRepainCall = 0;
	private Path nextBestPathSoFar; // For painting purposes only
	private Random rand;

	public Random getRand()
		{
		if (rand ==  null)
			rand = new Random(System.currentTimeMillis());
		return rand;
		}

	public void setRand(Random rand)
		{
		this.rand = rand;
		}

	public Colony()
		{
		this(W, H, S, V);
		}

	public Colony(int w, int h, int s, float e)
		{
		//rand = new Random(System.currentTimeMillis());
		feromons = new ArrayList<Pheromone>();
		grid = new Grid(getRand(), w, h, s);
		evaporation = e;
		bestPathSoFar = null;
		nextBestPathSoFar = null;
		}

	public void applyPath(Path newPath)
		{
		// p(newPath.toString());
		lastPath = newPath; // Just for painting

		// Just check if this path is an all time best
		float score = newPath.getScore();

		if (bestPathSoFar == null)
			{
			bestPathSoFar = newPath;
			}
		else if (score > bestPathSoFar.getScore())
			{
			nextBestPathSoFar = bestPathSoFar; // Recall the old to remove the old red
																					// line from grid
			bestPathSoFar = newPath;
			p("New best path found: " + newPath);
			// repaint();
			}
		slowRepaint();

		// Apply the provided path to the grid.
		// The path score should affect the feromones
		// between the cells on the provided path.
		Cell previousCell = null;
		for (Cell c : newPath.getCells())
			{
			if (previousCell == null)
				{
				// System.out.println("DEBUG FIRST CELL: " + c);
				previousCell = c;
				continue;
				}
			Pheromone f = getFeromon(previousCell, c);
			// Note, mapping between goodness and score is happening here
			f.goodness += Simulator.K_ANT_SCORE_TO_FEROMON * score;
			previousCell = c;
			}
		}

	public void evaporate()
		{
		feromons.forEach(a -> a.goodness -= a.goodness * evaporation);
		}

	public List<Ant> getAnts()
		{
		return ants;
		}

	public Path getBestPathSoFar()
		{
		return bestPathSoFar;
		}

	public Pheromone getFeromon(Cell start, Cell end)
		{
		return feromons.stream().filter(f -> f.fromCell.equals(start) && f.toCell.equals(end)).findAny().get();
		}

	public final Grid getGrid()
		{
		return grid;
		}

	/*
	 * public void setAnts(List<Ant> ants) { this.ants = ants; }
	 * 
	 * public List<Feromon> getFeromons() { return feromons; }
	 * 
	 * public void setFeromons(List<Feromon> feromons) { this.feromons = feromons;
	 * }
	 */
	void p(String s)
		{
		System.out.println(s);
		}

	public void paint(Graphics g)
		{
		//g.setColor(getBackground());
		//g.fillRect(0, 0, getWidth(), getHeight());
		
		Font backupCurrentFont = g.getFont();

		// Draw border
		int m = 40; // margin
		g.setColor(Color.BLACK);
		g.drawRect(0 + m, 0 + m, getWidth() - 2 * m, getHeight() - 2 * m);

		// Draw digits inside border
		g.setColor(Color.BLACK);
		// grid.loopAllCells(a->g.drawString(""+a.getV(), 50, 50));
		int y = m;
		int step = 16;
		for (Cell[] row : grid.getCells())
			{
			y += step;
			int x = m;
			for (Cell c : row)
				{

				if (bestPathSoFar != null && bestPathSoFar.getCells().contains(c))
					{
					g.setFont(boldFont);
					g.setColor(Color.RED);
					}
				else if (nextBestPathSoFar != null && nextBestPathSoFar.getCells().contains(c))
					{
					g.setFont(boldFont);
					g.setColor(Color.GRAY);
					}
				else
					{
					g.setFont(backupCurrentFont);
					g.setColor(Color.LIGHT_GRAY);
					}
				x += step;
				g.drawString("" + c.getV(), x, y);
				}
			}

		/*
		 * 
		 * g.setColor(Color.LIGHT_GRAY); paintPath(lastPath, g, step, m);
		 * 
		 * // Remove the old red g.setColor(Color.LIGHT_GRAY);
		 * paintPath(nextBestPathSoFar, g, step, m);
		 * 
		 * g.setColor(Color.RED); paintPath(bestPathSoFar, g, step, m);
		 * 
		 */

		// Paint feronomes between cells
		for (Cell[] row : grid.getCells())
			for (Cell c : row)
				if (!grid.isOnLastCol(c))
					for (Cell trioCell : grid.get3CellsOnTheRight(c))
						paintFeromon(c, trioCell, g, step, m);

		}

	private void paintFeromon(Cell fromCell, Cell toCell, Graphics g, int step, int m)
		{
		if (fromCell.getVisits()==0)return;
		float mx = 9.5f;
		float f = 1.3f;
		int x1 = (int) (fromCell.getX() * step + f * m + mx);
		int y1 = (int) (fromCell.getY() * step + f * m);
		int x2 = (int) (toCell.getX() * step + f * m + mx);
		int y2 = (int) (toCell.getY() * step + f * m);

		Pheromone pheromon = getFeromon(fromCell, toCell);
		if (pheromon.isGood())
			g.setColor(Color.GRAY);
		else if (pheromon.isSemi())
			g.setColor(Color.LIGHT_GRAY);
		else
			g.setColor(Color.WHITE);
		// Make half len:
		x2 = (x1+x2)/2;
		y2=(y1+y2)/2;
		
		g.drawLine(x1, y1, x2, y2);
		}

	private void paintPath(Path path, Graphics g, int step, int m)
		{
		if (path == null)
			return;
		Cell previousCell = null;// getGrid().getStartingCell();
		if (path == null)
			return;
		for (Cell c : path.getCells())
			{
			if (previousCell == null)
				{
				previousCell = c;
				continue;
				}
			float mx = 6.0f;
			float f = 1.3f;
			int x1 = (int) (previousCell.getX() * step + f * m + mx);
			int y1 = (int) (previousCell.getY() * step + f * m);
			int x2 = (int) (c.getX() * step + f * m + mx);
			int y2 = (int) (c.getY() * step + f * m);
			g.drawLine(x1, y1, x2, y2);
			previousCell = c;
			}
		}

	public void prepareAnts(int numberOfAnts)
		{
		ants = new ArrayList<Ant>(numberOfAnts);
		for (int i = 0; i < numberOfAnts; i++)
			{
			Ant ant = new Ant(rand, this);
			ant.setCurrCell(grid.getStartingCell());
			ants.add(ant);
			}

		for (Cell[] row : grid.getCells())
			{
			
			
			for (Cell c : row)
				{
				if (!grid.isOnLastCol(c))
					{
					for (Cell trioCell : grid.get3CellsOnTheRight(c))
						{
						// p("trio: " + trioCell);
						feromons.add(new Pheromone(c, trioCell));
						}
					}
				}
			}
		// feromons.stream().forEach(a->a = new Feromon());
		}

	public void slowRepaint()
		{
		long now = System.currentTimeMillis();
		if (now - lastRepainCall < Simulator.K_SLOW_REPAINT_IGNORE_TIME_MS)
			return;
		repaint();
		try
			{
			// A sleep is needed for async drawing in the
			// background (to see the evolving best path lines)
			Thread.sleep(Simulator.K_PAINT_SLEEP);
			} catch (InterruptedException e)
			{
			}

		}

	// To avoid background repaint
	@Override
	public void update(Graphics g)
		{
		
		paint(g);
		}
	}
