package nl.tue.we;

import java.util.ArrayList;

/**
 * Created by yluo on 9/23/2014.
 */
public class PTrieEnum implements RestrictedEnum{

    @Override
    public long ToEnum(long r, ArrayList<Long> S) {
        Tester.buildRunWatch.reset().start();
        PTrie pt = new PTrie();

        for(long item:S) {
            pt.put(item);
        }

        //pt.print();
        Tester.runWatch.reset().start();
        return pt.getSubsets(r);
    }
}
