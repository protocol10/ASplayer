package com.akshay.protocol10.asplayer.service;

/**
 * @author akshay
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.database.MediaManager;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class MediaServiceContoller extends Service {

	// private final String ID_KEY = "id";
	public static final String TITLE_KEY = "title";
	private final String PATH_KEY = "src";
	public static final String ARTIST_KEY = "artist";
	public static final String ALBUM_KEY = "album";
	// private final String DURATION_KEY = "duration";

	private String title_text, artist_text, album_text;

	public static final String BROADCAST_ACTION = "com.akshay.protocol10.asplayer.UPDATE_TEXT";
	private static final String TAG = "MEDIASERVICE CONTROLLER";

	private static int playBackIndex = 0;

	static MediaPlayer mediaplayer;
	MediaManager manager;
	List<HashMap<String, Object>> media_list;

	// mBinder object which is responsible for interacting with client.
	private final IBinder mbinder = new MediaBinder();
	Intent intent;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stubLO
		return mbinder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Log.i("SERVICE ", "Service Started");
		// mediaplayer = new MediaPlayer();
		media_list = new ArrayList<HashMap<String, Object>>();
		manager = new MediaManager();
		media_list = manager.retriveContent(getApplicationContext());
		Log.i("size", " " + media_list.size());

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		/**
		 * We want this service to continue until it is explicitly stopped.
		 */
		return START_STICKY;
	}

	public void play(int index) {

		try {
			playBackIndex = index;
			if (mediaplayer == null) {
				mediaplayer = new MediaPlayer();
			} else {
				mediaplayer.reset();
			}

			mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaplayer.setDataSource(media_list.get(playBackIndex)
					.get(PATH_KEY).toString());
			mediaplayer.prepare();
			mediaplayer.start();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void pauseSong() {

		if (mediaplayer != null) {
			Log.i("mediaplayer", "Not Null");
			if (mediaplayer.isPlaying()) {
				mediaplayer.pause();
			} else {
				mediaplayer.start();
			}
		} else {
			Log.i("MEDIAPLAYER", "NULL");
		}
	}

	public void nextSong() {
		if (playBackIndex < (media_list.size() - 1)) {
			playBackIndex += 1;
			play(playBackIndex);
		} else {
			playBackIndex = 0;
			play(playBackIndex);
		}

		title_text = media_list.get(playBackIndex).get(TITLE_KEY).toString();
		artist_text = media_list.get(playBackIndex).get(ARTIST_KEY).toString();
		album_text = media_list.get(playBackIndex).get(ALBUM_KEY).toString();
		updateView(title_text, album_text, artist_text);
	}

	public void previousSong() {
		if (playBackIndex > 0) {
			playBackIndex -= 1;
			play(playBackIndex);
		} else {
			playBackIndex = 0;
			play(playBackIndex);
		}

		title_text = media_list.get(playBackIndex).get(TITLE_KEY).toString();
		artist_text = media_list.get(playBackIndex).get(ARTIST_KEY).toString();
		album_text = media_list.get(playBackIndex).get(ALBUM_KEY).toString();
		updateView(title_text, album_text, artist_text);
	}

	private void updateView(String title, String album, String artist) {
		intent = new Intent(BROADCAST_ACTION);
		// intent.setAction(BROADCAST_ACTION);
		intent.putExtra(TITLE_KEY, title);
		intent.putExtra(ALBUM_KEY, album);
		intent.putExtra(ARTIST_KEY, album);
		sendBroadcast(intent);
		Log.d(TAG, "BroadCast Sent");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("SERVICE", "DESTROY");

	}

	/**
	 * 
	 * @author akshay Class for the client to access. Because Service runs in
	 *         the same process
	 *
	 */

	public class MediaBinder extends Binder {
		public MediaServiceContoller getService() {
			return MediaServiceContoller.this;
		}
	}
}
