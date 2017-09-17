package ninja.qian.warp;


import java.lang.Math;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Created by gary on 9/17/2017.
 */

public class Smooth {

    public static class Interval {
        public float begin;
        public float end;

        public Interval(float bg, float en) {
            this.begin = bg;
            this.end = en;
        }

        public boolean within(float t) {
            if (t <= end && t >= begin) {
                return true;
            }
            return false;
        }

        public float length() {
            return Math.max(end, begin) - Math.min(end, begin);
        }

        public float mean() {
            return (begin + end) / 2.0f;
        }

        public int compareTo(Interval o) {
            if (o.length() == length()) return 0;
            return o.length() > length() ? -1 : 1;
        }
    }

    public static List<Interval> longestFreeIntervals(List<Interval> events, int n) {
        PriorityQueue<Interval> pq;
        return null;

    }



}
