
public class Euler_001
    {

    public static void main(String[] args)
        {
        long sum =0;
        for (int i=1; i<1000; i++) 
            {
            sum += (i%3==0||i%5==0)?i:0;
            }
        System.out.println(sum);
        }
    }
