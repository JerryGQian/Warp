

public class Settings {
	public static class Handles {
		String prefix;
		
		float h1;
		float h2;
		
		float h1N;
		float h2N;
		
		public Handles(String pref) {
			this.prefix = pref;
		}
		
		public Handles(String pref, float h1, float h2) {
			Handles(pref, h1, h2, h1, h2);
		}
		
		public Handles(String pref, float h1, float h2, float h1N, float h2N) {
			this.h1 = h1;
			this.h1N = h1N;
			
			this.h2 = h2;
			this.h1N = h2N;
		}
		
		public void save(SharedPreferences.Editor editor) {
			editor.putString(prefix + "h1", h1);
			editor.putString(prefix + "h2", h2);
			editor.putString(prefix + "h1N", h1N);
			editor.putString(prefix + "h2N", h2N);
		}
		
		public void load(SharedPreferences settings) {
			this.h1  = settings.getString(prefix + "h1", 8.0f);
			this.h2  = settings.getString(prefix + "h2", 16.0f);
			this.h1N = settings.getString(prefix + "h1N", 8.0f);
			this.h2N = settings.getString(prefix + "h2N", 16.0f);
		}
	}
	
	String mode = "";
	Handles hExtend;
	Handles hStretch;
	Handles hSmooth;
	
	public static final String PREFS_NAME = "WarpPrefs";
	
	public Settings() {
		
	}
	
    public void onCreate(Bundle state){
       super.onCreate(state);
       // Restore preferences
       SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	   
	   hExtend = new Handles("Extend");
	   hStretch = new Handles("Stretch");
	   hSmooth = new Handles("Smooth");
	   
	   hExtend.load(settings);
	   hStretch.load(settings);
	   hSmooth.load(settings);
	   	   
	   mode = settings.getString("mode", false);
	   
    }
	
    public void onStop(){
       super.onStop();

       save();
    }
	
	public void save() {
		// We need an Editor object to make preference changes.
		// All objects are from android.context.Context
		SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("mode", mode);
		
		hExtend.save(editor);
		hStretch.save(editor);
		hSmooth.save(editor);
		
		
		// Commit the edits!
		editor.commit();
	}
}