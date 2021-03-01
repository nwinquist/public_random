/*
Problem 5
30 November 2001

2520 is the smallest number that can be divided by each of the numbers from 1 to 10 without any remainder.
What is the smallest positive number that is evenly divisible by all of the numbers from 1 to 20?
        
232792560
*/
public class Euler_005
    {
    public static void main(String[] args)
        {
        // long n = 1 * 2 * 3 * 4 * 5 * 6 * 7 * 8 * 9 * 10 * 11 * 12 * 13 * 14
        // * 15 * 16 * 17 * 18 * 19 * 20;
        // for (long i = 1; i < n; i++)
        long i = 0;
        while (true)
            {
            i++;
            if (divideableWithAll(i))
                {
                System.out.println(i);
                break;
                }
            }
        }

    static boolean divideableWithAll(long n)
        {
        for (int i = 1; i <= 20; i++)
            {
            if (n % i != 0) return false;
            }
        return true; // jackpot
        }
    }
