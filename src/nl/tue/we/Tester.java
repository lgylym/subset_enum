package nl.tue.we;

import com.google.common.base.Stopwatch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.apache.commons.math3.stat.descriptive.SummaryStatistics;

/**
 * This method tests the enum performances
 */
public class Tester {

    public enum EnumAlgorithm {
        FilterEnum, JumpEnum, TrieEnum, PTrieEnum
    }

    private static int noRounds = 1;//rounds to do each test
    private static long seed = 0;//seed for random generator

    public static Stopwatch runWatch = Stopwatch.createUnstarted();
    public static Stopwatch buildRunWatch = Stopwatch.createUnstarted();

    public static void main(String[] args) {
        EnumAlgorithm algorithm;
        int noBits = 25;
        int sSize = 18;//2^sSize
        int rSize = 0;//2^rSize result size


        OptionParser parser = new OptionParser( "m:n:s:r:" );
        OptionSet options = parser.parse(args);
        algorithm = EnumAlgorithm.valueOf((String) options.valueOf("m"));
        noBits = Integer.valueOf((String) options.valueOf("n"));
        sSize = Integer.valueOf((String) options.valueOf("s"));
        rSize = Integer.valueOf((String) options.valueOf("r"));


        Random rd = new Random(seed);
        SummaryStatistics runTime = new SummaryStatistics();
        SummaryStatistics buildRunTime = new SummaryStatistics();

        //prepare r
        rd.setSeed(seed);
        long r = genR(rd, noBits);
        //prepare S
        rd.setSeed(seed);
        ArrayList<Long> S;
        if(rSize == 0){
            S = genS(rd, r, noBits, (int) Math.pow(2, sSize), 0);
        }else {
            S = genS(rd, r, noBits, (int) Math.pow(2, sSize), (int) Math.pow(2, rSize));
        }

        switch (algorithm) {
            case FilterEnum:
                runTest(runTime, buildRunTime, noRounds, new FilterEnum(), r, S, noBits);
                break;
            case JumpEnum:
                runTest(runTime, buildRunTime, noRounds, new JumpEnum(), r, S, noBits);
                break;
            case TrieEnum:
                runTest(runTime, buildRunTime, noRounds, new TrieEnum(), r, S, noBits);
                break;
            case PTrieEnum:
                runTest(runTime, buildRunTime, noRounds, new PTrieEnum(), r, S, noBits);
                break;
            default:
                //
        }

        /*
        for(int noBits = 10; noBits < 35; noBits += 5) {//10,15,...,30
            rd.setSeed(0);
            long r = genR(rd, noBits);

            for(int sSize = 15; sSize < 20; sSize++) {//2^15,...,2^19
                rd.setSeed(0);
                ArrayList<Long> S = genS(rd,r,noBits,(int)Math.pow(2,sSize),0);
                runTest(runTime, buildRunTime, noRounds, new FilterEnum(), r, S, noBits);
                runTest(runTime, buildRunTime, noRounds, new JumpEnum(), r, S, noBits);
                runTest(runTime, buildRunTime, noRounds, new TrieEnum(), r, S, noBits);
                runTest(runTime, buildRunTime, noRounds, new PTrieEnum(), r, S, noBits);
            }
        }
        */
        //smallTest();
    }


    private static void smallTest() {
        long r = 0xFL;
        ArrayList<Long> S = new ArrayList<Long>();
        S.add(0xFL);
        S.add(0xEL);
        S.add(0L);
        S.add(0x8L);
        //S.add(0x9L);
        S.add(0x1010L);
        S.add(0x1011L);

        System.out.println(new TrieEnum().ToEnum(r, S));
        System.out.println(new PTrieEnum().ToEnum(r, S));

        System.out.println(new FilterEnum().ToEnum(r, S));
        System.out.println(new JumpEnum().ToEnum(r, S));
//


//        S.add(0x0000L);
//
//        System.out.println(new FilterEnum().ToEnum(r, S));
//        System.out.println(new JumpEnum().ToEnum(r, S));
//        System.out.println(new TrieEnum().ToEnum(r, S));
//
//        S.add(0x1110L);
//        S.add(0x0101110L);
//
//        System.out.println(new FilterEnum().ToEnum(r, S));
//        System.out.println(new JumpEnum().ToEnum(r, S));
//        System.out.println(new TrieEnum().ToEnum(r, S));
    }



    private static long genR(Random rd, int noBits) {
        long r = 0;
        HashSet<Integer> positions = new HashSet<Integer>();
        while(positions.size() < noBits) {
            positions.add(rd.nextInt(64));
        }
        for(int i:positions) {
            //set ith position to be 1
            r |= (1L << i);
        }//r done
        return r;
    }

    /**
     *
     * @param rd
     * @param sSize |S|
     * @param rSize |Result|
     * @return
     */
    private static ArrayList<Long> genS(Random rd, long r, int noBits, int sSize, int rSize) {
        assert sSize >= rSize;
        assert Math.pow(2,noBits) >= rSize;

        HashSet<Long> S = new HashSet<Long>();
        if(rSize == 0) {
            while(S.size() < sSize) {
                S.add(rd.nextLong());
            }//S done
        }else {
            //first generate results
            long t = r;
            while(S.size() < rSize) {
                S.add(t);
                t = (t-1)&r;
            }
            while(S.size() < sSize) {
                S.add(rd.nextLong());
            }
            //then generate others
        }
        ArrayList<Long> sList = new ArrayList<Long>(S);
        return sList;
    }


    private static void runTest(SummaryStatistics runTime, SummaryStatistics buildRunTime, int experimentRound, RestrictedEnum method,
                         long r, ArrayList<Long> sList, int noBits) {
        runTime.clear();
        buildRunTime.clear();

        long rSize = 0;

        for(int i = 0; i < experimentRound; i++) {

            rSize = method.ToEnum(r, sList);

            runWatch.stop();buildRunWatch.stop();
            runTime.addValue(runWatch.elapsed(TimeUnit.MILLISECONDS));
            buildRunTime.addValue(buildRunWatch.elapsed(TimeUnit.MILLISECONDS));

        }
        //System.out.println(method.getClass().getSimpleName()+ " " + noBits + " " + sSize + " " + rSize + " " + runTime.getMean() + " " + runTime.getStandardDeviation());
        //System.out.println(method.getClass().getSimpleName()+ "B " + noBits + " " + sSize + " " + rSize + " " + buildRunTime.getMean() + " " + buildRunTime.getStandardDeviation());
        System.out.format(method.getClass().getSimpleName()+" %d %d %d %.2f %.2f\n",noBits, sList.size(), rSize, runTime.getMean(),runTime.getStandardDeviation());
        System.out.format(method.getClass().getSimpleName()+"B %d %d %d %.2f %.2f\n",noBits, sList.size(), rSize, buildRunTime.getMean(),buildRunTime.getStandardDeviation());
        //System.out.println(buildRunTime.getMin() + "," + buildRunTime.getMax());
    }

}
