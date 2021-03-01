/*
 * Largest prime factor
Problem 3
The prime factors of 13195 are 5, 7, 13 and 29.

What is the largest prime factor of the number 600851475143 
 * 
 * Oiskohan se: 71*839*1471*6857?
 */
public class Euler_003
    {

    public static void main(String[] args)
        {
        long n = 3*5*7*11;//600851475143l;//13195;
        
        for (int i=3; i<=n/3;i+=2)
            {
            if (n%i==0) 
                {
                n=n/i;
                
                System.out.print(i + "*");
                }
            }
            System.out.println(n);
        }
    }
