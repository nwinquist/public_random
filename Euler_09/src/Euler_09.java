/*
 *     Problem 9
    25 January 2002


    A Pythagorean triplet is a set of three natural numbers, a  b  c, for which,
    a2 + b2 = c2

    For example, 32 + 42 = 9 + 16 = 25 = 52.

    There exists exactly one Pythagorean triplet for which a + b + c = 1000.
    Find the product abc.
 */
public class Euler_09
    {

    public static void main(String[] args)
        {
        int a = 0;
        int b = 0;
        int c = 0;
        int i = 0;
        for (a = 1; a < 1000; a++)
            {
            // if (a + b + c > 1000) break;
            for (b = a + 1; b < 1000; b++)
                {
                if (a + b > 1000) b = 1000;
                for (c = b + 1; c < 1000; c++)
                    {
                    i++;
                    // p("a=" + a + ", b= " + b + ", c=" + c + "\n");
                    if (a + b + c > 1000) c = 1000;
                    if (a + b + c == 1000)
                        {
                        if (a * a + b * b == c * c)
                            {
                            p("Found: a=" + a + ", b= " + b + ", c=" + c
                                    + ", i=" + i + ", a*b*c=" + a * b * c
                                    + "\n");
                            System.exit(0);
                            }
                        }
                    }
                }
            }
        }

    static void p(String s)
        {
        System.out.print(s);
        }
    }
