package nl.tue.we;

import com.google.common.primitives.Longs;
import com.google.common.primitives.UnsignedLongs;

import java.util.*;

/**
 * Check and jump
 * Created by yluo on 9/22/2014.
 */
class JumpEnum implements RestrictedEnum {
    @Override
    public long ToEnum(long r, ArrayList<Long> S) {
        Tester.buildRunWatch.reset().start();
        long count = 0;
        Long[] sArray = S.toArray(new Long[S.size()]);

        Comparator<Long> lexi_comparator =
                new Comparator<Long>() {
                    @Override
                    public int compare(Long o1, Long o2) {
                        return UnsignedLongs.compare(o1,o2);
                    }
                };

        Arrays.sort(sArray, lexi_comparator);

        Tester.runWatch.reset().start();
        //enumerate subsets for r
        long t = r;
        int fromIdx = 0;
        int toIdx = sArray.length;
        while(t != 0) {
            int cursor = Arrays.binarySearch(sArray, fromIdx, toIdx, t, lexi_comparator);

            if(cursor >= 0) {
                count ++;
                t = (t-1)&r;
                toIdx = cursor;
            }else {
                //sArray[]
                int insertion_point = -cursor-1;
                if(insertion_point == 0) {
                    break;
                }
                t = subsetSmallerThan(r,sArray[insertion_point-1]);
            }
        }

        if(sArray[0] == 0) {
            count ++;
        }

        return count;
    }

    /**
     *
     * @param mask
     * @param value
     * @return subset of mask that is smaller than value
     */
    private static long subsetSmallerThan(long mask, long value) {
        long temp = (value^mask)&value;
        int leading = Long.numberOfLeadingZeros(temp);
        if(leading == 0) {
            return mask;
        }
        long prefix = (1L<<63)>>(leading-1);
        return (prefix & value) | ((~prefix)&mask);
    }

}
