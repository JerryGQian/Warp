

import java.util.Calendar;

public class Util {
	
	Settings
	
    @Override
    protected void onHandleIntent(Intent workIntent) {
        // Gets data from the incoming Intent
        String dataString = workIntent.getDataString();
        // Do work here, based on the contents of dataString
        
		
		//Broadcast the calculated times out of the system
		/*
		 * Creates a new Intent containing a Uri object
		 * BROADCAST_ACTION is a custom Intent action
		 */
		Intent localIntent =
				new Intent(Constants.BROADCAST_ACTION)
				// Puts the status into the Intent
				.putExtra(Constants.EXTENDED_DATA_STATUS, status);
		// Broadcasts the Intent to receivers in this app.
		LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }
	
	private Date getTime() {
		//get actual time from android
		return Calendar.getInstance().getTime();
	}
	
	private String convertStretch() {
		
	}
	
	/*
     * Creates a new Intent containing a Uri object
     * BROADCAST_ACTION is a custom Intent action
     */
    Intent localIntent =
            new Intent(Constants.BROADCAST_ACTION)
            // Puts the status into the Intent
            .putExtra(Constants.EXTENDED_DATA_STATUS, status);
    // Broadcasts the Intent to receivers in this app.
    LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);

}