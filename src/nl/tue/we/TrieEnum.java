package nl.tue.we;

import java.util.ArrayList;

/**
 * Created by yluo on 9/22/2014.
 */
public class TrieEnum implements RestrictedEnum {
    @Override
    public long ToEnum(long r, ArrayList<Long> S) {
        Tester.buildRunWatch.reset().start();
        //System.out.println(S.size());
        SimpleTrie st = new SimpleTrie();
        //build the trie
        for(long item:S) {
            st.put(item);
        }
        Tester.runWatch.reset().start();
        //st.printMe();
        //query the trie
        //return 0;
        return st.getSubsets(r);
    }
}
