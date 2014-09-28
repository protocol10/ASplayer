package com.akshay.protocol10.asplayer.service;

/**
 * @author akshay
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.MainActivity;
import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.database.MediaManager;
import com.akshay.protocol10.asplayer.utils.ASUtils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

public class MediaServiceContoller extends Service implements
		OnCompletionListener, OnAudioFocusChangeListener {

	public static final String TITLE_KEY = "title";
	private final String PATH_KEY = "src";
	public static final String ARTIST_KEY = "artist";
	public static final String ALBUM_KEY = "album";
	public static final String ALBUM_ID = "album_id";

	private String title_text, artist_text, album_text;
	private long album_id;

	public static final String BROADCAST_ACTION = "com.akshay.protocol10.asplayer.UPDATE_TEXT";
	public static final String SEEKBAR_ACTION = "com.akshay.protocol10.asplayer.UPDATE_SEEKBAR";
	public static final String SEEKTO_ACTION = "com.akshay.protocol10.asplayer.SEEK_TO";
	private static final String TAG = "MEDIASERVICE CONTROLLER";

	private static int playBackIndex = 0;

	List<HashMap<String, Object>> media_list;

	int result;
	static MediaPlayer mediaplayer;
	MediaManager manager;
	AudioManager audioManager;

	Handler handler;
	Intent intent;
	IntentFilter filter;
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
		media_list = new ArrayList<HashMap<String, Object>>();
		manager = new MediaManager();
		handler = new Handler();
		filter = new IntentFilter();
		filter.addAction(SEEKBAR_ACTION);
		filter.addAction(SEEKTO_ACTION);
		registerReceiver(receiver, filter);

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
			audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			result = audioManager.requestAudioFocus(this,
					AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
			mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaplayer.setDataSource(media_list.get(playBackIndex)
					.get(PATH_KEY).toString());
			mediaplayer.prepare();
			mediaplayer.setOnCompletionListener(this);
			mediaplayer.start();
			setUpHandlers();
			updateView();

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
			if (mediaplayer.isPlaying()) {
				mediaplayer.pause();
			} else {
				mediaplayer.start();
			}
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
		// updateView();
	}

	public void previousSong() {
		if (playBackIndex > 0) {
			playBackIndex -= 1;
			play(playBackIndex);
		} else {
			playBackIndex = 0;
			play(playBackIndex);
		}
		// updateView();
	}

	public void setSongs(List<HashMap<String, Object>> list) {

		media_list.clear();
		media_list.addAll(list);
	}

	private void updateView() {
		title_text = media_list.get(playBackIndex).get(TITLE_KEY).toString();
		artist_text = media_list.get(playBackIndex).get(ARTIST_KEY).toString();
		Log.i(ARTIST_KEY, artist_text);
		album_text = media_list.get(playBackIndex).get(ALBUM_KEY).toString();
		album_id = (Long) media_list.get(playBackIndex).get(ALBUM_ID);
		sendBroadCastToView(title_text, album_text, artist_text, album_id);
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent.getAction().equals(SEEKTO_ACTION)) {
				int seekTo = intent.getIntExtra("seekpos", 0);
				if (mediaplayer.isPlaying())
					mediaplayer.seekTo(seekTo);
			}
		}
	};

	private void setUpHandlers() {
		// TODO Auto-generated method stub
		handler.removeCallbacks(sendUpdatesToUI);
		handler.postDelayed(sendUpdatesToUI, 1000);

	}

	private Runnable sendUpdatesToUI = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			logMediaPosition();
			handler.postDelayed(this, 1000);
		}
	};

	private void logMediaPosition() {
		if (mediaplayer.isPlaying()) {
			int currentPosition = mediaplayer.getCurrentPosition();
			int totalduration = mediaplayer.getDuration();
			intent = new Intent(SEEKBAR_ACTION);
			intent.putExtra(ASUtils.CURRENT_POSITION, currentPosition);
			intent.putExtra(ASUtils.MAX_DURATION, totalduration);
			sendBroadcast(intent);
		} else if (!mediaplayer.isPlaying())
			handler.removeCallbacks(sendUpdatesToUI);
	}

	/**
	 *
	 * @param title
	 *            Title of the MediaTrack
	 * @param album
	 *            Album Name of MediaTrack
	 * @param artist
	 *            Artist Name of MediaTrack
	 * @param album_id
	 *            ID for retriving ArtWork
	 */

	private void sendBroadCastToView(String title, String album, String artist,
			long album_id) {
		intent = new Intent(BROADCAST_ACTION);
		intent.putExtra(TITLE_KEY, title);
		intent.putExtra(ALBUM_KEY, album);
		intent.putExtra(ARTIST_KEY, artist);
		intent.putExtra(ALBUM_ID, album_id);
		sendBroadcast(intent);
		showNotification(title, album, artist, album_id);
	}

	private void showNotification(String title, String album, String artist,
			long id) {

		Bitmap bitmap = getCover(id);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher).setLargeIcon(bitmap)
				.setContentTitle(title).setContentText(album);

		Intent intent = new Intent(this, MainActivity.class);
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
		stackBuilder.addParentStack(MainActivity.class);
		stackBuilder.addNextIntent(intent);
		PendingIntent pendingIntent = stackBuilder.getPendingIntent(0,
				PendingIntent.FLAG_UPDATE_CURRENT);
		builder.setContentIntent(pendingIntent);
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(ASUtils.NOTIFICATION_ID, builder.build());
		startService(new Intent(this, MediaServiceContoller.class));
		startForeground(ASUtils.NOTIFICATION_ID, builder.build());
	}

	private Bitmap getCover(long id) {
		// TODO Auto-generated method stub
		Bitmap bitmap = MediaManager.getAlbumArt(id, getApplicationContext());

		if (bitmap == null)
			bitmap = BitmapFactory.decodeResource(this.getResources(),
					R.drawable.ic_launcher);

		return bitmap;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		handler.removeCallbacks(sendUpdatesToUI);
		unregisterReceiver(receiver);
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

	@Override
	public void onCompletion(MediaPlayer mp) {
		// TODO Auto-generated method stub
		nextSong();
	}

	@Override
	public void onAudioFocusChange(int focusChange) {
		// TODO Auto-generated method stub
		switch (focusChange) {
		case AudioManager.AUDIOFOCUS_GAIN:
			if (mediaplayer != null) {
				mediaplayer.setVolume(1.0f, 1.0f);
				mediaplayer.start();
			} else
				mediaplayer = new MediaPlayer();
			break;
		case AudioManager.AUDIOFOCUS_LOSS:
			if (mediaplayer != null)
				mediaplayer.stop();
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
			if (mediaplayer != null)
				mediaplayer.pause();
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
			if (mediaplayer.isPlaying())
				mediaplayer.setVolume(0.1f, 0.1f);
			break;
		default:
			break;
		}
	}
}
