

import java.util.Calendar;
import java.util.GregorianCalendar

public class Util {
	public static class Daytime {
		int hour;
		int min;
		int sec;
		int mil;
		
		public Daytime() {
			hour = Calendar.get(Calendar.HOUR_OF_DAY);
			min = Calendar.get(Calendar.MINUTE);
			sec = Calendar.get(Calendar.SECOND);
			mil = 0;
		}
		
		public Daytime(Date d) {
			hour = d.getHours();
			min = d.getMinutes();
			sec = d.getSeconds()
			mil = 0;
		}
		
		public Daytime(float t) {
			while (t > 24) t -= 24;
			hour = (int) t;
			min = (int) ((t - hour) * 60);
			sec = (t - hour) - (min / 60) * 60;
		}
		
		public float toFloat() {
			return hour + (min / 60) + (sec / 60 / 60);
		}
		
		public String ampm() {
			if (hour < 12) return "AM";
			return "PM";
		}
		
		@Override
		public toString() {
			return "" + (hour + 1 % 13) + ":" + min;
		}
		
		@Override
		public int hashCode() {
			return hour * 10000 + min * 100 + sec;
		}
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
	
	public Daytime convertExtend() {
		Daytime curr = new Daytime();
		//in shrunk part?
		if (curr.toFloat() < settings.hExtend.h2N || curr.toFloat() > settings.hExtend.h1N) {
			float origDur = timeBetween(settings.hExtend.h1, settings.hExtend.h2);
			float newDur = timeBetween(settings.hExtend.h1N, settings.hExtend.h2N);
			float elapsed = timeBetween(settings.hExtend.h1N, curr.toFloat());
			float elapsedRatio = elapsed / newDur;
			float ratio = origDur / newDur;
			return new Daytime(elapsedRatio * ratio + settings.hExtend.h1N);
		}
		//In normal time
		else if (curr.toFloat() > settings.hExtend.h2 || curr.toFloat() > settings.hExtend.h1) {
			return curr;
		}
		//In h1 gap
		else if () {
			
		}
		//In h2 gap
		else if () {
			
		}
	}
	
	private float timeBetween(float min, float max) {
		if (min > max) {
			max += 24.0f;
		}
		return max - min;
	}

}