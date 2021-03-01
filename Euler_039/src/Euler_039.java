import java.util.HashMap;

/*
 Problem 39
 14 March 2003
 If p is the perimeter of a right angle triangle with integral length sides, {a,b,c}, there are exactly three solutions for p = 120.
 {20,48,52}, {24,45,51}, {30,40,50}
 For which value of p  1000, is the number of solutions maximised?
 
 
12, new max=1
60, new max=2
120, new max=3
240, new max=4
420, new max=5
720, new max=6
840, new max=8
Done

840 is the answer
 */
public class Euler_039
    {

    // What we know:
    // a*a + b*b = c*c
    // a + b + c = M

    // c = M -a -b
    // a*a + b*b = (M-a-b)(M-a-b)
    public static final int M = 120;

    class Point
        {
        public Point(int x, int y)
            {
            this.x = x;
            this.y = y;
            }

        public int x;
        public int y;
        }

    private HashMap<Integer, Point> map;

    public static void main(String[] args)
        {
        new Euler_039().go();
        }

    void go()
        {
        map = new HashMap<Integer, Point>(10);
        int max = 0;
        for (int i = 1; i <= 1000; i++) // TBD Think about optimization, could e.g. getComb(997) result be reused for 998 to reduce processing?
            {
            int c = getComb(i);
            if (c > max)
                {
                max = c;
                System.out.println(i + ", new max=" + max);
                }
            }
        System.out.println("Done");
        }

    int getComb(int m)
        {
        int n = 0;
        map.clear();
        // Note. Map is used because the loop generates doublets, e.g.:
        // 3: a=30, b=40, c=50
        // 4: a=40, b=30, c=50
        //.. which should be considered as the same
        for (int a = 1; a < m; a++)
            {
            for (int b = 1; b < m; b++)
                {
                if (a * a + b * b == (m - a - b) * (m - a - b))
                    {
                    // Found
                    n++;
                    int c = m - a - b;
                    //System.out.println(m + "/" + n + ":\ta=" + a + ", b=" + b + ", c=" + c);
                    map.put(c, new Point(a, b));
                    }
                }
            }
        return map.size();
        }
    }
