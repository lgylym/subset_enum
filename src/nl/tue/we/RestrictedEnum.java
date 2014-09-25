package nl.tue.we;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by yluo on 9/22/2014.
 */
public interface RestrictedEnum {

    /**
     *
     * @param r subsets of
     * @param S belong to
     * @return number of results
     */
    public long ToEnum(long r, ArrayList<Long> S);
}
