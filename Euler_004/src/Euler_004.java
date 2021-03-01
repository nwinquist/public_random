/*
Problem 4
16 November 2001

A palindromic number reads the same both ways. The largest 
palindrome made from the product of two 2-digit numbers is 9009 = 91  99.

Find the largest palindrome made from the product of two 3-digit numbers.

Answer: 906609
 */
public class Euler_004
    {

    public static void main(String[] args)
        {
        new Euler_004().go();
        }

    void go()
        {
        int prod = 0;
        int rev = 0;
        int max = 0;
        for (int i = 100; i < 1000; i++)
            {
            for (int j = 100; j < 1000; j++)
                {
                prod = i * j;

                rev = rev(prod);
                if (prod == rev)
                    {
                    System.out.println(prod);
                    if (prod > max) max = prod;
                    }
                }
            }
        System.out.println("Answer: " + max);
        }

    int rev(int p)
        {
        String s = "" + p;
        String r = "";
        int len = s.length();
        for (int i = len - 1; i >= 0; i--)
            {
            r += s.charAt(i);
            }
        return Integer.parseInt(r);
        }

    }
