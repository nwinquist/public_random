

/*
Problem 10
08 February 2002
The sum of the primes below 10 is 2 + 3 + 5 + 7 = 17.
Find the sum of all the primes below two million.
...
1999891  142905829024
1999957 142907828981
1999969 142909828950
1999979 142911828929
1999993 142913828922
 */
public class Euler_010
    {

    public static void main(String[] args)
        {
        new Euler_010().go();
        }

    void go()
        {
        long sum = 0;
        for (int i = 0; i < 2000000; i++)
            {
            // sum += Prime.isPrime(i)?i:0;
            if (Prime.isPrime(i))
                {
                sum += i;
                System.out.println(i + "\t" + sum);
                }
            }

        }
    }

class Prime
    {
    public static boolean isPrime(int val)
        {
        if (val < 2) return false;
        if (val == 2) return true;
        if (val % 2 == 0) return false;
        int n = val / 2;
        for (int i = 3; i < n; i += 2)
            {
            if (val % i == 0) return false;
            }

        return true;
        }
    }
