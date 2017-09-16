

/*
    <application
        android:icon="@drawable/icon"
        android:label="@string/app_name">
        ...
        <!--
            Because android:exported is set to "false",
            the service is only available to this app.
        -->
        <service
            android:name=".RSSPullService"
            android:exported="false"/>
        ...
    <application/>

*/

import java.util.Calendar;

public class WarpService extends IntentService {
	public final class Constants {
		// Defines a custom Intent action
		public static final String BROADCAST_ACTION =
			"com.example.android.threadsample.BROADCAST";
		// Defines the key for the status "extra" in an Intent
		public static final String EXTENDED_DATA_STATUS =
			"com.example.android.threadsample.STATUS";
	}
	
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





//Driver for sending work
/*
 * Creates a new Intent to start the RSSPullService
 * IntentService. Passes a URI in the
 * Intent's "data" field.
 */
mServiceIntent = new Intent(getActivity(), RSSPullService.class);
mServiceIntent.setData(Uri.parse(dataUrl));

// Starts the IntentService
getActivity().startService(mServiceIntent);







// Broadcast receiver for receiving status updates from the IntentService
private class DownloadStateReceiver extends BroadcastReceiver
{
    // Prevents instantiation
    private DownloadStateReceiver() {
    }
    // Called when the BroadcastReceiver gets an Intent it's registered to receive
    @Override
    public void onReceive(Context context, Intent intent) {
        /*
         * Handle Intents here.
         */
    }
}


// Class that displays photos
public class DisplayActivity extends FragmentActivity {
    
	
    public void onCreate(Bundle stateBundle) {
        super.onCreate(stateBundle);
        // The filter's action is BROADCAST_ACTION
        IntentFilter statusIntentFilter = new IntentFilter(
                Constants.BROADCAST_ACTION);

        // Adds a data filter for the HTTP scheme
        statusIntentFilter.addDataScheme("http");
        
		
		 // Instantiates a new DownloadStateReceiver
        DownloadStateReceiver mDownloadStateReceiver =
                new DownloadStateReceiver();
        // Registers the DownloadStateReceiver and its intent filters
        LocalBroadcastManager.getInstance(this).registerReceiver(
                mDownloadStateReceiver,
                statusIntentFilter);
				
				
        /*
         * Instantiates a new action filter.
         * No data filter is needed.
         */
        statusIntentFilter = new IntentFilter(Constants.ACTION_ZOOM_IMAGE);
        ...
        // Registers the receiver with the new filter
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(
                mDownloadStateReceiver,
                statusIntentFilter);
				
	}
	
}