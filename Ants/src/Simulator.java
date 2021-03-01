
/*
 * Created on 22 Dec 2020
 * Last updated 2021-01-26
 *
 * Ant Colony Optimization simulator
 * Ants find a optimal path through terrain
 * Grid numbers represents ground elevation
 * Red color highlights the current best tral  
 * Author Niclas Winquist
 * www.winquist.net
 * Finland
 * 
 * My first proto with ants ever. Works but has a lot of rough edges
 * and unnecessary bends that could be sorted. (Will be in proto 2 ;)
 * 
 * v.0.1
 * 
 */
import java.awt.*;
import java.awt.event.*;
import java.util.stream.Collectors;

public class Simulator extends Frame
	{

	public static final boolean DEBUG = false;

	// The number of ants
	public static final int K_ANT_QUANTITY = 29;
	// A factor on how much a score affects the feromones in the path (values 0-1,
	// default 0.1)
	public static final float K_ANT_SCORE_TO_FEROMON = 0.1f;
	// To avoid Effort values less than 1, just an arbitrary factor for lifting up
	// the value ten fold
	public static final int K_EFFORT_FACTOR = 10;
	// The amount of effort for each step taken regardless of elevation
	public static final int K_EFFORT_STEP_MIN = 1;
	// Exponent for effort in probability calculation
	public static final float K_FEROMON_ALPHA = 2.00f;
	// Exponent for feromones in probability calculation
	public static final float K_FEROMON_BETA = 2.00f;
	// Evaporation
	public static final float K_FEROMON_EVAPORATION = 0.1f;
	// Graphical windows size height
	public static final int K_FRAME_SIZE_H = 560;
	// Graphical windows size width
	public static final int K_FRAME_SIZE_W = 1520;
	// The number of cells in grid (height)
	public static final int K_GRID_HEIGHT = 29;
	// The number of cells in grid (width)
	public static final int K_GRID_WIDTH = 89;
	// Repaint sleep ms
	public static final int K_PAINT_SLEEP = 1;
	// A good, non-elevated path scores 9
	public static final int K_SCORE_PATH_MAX = 9;
	// The percentige above which a Path score can be
	// considered as 'good'
	public static final int K_SCORE_PERCENTIGE_FOR_GOOD = 99;
	// A good, non-elevated step scores 9
	public static final int K_SCORE_STEP_MAX = 9;
	// How long to skip actual painting (because it is not needed so often)
	public static final long K_SLOW_REPAINT_IGNORE_TIME_MS = 1000;
	// Iterations, make all ants run K_T times (optional)
	public static final int K_T = 100;
	// The number of kicks upon ground to mould the terrain (each leaves a dent)
	public static final int K_TERRAIN_KICKS = (int) K_GRID_HEIGHT * K_GRID_WIDTH / 4;

	private static Simulator instance;

	// An anomaly or glitch in the system
	// to promote evolution. May help solve dead locks
	// and spot seemingly impossible but yet feasible pathways.
	// The "Touch Of God".
	// Update: The word "May" above is a under statement. 
	// Tests have shown, that it really works. (Maybe this "Touch of God" - 
	// technique already has a name? Please, enlighten me.)
	public static int K_SCORE_PERCENTIGE_BAD_IS_GOOD_CHANCE = 1; // 0-100 %, default 1   
	
	public static Simulator getInstance()
		{
		if (instance == null)
			instance = getInstance(K_ANT_QUANTITY, K_GRID_WIDTH, K_GRID_HEIGHT, K_TERRAIN_KICKS, K_FEROMON_EVAPORATION);
		return instance;
		}

	public static Simulator getInstance(int num_of_ants, int w, int h, int k, float e)
		{
		if (instance == null)
			instance = new Simulator(num_of_ants, w, h, k, e);
		return instance;
		}

	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 3091410224824471167L;
	private Colony colony;

	public Colony getColony()
		{
		return colony;
		}

	public static void main(String... args) throws Exception
		{

		// Simulator sim = new Simulator(10, 15, 10, 70, 0.03f);
		// p("" + sim.getColony().getGrid());

		int a = K_ANT_QUANTITY;
		int w = K_GRID_WIDTH;
		int h = K_GRID_HEIGHT;
		int k = K_TERRAIN_KICKS;
		float e = K_FEROMON_EVAPORATION;
		int t = K_T;
		
		if (args.length == 4)
			{
			a = Integer.parseInt(args[0]);
			w = Integer.parseInt(args[1]);
			h = Integer.parseInt(args[2]);
			k = Integer.parseInt(args[3]);
			}
		instance = getInstance(a, w, h, k, e);
		
		// <DEBUG> OVERRIDE
		if (DEBUG)
			{
			// OVERRIDE!
			int table[][] = {
						{ 0, 0, 9, 0, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9 },
						{ 0, 0, 9, 8, 9, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8 },
						{ 0, 0, 9, 0, 9, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8 },
						{ 0, 0, 9, 0, 9, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8 },
						{ 5, 4, 9, 0, 9, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8 },
						{ 0, 6, 9, 0, 9, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8 },
						{ 0, 0, 8, 0, 9, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8 },
						{ 0, 0, 9, 5, 9, 0, 8, 0, 8, 0, 8, 0, 8, 0, 8 },
						{ 0, 0, 9, 0, 9, 2, 8, 0, 8, 0, 8, 0, 8, 0, 8 }
						};
			instance.getColony().getGrid().setGrid(table);
			a = 100;
			instance.getColony().prepareAnts(a);
			t = 50;
			//Grid grid = instance.getColony().getGrid(); 
			//grid.setStartingCell(grid.getCells()[5][7]); // FORCE START CELL
			// <DEBUG/>
			}		
		instance.go(t);
		}

	public Simulator(int num_of_ants, int w, int h, int s, float v)
		{
		colony = new Colony(w, h, s, v);

		p(colony.getGrid().toString());
		p("Average height: " + colony.getGrid().getAverageHeight());

		this.colony.prepareAnts(num_of_ants);
		// prepare phase is separated like this to
		// avoid passing a Kolonna-this-pointer to
		// the Ants in the Kolonna constructor

		// Graphics related stuff:
		colony.setSize(K_FRAME_SIZE_W, K_FRAME_SIZE_H);
		setSize(K_FRAME_SIZE_W, K_FRAME_SIZE_H);
		add(colony);
		addMouseListener(new MouseAdapter()
			{
			public void mouseClicked(MouseEvent e)
				{
				p("click");
				go(1);

				// TODO Auto-generated method stub
				super.mouseClicked(e);
				}
			});
		addWindowListener(new WindowAdapter()
			{
			public void windowClosing(WindowEvent e)
				{
				dispose();
				}

			});
		setLayout(null);
		setVisible(true);
		try
			{
			Thread.sleep(8000);
			} catch (InterruptedException e1)
			{
			// TODO Auto-generated catch block
			e1.printStackTrace();
			}
		}

	// Here is all the action
	void go(int times)
		{
		for (int i = 0; i < times; i++)
			{
			colony.getAnts().stream().map(a -> a.doTheWalk()).collect(Collectors.toList()).stream()
					.filter(p -> p.hasLikelyAGoodScore()).forEach(p -> colony.applyPath(p));
			// Diminish all the pheromon scents over time
			colony.evaporate();
			}
		if (colony.getBestPathSoFar() != null) {
		p("Best score: " + colony.getBestPathSoFar().getScore());
			p("Best path: " + colony.getBestPathSoFar().toString());
		}
		p("Visits in first trio");
		colony.getGrid().get3CellsOnTheRight(colony.getGrid().getStartingCell())
				.forEach(c -> p(c + " visits: " + c.getVisits()));
		}

	/*
	 * void go_old_one_ant_at_a_time(int times) { for (int i = 0; i < times; i++)
	 * { colony.getAnts().forEach(a -> colony.applyPath(a.doTheWalk())); }
	 * p("Best path: " + colony.getBestPathSoFar().toString());
	 * p("Visits in first trio");
	 * colony.getGrid().get3CellsOnTheRight(colony.getGrid().getStartingCell())
	 * .forEach(c -> p(c + " visits: " + c.getVisits())); }
	 */
	static void p(String s)
		{
		System.out.println(s);
		}
	}
