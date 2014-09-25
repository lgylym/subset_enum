package nl.tue.we;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

/**
 * Created by yluo on 9/23/2014.
 */
public class JustTest {
    public static void main(String[] args) {
        //System.out.println((1L>>0)&1);
        int pos = 63;
        System.out.println(Long.toBinaryString((Long.MIN_VALUE >>> pos)&0xfL));
    }
}
