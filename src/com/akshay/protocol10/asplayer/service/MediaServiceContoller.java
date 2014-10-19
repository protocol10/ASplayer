package com.akshay.protocol10.asplayer.service;

/**
 * @author akshay
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akshay.protocol10.asplayer.ASPlayer;
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
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.audiofx.Equalizer;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

public class MediaServiceContoller extends Service implements
		OnCompletionListener, OnAudioFocusChangeListener {

	/* HASHMAP KEYS */
	public static final String TITLE_KEY = "title";
	private final String PATH_KEY = "src";
	public static final String ARTIST_KEY = "artist";
	public static final String ALBUM_KEY = "album";
	public static final String ALBUM_ID = "album_id";

	private String title_text, artist_text, album_text;
	private long album_id;
	boolean wasPlaying = false;
	boolean isPLaying = false;
	boolean defaultLoaded = false;

	/* INTENTFILTER ACTIONS FOR BROADCAST RECEIVER */
	public static final String BROADCAST_ACTION = "com.akshay.protocol10.asplayer.UPDATE_TEXT";
	public static final String SEEKBAR_ACTION = "com.akshay.protocol10.asplayer.UPDATE_SEEKBAR";
	public static final String SEEKTO_ACTION = "com.akshay.protocol10.asplayer.SEEK_TO";

	public final static String APPWIDGET_INIT = "com.akshay.protocol10.widget.INIT";
	public final static String APPWIDGET_UPDATE_TEXT = "com.akshay.protocol10.widget.UPDATETXT";
	public final static String APPWIDGET_UPDATE_COVER = "com.akshay.protocol10.widget.UPDATECOVER";
	public final static String APPWIDGET_PLAY = "com.akshay.protocol10.widget.PLAY";
	public final static String APPWIDGET_BACK = "com.akshay.protocol10.widget.PREVIOUS";
	public final static String APPWIDGET_NEXT = "com.akshay.protocol10.widget.NEXT";

	public final static String CONTROL_PLAY = "com.akshay.protocol10.control.PLAY";
	public final static String NOWPLAYING_PLAY = "com.akshay.protocol10.nowplaying.PLAY";
	public final static String NOTIFY_PAUSE = "com.akshay.protocol10.notify.PLAYPAUSE";
	public final static String NOTIFY_NEXT = "com.akshay.protocol10.notify.NEXT";
	public final static String NOTIFY_CLOSE = "com.akshay.protocol10.notify.CLOSE";
	public final static String NOTIFY_BACK = "com.akshay.protocol10.notify.BACK";

	private final static String NOTIFICATION_ACTION = "com.akshay.protocol10.NOTIFICATION";

	/* USED FOR APPWIDGET INTENTS */
	private final String PACKAGE_NAME = "com.akshay.protocol10.asplayer";
	private final String CLASS_NAME = "com.akshay.protocol10.asplayer.ASPlayerWidget";

	public final static String PLAY_PATH = "com.akshay.protocol10.PLAYPATH";
	private static int playBackIndex = 0;
	public final static String PATH = "path";

	List<HashMap<String, Object>> media_list;

	int result;
	static MediaPlayer mediaplayer;
	MediaManager manager;
	AudioManager audioManager;
	Equalizer equalizer;

	Handler handler;
	Intent intent;
	IntentFilter filter;
	int db_condition;
	SharedPreferences preferences;
	Editor editor;
	String tag;
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
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		manager = new MediaManager();
		media_list = manager.retriveContent(this);
		handler = new Handler();

		// register intents for BroadCast Receivers
		filter = new IntentFilter();
		filter.addAction(SEEKBAR_ACTION);
		filter.addAction(SEEKTO_ACTION);
		filter.addAction(ASPlayer.INCOMING_CALL_INTENT);
		filter.addAction(ASPlayer.CALL_ENDED_INTENT);
		filter.addAction(APPWIDGET_INIT);
		filter.addAction(APPWIDGET_UPDATE_TEXT);
		filter.addAction(APPWIDGET_UPDATE_COVER);
		filter.addAction(APPWIDGET_BACK);
		filter.addAction(APPWIDGET_PLAY);
		filter.addAction(APPWIDGET_NEXT);
		filter.addAction(Intent.ACTION_HEADSET_PLUG);
		filter.addAction(CONTROL_PLAY);
		filter.addAction(NOWPLAYING_PLAY);
		filter.addAction(NOTIFY_CLOSE);
		filter.addAction(NOTIFY_PAUSE);
		filter.addAction(NOTIFY_NEXT);
		filter.addAction(NOTIFY_BACK);
		registerReceiver(receiver, filter);

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		/**
		 * We want this service to continue until it is explicitly stopped.
		 */
		if (intent != null) {
			String action = intent.getAction();
			if (action != null && action.equals(PLAY_PATH)) {

				String path = intent.getStringExtra(PATH);
				playFromPath(path);

			}
		}
		return START_STICKY;
	}

	public void play(int index) {

		checkForDefault();
		try {
			playBackIndex = index;
			if (mediaplayer == null) {
				mediaplayer = new MediaPlayer();
			} else {
				mediaplayer.reset();
			}

			// obtain the AudioFocus for our app
			audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
			result = audioManager.requestAudioFocus(this,
					AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
			equalizer = new Equalizer(1, mediaplayer.getAudioSessionId());
			Short band = equalizer.getNumberOfBands();
			Short m = equalizer.getNumberOfPresets();
			String[] music = new String[m];
			for (int i = 0; i < music.length; i++) {
				music[i] = equalizer.getPresetName((short) i);

			}
			equalizer.setEnabled(true);

			mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaplayer.setDataSource(media_list.get(playBackIndex)
					.get(PATH_KEY).toString());
			mediaplayer.prepare();
			mediaplayer.setOnCompletionListener(this);
			mediaplayer.start();
			updatewidget(this);
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

	private void checkForDefault() {
		// TODO Auto-generated method stub
		if (getTag() != null && getTag().equals(ASUtils.TRACKS_TAGS)
				&& !defaultLoaded) {
			media_list.clear();
			retriveContent();
			defaultLoaded = true;
		} else {
			defaultLoaded = false;
		}
	}

	private void retriveContent() {
		// TODO Auto-generated method stub
		media_list = manager.retriveContent(getApplicationContext());
	}

	public void pauseSong() {

		if (mediaplayer != null) {
			if (mediaplayer.isPlaying()) {
				mediaplayer.pause();
				wasPlaying = true;
				isPLaying = false;
			} else {
				mediaplayer.start();
				isPLaying = true;
			}
			Intent i = new Intent(CONTROL_PLAY);
			i.putExtra(ASUtils.IS_PLAYING, isPLaying);
			sendBroadcast(i);
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
	}

	public void previousSong() {
		if (playBackIndex > 0) {
			playBackIndex -= 1;
			play(playBackIndex);
		} else {
			playBackIndex = 0;
			play(playBackIndex);
		}
	}

	public void setSongs(List<HashMap<String, Object>> list) {

		media_list.clear();
		media_list.addAll(list);
	}

	private void updateView() {
		title_text = media_list.get(playBackIndex).get(TITLE_KEY).toString();
		artist_text = media_list.get(playBackIndex).get(ARTIST_KEY).toString();
		album_text = media_list.get(playBackIndex).get(ALBUM_KEY).toString();
		album_id = (Long) media_list.get(playBackIndex).get(ASUtils.ALBUM_ART);
		sendBroadCastToView(title_text, album_text, artist_text, album_id);
	}

	BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			/* BroadCastReciever Action SEEKBAR */
			if (action.equals(SEEKTO_ACTION)) {
				int seekTo = intent.getIntExtra("seekpos", 0);
				if (mediaplayer.isPlaying())
					mediaplayer.seekTo(seekTo);
			}
			/* BroadCastReciever Action for INCOMING CALL */
			if (action.equals(ASPlayer.INCOMING_CALL_INTENT)) {
				if (mediaplayer.isPlaying())
					mediaplayer.pause();
			}
			/* BroadCastReciever Action for TELEPHONE STATE */
			if (action.equals(ASPlayer.CALL_ENDED_INTENT) && wasPlaying) {
				mediaplayer.start();
			}
			if (action.equals(APPWIDGET_INIT)) {
				updatewidget(context);
			}

			if (action.equals(APPWIDGET_PLAY) || action.equals(NOTIFY_PAUSE)) {
				if (mediaplayer.isPlaying())
					mediaplayer.pause();
				else if (!mediaplayer.isPlaying()) {
					mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaplayer.start();
				}
			}
			if (action.equals(Intent.ACTION_HEADSET_PLUG)) {

				int state = intent.getIntExtra("state", 0);

				if (mediaplayer != null && mediaplayer.isPlaying()
						&& wasPlaying && state != 0) {
					mediaplayer.start();
				} else if (state == 0) {
					if (mediaplayer != null && mediaplayer.isPlaying())
						mediaplayer.pause();
				}
			}
			if (action.equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)) {
				if (mediaplayer != null && mediaplayer.isPlaying()
						&& wasPlaying) {
					mediaplayer.pause();
				}
			}
			if (action.equals(APPWIDGET_NEXT) || action.equals(NOTIFY_NEXT)) {
				nextSong();
			}
			if (action.equals(APPWIDGET_BACK) || action.equals(NOTIFY_BACK)) {
				previousSong();
			}
			if (action.equals(NOTIFY_CLOSE)) {
				stopForeground(true);
				pauseSong();
			}
		}
	};

	private void setUpHandlers() {
		// TODO Auto-generated method stub
		handler.removeCallbacks(sendUpdatesToUI);
		handler.postDelayed(sendUpdatesToUI, 1000);

	}

	protected void updatewidget(Context context) {
		// TODO Auto-generated method stub

		updateWidgetText(context);
		updateWidgetCover(context);
	}

	private void updateWidgetCover(Context context) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.setClassName(PACKAGE_NAME, CLASS_NAME);
		intent.setAction(APPWIDGET_UPDATE_COVER);
		if (wasPlaying) {
			intent.putExtra(ALBUM_ID, album_id());
		}
		sendBroadcast(intent);
	}

	private void updateWidgetText(Context context) {
		// TODO Auto-generated method stub

		Intent intent = new Intent();
		intent.setClassName(PACKAGE_NAME, CLASS_NAME);
		intent.setAction(APPWIDGET_UPDATE_TEXT);
		if (wasPlaying) {
			intent.putExtra(TITLE_KEY, getTitle());
			intent.putExtra(ASUtils.IS_PLAYING, mediaplayer.isPlaying());
		} else {
			intent.putExtra(TITLE_KEY, context.getString(R.string.title));
		}

		sendBroadcast(intent);
	}

	private long album_id() {
		return (Long) media_list.get(playBackIndex).get(ASUtils.ALBUM_ART);
	}

	private Runnable sendUpdatesToUI = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			logMediaPosition();
			handler.postDelayed(this, 1000);
		}
	};

	private String getTitle() {
		return media_list.get(playBackIndex).get(TITLE_KEY).toString();
	}

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

		RemoteViews remoteViews = new RemoteViews(getPackageName(),
				R.layout.as_widget_main);

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher).setContent(
				remoteViews);

		if (bitmap != null) {
			remoteViews.setImageViewBitmap(R.id.notify_icon, bitmap);
		} else {
			remoteViews.setImageViewResource(R.id.notify_icon,
					R.drawable.ic_launcher);
		}
		remoteViews.setTextViewText(R.id.notify_title, title.toString());
		/* Launch the App from Notification */
		Intent intent = new Intent(this, MainActivity.class);
		intent.setAction(NOTIFICATION_ACTION);

		/* Play/Pause a media through notification */
		Intent pauseIntent = new Intent(NOTIFY_PAUSE);
		PendingIntent pendingPauseIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, pauseIntent, 0);
		remoteViews.setImageViewResource(R.id.notify_pause, mediaplayer
				.isPlaying() ? R.drawable.ic_notifypause
				: R.drawable.ic_notifyplay);
		remoteViews.setOnClickPendingIntent(R.id.notify_pause,
				pendingPauseIntent);

		/* Play next media through Notification */
		Intent nextIntent = new Intent(NOTIFY_NEXT);
		PendingIntent pendingNextIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, nextIntent, 0);
		remoteViews
				.setOnClickPendingIntent(R.id.notify_next, pendingNextIntent);

		/* Close the media and stopForegroundService */
		Intent closeIntent = new Intent(NOTIFY_CLOSE);
		PendingIntent pendingCloseIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, closeIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.notify_exit,
				pendingCloseIntent);

		/* Play the previous Track */
		Intent backIntent = new Intent(NOTIFY_BACK);
		PendingIntent pendingBackIntent = PendingIntent.getBroadcast(
				getApplicationContext(), 0, backIntent, 0);
		remoteViews.setOnClickPendingIntent(R.id.notify_previous,
				pendingBackIntent);

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
		wasPlaying = false;
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

	/* getter setter methods */
	public void setTag(String tag) {
		this.tag = tag;
	}

	String getTag() {
		return tag;
	}

	public void playFromPath(String path) {

		try {
			for (Map<String, Object> map : media_list) {

				for (Map.Entry<String, Object> entry : map.entrySet()) {
					if (entry.getValue().equals(path)) {
						playBackIndex = media_list.indexOf(map);
						play(playBackIndex);
						break;
					}
				}
			}
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
