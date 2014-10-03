package com.akshay.protocol10.asplayer.receiver;

/**
 * @author akshay 
 * BroadCastReceiver for Telephone State to manage the PlayBack of Media
 */
import com.akshay.protocol10.asplayer.ASPlayer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class PhoneStateReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

		if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)
				|| state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
			Toast.makeText(ASPlayer.getAppContext(), "RINGING",
					Toast.LENGTH_SHORT).show();
			Intent newIntent = new Intent(ASPlayer.INCOMING_CALL_INTENT);
			ASPlayer.getAppContext().sendBroadcast(newIntent);
		}
		if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
			Log.i("STATE", "END");
			Toast.makeText(ASPlayer.getAppContext(), "ENDED",
					Toast.LENGTH_SHORT).show();
			Intent newIntent = new Intent(ASPlayer.CALL_ENDED_INTENT);
			ASPlayer.getAppContext().sendBroadcast(newIntent);
		}
	}
}
