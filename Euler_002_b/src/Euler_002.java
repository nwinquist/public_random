/*
 * Each new term in the Fibonacci sequence is generated 
 by adding the previous two terms. By starting with 
 1 and 2, the first 10 terms will be:

 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...

 By considering the terms in the Fibonacci sequence whose values do 
 not exceed four million, find the sum of the even-valued terms.

 http://projecteuler.net/problem=2
 
 A: 4613732 (My own)
 */

public class Euler_002
    {
    static long p = 0; // Previous value
    static long c = 1; // Current value
    static long n = 0; // New value
    static long sum_even = 0;

    public static void main(String[] args)
        {
        while (sum_even < 4 * 1000 * 1000)
            {
            n = c + p;

            if (n % 2 == 0) sum_even += n;

            p = c;
            c = n;

            }
        System.out.println(sum_even);
        }
    }
