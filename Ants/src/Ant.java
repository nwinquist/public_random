import java.util.List;
import java.util.Random;

public class Ant
	{

	private Path path;
	private Cell currCell;
	private Colony colony; // just a reference to the pheromon blob
	private Random rand;

	public Ant(Random rand, Colony colony)
		{
		this.colony = colony;
		this.rand = rand;
		}

	public Path getPath()
		{
		return path;
		}

	public void setPath(Path path)
		{
		this.path = path;
		}

	public Cell getCurrCell()
		{
		return currCell;
		}

	public void setCurrCell(Cell currCell)
		{
		this.currCell = currCell;
		}

	public Path doTheWalk()
		{
		Grid grid = colony.getGrid();
		Path trail = new Path();
		// Start point at column 1, in the middle
		currCell = grid.getStartingCell();
		trail.addCell(currCell);

		float totalEffort = 0;
		int n = 0;
		while (true)
			{
			n++;
			// Find the trio cells on the right
			List<Cell> trio = grid.get3CellsOnTheRight(currCell);
			if (trio == null || trio.size() == 0)
				{
				// p("No more cells left (reached the end?).");
				break;
				}

			// Pick the best one
			Cell nextCell = pickOne(trio);
			if (nextCell == null)
				{
				p("next cells is null? Ending the loop.");
				break;
				}

			int effort = Math.abs(getCurrCell().getV() - nextCell.getV());
			totalEffort += effort;
			// p("Total effort: " + totalEffort);

			// Go to that one
			// p("Stepping to next cell: " + nextCell);
			nextCell.recordAntPayingAVisit(this, currCell); // Statistics only
			currCell = nextCell;

			// Add the cell to the trail
			trail.addCell(nextCell);
			}

		int maxEffort = n * Cell.MAX_CELL_HEIGHT;
		// Max effort comes from a path
		// with Steep elevation changes on every step (from 0 to 9)
		// int minEffort = 0; // No elevation changes at all

		float score = (1 - totalEffort / maxEffort) * Simulator.K_SCORE_PATH_MAX;
		trail.setScore(score);
		
		setPath(trail); // remember the last walked path
		return trail;
		}

	private Cell pickOneFromDuo(List<Cell> duo)// throws Exception
		{
		if (duo.size() != 2)
			throw new IllegalArgumentException();
		Cell c = getCurrCell();
		Cell c1 = duo.get(0);
		Cell c2 = duo.get(1);

		// Effort
		float e1 = Math.abs(c.getV() - c1.getV()) + Simulator.K_EFFORT_STEP_MIN;
		float e2 = Math.abs(c.getV() - c2.getV()) + Simulator.K_EFFORT_STEP_MIN;
		// Effort needs to be inverted since a big effort is not a 'big good' (on
		// the contrary)
		e1 = 1 / e1;
		e2 = 1 / e2;

		// Lift values above 1 by multiplying with an arbitrary, yet big enough
		// factor
		e1 *= Simulator.K_EFFORT_FACTOR;
		e2 *= Simulator.K_EFFORT_FACTOR; 

		// Feromons
		float f1 = colony.getFeromon(c, c1).goodness;
		float f2 = colony.getFeromon(c, c2).goodness;

		double p1 = (Math.pow(e1, Simulator.K_FEROMON_ALPHA) * Math.pow(f1, Simulator.K_FEROMON_BETA)) / (e1 * f1);
		double p2 = (Math.pow(e2, Simulator.K_FEROMON_ALPHA) * Math.pow(f2, Simulator.K_FEROMON_BETA)) / (e2 * f2);

		double ptot = p1 + p2;
		double limit1 = p1;

		// old
		// Probabilities for effort
		// float pe = e1 + e2;
		// float pe1 = e1 / pe;
		// float pe2 = e2 / pe;
		// Invert, since a big effort is not a 'big good' (on the contrary)
		// pe1 = (float) 1 / pe1;
		// pe2 = (float) 1 / pe2;

		// float pe_tot = pe1 + pe2;
		// pe1 = pe1 / pe_tot;
		// pe2 = pe2 / pe_tot;

		// OLD
		// Probabilities from previous feromones
		// float pf_tot = f1.goodness + f2.goodness;
		// float pf1 = f1.goodness / pf_tot;
		// float pef = 2; // Max effort prob. plus max pheromon prob combined (=200%)
		// float limit1 = (pe1 + pf1) / pef;

		double guess = rand.nextFloat() * ptot;

		if (guess < limit1)
			return c1;
		return c2;
		}

	private Cell pickOne(List<Cell> trio)
		{
		if (trio.size() == 2)
			return pickOneFromDuo(trio);

		/*
		 * Investigate the goodness of the 3 feromons between current cell and the
		 * three options and also investigate the effort defined by height of the
		 * three options (getV).
		 * 
		 */

		Cell c = getCurrCell();
		Cell c1 = trio.get(0);
		Cell c2 = trio.get(1);
		Cell c3 = trio.get(2);

		// OLD:
		/*
		 * // Effort int e1 = (int) (Math.pow(2, Math.abs(c.getV() - c1.getV()) +
		 * Simulator.K_ANT_EFFORT_STEP_MIN)); int e2 = (int) (Math.pow(2,
		 * Math.abs(c.getV() - c2.getV()) + Simulator.K_ANT_EFFORT_STEP_MIN)); int
		 * e3 = (int) (Math.pow(2, Math.abs(c.getV() - c3.getV()) +
		 * Simulator.K_ANT_EFFORT_STEP_MIN));
		 * 
		 * // Feromons Feromon f1 = colony.getFeromon(c, c1); Feromon f2 =
		 * colony.getFeromon(c, c2); Feromon f3 = colony.getFeromon(c, c3);
		 * 
		 * // Probabilities for effort float pe = e1 + e2 + e3; float pe1 = e1 / pe;
		 * float pe2 = e2 / pe; float pe3 = e3 / pe;
		 * 
		 * // Invert pe1 = (float) 1 / pe1; pe2 = (float) 1 / pe2; pe3 = (float) 1 /
		 * pe3; float pe_tot = pe1 + pe2 + pe3; pe1 = pe1 / pe_tot; pe2 = pe2 /
		 * pe_tot; pe3 = pe3 / pe_tot;
		 * 
		 * // Probabilities from previous feromones float pf = f1.goodness +
		 * f2.goodness + f3.goodness; if (pf == 0) return null; // All bad, dead end
		 * 
		 * float pf1 = f1.goodness / pf; float pf2 = f2.goodness / pf; // float pf3
		 * = f3.goodness / pf;
		 * 
		 * // float pef = 2; // Max effort prob. plus max pheromon prob combined
		 * (=200%) // float limit1 = (pe1 + pf1) / pef; // float limit2 = limit1 +
		 * (pe2 + pf2) / pef;
		 * 
		 * float limit1 = (pe1 + preferFeromons * pf1) / (preferFeromons + 1); float
		 * limit2 = limit1 + (pe2 + preferFeromons * pf2) / (preferFeromons + 1);
		 * 
		 * float guess = rand.nextFloat();
		 */

		float e1 = Math.abs(c.getV() - c1.getV()) + Simulator.K_EFFORT_STEP_MIN;
		float e2 = Math.abs(c.getV() - c2.getV()) + Simulator.K_EFFORT_STEP_MIN;
		float e3 = Math.abs(c.getV() - c3.getV()) + Simulator.K_EFFORT_STEP_MIN;

		// Effort needs to be inverted since a big effort is not a 'big good' (on
		// the contrary)
		e1 = 1 / e1;
		e2 = 1 / e2;
		e3 = 1 / e3;

		// Lift values above 1 by multiplying with an arbitrary, yet big enough
		// factor
		e1 *= Simulator.K_EFFORT_FACTOR;
		e2 *= Simulator.K_EFFORT_FACTOR;
		e3 *= Simulator.K_EFFORT_FACTOR;
		
		// Feromons
		float f1 = colony.getFeromon(c, c1).goodness;
		float f2 = colony.getFeromon(c, c2).goodness;
		float f3 = colony.getFeromon(c, c3).goodness;

		double p1 = (Math.pow(e1, Simulator.K_FEROMON_ALPHA) * Math.pow(f1, Simulator.K_FEROMON_BETA)) / (e1 * f1);
		double p2 = (Math.pow(e2, Simulator.K_FEROMON_ALPHA) * Math.pow(f2, Simulator.K_FEROMON_BETA)) / (e2 * f2);
		double p3 = (Math.pow(e3, Simulator.K_FEROMON_ALPHA) * Math.pow(f3, Simulator.K_FEROMON_BETA)) / (e3 * f3);

		double ptot = p1 + p2 + p3;
		double limit1 = p1;
		double limit2 = p1 + p2;

		double guess = rand.nextDouble() * ptot;

		if (guess < limit1)
			return c1;

		if (guess < limit2)
			return c2;

		return c3;

		}



	void p(String s)
		{
		System.out.println(s);
		}
	}