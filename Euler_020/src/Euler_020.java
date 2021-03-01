/*
 * n! means n  (n  1)  ...  3  2  1

For example, 10! = 10  9  ...  3  2  1 = 3628800,
and the sum of the digits in the number 10! is 3 + 6 + 2 + 8 + 8 + 0 + 0 = 27.

Find the sum of the digits in the number 100!
http://projecteuler.net/problem=20
 */

public class Euler_020
    {
    public static void main(String[] args)
        {
        double r = 1;
        for (int i = 100; i > 0; i--)
            {
            r *= i;
            //System.out.println("i=" + i + ", r=" + r);
            }
        String s = ("" + r);
        String s2 = s.substring(0, s.indexOf('E'));
        s2=s2.replace('.', '0'); // replace dot with zero (won't affect the sum of
                              // digits)
        int sum = 0;
        for (int j = 0; j < s2.length(); j++)
            {
            int x = Character.digit(s2.charAt(j), 10);

            sum += x;
            //System.out.println("x=" + x + ", sum=" + sum);
            }

        System.out.println("Sum = " + sum + ", s= " + s);
        }
    }
