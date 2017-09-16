package backend;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Date;
import java.lang.Math;

public class Util {
    public static class Daytime {
        int hour;
        int min;
        int sec;
        int mil;

        public Daytime() {
            Calendar calendar = new GregorianCalendar();
            hour = calendar.getTime().getHours();
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
            return hour + (min / 60) + (sec / 60 / 60);
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
        Daytime t;
        Boolean sync;
        Daytime tot = null;
        Daytime elapsed = null;
    }


    Settings settings;
    Calendar calendar = new GregorianCalendar();

    public Util(Settings set) {
        settings = set;
    }

    public Date getTime() {
        //get actual time from android
        return calendar.getTime();
        //return Calendar.getInstance().getTime();
    }

    public PrintData convert() {
        switch (settings.mode) {
            case "Extend": {
                return convertExtend();
            }
        }
        return new PrintData();
    }

    public PrintData convertExtend() {
        Daytime curr = new Daytime();
        PrintData pd = new PrintData();
        //in shrunk part?
        if (curr.toFloat() < settings.hExtend.h2N || curr.toFloat() > settings.hExtend.h1N) {
            float origDur = timeBetween(settings.hExtend.h1, settings.hExtend.h2);
            float newDur = timeBetween(settings.hExtend.h1N, settings.hExtend.h2N);
            float elapsed = timeBetween(settings.hExtend.h1N, curr.toFloat());
            float elapsedRatio = elapsed / newDur;
            float ratio = origDur / newDur;
            pd.t = new Daytime(elapsedRatio * ratio + settings.hExtend.h1N);
            return pd;
        }
        //In normal time
        else if (curr.toFloat() > settings.hExtend.h2 || curr.toFloat() > settings.hExtend.h1) {
            pd.t = curr;
            return pd;
        }
        else {
            //In h1 gap
            int gapSign = timeBetween(settings.hExtend.h1, settings.hExtend.h1N) < timeBetween(settings.hExtend.h1N, settings.hExtend.h1) ? 1 : -1;
            float gapSize = Math.min(timeBetween(settings.hExtend.h1, settings.hExtend.h1N), timeBetween(settings.hExtend.h1N, settings.hExtend.h1));
            if ((gapSign > 0 && curr.toFloat() < settings.hExtend.h1 + gapSize) || (gapSign < 0 && curr.toFloat() < settings.hExtend.h1 - gapSize)) {
                pd.tot = new Daytime(gapSign);
                if (gapSign < 0) {
                    pd.t = new Daytime(settings.hExtend.h1N);
                    pd.elapsed = new Daytime(timeBetween(settings.hExtend.h1N, curr.toFloat()));
                }
                else {
                    pd.t = new Daytime(settings.hExtend.h1);
                    pd.elapsed = new Daytime(timeBetween(settings.hExtend.h1, curr.toFloat()));
                }
                return pd;
            }

            //In h2 gap
            gapSign = timeBetween(settings.hExtend.h2, settings.hExtend.h2N) < timeBetween(settings.hExtend.h2N, settings.hExtend.h2) ? 1 : -1;
            gapSize = Math.min(timeBetween(settings.hExtend.h2, settings.hExtend.h2N), timeBetween(settings.hExtend.h2N, settings.hExtend.h2));
            if ((gapSign > 0 && curr.toFloat() < settings.hExtend.h2 + gapSize) || (gapSign < 0 && curr.toFloat() < settings.hExtend.h2 - gapSize)) {
                pd.tot = new Daytime(gapSign);
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
        }
        return pd;
    }

    private float timeBetween(float min, float max) {
        if (min > max) {
            max += 24.0f;
        }
        return max - min;
    }
}
