import java.io.PrintStream;

/*
 Problem 2
 19 October 2001
 Each new term in the Fibonacci sequence is generated 
 by adding the previous two terms. By starting with 
 1 and 2, the first 10 terms will be:

 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, ...

 By considering the terms in the Fibonacci sequence whose values do 
 not exceed four million, find the sum of the even-valued terms.

 http://projecteuler.net/problem=2

 */
public class Euler_002
    {
    private static PrintStream p = System.out;

    public static void main(String[] args)
        {
        final int M = 4 * 1000 * 1000;
        int a = 0;
        int b = 1;
        int c = 0;
        long sum = 0;
        while (c < M)
            {
            c = a + b;
            a = b;
            b = c;
            sum += (c % 2 == 0) ? c : 0;
            p.print(c + ", ");
            }
        p.println("\nAnswer: " + sum);
        }
    }
