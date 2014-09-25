package nl.tue.we;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by yluo on 9/22/2014.
 * This corresponds to the method gen and filter
 */
public class FilterEnum implements RestrictedEnum{

    @Override
    public long ToEnum(long r, ArrayList<Long> S) {
        //create HashSet for S
        Tester.buildRunWatch.reset().start();
        HashSet sSet = new HashSet(S);
        //enumerate subsets for r
        Tester.runWatch.reset().start();
        long t = r;
        long count = 0;
        while(t != 0) {
            if(sSet.contains(t)) {
                count ++;
            }
            t = (t-1)&r;
        }
        if(sSet.contains(t)) {
            count ++;//consider 0
        }
        return count;
    }
}
