package ninja.qian.warp;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;
import java.lang.Math;

public class Util {
    public static class Daytime {
        public int hour = 0;
        public int min = 0;
        public int sec = 0;
        public int mil = 0;

        public Daytime() {
            TimeZone est = TimeZone.getTimeZone("EST");
            Calendar calendar = Calendar.getInstance(est);
            System.out.println("" + Calendar.AM_PM + " " + Calendar.AM + " " + calendar.getTime().getHours());
            hour = calendar.getTime().getHours();// + (Calendar.AM_PM == Calendar.AM ? 0 : 12);
            min = calendar.getTime().getMinutes();
            sec = calendar.getTime().getSeconds();
            mil = 0;
        }

        public Daytime(Date d) {
            hour = d.getHours();
            min = d.getMinutes();
            sec = d.getSeconds();
            mil = 0;
        }

        public Daytime(float t) {
            while (t > 24) t -= 24;
            hour = (int) t;
            min = (int) ((t - hour) * 60);
            sec = (int)(t - hour) - (min / 60) * 60 * 60;
        }

        public float toFloat() {
            return (float) hour + (min / 60.0f) + (sec / 60.0f / 60.0f);
        }

        public String ampm() {
            if (hour < 12) return "AM";
            return "PM";
        }

        public int getModHour() {
            return hour % 12;
        }

        @Override
        public String toString() {
            return "" + (hour + 1 % 13) + ":" + min + ":";
        }

        @Override
        public int hashCode() {
            return hour * 10000 + min * 100 + sec;
        }
    }

    public static class PrintData {
        public Daytime t;
        public Boolean sync;
        public Daytime tot = null;
        public Daytime elapsed = null;
    }


    public Settings settings;
    public Calendar calendar = new GregorianCalendar();
    public long count = 0;

    public Util(Settings set) {
        settings = set;
    }

    public Date getTime() {
        //get actual time from android
        return calendar.getTime();
        //return Calendar.getInstance().getTime();
    }

    public PrintData convert() {
        count++;
        if (count % 4 == 0) settings.load();
        //FORCE EXTEND MODE
        //settings.mode = "Extend";
        switch (settings.mode) {
            case "Extend":
                return convertExtend();
            case "Stretch":
                return convertStretch();
            case "Smooth":
                return convertExtend();
        }
        return new PrintData();
    }

    public PrintData convertStretch() {
        Daytime curr = new Daytime();
        System.out.println("" + curr.toFloat() + " " + settings.hExtend.h1 + " " + settings.hExtend.h1N + " " + settings.hExtend.h2 + " " + settings.hExtend.h2N);
        PrintData pd = new PrintData();
        pd.sync = false;
        pd.t = curr;
        if (settings.hExtend.h2N == settings.hExtend.h2 && settings.hExtend.h1N == settings.hExtend.h1) {
            pd.sync = true;
        }
        if ((curr.toFloat() < settings.hExtend.h2N && curr.toFloat() < settings.hExtend.h1N) || (curr.toFloat() > settings.hExtend.h1N && curr.toFloat() > settings.hExtend.h2N)) {
            float origDur = timeBetween(settings.hExtend.h1, settings.hExtend.h2);
            float newDur = timeBetween(settings.hExtend.h1N, settings.hExtend.h2N);
            float elapsed = timeBetween(settings.hExtend.h1N, curr.toFloat());
            float elapsedRatio = elapsed / newDur;
            float ratio = origDur / newDur;
            pd.t = new Daytime(elapsedRatio * origDur + settings.hExtend.h1);
            System.out.println("Squeeze" + elapsed + " " + curr.toFloat() + " " + elapsedRatio + " " + ratio);
            return pd;
        }
        else {
            float origDur = timeBetween(settings.hExtend.h2, settings.hExtend.h1);
            float newDur = timeBetween(settings.hExtend.h2N, settings.hExtend.h1N);
            float elapsed = timeBetween(curr.toFloat(), settings.hExtend.h2N);
            float elapsedRatio = elapsed / newDur;
            float ratio = origDur / newDur;
            pd.t = new Daytime(elapsedRatio * origDur + settings.hExtend.h2N);
            System.out.println("Squeeze" + elapsed + " " + curr.toFloat() + " " + elapsedRatio + " " + ratio);
            return pd;
        }
    }

    public PrintData convertExtend() {
        Daytime curr = new Daytime();
        System.out.println("" + curr.toFloat() + " " + settings.hExtend.h1 + " " + settings.hExtend.h1N + " "  + settings.hExtend.h2 + " " + settings.hExtend.h2N);
        PrintData pd = new PrintData();
        pd.sync = false;
        pd.t = curr;
        if (settings.hExtend.h2N == settings.hExtend.h2 && settings.hExtend.h1N == settings.hExtend.h1) {
            pd.sync = true;
        }

        //int result = getPartition(settings.hExtend, curr.toFloat());
        //In normal time
        if ((curr.toFloat() > settings.hExtend.h2 && curr.toFloat() < settings.hExtend.h1)) {
            pd.t = curr;
            System.out.println("Normal");
            pd.sync = true;
            return pd;
        }
        //in shrunk part?
        else if ((curr.toFloat() < settings.hExtend.h2N && curr.toFloat() < settings.hExtend.h1N) || (curr.toFloat() > settings.hExtend.h1N && curr.toFloat() > settings.hExtend.h2N)) {
            float origDur = timeBetween(settings.hExtend.h1, settings.hExtend.h2);
            float newDur = timeBetween(settings.hExtend.h1N, settings.hExtend.h2N);
            float elapsed = timeBetween(settings.hExtend.h1N, curr.toFloat());
            float elapsedRatio = elapsed / newDur;
            float ratio = origDur / newDur;
            pd.t = new Daytime(elapsedRatio * origDur + settings.hExtend.h1);
            System.out.println("Squeeze" + elapsed + " " + curr.toFloat() + " " + elapsedRatio + " " + ratio);
            return pd;
        }
        else if (!pd.sync) {
            //In h1 gap
            int gapSign = timeBetween(settings.hExtend.h1, settings.hExtend.h1N) < timeBetween(settings.hExtend.h1N, settings.hExtend.h1) ? 1 : -1;
            float gapSize = Math.min(timeBetween(settings.hExtend.h1, settings.hExtend.h1N), timeBetween(settings.hExtend.h1N, settings.hExtend.h1));
            if ((gapSign > 0 && curr.toFloat() < settings.hExtend.h1 + gapSize && curr.toFloat() > settings.hExtend.h1) || (gapSign < 0 && curr.toFloat() > settings.hExtend.h1 - gapSize && curr.toFloat() < settings.hExtend.h1)) {
                pd.tot = new Daytime(gapSize);
                if (gapSign < 0) {
                    pd.t = new Daytime(settings.hExtend.h1N);
                    pd.elapsed = new Daytime(timeBetween(settings.hExtend.h1N, curr.toFloat()));
                }
                else {
                    pd.t = new Daytime(settings.hExtend.h1);
                    pd.elapsed = new Daytime(timeBetween(settings.hExtend.h1, curr.toFloat()));
                }
                System.out.println("Gap");
                return pd;
            }

            //In h2 gap
            gapSign = timeBetween(settings.hExtend.h2, settings.hExtend.h2N) < timeBetween(settings.hExtend.h2N, settings.hExtend.h2) ? 1 : -1;
            gapSize = Math.min(timeBetween(settings.hExtend.h2, settings.hExtend.h2N), timeBetween(settings.hExtend.h2N, settings.hExtend.h2));
            if ((gapSign > 0 && curr.toFloat() < settings.hExtend.h2 + gapSize && curr.toFloat() > settings.hExtend.h2) || (gapSign < 0 && curr.toFloat() > settings.hExtend.h2 - gapSize && curr.toFloat() < settings.hExtend.h2)) {
                pd.tot = new Daytime(gapSize);
                if (gapSign < 0) {
                    pd.t = new Daytime(settings.hExtend.h2N);
                    pd.elapsed = new Daytime(timeBetween(settings.hExtend.h2N, curr.toFloat()));
                }
                else {
                    pd.t = new Daytime(settings.hExtend.h2);
                    pd.elapsed = new Daytime(timeBetween(settings.hExtend.h2, curr.toFloat()));
                }
                return pd;
            }
        } else {
            pd.t = curr;
        }
        return pd;
    }

    public PrintData convertSmooth() {

        return null;
    }

    private float timeBetween(float min, float max) {
        if (min > max) {
            max += 24.0f;
        }
        return max - min;
    }

    private int getPartition(Settings.Handles h, float n) {
        float minL = 999;
        float minR = 999;
        int L = 0;
        int R = 0;


        float targ = h.h1;
        //leftside
        if (targ < n && n - targ < 12 || targ > n && targ - n > 12) {
            if (targ > n) {
                n += 24;
            }
            float dist = n - targ;
            if (dist < minL) {
                minL = dist;
                L = 1;
            }
            if (24 - dist < minR) {
                minR = dist;
                R = 1;
            }
        }
        //rightside
        else {
            if (targ < n) {
                targ += 24;
            }
            float dist = targ - n;
            if (24 - dist < minL) {
                minL = dist;
                L = 1;
            }
            if (dist < minR) {
                minR = dist;
                R = 1;
            }
        }

        targ = h.h2;
        //leftside
        if (targ < n && n - targ < 12 || targ > n && targ - n > 12) {
            if (targ > n) {
                n += 24;
            }
            float dist = n - targ;
            if (dist < minL) {
                minL = dist;
                L = 2;
            }
            if (24 - dist < minR) {
                minR = dist;
                R = 2;
            }
        }
        //rightside
        else {
            if (targ < n) {
                targ += 24;
            }
            float dist = targ - n;
            if (24 - dist < minL) {
                minL = dist;
                L = 2;
            }
            if (dist < minR) {
                minR = dist;
                R = 2;
            }
        }

        targ = h.h1N;
        //leftside
        if (targ < n && n - targ < 12 || targ > n && targ - n > 12) {
            if (targ > n) {
                n += 24;
            }
            float dist = n - targ;
            if (dist < minL) {
                minL = dist;
                L = 3;
            }
            if (24 - dist < minR) {
                minR = dist;
                R = 3;
            }
        }
        //rightside
        else {
            if (targ < n) {
                targ += 24;
            }
            float dist = targ - n;
            if (24 - dist < minL) {
                minL = dist;
                L = 3;
            }
            if (dist < minR) {
                minR = dist;
                R = 3;
            }
        }

        targ = h.h2N;
        //leftside
        if (targ < n && n - targ < 12 || targ > n && targ - n > 12) {
            if (targ > n) {
                n += 24;
            }
            float dist = n - targ;
            if (dist < minL) {
                minL = dist;
                L = 4;
            }
            if (24 - dist < minR) {
                minR = dist;
                R = 4;
            }
        }
        //rightside
        else {
            if (targ < n) {
                targ += 24;
            }
            float dist = targ - n;
            if (24 - dist < minL) {
                minL = dist;
                L = 4;
            }
            if (dist < minR) {
                minR = dist;
                R = 4;
            }
        }

        if (L == 1 || R == 1 && L == 3 || R == 3) {
            return 1;
        }
        else if (L == 3 || L == 1 && R == 2 || R == 4) {
            return 2;
        }
        else if (L == 2 || R == 2 && L == 4 || R == 4) {
            return 3;
        }
        else {
            return 4;
        }

    }

}
