/**
 * http://projecteuler.net/problem=3
 * 
 * The prime factors of 13195 are 5, 7, 13 and 29. What is the largest prime
 * factor of the number 600851475143 ?
 * 
 * @author niclas.winquist
 * 
 */
public class Euler_003
    {

    public static void main(String[] args) throws Exception
        {
        new Euler_003().go(600851475143L);
        }

    void go(long v)
        {
        String answer = factorize(v);
        System.out.println(answer);
        }

    String factorize(long v)
        {
        String retVal = "";
        while (v % 2 == 0)
            {
            retVal += 2 + "*";
            v /= 2;
            }

        for (long i = 3; i <= v / 2; i += 2)
            {
            while (v % i == 0)
                {
                retVal += i + "*";
                v /= i;
                }
            }
        if (v != 1)
            {
            retVal += v;
            }
        return retVal;
        }
    }

/*
 * 71*839*1471*6857
 */