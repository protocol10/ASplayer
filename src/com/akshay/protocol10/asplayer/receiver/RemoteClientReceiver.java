package com.akshay.protocol10.asplayer.receiver;

/**
 * @author akshay
 * Reference from Google Default music player and google samples for RandomMusicPlayer
 */
import com.akshay.protocol10.asplayer.service.MediaServiceContoller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

public class RemoteClientReceiver extends BroadcastReceiver {
	String CMD;

	@Override
	public void onReceive(Context context, Intent intent) {

		String intentAction = intent.getAction();

		if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) {
			KeyEvent event = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
			if (event == null)
				return;
			int keyCode = event.getKeyCode();
			int keyAction = event.getAction();
			long eventtime = event.getEventTime();
			switch (keyCode) {
			case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
				CMD = MediaServiceContoller.NOTIFY_PAUSE;
				break;
			case KeyEvent.KEYCODE_MEDIA_NEXT:
				CMD = MediaServiceContoller.NOTIFY_NEXT;
				break;
			case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
				CMD = MediaServiceContoller.NOTIFY_BACK;
				break;
			default:
				break;
			}
			if (CMD != null) {
				if (keyAction == KeyEvent.ACTION_DOWN) {
					context.sendBroadcast(new Intent(CMD));
				}
			}
		}
	}

}
