package com.akshay.protocol10.asplayer.service;

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
	// private final String TITLE_KEY = "title";
	private final String PATH_KEY = "src";
	// private final String ARTIST_KEY = "artist";
	// private final String ALBUM_KEY = "album";
	// private final String DURATION_KEY = "duration";

	MediaPlayer mediaplayer;
	MediaManager manager;
	List<HashMap<String, Object>> media_list;

	// mBinder object which is responsible for interacting with client.
	private final IBinder mbinder = new MediaBinder();

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
		mediaplayer = new MediaPlayer();
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
			mediaplayer.reset();
			mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaplayer.setDataSource(media_list.get(index).get(PATH_KEY)
					.toString());
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
