import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;

/*
 Problem 100
 15 July 2005

 If a box contains twenty-one coloured discs, composed of fifteen blue discs and six red 
 discs, and two discs were taken at random, it can be seen that the probability of 
 taking two blue discs, P(BB) = (15/21)(14/20) = 1/2.

 The next such arrangement, for which there is exactly 50% chance of taking two blue 
 discs at random, is a box containing eighty-five blue discs and thirty-five red discs.

 By finding the first arrangement to contain over 1012 = 1,000,000,000,000 discs in 
 total, determine the number of blue discs that the box would contain.

 */
public class Euler_100
    {
    public static boolean global_thread_stop = false;
    public static final BigSquareRoot bigSqrt = new BigSquareRoot();
    public static final BigDecimal n = new BigDecimal("0");
    public static BigDecimal v = new BigDecimal("0");
    public static BigDecimal left = new BigDecimal("0");
    public static BigDecimal right = new BigDecimal("0");
    public static BigDecimal start = new BigDecimal("0");
    public static final BigInteger ONE = new BigInteger("1");
    public static final BigInteger TWO = new BigInteger("2");
    public static final BigDecimal TEN = new BigDecimal("10");
    public static final BigDecimal ONE_BD = new BigDecimal("1");
    private Random rand = new Random(System.currentTimeMillis());
    private Point ret = new Point(0, 0);
    static long nmax = 0; // largest value for this run, printed as last value

    public static void main(String[] args)
        {
        // new Euler_100().goMultiThread();
        // new Euler_100().goAnotherApproach();
        // new Euler_100().goFormulaApproach2();
        // new Euler_100().
        // new Euler_100().goIntegerOnlyApproach();
        // new Euler_100().test();

        // Go
        // final long N = 1000000000000l;
        //final long N = 1000750000000l; 
        final long N = 1001549999997l; // NOTE! Already checked N's from 1000000000000 up to this (see value)
        final long D = 100000000l;
        
        final long THREADS = 2, MAX_THREADS=64;
        long threads = THREADS;
        try
            {
            threads = Integer.parseInt(args[0]);
            if (threads > MAX_THREADS)
                threads = MAX_THREADS;
            }
        catch (Exception e){}
        long n = N;
        for (int t = 0; t < threads; t++)
            {
            goThread(n, D);
            n += D;
            }
        }

    static void goThread(final long n, final long d)
        {
        Thread t = new Thread(new Runnable()
            {
                
                public void run()
                    {
                    new Euler_100().tryEleven(n, n + d);
                    if (n>nmax) nmax=n;
                    System.out.println(this + " stopped with n=" + n + ", d="
                            + d + ", nmax (so far) = " +nmax);
                    }
            });
        
        t.setPriority(Thread.MIN_PRIORITY); // Give some slack to the rest of the system (use only surplus CPU power)
        t.start();
        System.out.println(t + " started with n=" + n + ", d=" + d);
        }

    void tryEleven(long N, long Nend)
        {
        System.out.println("Eleven");

        // final long N = 119;
        // final long Nend = N + 4;
        // final long N = 20;
        // final long Nend = N + 10;
        for (long _n = N; _n < Nend; _n++)
            {
            if (global_thread_stop)
                {
                System.out
                        .println("global_thread_stop signalled, stopping thread "
                                + Thread.currentThread());
                break;
                }
            // Point initalValues = ballPark(n, 10000);
            Point iv = ballPark(_n, 72); // initalValues
            // System.out.println("diff " + (iv.positive-iv.negative));
            double b = 0;
            double n = _n;
            
            // Step through from minimum to maximum blue disks one by one
            for (long _b = iv.negative; _b <= iv.positive; _b++)
                {
                b = _b;
                double d = 2 * b / (n - 1) - n / (b - 1);
                if (d == 0.0d)
                    {
                    // We got a suggestions for a splendid amount of blue disks.
                    // At least the Double data type thinks we are close enough and 
                    // reached the goal, but hold on, we still 
                    // need to consult mr. BigDecimal to make sure.
                    
                    // System.out.println("Found exact. n=" + (long) n + ", b="
                    // + (long) b + ", r=" + (long) (n - b));
                    if (checkResults(n, b)) // TODO, instead if passing the double data type, why not use the (accurate) string data type instead (using the long values, _n and _b)
                        {
                        System.out.println("This! n=" +n+", b="+b);
                        global_thread_stop = true; // signal other threads to
                                                   // stop
                        break;
                        }
                    }
                }
            if (n>nmax) 
                nmax = _n;
            }
        }
    
    // Finds the number of blue disks for a given amount of disks in total (n)
    // The return value contains two values for blue disks, negative and posivite
    // where 'negative' only means the minimum value for blue disks, and the 'positive'
    // the maximum amount of blue disks. The truth lies somewhere in between.
    // Note. By increasing the parameter 'iterations' you can narrow down the 
    // gap between the 'negative' and the 'positive'.
    // In practice, with a value 72 for iterations, you mostly end up in the 
    // exact value for blue disks (returned in both of the variables).
    Point ballPark(long _n, int interations)
        {
        ret.negative = 0;
        ret.positive = _n;
        if (_n == 1) return ret; // avoid div by zero
        double n = _n;
        double bestNeg = 0;
        double bestPos = 0;
        long _b = 0;
        double b = 0.0;
        double d = 0.0;
        for (int i = 0; i < interations; i++)
            {
            _b = rand.nextLong();
            b = _b;
            b *= (b < 0) ? -1 : 1;
            b = (ret.negative + (b % (ret.positive - ret.negative)) + 1);
            if (b == 1) continue; // avoid divide by zero. Skipping blue = 1 is not an issue.
            d = 2 * b / (n - 1) - n / (b - 1);
            if (d > 0)
                {
                if ((bestPos == 0d) || (b < ret.positive))
                    {
                    bestPos = d;
                    ret.positive = (long) b;
                    }
                }
            else
                if (d < 0)
                    {
                    if ((bestNeg == 0d) || (b > ret.negative))
                        {
                        bestNeg = d;
                        ret.negative = (long) b;
                        }
                    }
                else
                    {
                    // System.out.println("Jackpot, b=" + (long) b);
                    ret.positive = (long) b;
                    ret.negative = (long) b;
                    break;

                    }
            }
        return ret;
        }
    void test()
        {
        AverageTimeCounter c = new AverageTimeCounter();
        c.selfTest();
        }
    public boolean checkResults(double _n, double _b)
        {
        int S = 24; // Scale

        // BigDecimal n = new BigDecimal("3");
        // BigDecimal b = new BigDecimal("4");

        // BigDecimal n = new BigDecimal("1.3");
        // BigDecimal b = new BigDecimal("12.3");

        BigDecimal n = BigDecimal.valueOf(_n);
        BigDecimal b = BigDecimal.valueOf(_b); // valueOf() is more precise

        // BigDecimal n = new BigDecimal(""+_n);
        // BigDecimal b = new BigDecimal(""+_b);

        // prob = b/n*(b-1)/(n-1);
        // Exception... BigDecimal prob =
        // b.divide(n).multiply(b.subtract(ONE)).divide(n.subtract(ONE));
        BigDecimal p1 = b.divide(n, S, BigDecimal.ROUND_HALF_UP);
        BigDecimal p2 = b.subtract(ONE_BD);
        BigDecimal p3 = n.subtract(ONE_BD);
        BigDecimal prob = p1.multiply(p2).divide(p3, S,
                BigDecimal.ROUND_HALF_UP);
        String str = prob.toString();
        System.out.println(str + "\tn=" + n + ", b=" + b);
        if (str.equals("0.500000000000000000000000")) return true;

        return false;
        }
    void checkSomeResults()
        {
        // Check results
        double n = 0;
        double b = 0;
        double prob = 0;

        // Found exact. n=1000000010723, b=707106788769, r=292893221954
        n = 1000000010723l;
        b = 707106788769l;
        // prob = b/n*(b-1)/(n-1);
        if (checkResults(n, b)) System.out.println("This!");
        // Found exact. n=1000000030324, b=707106802629, r=292893227695
        n = 1000000030324l;
        b = 707106802629l;
        // prob = b/n*(b-1)/(n-1);
        if (checkResults(n, b)) System.out.println("This!");
        // Found exact. n=1000000077645, b=707106836090, r=292893241555
        n = 1000000077645l;
        b = 707106836090l;
        // prob = b/n*(b-1)/(n-1);
        if (checkResults(n, b)) System.out.println("This!");
        // Found exact. n=1000000097246, b=707106849950, r=292893247296
        n = 1000000097246l;
        b = 707106849950l;
        // prob = b/n*(b-1)/(n-1);
        if (checkResults(n, b)) System.out.println("This!");

        }
    public void goIntegerOnlyApproach()
        {
        AverageTimeCounter c = new AverageTimeCounter();
        long N = 1000000000001l;
        long ITER = N + 10 * 1000 * 1000;
        boolean s;
        for (long n = N; n < ITER; n++)
            {
            if (n % 1000 == 0)
                {
                c.tick(); // Count average durations for X iterations (instead
                          // of every iteration, improves perfo)
                System.out.println(n + ", time=" + c.avg());
                }
            s = subBigInteger(BigDecimal.valueOf(n));
            if (s)
                {
                break;
                }
            }
        }

    boolean subBigInteger(BigDecimal n)
        {
        boolean ret = false;
        left = n;
        right = n.multiply(n).subtract(n);
        // v=1+2*n*n-2*n;
        v = n.multiply(n).multiply(BigSquareRoot.TWO)
                .subtract(n.multiply(BigSquareRoot.TWO)).add(BigSquareRoot.ONE);
        start = (bigSqrt.get(v).divide(BigSquareRoot.TWO)
                .add(BigSquareRoot.ONE)).subtract(TEN);// ((1 + Math.sqrt(v)) /
                                                       // 2) - 10;
        // BigDecimal b = new BigDecimal("0");
        BigDecimal b = start;
        do
            {
            // left = 2 * b * b - 2 * b;
            left = b.multiply(BigSquareRoot.TWO);
            if (left == right)
                {
                System.out.println("Found, b=" + b);
                ret = true;
                break;
                }
            b.add(BigSquareRoot.ONE);
            } while (left.subtract(right).signum() < 0);
        return ret;
        }

    boolean sub2(double n)
        {
        boolean ret = false;
        double left = 0;
        double right = n * n - n;
        double v = 1 + 2 * n * n - 2 * n;
        if (v < 0)
            {
            System.err.println("Datatype overflow, v = " + v + ", n=" + n
                    + ",v=" + v);
            System.exit(99);
            }
        double start = (double) ((1 + Math.sqrt(v)) / 2) - 10;
        double b = start;
        do
            {
            left = 2 * b * b - 2 * b;
            if (left == right)
                {
                System.out.println("Found, b=" + b);
                ret = true;
                break;
                }
            b++;
            } while (left <= right);
        return ret;
        }

    boolean sub(long n)
        {
        boolean ret = false;
        long left = 0;
        long right = n * n - n;
        long v = 1 + 2 * n * n - 2 * n;
        if (v < 0)
            {
            System.err.println("Datatype overflow, v = " + v + ", n=" + n
                    + ",v=" + v);
            System.exit(99);
            }
        long start = (long) ((1 + Math.sqrt(v)) / 2) - 10;
        long b = start;
        do
            {
            left = 2 * b * b - 2 * b;
            if (left == right)
                {
                System.out.println("Found, b=" + b);
                ret = true;
                break;
                }
            b++;
            } while (left <= right);
        return ret;
        }

    public void goFormulaApproach2()
        {

        double n = 1000000000000d;

        double Td = n;
        double previous = 1;
        for (int i = 0; i < 30000000; i++)
            {
            double b2 = (1 + Math.sqrt(1 + 2 * n * n - 2 * n)) / 2;
            double rd = n - b2;
            double v = ((Td - rd) / ((Td - rd) + rd))
                    * (((Td - rd) - 1) / ((Td - rd) + rd - 1));
            if (n % 1000 == 0)
                System.out.println("b2 = " + b2 + ", n=" + (long) n + ", v="
                        + v);
            if (v - 0.5d > previous)
                {
                System.out.println("Break, going bad direction");
                break;
                }
            if (v == 0.5d)
                {
                System.out.println("Found exact. b2 = " + b2 + ", n=" + n); // Found
                                                                            // exact.
                                                                            // b2
                                                                            // =
                                                                            // 7.07106781186694E11,
                                                                            // n=1.0E12
                break;
                }
            previous = v - 0.5d;
            n++;
            }
        // long b = 15;
        // long ans = 2*b*b-2*b-n*n+n;
        // long ans2 = 2*b*b-2*b-(n*n-n);
        double b2 = (1 + Math.sqrt(1 + 2 * n * n - 2 * n)) / 2; // 7.07106781186694E11
                                                                // //
                                                                // 1.0009403596640455E9

        long b3 = (long) b2; // 707106781186 // 1000940359
        // 707106781186.694
        // 707106781187
        // 707106781186.694
        // 1000000000000

        // 7.071067804802943E11
        // 707106780480.2943
        // 707106780480

        // tai b2 = 7.071067804795872E11, n=9.99999999E11, v=0.4999999999999999
        // 7.071067804795872E11
        // 707106780479 (nope, not correct)

        }

    public void goFormulaApproach(String[] args)
        {

        // long n = 21;
        // long n = 120;
        // long n = 1000000000000l;
        double n = 1000000000000d;

        // long b = 15;
        // long ans = 2*b*b-2*b-n*n+n;
        // long ans2 = 2*b*b-2*b-(n*n-n);
        double b2 = (1 + Math.sqrt(1 + 2 * n * n - 2 * n)) / 2; // 7.07106781186694E11
                                                                // //
                                                                // 1.0009403596640455E9
        long b3 = (long) b2; // 707106781186 // 1000940359
        // 707106781186.694
        // 707106781187

        // 1000000000000
        }

    void goAnotherApproach()
        {
        // final long T = 21;
        // final long T = 120;
        final long T = 1000000000000l;
        Random rand = new Random();
        // Narrow down closer to a ball park
        long rMax = T;
        long rMin = 1;
        long r;
        double v;
        double rd;
        double Td = T;
        System.out.println("Please wait...");
        for (int i = 1; i < 10000000; i++)
        // for (int i = 1; i < 10; i++)
            {
            long ra = rand.nextLong();
            ra *= (ra < 0) ? -1 : 1;
            r = rMin + ra % (long) rMax;
            rd = r;

            // double v = (b/(b+r))*((b-1)/(b+r-1));
            // v = (double) ((double) (T - r) / (double) ((T - r) + r)) *
            // (double) ((double) ((T - r) - 1) / (double) ((T - r) + r - 1));
            v = ((Td - rd) / ((Td - rd) + rd))
                    * (((Td - rd) - 1) / ((Td - rd) + rd - 1));
            if ((v < 0.5d) && (rMax > r)) rMax = r;
            else
                if ((v > 0.5d) && (rMin < r)) rMin = r;
                else
                    if (v == 0.5d)
                        {
                        long b = T - r;
                        System.out.println("Jackpot: r=" + r + ", b=" + b);
                        break;
                        }
            }
        System.out.println("rMin=" + rMin + ", rMax=" + rMax + " (diff="
                + (rMax - rMin) + ")");

        // Start combing the desert for (double x = start; x < end; x++) {
        for (r = rMin; r < rMax; r++)
            {
            rd = r;
            v = ((Td - rd) / ((Td - rd) + rd))
                    * (((Td - rd) - 1) / ((Td - rd) + rd - 1));
            long b = T - r;
            System.out.println("r=" + r + ", b=" + b + ", v=" + v);
            if (v < 0.5d)
                {
                System.out.println("Stop. r=" + (r - 1) + ", b=" + (b + 1));
                break;
                }
            }

        }

    void goMultiThread()
        {

        long m = 1000000000000l;
        new Worker(m * 0 / 4, m * 1 / 4, m).start();
        new Worker(m * 1 / 4, m * 2 / 4, m).start();
        new Worker(m * 2 / 4, m * 3 / 4, m).start();
        new Worker(m * 3 / 4, m * 4 / 4, m).start();
        }

    void go()
        {
        long m = 1000000000000l;
        StringBuilder sb = new StringBuilder();
        long b = 0;
        long bmr = 0; // b minus r
        long bpr = 0; // b plus r
        long startTime = System.currentTimeMillis();
        for (long r = 1; r < m; r++)
            {
            if (r % 100000000 == 0)
                {
                long time = System.currentTimeMillis();
                long diff = time - startTime;
                long leftMillis = diff * (m / r);
                long diffSecondsLeft = leftMillis / 1000;
                long diffMinutesLeft = diffSecondsLeft / 60;
                long diffHoursLeft = diffMinutesLeft / 60;
                System.out.println("m/r=" + m / r + "(minutes left "
                        + diffMinutesLeft + " = " + diffHoursLeft + " hours)");
                }

            b = m - r;
            bmr = b - r;
            bpr = b + r;
            // if ((b - r) * (b - r - 1) == 0.5 * (b + r) * (b + r - 1))
            if ((bmr) * (bmr - 1) == 0.5 * (bpr) * (bpr - 1))
                {
                String s = "b=" + b + ", r=" + r;
                sb.append(s + "\n");
                System.out.println(s);
                }
            }
        System.out.println("Done\n" + sb.toString());
        }
    }

class AverageTimeCounter
    {
    final int N = 10;
    int n = 0;
    long[] ticks = new long[N];

    public void selfTest()
        {
        for (int i = 0; i < 10; i++)
            {
            tick();
            try
                {
                Thread.sleep(75);
                }
            catch (InterruptedException e)
                {
                }
            }
        System.out.println(avg());
        }

    public void tick()
        {
        ticks[n++ % N] = System.currentTimeMillis();
        }

    public long avg()
        {
        long sum = 0;
        for (int i = 1; i < N; i++)
            {
            sum += (ticks[i] - ticks[i - 1]); // diffs
            }
        return sum / N;
        }
    }

class Point
    {
    public Point(long p, long n)
        {
        positive = p;
        negative = n;
        }

    public long positive = 0;
    public long negative = 0;
    }

class BigSquareRoot
    {

    public static final BigDecimal ZERO = new BigDecimal("0");
    public static final BigDecimal ONE = new BigDecimal("1");
    public static final BigDecimal TWO = new BigDecimal("2");
    public static final int DEFAULT_MAX_ITERATIONS = 50;
    public static final int DEFAULT_SCALE = 10;

    private BigDecimal error;
    private int iterations;
    private boolean traceFlag;
    private int scale = DEFAULT_SCALE;
    private int maxIterations = DEFAULT_MAX_ITERATIONS;

    // ---------------------------------------
    // The error is the original number minus
    // (sqrt * sqrt). If the original number
    // was a perfect square, the error is 0.
    // ---------------------------------------

    public BigDecimal getError()
        {
        return error;
        }

    // -------------------------------------------------------------
    // Number of iterations performed when square root was computed
    // -------------------------------------------------------------

    public int getIterations()
        {
        return iterations;
        }

    // -----------
    // Trace flag
    // -----------

    public boolean getTraceFlag()
        {
        return traceFlag;
        }
    public void setTraceFlag(boolean flag)
        {
        traceFlag = flag;
        }

    // ------
    // Scale
    // ------
    public int getScale()
        {
        return scale;
        }
    public void setScale(int scale)
        {
        this.scale = scale;
        }

    // -------------------
    // Maximum iterations
    // -------------------
    public int getMaxIterations()
        {
        return maxIterations;
        }
    public void setMaxIterations(int maxIterations)
        {
        this.maxIterations = maxIterations;
        }
    // --------------------------
    // Get initial approximation
    // --------------------------
    private static BigDecimal getInitialApproximation(BigDecimal n)
        {
        BigInteger integerPart = n.toBigInteger();
        int length = integerPart.toString().length();
        if ((length % 2) == 0)
            {
            length--;
            }
        length /= 2;
        BigDecimal guess = ONE.movePointRight(length);
        return guess;
        }

    // ----------------
    // Get square root
    // ----------------
    public BigDecimal get(BigInteger n)
        {
        return get(new BigDecimal(n));
        }
    public BigDecimal get(BigDecimal n)
        {

        // Make sure n is a positive number

        if (n.compareTo(ZERO) <= 0) { throw new IllegalArgumentException(); }

        BigDecimal initialGuess = getInitialApproximation(n);
        trace("Initial guess " + initialGuess.toString());
        BigDecimal lastGuess = ZERO;
        BigDecimal guess = new BigDecimal(initialGuess.toString());

        // Iterate

        iterations = 0;
        boolean more = true;
        while (more)
            {
            lastGuess = guess;
            guess = n.divide(guess, scale, BigDecimal.ROUND_HALF_UP);
            guess = guess.add(lastGuess);
            guess = guess.divide(TWO, scale, BigDecimal.ROUND_HALF_UP);
            trace("Next guess " + guess.toString());
            error = n.subtract(guess.multiply(guess));
            if (++iterations >= maxIterations)
                {
                more = false;
                }
            else
                if (lastGuess.equals(guess))
                    {
                    more = error.abs().compareTo(ONE) >= 0;
                    }
            }
        return guess;

        }
    // ------
    // Trace
    // ------
    private void trace(String s)
        {
        if (traceFlag)
            {
            System.out.println(s);
            }
        }

    // ----------------------
    // Get random BigInteger
    // ----------------------
    public static BigInteger getRandomBigInteger(int nDigits)
        {
        StringBuffer sb = new StringBuffer();
        java.util.Random r = new java.util.Random();
        for (int i = 0; i < nDigits; i++)
            {
            sb.append(r.nextInt(10));
            }
        return new BigInteger(sb.toString());
        }

    // -----
    // Test
    // -----
    public static void main(String[] args)
        {

        BigInteger n;
        BigDecimal sqrt;
        BigSquareRoot app = new BigSquareRoot();
        app.setTraceFlag(true);

        // Generate a random big integer with a hundred digits

        n = BigSquareRoot.getRandomBigInteger(100);

        // Build an array of test numbers

        String testNums[] = { "9", "30", "720", "1024", n.toString() };

        for (int i = 0; i < testNums.length; i++)
            {
            n = new BigInteger(testNums[i]);
            if (i > 0)
                {
                System.out.println("----------------------------");
                }
            System.out.println("Computing the square root of");
            System.out.println(n.toString());
            int length = n.toString().length();
            if (length > 20)
                {
                app.setScale(length / 2);
                }
            sqrt = app.get(n);
            System.out.println("Iterations " + app.getIterations());
            System.out.println("Sqrt " + sqrt.toString());
            System.out.println(sqrt.multiply(sqrt).toString());
            System.out.println(n.toString());
            System.out.println("Error " + app.getError().toString());
            }

        }

    }

class Worker extends Thread
    {
    long start;
    long end;
    long total;

    public Worker(long start, long end, long total)
        {
        this.start = start;
        this.end = end;
        this.total = total;
        }
    public void run()
        {
        StringBuilder sb = new StringBuilder();
        long b = 0;
        long bmr = 0; // b minus r
        // same as total... long bpr = 0; // b plus r
        long startTime = System.currentTimeMillis();
        long m = end - start;

        for (long r = start + 1; r < end; r++)
            {
            if (r % 100000000 == 0)
                {

                long time = System.currentTimeMillis();
                long diff = time - startTime;
                long leftMillis = diff * (m / (r - start));
                long diffSecondsLeft = leftMillis / 1000;
                long diffMinutesLeft = diffSecondsLeft / 60;
                long diffHoursLeft = diffMinutesLeft / 60;
                System.out.println(this + "\tminutes left " + diffMinutesLeft
                        + " = " + diffHoursLeft + " hours");
                }

            // b = total - r;
            // bmr = b - r;
            bmr = total - 2 * r;
            // bpr = b + r;
            // if ((b - r) * (b - r - 1) == 0.5 * (b + r) * (b + r - 1))
            // if ((bmr) * (bmr - 1) == 0.5 * (bpr) * (bpr - 1))
            if ((bmr) * (bmr - 1) == 0.5 * (total) * (total - 1))
                {
                b = total - r;
                String s = "b=" + b + ", r=" + r;
                sb.append(s + "\n");
                System.out.println(s);
                }
            }
        System.out.println("Done\n" + sb.toString());
        }
    }
/*
 * toimiva!
 * 
 * new Euler_100().tryEleven(); }
 * 
 * void tryEleven() { System.out.println("Eleven"); //final long N =
 * 1000000000000l; //final long Nend = N + 1000000;
 * 
 * final long N = 119; final long Nend = N + 2; // final long N = 21; // final
 * long Nend = N + 10; for (long _n = N; _n < Nend; _n++) { // Point
 * initalValues = ballPark(n, 10000); Point iv = ballPark(_n, 18); //
 * initalValues double b = 0; double n = _n; for (long _b = iv.negative; _b <=
 * iv.positive; _b++) { b = _b; double d = 2 * b / (n - 1) - n / (b - 1); if (d
 * == 0.0d) { System.out.println("Found exact. n=" + n + ", b=" + b); } } } }
 * 
 * private Random rand = new Random(System.currentTimeMillis()); private Point
 * ret = new Point(0, 0);
 * 
 * Point ballPark(long _n, int interations) { ret.negative = 0; ret.positive =
 * _n; if (_n == 1) return ret; // avoid div by zero double n = _n; double
 * bestNeg = 0; double bestPos = 0; long _b = 0; double b = 0.0; double d = 0.0;
 * for (int i = 0; i < interations; i++) { _b = rand.nextLong(); b = _b; b *= (b
 * < 0) ? -1 : 1; b = (ret.negative + (b % (ret.positive - ret.negative)) + 1);
 * if (b == 1) continue; // avoid divide by zero d = 2 * b / (n - 1) - n / (b -
 * 1); if (d > 0) { if ((bestPos == 0d) || (b < ret.positive)) { bestPos = d;
 * ret.positive = (long) b; } } else if (d < 0) { if ((bestNeg == 0d) || (b >
 * ret.negative)) { bestNeg = d; ret.negative = (long) b; } } else {
 * System.out.println("Jackpot, b=" + (long) b); ret.positive = (long) b;
 * ret.negative = (long) b; break;
 * 
 * } } return ret; }
 * 
 * Eleven Jackpot, b=707106788769 Found exact. n=1.000000010723E12,
 * b=7.07106788769E11 Jackpot, b=707106802629 Found exact. n=1.000000030324E12,
 * b=7.07106802629E11
 */

/*
 * Thread[Thread-0,5,main] started with n=1000010000000, d=10000000
 * Thread[Thread-1,5,main] started with n=1000020000000, d=10000000
 * Thread[Thread-2,5,main] started with n=1000030000000, d=10000000
 * Thread[Thread-3,5,main] started with n=1000040000000, d=10000000 Eleven
 * Eleven Eleven Eleven Found exact. n=1000040009464, b=707135072150,
 * r=292904937314 0.499999999999999985772573 Found exact. n=1000010018179,
 * b=707113865109, r=292896153070 0.499999999999999983488172 Found exact.
 * n=1000030021609, b=707128009670, r=292902011939 0.499999999999999997035772
 * Found exact. n=1000040056785, b=707135105611, r=292904951174
 * 0.499999999999999996338283 Found exact. n=1000010065500, b=707113898570,
 * r=292896166930 0.499999999999999994054200 Found exact. n=1000040104106,
 * b=707135139072, r=292904965034 0.500000000000000006903993 Found exact.
 * n=1000010112821, b=707113932031, r=292896180790 0.500000000000000004620226
 * Found exact. n=1000020147997, b=707121027972, r=292899120025
 * 0.500000000000000003922648 Found exact. n=1000040151427, b=707135172533,
 * r=292904978894 0.500000000000000017469702 Found exact. n=1000010160142,
 * b=707113965492, r=292896194650 0.500000000000000015186252 Found exact.
 * n=1000040171028, b=707135186393, r=292904984635 0.499999999999999991961824
 * Found exact. n=1000010179743, b=707113979352, r=292896200391
 * 0.499999999999999989677609 Found exact. n=1000030183173, b=707128123913,
 * r=292902059260 0.500000000000000003225084 Found exact. n=1000040218349,
 * b=707135219854, r=292904998495 0.500000000000000002527533 Found exact.
 * n=1000010227064, b=707114012813, r=292896214251 0.500000000000000000243635
 * Found exact. n=1000020262240, b=707121108754, r=292899153486
 * 0.499999999999999999546100 Found exact. n=1000040265670, b=707135253315,
 * r=292905012355 0.500000000000000013093241 Found exact. n=1000010274385,
 * b=707114046274, r=292896228111 0.500000000000000010809660 Found exact.
 * n=1000040285271, b=707135267175, r=292905018096 0.499999999999999987585365
 * Found exact. n=1000010293986, b=707114060134, r=292896233852
 * 0.499999999999999985301020 Found exact. n=1000030297416, b=707128204695,
 * r=292902092721 0.499999999999999998848580 Found exact. n=1000040332592,
 * b=707135300636, r=292905031956 0.499999999999999998151073 Found exact.
 * n=1000010341307, b=707114093595, r=292896247712 0.499999999999999995867045
 * Found exact. n=1000020376483, b=707121189536, r=292899186947
 * 0.499999999999999995169554 Found exact. n=1000040379913, b=707135334097,
 * r=292905045816 0.500000000000000008716780 Found exact. n=1000010388628,
 * b=707114127056, r=292896261572 0.500000000000000006433068 Found exact.
 * n=1000040399514, b=707135347957, r=292905051557 0.499999999999999983208908
 * Found exact. n=1000030411659, b=707128285477, r=292902126182
 * 0.499999999999999994472078 Found exact. n=1000010435949, b=707114160517,
 * r=292896275432 0.500000000000000016999091 Found exact. n=1000040446835,
 * b=707135381418, r=292905065417 0.499999999999999993774615 Found exact.
 * n=1000010455550, b=707114174377, r=292896281173 0.499999999999999991490455
 * Found exact. n=1000030458980, b=707128318938, r=292902140042
 * 0.500000000000000005037889 Found exact. n=1000010502871, b=707114207838,
 * r=292896295033 0.500000000000000002056478 Found exact. n=1000040494156,
 * b=707135414879, r=292905079277 0.500000000000000004340321 Found exact.
 * n=1000020538047, b=707121303779, r=292899234268 0.500000000000000001358925
 * Found exact. n=1000040541477, b=707135448340, r=292905093137
 * 0.500000000000000014906026 Found exact. n=1000010550192, b=707114241299,
 * r=292896308893 0.500000000000000012622500 Found exact. n=1000010569793,
 * b=707114255159, r=292896314634 0.499999999999999987113867 Found exact.
 * n=1000040561078, b=707135462200, r=292905098878 0.499999999999999989398157
 * Found exact. n=1000030573223, b=707128399720, r=292902173503
 * 0.500000000000000000661386 Found exact. n=1000010617114, b=707114288620,
 * r=292896328494 0.499999999999999997679889 Found exact. n=1000040608399,
 * b=707135495661, r=292905112738 0.499999999999999999963862 Found exact.
 * n=1000020652290, b=707121384561, r=292899267729 0.499999999999999996982380
 * Found exact. n=1000010664435, b=707114322081, r=292896342354
 * 0.500000000000000008245909 Found exact. n=1000040655720, b=707135529122,
 * r=292905126598 0.500000000000000010529566 Found exact. n=1000040675321,
 * b=707135542982, r=292905132339 0.499999999999999985021700 Found exact.
 * n=1000030687466, b=707128480502, r=292902206964 0.499999999999999996284885
 * Found exact. n=1000010731357, b=707114369402, r=292896361955
 * 0.499999999999999993303300 Found exact. n=1000040722642, b=707135576443,
 * r=292905146199 0.499999999999999995587404 Found exact. n=1000030734787,
 * b=707128513963, r=292902220824 0.500000000000000006850694 Found exact.
 * n=1000010778678, b=707114402863, r=292896375815 0.500000000000000003869319
 * Found exact. n=1000040769963, b=707135609904, r=292905160059
 * 0.500000000000000006153108 Found exact. n=1000040789564, b=707135623764,
 * r=292905165800 0.499999999999999980645245 Found exact. n=1000020813854,
 * b=707121498804, r=292899315050 0.500000000000000003171749 Found exact.
 * n=1000010825999, b=707114436324, r=292896389675 0.500000000000000014435338
 * Found exact. n=1000040817284, b=707135643365, r=292905173919
 * 0.500000000000000016718809 Found exact. n=1000010845600, b=707114450184,
 * r=292896395416 0.499999999999999988926712 Found exact. n=1000040836885,
 * b=707135657225, r=292905179660 0.499999999999999991210948 Found exact.
 * n=1000030849030, b=707128594745, r=292902254285 0.500000000000000002474192
 * Found exact. n=1000010892921, b=707114483645, r=292896409276
 * 0.499999999999999999492731 Found exact. n=1000040884206, b=707135690686,
 * r=292905193520 0.500000000000000001776650 Found exact. n=1000020928097,
 * b=707121579586, r=292899348511 0.499999999999999998795204 Found exact.
 * n=1000010940242, b=707114517106, r=292896423136 0.500000000000000010058749
 * Found exact. n=1000040931527, b=707135724147, r=292905207380
 * 0.500000000000000012342351 Found exact. n=1000010959843, b=707114530966,
 * r=292896428877 0.499999999999999984550126 Found exact. n=1000040951128,
 * b=707135738007, r=292905213121 0.499999999999999986834492 Found exact.
 * n=1000030963273, b=707128675527, r=292902287746 0.499999999999999998097692
 * Found exact. n=1000011007164, b=707114564427, r=292896442737
 * 0.499999999999999995116143 Found exact. n=1000040998449, b=707135771468,
 * r=292905226981 0.499999999999999997400193 Found exact. n=1000011054485,
 * b=707114597888, r=292896456597 0.500000000000000005682160 Found exact.
 * n=1000041045770, b=707135804929, r=292905240841 0.500000000000000007965893
 * Found exact. n=1000041065371, b=707135818789, r=292905246582
 * 0.499999999999999982458038 Found exact. n=1000031077516, b=707128756309,
 * r=292902321207 0.499999999999999993721192 Found exact. n=1000011101806,
 * b=707114631349, r=292896470457 0.500000000000000016248176 Found exact.
 * n=1000041093091, b=707135838390, r=292905254701 0.500000000000000018531592
 * Found exact. n=1000011121407, b=707114645209, r=292896476198
 * 0.499999999999999990739557 Found exact. n=1000041112692, b=707135852250,
 * r=292905260442 0.499999999999999993023738 Found exact. n=1000031124837,
 * b=707128789770, r=292902335067 0.500000000000000004286997 Found exact.
 * n=1000011168728, b=707114678670, r=292896490058 0.500000000000000001305573
 * Found exact. n=1000041160013, b=707135885711, r=292905274302
 * 0.500000000000000003589437 Found exact. n=1000021203904, b=707121774611,
 * r=292899429293 0.500000000000000000608028 Found exact. n=1000011216049,
 * b=707114712131, r=292896503918 0.500000000000000011871587 Found exact.
 * n=1000041207334, b=707135919172, r=292905288162 0.500000000000000014155134
 * Found exact. n=1000011235650, b=707114725991, r=292896509659
 * 0.499999999999999986362971 Found exact. n=1000041226935, b=707135933032,
 * r=292905293903 0.499999999999999988647283 Found exact. n=1000031239080,
 * b=707128870552, r=292902368528 0.499999999999999999910498 Found exact.
 * n=1000011282971, b=707114759452, r=292896523519 0.499999999999999996928986
 * Found exact. n=1000041274256, b=707135966493, r=292905307763
 * 0.499999999999999999212981 Found exact. n=1000021318147, b=707121855393,
 * r=292899462754 0.499999999999999996231485 Found exact. n=1000011330292,
 * b=707114792913, r=292896537379 0.500000000000000007495000 Found exact.
 * n=1000041321577, b=707135999954, r=292905321623 0.500000000000000009778678
 * Found exact. n=1000041341178, b=707136013814, r=292905327364
 * 0.499999999999999984270830 Found exact. n=1000031353323, b=707128951334,
 * r=292902401989 0.499999999999999995533999 Found exact. n=1000011397214,
 * b=707114840234, r=292896556980 0.499999999999999992552401 Found exact.
 * n=1000041388499, b=707136047275, r=292905341224 0.499999999999999994836527
 * Found exact. n=1000031400644, b=707128984795, r=292902415849
 * 0.500000000000000006099801 Found exact. n=1000011444535, b=707114873695,
 * r=292896570840 0.500000000000000003118413 Found exact. n=1000041435820,
 * b=707136080736, r=292905355084 0.500000000000000005402222 Found exact.
 * n=1000021479711, b=707121969636, r=292899510075 0.500000000000000002420851
 * Found exact. n=1000041455421, b=707136094596, r=292905360825
 * 0.499999999999999979894377 Found exact. n=1000011491856, b=707114907156,
 * r=292896584700 0.500000000000000013684425 Found exact. n=1000041483141,
 * b=707136114197, r=292905368944 0.500000000000000015967917 Found exact.
 * n=1000011511457, b=707114921016, r=292896590441 0.499999999999999988175816
 * Found exact. n=1000041502742, b=707136128057, r=292905374685
 * 0.499999999999999990460073 Found exact. n=1000031514887, b=707129065577,
 * r=292902449310 0.500000000000000001723302 Found exact. n=1000011558778,
 * b=707114954477, r=292896604301 0.499999999999999998741828 Found exact.
 * n=1000041550063, b=707136161518, r=292905388545 0.500000000000000001025767
 * Found exact. n=1000021593954, b=707122050418, r=292899543536
 * 0.499999999999999998044309 Found exact. n=1000011606099, b=707114987938,
 * r=292896618161 0.500000000000000009307839 Found exact. n=1000041597384,
 * b=707136194979, r=292905402405 0.500000000000000011591462 Found exact.
 * n=1000041616985, b=707136208839, r=292905408146 0.499999999999999986083620
 * Found exact. n=1000031629130, b=707129146359, r=292902482771
 * 0.499999999999999997346805 Found exact. n=1000011673021, b=707115035259,
 * r=292896637762 0.499999999999999994365243 Found exact. n=1000041664306,
 * b=707136242300, r=292905422006 0.499999999999999996649314 Found exact.
 * n=1000031676451, b=707129179820, r=292902496631 0.500000000000000007912604
 * Found exact. n=1000011720342, b=707115068720, r=292896651622
 * 0.500000000000000004931254 Found exact. n=1000041711627, b=707136275761,
 * r=292905435866 0.500000000000000007215007 Found exact. n=1000041731228,
 * b=707136289621, r=292905441607 0.499999999999999981707169 Found exact.
 * n=1000031743373, b=707129227141, r=292902516232 0.499999999999999992970308
 * Found exact. n=1000011767663, b=707115102181, r=292896665482
 * 0.500000000000000015497262 Found exact. n=1000011787264, b=707115116041,
 * r=292896671223 0.499999999999999989988660 Found exact. n=1000041758948,
 * b=707136309222, r=292905449726 0.500000000000000017780699 Found exact.
 * n=1000041778549, b=707136323082, r=292905455467 0.499999999999999992272862
 * Found exact. n=1000031790694, b=707129260602, r=292902530092
 * 0.500000000000000003536106 Found exact. n=1000011834585, b=707115149502,
 * r=292896685083 0.500000000000000000554669 Found exact. n=1000041825870,
 * b=707136356543, r=292905469327 0.500000000000000002838554 Found exact.
 * n=1000021869761, b=707122245443, r=292899624318 0.499999999999999999857132
 * Found exact. n=1000011881906, b=707115182963, r=292896698943
 * 0.500000000000000011120677 Found exact. n=1000011901507, b=707115196823,
 * r=292896704684 0.499999999999999985612077 Found exact. n=1000041873191,
 * b=707136390004, r=292905483187 0.500000000000000013404244 Found exact.
 * n=1000031904937, b=707129341384, r=292902563553 0.499999999999999999159610
 * Found exact. n=1000041892792, b=707136403864, r=292905488928
 * 0.499999999999999987896410 Found exact. n=1000011948828, b=707115230284,
 * r=292896718544 0.499999999999999996178085 Found exact. n=1000041940113,
 * b=707136437325, r=292905502788 0.499999999999999998462101 Found exact.
 * n=1000011996149, b=707115263745, r=292896732404 0.500000000000000006744092
 * Found exact. n=1000041987434, b=707136470786, r=292905516648
 * 0.500000000000000009027791 Found exact. n=1000032019180, b=707129422166,
 * r=292902597014 0.499999999999999994783114 Found exact. n=1000042007035,
 * b=707136484646, r=292905522389 0.499999999999999983519959 Found exact.
 * n=1000012063071, b=707115311066, r=292896752005 0.499999999999999991801503
 * Found exact. n=1000042034755, b=707136504247, r=292905530508
 * 0.500000000000000019593480 Found exact. n=1000032066501, b=707129455627,
 * r=292902610874 0.500000000000000005348909 Found exact. n=1000042054356,
 * b=707136518107, r=292905536249 0.499999999999999994085649 Found exact.
 * n=1000012110392, b=707115344527, r=292896765865 0.500000000000000002367508
 * Found exact. n=1000022145568, b=707122440468, r=292899705100
 * 0.500000000000000001669954 Found exact. n=1000042101677, b=707136551568,
 * r=292905550109 0.500000000000000004651338 Found exact. n=1000012157713,
 * b=707115377988, r=292896779725 0.500000000000000012933513 Found exact.
 * n=1000042121278, b=707136565428, r=292905555850 0.499999999999999979143510
 * Found exact. n=1000012177314, b=707115391848, r=292896785466
 * 0.499999999999999987424921 Found exact. n=1000042148998, b=707136585029,
 * r=292905563969 0.500000000000000015217026 Found exact. n=1000032180744,
 * b=707129536409, r=292902644335 0.500000000000000000972413 Found exact.
 * n=1000042168599, b=707136598889, r=292905569710 0.499999999999999989709199
 * Found exact. n=1000012224635, b=707115425309, r=292896799326
 * 0.499999999999999997990926 Found exact. n=1000022259811, b=707122521250,
 * r=292899738561 0.499999999999999997293416 Found exact. n=1000042215920,
 * b=707136632350, r=292905583570 0.500000000000000000274887 Found exact.
 * n=1000012271956, b=707115458770, r=292896813186 0.500000000000000008556930
 * Found exact. n=1000042263241, b=707136665811, r=292905597430
 * 0.500000000000000010840574 Found exact. n=1000032294987, b=707129617191,
 * r=292902677796 0.499999999999999996595919 Found exact. n=1000042282842,
 * b=707136679671, r=292905603171 0.499999999999999985332750 Found exact.
 * n=1000012338878, b=707115506091, r=292896832787 0.499999999999999993614344
 * Found exact. n=1000032342308, b=707129650652, r=292902691656
 * 0.500000000000000007161711 Found exact. n=1000042330163, b=707136713132,
 * r=292905617031 0.499999999999999995898436 Found exact. n=1000012386199,
 * b=707115539552, r=292896846647 0.500000000000000004180348 Found exact.
 * n=1000042377484, b=707136746593, r=292905630891 0.500000000000000006464122
 * Found exact. n=1000012433520, b=707115573013, r=292896860507
 * 0.500000000000000014746350 Found exact. n=1000032409230, b=707129697973,
 * r=292902711257 0.499999999999999992219425 Found exact. n=1000042397085,
 * b=707136760453, r=292905636632 0.499999999999999980956301 Found exact.
 * n=1000012453121, b=707115586873, r=292896866248 0.499999999999999989237764
 * Found exact. n=1000042424805, b=707136780054, r=292905644751
 * 0.500000000000000017029807 Found exact. n=1000032456551, b=707129731434,
 * r=292902725117 0.500000000000000002785216 Found exact. n=1000042444406,
 * b=707136793914, r=292905650492 0.499999999999999991521987 Found exact.
 * n=1000012500442, b=707115620334, r=292896880108 0.499999999999999999803766
 * Found exact. n=1000022535618, b=707122716275, r=292899819343
 * 0.499999999999999999106237 Found exact. n=1000042491727, b=707136827375,
 * r=292905664352 0.500000000000000002087672 Found exact. n=1000012547763,
 * b=707115653795, r=292896893968 0.500000000000000010369767 Found exact.
 * n=1000042539048, b=707136860836, r=292905678212 0.500000000000000012653355
 * Found exact. n=1000032570794, b=707129812216, r=292902758578
 * 0.499999999999999998408723 Found exact. n=1000042558649, b=707136874696,
 * r=292905683953 0.499999999999999987145538 Found exact. n=1000012614685,
 * b=707115701116, r=292896913569 0.499999999999999995427185 Found exact.
 * n=1000032618115, b=707129845677, r=292902772438 0.500000000000000008974512
 * Found exact. n=1000042605970, b=707136908157, r=292905697813
 * 0.499999999999999997711222 Found exact. n=1000012662006, b=707115734577,
 * r=292896927429 0.500000000000000005993185 Found exact. n=1000042653291,
 * b=707136941618, r=292905711673 0.500000000000000008276905 Found exact.
 * n=1000032685037, b=707129892998, r=292902792039 0.499999999999999994032230
 * Found exact. n=1000042672892, b=707136955478, r=292905717414
 * 0.499999999999999982769091 Found exact. n=1000012728928, b=707115781898,
 * r=292896947030 0.499999999999999991050606 Found exact. n=1000042700612,
 * b=707136975079, r=292905725533 0.500000000000000018842587 Found exact.
 * n=1000032732358, b=707129926459, r=292902805899 0.500000000000000004598018
 * Found exact. n=1000042720213, b=707136988939, r=292905731274
 * 0.499999999999999993334773 Found exact. n=1000012776249, b=707115815359,
 * r=292896960890 0.500000000000000001616605 Found exact. n=1000022811425,
 * b=707122911300, r=292899900125 0.500000000000000000919058 Found exact.
 * n=1000012823570, b=707115848820, r=292896974750 0.500000000000000012182603
 * Found exact. n=1000042767534, b=707137022400, r=292905745134
 * 0.500000000000000003900455 Found exact. n=1000012843171, b=707115862680,
 * r=292896980491 0.499999999999999986674027 Found exact. n=1000042787135,
 * b=707137036260, r=292905750875 0.499999999999999978392644 Found exact.
 * n=1000042814855, b=707137055861, r=292905758994 0.500000000000000014466136
 * Found exact. n=1000032846601, b=707130007241, r=292902839360
 * 0.500000000000000000221525 Found exact. n=1000012890492, b=707115896141,
 * r=292896994351 0.499999999999999997240025 Found exact. n=1000042834456,
 * b=707137069721, r=292905764735 0.499999999999999988958326 Found exact.
 * n=1000012937813, b=707115929602, r=292897008211 0.500000000000000007806022
 * Found exact. n=1000042881777, b=707137103182, r=292905778595
 * 0.499999999999999999524007 Found exact. n=1000042929098, b=707137136643,
 * r=292905792455 0.500000000000000010089687 Found exact. n=1000032960844,
 * b=707130088023, r=292902872821 0.499999999999999995845034 Found exact.
 * n=1000013004735, b=707115976923, r=292897027812 0.499999999999999992863446
 * Found exact. n=1000042948699, b=707137150503, r=292905798196
 * 0.499999999999999984581879 Found exact. n=1000042976419, b=707137170104,
 * r=292905806315 0.500000000000000020655366 Found exact. n=1000033008165,
 * b=707130121484, r=292902886681 0.500000000000000006410819 Found exact.
 * n=1000013052056, b=707116010384, r=292897041672 0.500000000000000003429443
 * Found exact. n=1000042996020, b=707137183964, r=292905812056
 * 0.499999999999999995147559 Found exact. n=1000013099377, b=707116043845,
 * r=292897055532 0.500000000000000013995438 Found exact. n=1000043043341,
 * b=707137217425, r=292905825916 0.500000000000000005713238 Found exact.
 * n=1000033075087, b=707130168805, r=292902906282 0.499999999999999991468543
 * Found exact. n=1000013118978, b=707116057705, r=292897061273
 * 0.499999999999999988486869 Found exact. n=1000043062942, b=707137231285,
 * r=292905831657 0.499999999999999980205434 Found exact. n=1000043090662,
 * b=707137250886, r=292905839776 0.500000000000000016278916 Found exact.
 * n=1000033122408, b=707130202266, r=292902920142 0.500000000000000002034327
 * Found exact. n=1000013166299, b=707116091166, r=292897075133
 * 0.499999999999999999052864 Found exact. n=1000043110263, b=707137264746,
 * r=292905845517 0.499999999999999990771113 Found exact. n=1000023201475,
 * b=707123187107, r=292900014368 0.499999999999999998355343 Found exact.
 * n=1000013213620, b=707116124627, r=292897088993 0.500000000000000009618857
 * Found exact. n=1000043157584, b=707137298207, r=292905859377
 * 0.500000000000000001336791 Found exact. n=1000043204905, b=707137331668,
 * r=292905873237 0.500000000000000011902467 Found exact. n=1000033236651,
 * b=707130283048, r=292902953603 0.499999999999999997657837 Found exact.
 * n=1000013280542, b=707116171948, r=292897108594 0.499999999999999994676287
 * Found exact. n=1000043224506, b=707137345528, r=292905878978
 * 0.499999999999999986394667 Found exact. n=1000033283972, b=707130316509,
 * r=292902967463 0.500000000000000008223619 Found exact. n=1000013327863,
 * b=707116205409, r=292897122454 0.500000000000000005242279 Found exact.
 * n=1000043271827, b=707137378989, r=292905892838 0.499999999999999996960344
 * Found exact. n=1000043319148, b=707137412450, r=292905906698
 * 0.500000000000000007526020 Found exact. n=1000033350894, b=707130363830,
 * r=292902987064 0.499999999999999993281347 Found exact. n=1000013394785,
 * b=707116252730, r=292897142055 0.499999999999999990299710 Found exact.
 * n=1000043338749, b=707137426310, r=292905912439 0.499999999999999982018223
 * Found exact. n=1000043366469, b=707137445911, r=292905920558
 * 0.500000000000000018091695 Found exact. n=1000033398215, b=707130397291,
 * r=292903000924 0.500000000000000003847128 Found exact. n=1000013442106,
 * b=707116286191, r=292897155915 0.500000000000000000865701 Found exact.
 * n=1000043386070, b=707137459771, r=292905926299 0.499999999999999992583898
 * Found exact. n=1000023477282, b=707123382132, r=292900095150
 * 0.500000000000000000168163 Found exact. n=1000013489427, b=707116319652,
 * r=292897169775 0.500000000000000011431692 Found exact. n=1000043433391,
 * b=707137493232, r=292905940159 0.500000000000000003149573 Found exact.
 * n=1000043452992, b=707137507092, r=292905945900 0.499999999999999977641779
 * Found exact. n=1000043480712, b=707137526693, r=292905954019
 * 0.500000000000000013715248 Found exact. n=1000033512458, b=707130478073,
 * r=292903034385 0.499999999999999999470639 Found exact. n=1000013556349,
 * b=707116366973, r=292897189376 0.499999999999999996489125 Found exact.
 * n=1000043500313, b=707137540553, r=292905959760 0.499999999999999988207454
 * Found exact. n=1000033559779, b=707130511534, r=292903048245
 * 0.500000000000000010036417 Found exact. n=1000013603670, b=707116400434,
 * r=292897203236 0.500000000000000007055115 Found exact. n=1000043547634,
 * b=707137574014, r=292905973620 0.499999999999999998773128 Found exact.
 * n=1000033626701, b=707130558855, r=292903067846 0.499999999999999995094150
 * Found exact. n=1000043594955, b=707137607475, r=292905987480
 * 0.500000000000000009338800 Found exact. n=1000013670592, b=707116447755,
 * r=292897222837 0.499999999999999992112550 Found exact. n=1000043614556,
 * b=707137621335, r=292905993221 0.499999999999999983831011 Found exact.
 * n=1000033674022, b=707130592316, r=292903081706 0.500000000000000005659928
 * Found exact. n=1000013717913, b=707116481216, r=292897236697
 * 0.500000000000000002678539 Found exact. n=1000043642276, b=707137640936,
 * r=292906001340 0.500000000000000019904472 Found exact. n=1000043661877,
 * b=707137654796, r=292906007081 0.499999999999999994396683 Found exact.
 * n=1000033740944, b=707130639637, r=292903101307 0.499999999999999990717662
 * Found exact. n=1000013784835, b=707116528537, r=292897256298
 * 0.499999999999999987735975 Found exact. n=1000043709198, b=707137688257,
 * r=292906020941 0.500000000000000004962355 Found exact. n=1000043728799,
 * b=707137702117, r=292906026682 0.499999999999999979454568 Found exact.
 * n=1000033788265, b=707130673098, r=292903115167 0.500000000000000001283439
 * Found exact. n=1000013832156, b=707116561998, r=292897270158
 * 0.499999999999999998301963 Found exact. n=1000043756519, b=707137721718,
 * r=292906034801 0.500000000000000015528026 Found exact. n=1000043776120,
 * b=707137735578, r=292906040542 0.499999999999999990020240 Found exact.
 * n=1000013879477, b=707116595459, r=292897284018 0.500000000000000008867950
 * Found exact. n=1000043823441, b=707137769039, r=292906054402
 * 0.500000000000000000585910 Found exact. n=1000013946399, b=707116642780,
 * r=292897303619 0.499999999999999993925389 Found exact. n=1000033902508,
 * b=707130753880, r=292903148628 0.499999999999999996906952 Found exact.
 * n=1000043870762, b=707137802500, r=292906068262 0.500000000000000011151581
 * Found exact. n=1000043890363, b=707137816360, r=292906074003
 * 0.499999999999999985643797 Found exact. n=1000013993720, b=707116676241,
 * r=292897317479 0.500000000000000004491374 Found exact. n=1000033949829,
 * b=707130787341, r=292903162488 0.500000000000000007472726 Found exact.
 * n=1000043918083, b=707137835961, r=292906082122 0.500000000000000021717250
 * Found exact. n=1000043937684, b=707137849821, r=292906087863
 * 0.499999999999999996209467 Found exact. n=1000014060642, b=707116723562,
 * r=292897337080 0.499999999999999989548815 Found exact. n=1000034016751,
 * b=707130834662, r=292903182089 0.499999999999999992530465 Found exact.
 * n=1000043985005, b=707137883282, r=292906101723 0.500000000000000006775136
 * Found exact. n=1000044004606, b=707137897142, r=292906107464
 * 0.499999999999999981267356 Found exact. n=1000014107963, b=707116757023,
 * r=292897350940 0.500000000000000000114800 Found exact. n=1000034064072,
 * b=707130868123, r=292903195949 0.500000000000000003096239 Found exact.
 * n=1000044032326, b=707137916743, r=292906115583 0.500000000000000017340804
 * Found exact. n=1000024143139, b=707123852964, r=292900290175
 * 0.499999999999999999417269 Found exact. n=1000044051927, b=707137930603,
 * r=292906121324 0.499999999999999991833024 Found exact. n=1000014155284,
 * b=707116790484, r=292897364800 0.500000000000000010680784 Found exact.
 * n=1000044099248, b=707137964064, r=292906135184 0.500000000000000002398692
 * Found exact. n=1000044118849, b=707137977924, r=292906140925
 * 0.499999999999999976890915 Found exact. n=1000014222206, b=707116837805,
 * r=292897384401 0.499999999999999995738226 Found exact. n=1000034178315,
 * b=707130948905, r=292903229410 0.499999999999999998719752 Found exact.
 * n=1000044146569, b=707137997525, r=292906149044 0.500000000000000012964360
 * Found exact. n=1000044166170, b=707138011385, r=292906154785
 * 0.499999999999999987456583 Found exact. n=1000014269527, b=707116871266,
 * r=292897398261 0.500000000000000006304209 Found exact. n=1000034225636,
 * b=707130982366, r=292903243270 0.500000000000000009285525 Found exact.
 * n=1000044213491, b=707138044846, r=292906168645 0.499999999999999998022250
 * Found exact. n=1000014336449, b=707116918587, r=292897417862
 * 0.499999999999999991361654 Found exact. n=1000034292558, b=707131029687,
 * r=292903262871 0.499999999999999994343267 Found exact. n=1000044260812,
 * b=707138078307, r=292906182505 0.500000000000000008587916 Found exact.
 * n=1000044280413, b=707138092167, r=292906188246 0.499999999999999983080142
 * Found exact. n=1000014383770, b=707116952048, r=292897431722
 * 0.500000000000000001927635 Found exact. n=1000034339879, b=707131063148,
 * r=292903276731 0.500000000000000004909038 Found exact. n=1000044308133,
 * b=707138111768, r=292906196365 0.500000000000000019153581 Found exact.
 * n=1000044327734, b=707138125628, r=292906202106 0.499999999999999993645809
 * Found exact. n=1000034406801, b=707131110469, r=292903296332
 * 0.499999999999999989966782 Found exact. n=1000044375055, b=707138159089,
 * r=292906215966 0.500000000000000004211473 Found exact. n=1000044394656,
 * b=707138172949, r=292906221707 0.499999999999999978703703 Found exact.
 * n=1000014498013, b=707117032830, r=292897465183 0.499999999999999997551063
 * Found exact. n=1000034454122, b=707131143930, r=292903310192
 * 0.500000000000000000532552 Found exact. n=1000044422376, b=707138192550,
 * r=292906229826 0.500000000000000014777137 Found exact. n=1000044441977,
 * b=707138206410, r=292906235567 0.499999999999999989269368 Found exact.
 * n=1000014545334, b=707117066291, r=292897479043 0.500000000000000008117043
 * Found exact. n=1000034501443, b=707131177391, r=292903324052
 * 0.500000000000000011098321 Found exact. n=1000044489298, b=707138239871,
 * r=292906249427 0.499999999999999999835032 Found exact. n=1000014612256,
 * b=707117113612, r=292897498644 0.499999999999999993174492 Found exact.
 * n=1000034568365, b=707131224712, r=292903343653 0.499999999999999996156068
 * Found exact. n=1000044536619, b=707138273332, r=292906263287
 * 0.500000000000000010400694 Found exact. n=1000044556220, b=707138287192,
 * r=292906269028 0.499999999999999984892928 Found exact. n=1000014659577,
 * b=707117147073, r=292897512504 0.500000000000000003740470 Found exact.
 * n=1000034615686, b=707131258173, r=292903357513 0.500000000000000006721835
 * Found exact. n=1000044583940, b=707138306793, r=292906277147
 * 0.500000000000000020966356 Found exact. n=1000044603541, b=707138320653,
 * r=292906282888 0.499999999999999995458591 Found exact. n=1000014726499,
 * b=707117194394, r=292897532105 0.499999999999999988797921 Found exact.
 * n=1000034682608, b=707131305494, r=292903377114 0.499999999999999991779584
 * Found exact. n=1000044650862, b=707138354114, r=292906296748
 * 0.500000000000000006024253 Found exact. n=1000044670463, b=707138367974,
 * r=292906302489 0.499999999999999980516490 Found exact. n=1000014773820,
 * b=707117227855, r=292897545965 0.499999999999999999363899 Found exact.
 * n=1000034729929, b=707131338955, r=292903390974 0.500000000000000002345351
 * Found exact. n=1000044698183, b=707138387575, r=292906310608
 * 0.500000000000000016589914 Found exact. n=1000044717784, b=707138401435,
 * r=292906316349 0.499999999999999991082152 Found exact. n=1000014821141,
 * b=707117261316, r=292897559825 0.500000000000000009929876 Found exact.
 * n=1000044765105, b=707138434896, r=292906330209 0.500000000000000001647812
 * Found exact. n=1000044784706, b=707138448756, r=292906335950
 * 0.499999999999999976140052 Found exact. n=1000014888063, b=707117308637,
 * r=292897579426 0.499999999999999994987328 Found exact. n=1000034844172,
 * b=707131419737, r=292903424435 0.499999999999999997968868 Found exact.
 * n=1000044812426, b=707138468357, r=292906344069 0.500000000000000012213473
 * Found exact. n=1000044832027, b=707138482217, r=292906349810
 * 0.499999999999999986705713 Found exact. n=1000014935384, b=707117342098,
 * r=292897593286 0.500000000000000005553304 Found exact. n=1000034891493,
 * b=707131453198, r=292903438295 0.500000000000000008534632 Found exact.
 * n=1000044859747, b=707138501818, r=292906357929 0.500000000000000022779131
 * Found exact. n=1000044879348, b=707138515678, r=292906363670
 * 0.499999999999999997271373 Found exact. n=1000015002306, b=707117389419,
 * r=292897612887 0.499999999999999990610759 Found exact. n=1000034958415,
 * b=707131500519, r=292903457896 0.499999999999999993592385 Found exact.
 * n=1000044926669, b=707138549139, r=292906377530 0.500000000000000007837032
 * Found exact. n=1000044946270, b=707138562999, r=292906383271
 * 0.499999999999999982329275 Found exact. n=1000015049627, b=707117422880,
 * r=292897626747 0.500000000000000001176734 Found exact. n=1000035005736,
 * b=707131533980, r=292903471756 0.500000000000000004158149 Found exact.
 * n=1000044973990, b=707138582600, r=292906391390 0.500000000000000018402690
 * Found exact. n=1000044993591, b=707138596460, r=292906397131
 * 0.499999999999999992894935 Found exact. n=1000035072658, b=707131581301,
 * r=292903491357 0.499999999999999989215904 Found exact. n=1000045040912,
 * b=707138629921, r=292906410991 0.500000000000000003460592 Found exact.
 * n=1000015163870, b=707117503662, r=292897660208 0.499999999999999996800164
 * Found exact. n=1000045060513, b=707138643781, r=292906416732
 * 0.499999999999999977952839 Found exact. n=1000035119979, b=707131614762,
 * r=292903505217 0.499999999999999999781667 Found exact. n=1000045088233,
 * b=707138663382, r=292906424851 0.500000000000000014026249 Found exact.
 * n=1000015211191, b=707117537123, r=292897674068 0.500000000000000007366137
 * Found exact. n=1000045107834, b=707138677242, r=292906430592
 * 0.499999999999999988518497 Found exact. n=1000035167300, b=707131648223,
 * r=292903519077 0.500000000000000010347428 Found exact. n=1000045135554,
 * b=707138696843, r=292906438711 0.500000000000000024591905 Found exact.
 * n=1000045155155, b=707138710703, r=292906444452 0.499999999999999999084154
 * Found exact. n=1000015278113, b=707117584444, r=292897693669
 * 0.499999999999999992423596 Found exact. n=1000035234222, b=707131695544,
 * r=292903538678 0.499999999999999995405185 Found exact. n=1000045202476,
 * b=707138744164, r=292906458312 0.500000000000000009649810 Found exact.
 * n=1000015325434, b=707117617905, r=292897707529 0.500000000000000002989567
 * Found exact. n=1000045222077, b=707138758024, r=292906464053
 * 0.499999999999999984142060 Found exact. n=1000035281543, b=707131729005,
 * r=292903552538 0.500000000000000005970945 Found exact. n=1000045249797,
 * b=707138777625, r=292906472172 0.500000000000000020215465 Found exact.
 * n=1000045269398, b=707138791485, r=292906477913 0.499999999999999994707716
 * Found exact. n=1000035348465, b=707131776326, r=292903572139
 * 0.499999999999999991028704 Found exact. n=1000045316719, b=707138824946,
 * r=292906491773 0.500000000000000005273371 Found exact. n=1000015439677,
 * b=707117698687, r=292897740990 0.499999999999999998612999 Found exact.
 * n=1000035395786, b=707131809787, r=292903585999 0.500000000000000001594464
 * Found exact. n=1000045336320, b=707138838806, r=292906497514
 * 0.499999999999999979765625 Found exact. n=1000045364040, b=707138858407,
 * r=292906505633 0.500000000000000015839025 Found exact. n=1000015486998,
 * b=707117732148, r=292897754850 0.500000000000000009178969 Found exact.
 * n=1000035443107, b=707131843248, r=292903599859 0.500000000000000012160223
 * Found exact. n=1000045383641, b=707138872267, r=292906511374
 * 0.499999999999999990331280 Found exact. n=1000045430962, b=707138905728,
 * r=292906525234 0.500000000000000000896934 Found exact. n=1000015553920,
 * b=707117779469, r=292897774451 0.499999999999999994236431 Found exact.
 * n=1000035510029, b=707131890569, r=292903619460 0.499999999999999997217984
 * Found exact. n=1000045450563, b=707138919588, r=292906530975
 * 0.499999999999999975389190 Found exact. n=1000045478283, b=707138939189,
 * r=292906539094 0.500000000000000011462586 Found exact. n=1000015601241,
 * b=707117812930, r=292897788311 0.500000000000000004802400 Found exact.
 * n=1000035557350, b=707131924030, r=292903633320 0.500000000000000007783741
 * Found exact. n=1000045497884, b=707138953049, r=292906544835
 * 0.499999999999999985954844 Found exact. n=1000045525604, b=707138972650,
 * r=292906552954 0.500000000000000022028238 Found exact. n=1000045545205,
 * b=707138986510, r=292906558695 0.499999999999999996520497 Found exact.
 * n=1000015668163, b=707117860251, r=292897807912 0.499999999999999989859864
 * Found exact. n=1000035624272, b=707131971351, r=292903652921
 * 0.499999999999999992841504 Found exact. n=1000045592526, b=707139019971,
 * r=292906572555 0.500000000000000007086149 Found exact. n=1000015715484,
 * b=707117893712, r=292897821772 0.500000000000000000425833 Found exact.
 * n=1000035671593, b=707132004812, r=292903666781 0.500000000000000003407261
 * Found exact. n=1000045612127, b=707139033831, r=292906578296
 * 0.499999999999999981578410 Found exact. n=1000025750660, b=707124989653,
 * r=292900761007 0.499999999999999999728300 Found exact. n=1000045639847,
 * b=707139053432, r=292906586415 0.500000000000000017651800 Found exact.
 * n=1000045659448, b=707139067292, r=292906592156 0.499999999999999992144062
 * Found exact. n=1000035738515, b=707132052133, r=292903686382
 * 0.499999999999999988465025 Found exact. n=1000015829727, b=707117974494,
 * r=292897855233 0.499999999999999996049266 Found exact. n=1000045706769,
 * b=707139100753, r=292906606016 0.500000000000000002709713 Found exact.
 * n=1000035785836, b=707132085594, r=292903700242 0.499999999999999999030781
 * Found exact. n=1000045726370, b=707139114613, r=292906611757
 * 0.499999999999999977201976 Found exact. n=1000015877048, b=707118007955,
 * r=292897869093 0.500000000000000006615232 Found exact. n=1000045754090,
 * b=707139134214, r=292906619876 0.500000000000000013275362 Found exact.
 * n=1000035833157, b=707132119055, r=292903714102 0.500000000000000009596536
 * Found exact. n=1000045773691, b=707139148074, r=292906625617
 * 0.499999999999999987767627 Found exact. n=1000045801411, b=707139167675,
 * r=292906633736 0.500000000000000023841011 Found exact. n=1000015943970,
 * b=707118055276, r=292897888694 0.499999999999999991672700 Found exact.
 * n=1000045821012, b=707139181535, r=292906639477 0.499999999999999998333276
 * Found exact. n=1000035900079, b=707132166376, r=292903733703
 * 0.499999999999999994654303 Found exact. n=1000015991291, b=707118088737,
 * r=292897902554 0.500000000000000002238666 Found exact. n=1000045868333,
 * b=707139214996, r=292906653337 0.500000000000000008898926 Found exact.
 * n=1000035947400, b=707132199837, r=292903747563 0.500000000000000005220057
 * Found exact. n=1000026026467, b=707125184678, r=292900841789
 * 0.500000000000000001541115 Found exact. n=1000045887934, b=707139228856,
 * r=292906659078 0.499999999999999983391193 Found exact. n=1000045915654,
 * b=707139248457, r=292906667197 0.500000000000000019464574 Found exact.
 * n=1000045935255, b=707139262317, r=292906672938 0.499999999999999993956842
 * Found exact. n=1000036014322, b=707132247158, r=292903767164
 * 0.499999999999999990277825 Found exact. n=1000016105534, b=707118169519,
 * r=292897936015 0.499999999999999997862100 Found exact. n=1000045982576,
 * b=707139295778, r=292906686798 0.500000000000000004522490 Found exact.
 * n=1000036061643, b=707132280619, r=292903781024 0.500000000000000000843578
 * Found exact. n=1000046002177, b=707139309638, r=292906692539
 * 0.499999999999999979014761 Found exact. n=1000016152855, b=707118202980,
 * r=292897949875 0.500000000000000008428063 Found exact. n=1000046029897,
 * b=707139329239, r=292906700658 0.500000000000000015088137 Found exact.
 * n=1000036108964, b=707132314080, r=292903794884 0.500000000000000011409330
 * Found exact. n=1000046049498, b=707139343099, r=292906706399
 * 0.499999999999999989580409 Found exact. n=1000046077218, b=707139362700,
 * r=292906714518 0.500000000000000025653783 Found exact. n=1000016219777,
 * b=707118250301, r=292897969476 0.499999999999999993485535 Found exact.
 * n=1000046096819, b=707139376560, r=292906720259 0.500000000000000000146055
 * Found exact. n=1000036175886, b=707132361401, r=292903814485
 * 0.499999999999999996467101 Found exact. n=1000046116420, b=707139390420,
 * r=292906726000 0.499999999999999974638329 Found exact. n=1000016267098,
 * b=707118283762, r=292897983336 0.500000000000000004051497 Found exact.
 * n=1000046144140, b=707139410021, r=292906734119 0.500000000000000010711702
 * Found exact. n=1000036223207, b=707132394862, r=292903828345
 * 0.500000000000000007032851 Found exact. n=1000046163741, b=707139423881,
 * r=292906739860 0.499999999999999985203976 Found exact. n=1000046191461,
 * b=707139443482, r=292906747979 0.500000000000000021277347 Found exact.
 * n=1000046211062, b=707139457342, r=292906753720 0.499999999999999995769622
 * Found exact. n=1000036290129, b=707132442183, r=292903847946
 * 0.499999999999999992090624 Found exact. n=1000016381341, b=707118364544,
 * r=292898016797 0.499999999999999999674932 Found exact. n=1000046258383,
 * b=707139490803, r=292906767580 0.500000000000000006335267 Found exact.
 * n=1000026416517, b=707125460485, r=292900956032 0.499999999999999998977408
 * Found exact. n=1000036337450, b=707132475644, r=292903861806
 * 0.500000000000000002656374 Found exact. n=1000046277984, b=707139504663,
 * r=292906773321 0.499999999999999980827545 Found exact. n=1000046305704,
 * b=707139524264, r=292906781440 0.500000000000000016900910 Found exact.
 * n=1000036384771, b=707132509105, r=292903875666 0.500000000000000013222123
 * Found exact. n=1000046325305, b=707139538124, r=292906787181
 * 0.499999999999999991393189 Found exact. n=1000036404372, b=707132522965,
 * r=292903881407 0.499999999999999987714149 Found exact. n=1000016495584,
 * b=707118445326, r=292898050258 0.499999999999999995298369 Found exact.
 * n=1000046372626, b=707139571585, r=292906801041 0.500000000000000001958834
 * Found exact. n=1000036451693, b=707132556426, r=292903895267
 * 0.499999999999999998279898 Found exact. n=1000046392227, b=707139585445,
 * r=292906806782 0.499999999999999976451114 Found exact. n=1000016542905,
 * b=707118478787, r=292898064118 0.500000000000000005864328 Found exact.
 * n=1000046419947, b=707139605046, r=292906814901 0.500000000000000012524476
 * Found exact. n=1000036499014, b=707132589887, r=292903909127
 * 0.500000000000000008845645 Found exact. n=1000046439548, b=707139618906,
 * r=292906820642 0.499999999999999987016758 Found exact. n=1000016609827,
 * b=707118526108, r=292898083719 0.499999999999999990921807 Found exact.
 * n=1000046467268, b=707139638507, r=292906828761 0.500000000000000023090118
 * Found exact. n=1000046486869, b=707139652367, r=292906834502
 * 0.499999999999999997582400 Found exact. n=1000036565936, b=707132637208,
 * r=292903928728 0.499999999999999993903422 Found exact. n=1000016657148,
 * b=707118559569, r=292898097579 0.500000000000000001487764 Found exact.
 * n=1000046534190, b=707139685828, r=292906848362 0.500000000000000008148043
 * Found exact. n=1000026692324, b=707125655510, r=292901036814
 * 0.500000000000000000790222 Found exact. n=1000036613257, b=707132670669,
 * r=292903942588 0.500000000000000004469169 Found exact. n=1000046553791,
 * b=707139699688, r=292906854103 0.499999999999999982640327 Found exact.
 * n=1000046581511, b=707139719289, r=292906862222 0.500000000000000018713683
 * Found exact. n=1000046601112, b=707139733149, r=292906867963
 * 0.499999999999999993205969 Found exact. n=1000036680179, b=707132717990,
 * r=292903962189 0.499999999999999989526947 Found exact. n=1000016771391,
 * b=707118640351, r=292898131040 0.499999999999999997111202 Found exact.
 * n=1000046648433, b=707139766610, r=292906881823 0.500000000000000003771610
 * Found exact. n=1000036727500, b=707132751451, r=292903976049
 * 0.500000000000000000092693 Found exact. n=1000046668034, b=707139780470,
 * r=292906887564 0.499999999999999978263898 Found exact. n=1000016818712,
 * b=707118673812, r=292898144900 0.500000000000000007677158 Found exact.
 * n=1000046695754, b=707139800071, r=292906895683 0.500000000000000014337250
 * Found exact. n=1000036774821, b=707132784912, r=292903989909
 * 0.500000000000000010658438 Found exact. n=1000046715355, b=707139813931,
 * r=292906901424 0.499999999999999988829538 Found exact. n=1000016885634,
 * b=707118721133, r=292898164501 0.499999999999999992734640 Found exact.
 * n=1000046743075, b=707139833532, r=292906909543 0.500000000000000024902889
 * Found exact. n=1000046762676, b=707139847392, r=292906915284
 * 0.499999999999999999395179 Found exact. n=1000036841743, b=707132832233,
 * r=292904009510 0.499999999999999995716219 Found exact. n=1000016932955,
 * b=707118754594, r=292898178361 0.500000000000000003300596 Found exact.
 * n=1000046782277, b=707139861252, r=292906921025 0.499999999999999973887469
 * Found exact. n=1000046809997, b=707139880853, r=292906929144
 * 0.500000000000000009960817 Found exact. n=1000036889064, b=707132865694,
 * r=292904023370 0.500000000000000006281962 Found exact. n=1000026968131,
 * b=707125850535, r=292901117596 0.500000000000000002603035 Found exact.
 * n=1000046829598, b=707139894713, r=292906934885 0.499999999999999984453109
 * Found exact. n=1000046857318, b=707139914314, r=292906943004
 * 0.500000000000000020526455 Found exact. n=1000046876919, b=707139928174,
 * r=292906948745 0.499999999999999995018748 Found exact. n=1000036955986,
 * b=707132913015, r=292904042971 0.499999999999999991339745 Found exact.
 * n=1000017047198, b=707118835376, r=292898211822 0.499999999999999998924033
 * Found exact. n=1000046924240, b=707139961635, r=292906962605
 * 0.500000000000000005584386 Found exact. n=1000027082374, b=707125931317,
 * r=292901151057 0.499999999999999998226517 Found exact. n=1000037003307,
 * b=707132946476, r=292904056831 0.500000000000000001905488 Found exact.
 * n=1000046943841, b=707139975495, r=292906968346 0.499999999999999980076680
 * Found exact. n=1000046971561, b=707139995096, r=292906976465
 * 0.500000000000000016150023 Found exact. n=1000037050628, b=707132979937,
 * r=292904070691 0.500000000000000012471230 Found exact. n=1000046991162,
 * b=707140008956, r=292906982206 0.499999999999999990642318 Found exact.
 * n=1000037070229, b=707132993797, r=292904076432 0.499999999999999986963273
 * Found exact. n=1000017161441, b=707118916158, r=292898245283
 * 0.499999999999999994547473 Found exact. n=1000047018882, b=707140028557,
 * r=292906990325 0.500000000000000026715658 Found exact. n=1000047038483,
 * b=707140042417, r=292906996066 0.500000000000000001207955 Found exact.
 * n=1000037117550, b=707133027258, r=292904090292 0.499999999999999997529014
 * Found exact. n=1000017208762, b=707118949619, r=292898259143
 * 0.500000000000000005113425 Found exact. n=1000047058084, b=707140056277,
 * r=292907001807 0.499999999999999975700253 Found exact. n=1000047085804,
 * b=707140075878, r=292907009926 0.500000000000000011773591 Found exact.
 * n=1000037164871, b=707133060719, r=292904104152 0.500000000000000008094755
 * Found exact. n=1000047105405, b=707140089738, r=292907015667
 * 0.499999999999999986265890 Found exact. n=1000047133125, b=707140109339,
 * r=292907023786 0.500000000000000022339226 Found exact. n=1000037231793,
 * b=707133108040, r=292904123753 0.499999999999999993152542 Found exact.
 * n=1000047152726, b=707140123199, r=292907029527 0.499999999999999996831526
 * Found exact. n=1000017323005, b=707119030401, r=292898292604
 * 0.500000000000000000736865 Found exact. n=1000027358181, b=707126126342,
 * r=292901231839 0.500000000000000000039330 Found exact. n=1000037279114,
 * b=707133141501, r=292904137613 0.500000000000000003718282 Found exact.
 * n=1000047200047, b=707140156660, r=292907043387 0.500000000000000007397161
 * Found exact. n=1000047219648, b=707140170520, r=292907049128
 * 0.499999999999999981889462 Found exact. n=1000037326435, b=707133174962,
 * r=292904151473 0.500000000000000014284020 Found exact. n=1000047247368,
 * b=707140190121, r=292907057247 0.500000000000000017962795 Found exact.
 * n=1000037346036, b=707133188822, r=292904157214 0.499999999999999988776070
 * Found exact. n=1000047266969, b=707140203981, r=292907062988
 * 0.499999999999999992455097 Found exact. n=1000017437248, b=707119111183,
 * r=292898326065 0.499999999999999996360305 Found exact. n=1000037393357,
 * b=707133222283, r=292904171074 0.499999999999999999341809 Found exact.
 * n=1000047314290, b=707140237442, r=292907076848 0.500000000000000003020731
 * Found exact. n=1000017484569, b=707119144644, r=292898339925
 * 0.500000000000000006926254 Found exact. n=1000047333891, b=707140251302,
 * r=292907082589 0.499999999999999977513036 Found exact. n=1000037440678,
 * b=707133255744, r=292904184934 0.500000000000000009907547 Found exact.
 * n=1000047361611, b=707140270903, r=292907090708 0.500000000000000013586364
 * Found exact. n=1000047381212, b=707140284763, r=292907096449
 * 0.499999999999999988078670 Found exact. n=1000017551491, b=707119191965,
 * r=292898359526 0.499999999999999991983747 Found exact. n=1000047408932,
 * b=707140304364, r=292907104568 0.500000000000000024151996 Found exact.
 * n=1000037507600, b=707133303065, r=292904204535 0.499999999999999994965337
 * Found exact. n=1000047428533, b=707140318224, r=292907110309
 * 0.499999999999999998644303 Found exact. n=1000017598812, b=707119225426,
 * r=292898373386 0.500000000000000002549694 Found exact. n=1000047448134,
 * b=707140332084, r=292907116050 0.499999999999999973136610 Found exact.
 * n=1000027633988, b=707126321367, r=292901312621 0.500000000000000001852142
 * Found exact. n=1000037554921, b=707133336526, r=292904218395
 * 0.500000000000000005531074 Found exact. n=1000047475854, b=707140351685,
 * r=292907124169 0.500000000000000009209935 Found exact. n=1000047495455,
 * b=707140365545, r=292907129910 0.499999999999999983702243 Found exact.
 * n=1000047523175, b=707140385146, r=292907138029 0.500000000000000019775565
 * Found exact. n=1000037621843, b=707133383847, r=292904237996
 * 0.499999999999999990588867 Found exact. n=1000047542776, b=707140399006,
 * r=292907143770 0.499999999999999994267875 Found exact. n=1000017713055,
 * b=707119306208, r=292898406847 0.499999999999999998173136 Found exact.
 * n=1000027748231, b=707126402149, r=292901346082 0.499999999999999997475627
 * Found exact. n=1000037669164, b=707133417308, r=292904251856
 * 0.500000000000000001154603 Found exact. n=1000047590097, b=707140432467,
 * r=292907157630 0.500000000000000004833506 Found exact. n=1000047609698,
 * b=707140446327, r=292907163371 0.499999999999999979325818 Found exact.
 * n=1000037716485, b=707133450769, r=292904265716 0.500000000000000011720338
 * Found exact. n=1000047637418, b=707140465928, r=292907171490
 * 0.500000000000000015399136 Found exact. n=1000037736086, b=707133464629,
 * r=292904271457 0.499999999999999986212398 Found exact. n=1000047657019,
 * b=707140479788, r=292907177231 0.499999999999999989891449 Found exact.
 * n=1000017827298, b=707119386990, r=292898440308 0.499999999999999993796578
 * Found exact. n=1000047684739, b=707140499389, r=292907185350
 * 0.500000000000000025964765 Found exact. n=1000037783407, b=707133498090,
 * r=292904285317 0.499999999999999996778132 Found exact. n=1000047704340,
 * b=707140513249, r=292907191091 0.500000000000000000457079 Found exact.
 * n=1000017874619, b=707119420451, r=292898454168 0.500000000000000004362523
 * Found exact. n=1000047723941, b=707140527109, r=292907196832
 * 0.499999999999999974949393 Found exact. n=1000027909795, b=707126516392,
 * r=292901393403 0.500000000000000003664952 Found exact. n=1000037830728,
 * b=707133531551, r=292904299177 0.500000000000000007343867 Found exact.
 * n=1000047751661, b=707140546710, r=292907204951 0.500000000000000011022707
 * Found exact. n=1000047771262, b=707140560570, r=292907210692
 * 0.499999999999999985515023 Found exact. n=1000047798982, b=707140580171,
 * r=292907218811 0.500000000000000021588335 Found exact. n=1000037897650,
 * b=707133578872, r=292904318778 0.499999999999999992401663 Found exact.
 * n=1000047818583, b=707140594031, r=292907224552 0.499999999999999996080652
 * Found exact. n=1000017988862, b=707119501233, r=292898487629
 * 0.499999999999999999985966 Found exact. n=1000028024038, b=707126597174,
 * r=292901426864 0.499999999999999999288439 Found exact. n=1000037944971,
 * b=707133612333, r=292904332638 0.500000000000000002967396 Found exact.
 * n=1000047865904, b=707140627492, r=292907238412 0.500000000000000006646280
 * Found exact. n=1000047885505, b=707140641352, r=292907244153
 * 0.499999999999999981138598 Found exact. n=1000037992292, b=707133645794,
 * r=292904346498 0.500000000000000013533128 Found exact. n=1000047913225,
 * b=707140660953, r=292907252272 0.500000000000000017211907 Found exact.
 * n=1000038011893, b=707133659654, r=292904352239 0.499999999999999988025195
 * Found exact. n=1000018103105, b=707119582015, r=292898521090
 * 0.499999999999999995609409 Found exact. n=1000047932826, b=707140674813,
 * r=292907258013 0.499999999999999991704226 Found exact. n=1000047960546,
 * b=707140694414, r=292907266132 0.500000000000000027777532 Found exact.
 * n=1000038059214, b=707133693115, r=292904366099 0.499999999999999998590926
 * Found exact. n=1000018150426, b=707119615476, r=292898534950
 * 0.500000000000000006175351 Found exact. n=1000047980147, b=707140708274,
 * r=292907271873 0.500000000000000002269853 Found exact. n=1000047999748,
 * b=707140722134, r=292907277614 0.499999999999999976762175 Found exact.
 * n=1000038106535, b=707133726576, r=292904379959 0.500000000000000009156657
 * Found exact. n=1000048027468, b=707140741735, r=292907285733
 * 0.500000000000000012835479 Found exact. n=1000048047069, b=707140755595,
 * r=292907291474 0.499999999999999987327802 Found exact. n=1000048074789,
 * b=707140775196, r=292907299593 0.500000000000000023401104 Found exact.
 * n=1000038173457, b=707133773897, r=292904399560 0.499999999999999994214458
 * Found exact. n=1000018264669, b=707119696258, r=292898568411
 * 0.500000000000000001798794 Found exact. n=1000048094390, b=707140789056,
 * r=292907305334 0.499999999999999997893428 Found exact. n=1000048113991,
 * b=707140802916, r=292907311075 0.499999999999999972385753 Found exact.
 * n=1000028299845, b=707126792199, r=292901507646 0.500000000000000001101250
 * Found exact. n=1000038220778, b=707133807358, r=292904413420
 * 0.500000000000000004780188 Found exact. n=1000048141711, b=707140822517,
 * r=292907319194 0.500000000000000008459053 Found exact. n=1000048161312,
 * b=707140836377, r=292907324935 0.499999999999999982951378 Found exact.
 * n=1000038268099, b=707133840819, r=292904427280 0.500000000000000015345917
 * Found exact. n=1000048189032, b=707140855978, r=292907333054
 * 0.500000000000000019024677 Found exact. n=1000038287700, b=707133854679,
 * r=292904433021 0.499999999999999989837990 Found exact. n=1000018378912,
 * b=707119777040, r=292898601872 0.499999999999999997422239 Found exact.
 * n=1000048208633, b=707140869838, r=292907338795 0.499999999999999993517003
 * Found exact. n=1000028414088, b=707126872981, r=292901541107
 * 0.499999999999999996724738 Found exact. n=1000038335021, b=707133888140,
 * r=292904446881 0.500000000000000000403719 Found exact. n=1000048255954,
 * b=707140903299, r=292907352655 0.500000000000000004082627 Found exact.
 * n=1000048275555, b=707140917159, r=292907358396 0.499999999999999978574956
 * Found exact. n=1000038382342, b=707133921601, r=292904460741
 * 0.500000000000000010969447 Found exact. n=1000048303275, b=707140936760,
 * r=292907366515 0.500000000000000014648250 Found exact. n=1000018493155,
 * b=707119857822, r=292898635333 0.499999999999999993045685 Found exact.
 * n=1000038401943, b=707133935461, r=292904466482 0.499999999999999985461524
 * Found exact. n=1000048322876, b=707140950620, r=292907372256
 * 0.499999999999999989140580 Found exact. n=1000048350596, b=707140970221,
 * r=292907380375 0.500000000000000025213871 Found exact. n=1000018540476,
 * b=707119891283, r=292898649193 0.500000000000000003611622 Found exact.
 * n=1000038449264, b=707133968922, r=292904480342 0.499999999999999996027252
 * Found exact. n=1000048370197, b=707140984081, r=292907386116
 * 0.499999999999999999706202 Found exact. n=1000028575652, b=707126987224,
 * r=292901588428 0.500000000000000002914060 Found exact. n=1000048389798,
 * b=707140997941, r=292907391857 0.499999999999999974198534 Found exact.
 * n=1000038496585, b=707134002383, r=292904494202 0.500000000000000006592979
 * Found exact. n=1000048417518, b=707141017542, r=292907399976
 * 0.500000000000000010271824 Found exact. n=1000048437119, b=707141031402,
 * r=292907405717 0.499999999999999984764157 Found exact. n=1000048464839,
 * b=707141051003, r=292907413836 0.500000000000000020837445 Found exact.
 * n=1000018654719, b=707119972065, r=292898682654 0.499999999999999999235068
 * Found exact. n=1000038563507, b=707134049704, r=292904513803
 * 0.499999999999999991650785 Found exact. n=1000048484440, b=707141064863,
 * r=292907419577 0.499999999999999995329779 Found exact. n=1000028689895,
 * b=707127068006, r=292901621889 0.499999999999999998537549 Found exact.
 * n=1000038610828, b=707134083165, r=292904527663 0.500000000000000002216510
 * Found exact. n=1000048531761, b=707141098324, r=292907433437
 * 0.500000000000000005895399 Found exact. n=1000048551362, b=707141112184,
 * r=292907439178 0.499999999999999980387736 Found exact. n=1000038658149,
 * b=707134116626, r=292904541523 0.500000000000000012782236 Found exact.
 * n=1000018768962, b=707120052847, r=292898716115 0.499999999999999994858514
 * Found exact. n=1000048579082, b=707141131785, r=292907447297
 * 0.500000000000000016461020 Found exact. n=1000038677750, b=707134130486,
 * r=292904547264 0.499999999999999987274320 Found exact. n=1000048598683,
 * b=707141145645, r=292907453038 0.499999999999999990953357 Found exact.
 * n=1000018816283, b=707120086308, r=292898729975 0.500000000000000005424449
 * Found exact. n=1000038725071, b=707134163947, r=292904561124
 * 0.499999999999999997840044 Found exact. n=1000048626403, b=707141165246,
 * r=292907461157 0.500000000000000027026639 Found exact. n=1000048646004,
 * b=707141179106, r=292907466898 0.500000000000000001518976 Found exact.
 * n=1000028851459, b=707127182249, r=292901669210 0.500000000000000004726868
 * Found exact. n=1000048665605, b=707141192966, r=292907472639
 * 0.499999999999999976011315 Found exact. n=1000038772392, b=707134197408,
 * r=292904574984 0.500000000000000008405768 Found exact. n=1000048693325,
 * b=707141212567, r=292907480758 0.500000000000000012084595 Found exact.
 * n=1000048712926, b=707141226427, r=292907486499 0.499999999999999986576935
 * Found exact. n=1000018930526, b=707120167090, r=292898763436
 * 0.500000000000000001047895 Found exact. n=1000038839314, b=707134244729,
 * r=292904594585 0.499999999999999993463578 Found exact. n=1000048740646,
 * b=707141246028, r=292907494618 0.500000000000000022650213 Found exact.
 * n=1000048760247, b=707141259888, r=292907500359 0.499999999999999997142554
 * Found exact. n=1000028965702, b=707127263031, r=292901702671
 * 0.500000000000000000350359 Found exact. n=1000048779848, b=707141273748,
 * r=292907506100 0.499999999999999971634895 Found exact. n=1000038886635,
 * b=707134278190, r=292904608445 0.500000000000000004029301 Found exact.
 * n=1000048807568, b=707141293349, r=292907514219 0.500000000000000007708172
 * Found exact. n=1000048827169, b=707141307209, r=292907519960
 * 0.499999999999999982200514 Found exact. n=1000038933956, b=707134311651,
 * r=292904622305 0.500000000000000014595023 Found exact. n=1000019044769,
 * b=707120247872, r=292898796897 0.499999999999999996671343 Found exact.
 * n=1000038953557, b=707134325511, r=292904628046 0.499999999999999989087114
 * Found exact. n=1000048854889, b=707141326810, r=292907528079
 * 0.500000000000000018273789 Found exact. n=1000048874490, b=707141340670,
 * r=292907533820 0.499999999999999992766132 Found exact. n=1000029079945,
 * b=707127343813, r=292901736132 0.499999999999999995973850 Found exact.
 * n=1000039000878, b=707134358972, r=292904641906 0.499999999999999999652836
 * Found exact. n=1000048902210, b=707141360271, r=292907541939
 * 0.500000000000000028839404 Found exact. n=1000048921811, b=707141374131,
 * r=292907547680 0.500000000000000003331749 Found exact. n=1000048941412,
 * b=707141387991, r=292907553421 0.499999999999999977824095 Found exact.
 * n=1000039048199, b=707134392433, r=292904655766 0.500000000000000010218557
 * Found exact. n=1000039067800, b=707134406293, r=292904661507
 * 0.499999999999999984710650 Found exact. n=1000048969132, b=707141407592,
 * r=292907561540 0.500000000000000013897365 Found exact. n=1000048988733,
 * b=707141421452, r=292907567281 0.499999999999999988389712 Found exact.
 * n=1000019206333, b=707120362115, r=292898844218 0.500000000000000002860722
 * Found exact. n=1000039115121, b=707134439754, r=292904675367
 * 0.499999999999999995276372 Found exact. n=1000049016453, b=707141441053,
 * r=292907575400 0.500000000000000024462980 Found exact. n=1000049036054,
 * b=707141454913, r=292907581141 0.499999999999999998955327 Found exact.
 * n=1000029241509, b=707127458056, r=292901783453 0.500000000000000002163168
 * Found exact. n=1000049055655, b=707141468773, r=292907586882
 * 0.499999999999999973447676 Found exact. n=1000039162442, b=707134473215,
 * r=292904689227 0.500000000000000005842091 Found exact. n=1000049083375,
 * b=707141488374, r=292907595001 0.500000000000000009520942 Found exact.
 * n=1000049102976, b=707141502234, r=292907600742 0.499999999999999984013292
 * Found exact. n=1000039209763, b=707134506676, r=292904703087
 * 0.500000000000000016407810 Found exact. n=1000019320576, b=707120442897,
 * r=292898877679 0.499999999999999998484171 Found exact. n=1000039229364,
 * b=707134520536, r=292904708828 0.499999999999999990899908 Found exact.
 * n=1000049130696, b=707141521835, r=292907608861 0.500000000000000020086556
 * Found exact. n=1000049150297, b=707141535695, r=292907614602
 * 0.499999999999999994578907 Found exact. n=1000029355752, b=707127538838,
 * r=292901816914 0.499999999999999997786660 Found exact. n=1000039276685,
 * b=707134553997, r=292904722688 0.500000000000000001465627 Found exact.
 * n=1000049197618, b=707141569156, r=292907628462 0.500000000000000005144521
 * Found exact. n=1000049217219, b=707141583016, r=292907634203
 * 0.499999999999999979636873 Found exact. n=1000039324006, b=707134587458,
 * r=292904736548 0.500000000000000012031345 Found exact. n=1000019434819,
 * b=707120523679, r=292898911140 0.499999999999999994107620 Found exact.
 * n=1000039343607, b=707134601318, r=292904742289 0.499999999999999986523446
 * Found exact. n=1000049244939, b=707141602617, r=292907642322
 * 0.500000000000000015710134 Found exact. n=1000049264540, b=707141616477,
 * r=292907648063 0.499999999999999990202487 Found exact. n=1000019482140,
 * b=707120557140, r=292898925000 0.500000000000000004673548 Found exact.
 * n=1000039390928, b=707134634779, r=292904756149 0.499999999999999997089163
 * Found exact. n=1000049292260, b=707141636078, r=292907656182
 * 0.500000000000000026275745 Found exact. n=1000029517316, b=707127653081,
 * r=292901864235 0.500000000000000003975975 Found exact. n=1000049311861,
 * b=707141649938, r=292907661923 0.500000000000000000768100 Found exact.
 * n=1000049331462, b=707141663798, r=292907667664 0.499999999999999975260456
 * Found exact. n=1000039438249, b=707134668240, r=292904770009
 * 0.500000000000000007654880 Found exact. n=1000049359182, b=707141683399,
 * r=292907675783 0.500000000000000011333712 Found exact. n=1000049378783,
 * b=707141697259, r=292907681524 0.499999999999999985826069 Found exact.
 * n=1000019596383, b=707120637922, r=292898958461 0.500000000000000000296997
 * Found exact. n=1000039505171, b=707134715561, r=292904789610
 * 0.499999999999999992712701 Found exact. n=1000049406503, b=707141716860,
 * r=292907689643 0.500000000000000021899323 Found exact. n=1000029631559,
 * b=707127733863, r=292901897696 0.499999999999999999599469 Found exact.
 * n=1000049426104, b=707141730720, r=292907695384 0.499999999999999996391681
 * Found exact. n=1000049445705, b=707141744580, r=292907701125
 * 0.499999999999999970884039 Found exact. n=1000039552492, b=707134749022,
 * r=292904803470 0.500000000000000003278417 Found exact. n=1000049473425,
 * b=707141764181, r=292907709244 0.500000000000000006957291 Found exact.
 * n=1000049493026, b=707141778041, r=292907714985 0.499999999999999981449652
 * Found exact. n=1000039599813, b=707134782483, r=292904817330
 * 0.500000000000000013844132 Found exact. n=1000019710626, b=707120718704,
 * r=292898991922 0.499999999999999995920448 Found exact. n=1000039619414,
 * b=707134796343, r=292904823071 0.499999999999999988336239 Found exact.
 * n=1000049520746, b=707141797642, r=292907723104 0.500000000000000017522901
 * Found exact. n=1000029745802, b=707127814645, r=292901931157
 * 0.499999999999999995222963 Found exact. n=1000049540347, b=707141811502,
 * r=292907728845 0.499999999999999992015262 Found exact. n=1000039666735,
 * b=707134829804, r=292904836931 0.499999999999999998901954 Found exact.
 * n=1000049568067, b=707141831103, r=292907736964 0.500000000000000028088511
 * Found exact. n=1000029793123, b=707127848106, r=292901945017
 * 0.500000000000000005788782 Found exact. n=1000049587668, b=707141844963,
 * r=292907742705 0.500000000000000002580872 Found exact. n=1000049607269,
 * b=707141858823, r=292907748446 0.499999999999999977073235 Found exact.
 * n=1000039714056, b=707134863265, r=292904850791 0.500000000000000009467668
 * Found exact. n=1000039733657, b=707134877125, r=292904856532
 * 0.499999999999999983959779 Found exact. n=1000049634989, b=707141878424,
 * r=292907756565 0.500000000000000013146481 Found exact. n=1000049654590,
 * b=707141892284, r=292907762306 0.499999999999999987638845 Found exact.
 * n=1000019872190, b=707120832947, r=292899039243 0.500000000000000002109823
 * Found exact. n=1000039780978, b=707134910586, r=292904870392
 * 0.499999999999999994525492 Found exact. n=1000049682310, b=707141911885,
 * r=292907770425 0.500000000000000023712089 Found exact. n=1000029907366,
 * b=707127928888, r=292901978478 0.500000000000000001412276 Found exact.
 * n=1000049701911, b=707141925745, r=292907776166 0.499999999999999998204453
 * Found exact. n=1000039828299, b=707134944047, r=292904884252
 * 0.500000000000000005091205 Found exact. n=1000049721512, b=707141939605,
 * r=292907781907 0.499999999999999972696819 Found exact. n=1000049749232,
 * b=707141959206, r=292907790026 0.500000000000000008770061 Found exact.
 * n=1000039875620, b=707134977508, r=292904898112 0.500000000000000015656917
 * Found exact. n=1000019986433, b=707120913729, r=292899072704
 * 0.499999999999999997733274 Found exact. n=1000049768833, b=707141973066,
 * r=292907795767 0.499999999999999983262428 Euler_100$1@64f6cd stopped with
 * n=1000010000000, d=10000000 Euler_100$1@872380 stopped with n=1000020000000,
 * d=10000000 Found exact. n=1000039895221, b=707134991368, r=292904903853
 * 0.499999999999999990149032 Found exact. n=1000049796553, b=707141992667,
 * r=292907803886 0.500000000000000019335668 Found exact. n=1000049816154,
 * b=707142006527, r=292907809627 0.499999999999999993828036 Found exact.
 * n=1000039942542, b=707135024829, r=292904917713 0.500000000000000000714744
 * Found exact. n=1000049843874, b=707142026128, r=292907817746
 * 0.500000000000000029901274 Found exact. n=1000049863475, b=707142039988,
 * r=292907823487 0.500000000000000004393643 Found exact. n=1000039989863,
 * b=707135058290, r=292904931573 0.500000000000000011280455 Found exact.
 * n=1000049883076, b=707142053848, r=292907829228 0.499999999999999978886012
 * Euler_100$1@2bb514 stopped with n=1000030000000, d=10000000 Found exact.
 * n=1000049910796, b=707142073449, r=292907837347 0.500000000000000014959248
 * Found exact. n=1000049930397, b=707142087309, r=292907843088
 * 0.499999999999999989451619 Found exact. n=1000049958117, b=707142106910,
 * r=292907851207 0.500000000000000025524854 Found exact. n=1000049977718,
 * b=707142120770, r=292907856948 0.500000000000000000017225 Found exact.
 * n=1000049997319, b=707142134630, r=292907862689 0.499999999999999974509598
 * Euler_100$1@17d5d2a stopped with n=1000040000000, d=10000000
 */

/*
 * b + r = tot; b=15 r=6 tot=21 (15/21)*(14/20)=0.5
 * (85/(85+35))*(84/(84+35))=0.5 (b/(b+r))*((b-1)/(b-1+r))=0.5
 * b*(b-1)=0.5*(b*r)*(b-1+r)
 * 
 * 
 * Answer: 707106781187? (not correct) 7.07106781186694E11 7.07106781186694E11
 * 
 * Found, b=7.071067811881082E11 707106781188.1082 ... r=292893218808,
 * b=707106781192, v=0.5000000000075039 r=292893218809, b=707106781191,
 * v=0.5000000000060897 r=292893218810, b=707106781190, v=0.5000000000046755
 * r=292893218811, b=707106781189, v=0.5000000000032613 r=292893218812,
 * b=707106781188, v=0.500000000001847 r=292893218813, b=707106781187,
 * v=0.5000000000004328 r=292893218814, b=707106781186, v=0.4999999999990186
 * Stop. r=292893218814, b=707106781186
 */
// Check. 292893218814+707106781186=1000000000000 (ok), hmm...

// Eleven
// Jackpot, b=707106788769
// Found exact. n=1000000010723, b=707106788769, r=292893221954
// Jackpot, b=707106802629
// Found exact. n=1000000030324, b=707106802629, r=292893227695
// Jackpot, b=707106836090
// Found exact. n=1000000077645, b=707106836090, r=292893241555
// Jackpot, b=707106849950
// Found exact. n=1000000097246, b=707106849950, r=292893247296

