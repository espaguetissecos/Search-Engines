package es.uam.eps.bmi.search.util;

import es.uam.eps.bmi.search.test.TestEngine;

/**
 *
 * @author pablo
 */
public class Timer {
    static long time;

    public static void reset() {
        time = System.currentTimeMillis();
    }
    
    public static void reset(String msg) {
        TestEngine.out.println(msg);
        reset();
    }
    
    public static void time() {
        long t = System.currentTimeMillis() - time;
        int min = (int) (t / 60000);
        int sec = (int) (t % 60000) / 1000;
        int msec = (int) (t % 1000);
        if (min > 0) TestEngine.out.print(min + "min ");
        if (sec > 0) TestEngine.out.print(sec + "s ");
        TestEngine.out.println(msec + "ms ");
        reset();
    }

    public static void time(String msg) {
        TestEngine.out.print(msg);
        time();
    }
}
