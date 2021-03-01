/* Largest palindrome product
Problem 4
A palindromic number reads the same both ways. The largest palindrome made from the product of two 2-digit numbers is 9009 = 91 × 99.

Find the largest palindrome made from the product of two 3-digit numbers.
*/

package euler_004_b;

public class Euler_004
    {
    // Works only with values 1000-9999
    public static boolean isPalin(int n)
        {
        int len = (int) Math.log10(n) + 1;

        int v[] = new int[len];

        for (int i = 0; i < len; i++)
            {
            v[i] = (int) (n / Math.pow(10, i)) % 10;
            }

        for (int i = 0; i < len; i++)
            {
            if (v[i] != v[len - 1 - i]) return false;
            }
        return true;

//         int one = n % 10;
//         int ten = n / 10 % 10;
//         int hun = n / 100 % 10;
//         int tho = n / 1000 % 10;

        // System.out.println("Thousand: " + v[3]);
        // System.out.println("Hundred: " + v[2]);
        // System.out.println("Ten: " + v[1]);
        // System.out.println("One: " + v[0]);

        // if (n < 1000 || n > 9999) return false;
        // int thousand = n / 1000;
        // int hundred = (n - thousand * 1000) / 100;
        // int ten = (n - thousand * 1000 - hundred * 100) / 10;
        // int one = (n - thousand * 1000 - hundred * 100 - ten * 10) / 1;

        // System.out.println("Thousand: " + tho);
        // System.out.println("Hundred: " + hun);
        // System.out.println("Ten: " + ten);
        // System.out.println("One: " + one);
        // return (tho == one) && (hun == ten);
        }

    public static void main(String[] args)
        {

        int n=0, x2=0, y2=0, n2 =0;
        for (int x = 100; x <= 999; x++)
            {
            for (int y = 100; y <= 999; y++)
                {
                n = x * y;
                if (isPalin(n) && n>n2)
                    {
                    x2 =x;
                    y2=y;
                    n2 =n; // Optional
                    System.out.println("isPalin(" + n + ")=true");
                    }
                }
            }
        System.out.println(x2 + "*" + y2 + "=" + n2);
        }

    }
