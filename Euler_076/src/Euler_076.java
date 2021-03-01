public class Euler_076
    {
    static final int N = 6;
    static int c = 0;

    private static void p(String s)
        {
        System.out.print(s);
        }

    private void p(int n)
        {
        p(""+n);
        }

    public static void main(String[] args)
        {
        new Euler_076().go(N, N + "=");
        System.out.println(c/3+1);
        }

    void go(int n, String s)
        {
        if (n == 0) return;
        for (int i = (n - 1); i > 0; i--)
            {
            c++;
            p(s);
            int opp = (n - 1) - i + 1;
            
            p(i + "+" + opp + "\n");
            go(opp, s + i + "+");
            }
        }

    void go_works_but_too_much(int n, String s)
        {
        if (n == 0) return;
        for (int i = (n - 1); i > 0; i--)
            {
            p(s);
            int opp = (n - 1) - i + 1;
            p(i + "+" + opp + "\n");
            go_works_but_too_much(opp, s + i + "+");
            }
        }

    void go_almost_good(int n, String s)
        {
        if (n == 0) return;
        if (n == 1) // safety, todo remove if not necessary
            {
            p(1);
            return;
            }
        for (int i = (n - 1); i > 0; i--)
            {
            p(s);
            int opp = (n - 1) - i + 1;
            if (opp == 1) p(i + "+" + opp + "\n");
            else go_almost_good(opp, i + "+");
            }
        }

    void go_toimii_sinnepain(int n, String s)
        {
        if (n == 0) return;
        if (n == 1) // safety, todo remove if not necessary
            {
            p(1);
            return;
            }
        for (int i = (n - 1); i > 0; i--)
            {
            p(s);
            int opp = (n - 1) - i + 1;
            String s2 = i + "+" + (opp + "\n");
            p(s2);
            if (opp > 1) go_toimii_sinnepain(opp, i + "+");
            }
        }
    }
