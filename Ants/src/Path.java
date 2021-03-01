import java.util.ArrayList;
import java.util.List;

public class Path
	{
	private List<Cell> cells;
	private float score;

	@Override
	public String toString()
		{
		return "Path [cells=" + cells + ", score=" + score + "]";
		}

	public Path()
		{
		cells = new ArrayList<Cell>();
		}

	public float getScore()
		{
		return score;
		}

	public void setScore(float score)
		{
		this.score = score;
		}

	public List<Cell> getCells()
		{
		return cells;
		}

	public void addCell(Cell cell)
		{
		this.cells.add(cell);
		}

	public boolean hasGoodScore()
		{
		return score > Simulator.K_SCORE_PATH_MAX * Simulator.K_SCORE_PERCENTIGE_FOR_GOOD / 100;
		}

	public boolean hasLikelyAGoodScore()
		{
		boolean ret = hasGoodScore();
		if (ret == false)
			{
			// Every now and then, let chance play a role.
			// Returning an clearly inaccurate result might just be
			// the thing needed to untangle a knot, or to break free from
			// a Witch Spiral.
			int p = Simulator.K_SCORE_PERCENTIGE_BAD_IS_GOOD_CHANCE;
			int r = Simulator.getInstance().getColony().getRand().nextInt(100);
			if (r < p)
				{
				//p("Touch of God");
				ret = true;
				}
			}
		return ret;
		}

	static void p(String s)
		{
		System.out.println(s);
		}
	}
