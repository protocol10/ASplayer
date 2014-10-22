package com.akshay.protocol10.asplayer.service;

/**
 * @author akshay
 */
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.akshay.protocol10.asplayer.ASPlayer;
import com.akshay.protocol10.asplayer.MainActivity;
import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.database.MediaManager;
import com.akshay.protocol10.asplayer.database.Preferences;
import com.akshay.protocol10.asplayer.utils.ASUtils;

import android.app.Notification;
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
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
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
	boolean isPLaying = false, isRepeat, isShuffle;
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
	short defaultReverb = PresetReverb.PRESET_NONE;
	int result;
	String tag;
	static MediaPlayer mediaplayer;
	MediaManager manager;
	AudioManager audioManager;
	Equalizer equalizer;
	BassBoost bassBoost;
	Handler handler;
	Intent intent;
	IntentFilter filter;
	int db_condition;
	Preferences preferences;
	Random random;
	PresetReverb reverb;
	RemoteViews remoteViews;
	NotificationManager notifiactioManager;
	Notification notification;
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

		preferences = new Preferences(getApplicationContext());

		manager = new MediaManager();

		media_list = manager.retriveContent(this);
		random = new Random();
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
		remoteViews = new RemoteViews(getPackageName(), R.layout.as_widget_main);
		notification = new Notification();
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
			if (equalizer == null) {
				equalizer = new Equalizer(1, mediaplayer.getAudioSessionId());
			}
			equalizer.setEnabled(true);

			mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaplayer.setDataSource(media_list.get(playBackIndex)
					.get(PATH_KEY).toString());

			/* Apply presetReverb */
			if (reverb != null) {
				mediaplayer.attachAuxEffect(reverb.getId());
				reverb.setPreset(defaultReverb);
				reverb.setEnabled(true);
				mediaplayer.setAuxEffectSendLevel(1.0f);
			}
			mediaplayer.prepare();
			mediaplayer.setOnCompletionListener(this);
			mediaplayer.start();

			updatewidget(this);
			setUpHandlers();
			updateView();

			intent = new Intent(CONTROL_PLAY);
			intent.putExtra(ASUtils.IS_PLAYING, mediaplayer.isPlaying());
			sendBroadcast(intent);

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
		preferences.setDuration(mediaplayer.getDuration());
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

			if (action.equals(APPWIDGET_PLAY)) {
				if (mediaplayer.isPlaying())
					mediaplayer.pause();
				else if (!mediaplayer.isPlaying()) {
					mediaplayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
					mediaplayer.start();
					remoteViews.setImageViewResource(R.id.notify_pause,
							isPLaying ? R.drawable.ic_notifypause
									: R.drawable.ic_notifyplay);
				}
				pauseSong();
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
				if (mediaplayer.isPlaying()) {
					pauseSong();
				}
				NotificationManager notificationManager = (NotificationManager) getApplicationContext()
						.getSystemService(NOTIFICATION_SERVICE);
				notificationManager.cancel(ASUtils.NOTIFICATION_ID);
			}
			if (action.equals(NOTIFY_PAUSE)) {
				pauseSong();
				remoteViews.setImageViewResource(R.id.notify_pause, mediaplayer
						.isPlaying() ? R.drawable.ic_notifypause
						: R.drawable.ic_notifyplay);
				/* Use to update the icon for pause button. weird android bug */
				notifiactioManager
						.notify(ASUtils.NOTIFICATION_ID, notification);
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
		remoteViews.setImageViewResource(R.id.notify_pause,
				R.drawable.ic_notifypause);
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
		notification = builder.build();
		notifiactioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		notifiactioManager.notify(ASUtils.NOTIFICATION_ID, notification);

		startService(new Intent(this, MediaServiceContoller.class));
		startForeground(ASUtils.NOTIFICATION_ID, notification);
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
		isRepeat = preferences.getRepeat();
		isShuffle = preferences.getShuffle();
		if (isRepeat) {
			play(playBackIndex);
		} else if (isShuffle) {
			playBackIndex = random.nextInt(media_list.size() - 1);
			play(playBackIndex);
		} else {
			nextSong();
		}

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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void applyReverb(int position) {

		if (reverb != null) {
			switch (position) {
			case 0:
				defaultReverb = PresetReverb.PRESET_LARGEHALL;
				break;
			case 1:
				defaultReverb = PresetReverb.PRESET_LARGEROOM;
				break;
			case 2:
				defaultReverb = PresetReverb.PRESET_MEDIUMHALL;
				break;
			case 3:
				defaultReverb = PresetReverb.PRESET_MEDIUMROOM;
				break;
			case 4:
				defaultReverb = PresetReverb.PRESET_NONE;
				break;
			case 5:
				defaultReverb = PresetReverb.PRESET_PLATE;
				break;
			case 6:
				defaultReverb = PresetReverb.PRESET_SMALLROOM;
			}
			setReverb(defaultReverb);
		}
	}

	private void setReverb(short m) {
		reverb.setPreset(m);
		mediaplayer.setAuxEffectSendLevel(1.0f);

	}

	public void applyEffect(int pos) {
		if (equalizer != null) {
			if (pos < equalizer.getNumberOfPresets()) {
				usePreset(pos);
			}

			int band1, band2, band3, band4, band5;
			switch (pos) {
			case 0:
				band1 = 300;
				band2 = 000;
				band3 = 000;
				band4 = 000;
				band5 = 300;
				applyPreset(band1, band2, band3, band4, band5);
				break;
			case 1:
				band1 = 500;
				band2 = 300;
				band3 = -200;
				band4 = 400;
				band5 = 400;
				applyPreset(band1, band2, band3, band4, band5);
				break;
			case 2:
				band1 = 600;
				band2 = 000;
				band3 = 200;
				band4 = 400;
				band5 = 100;
				applyPreset(band1, band2, band3, band4, band5);
				break;
			case 3:
				band1 = 000;
				band2 = 000;
				band3 = 000;
				band4 = 000;
				band5 = 000;
				applyPreset(band1, band2, band3, band4, band5);
				break;
			case 4:
				band1 = 600;
				band2 = 000;
				band3 = 000;
				band4 = 200;
				band5 = -100;
				applyPreset(band1, band2, band3, band4, band5);
				break;
			case 6:
				band1 = 500;
				band2 = 100;
				band3 = 900;
				band4 = 300;
				band5 = -100;
				applyPreset(band1, band2, band3, band4, band5);
				break;
			case 5:
				band1 = 500;
				band2 = 300;
				band3 = 000;
				band4 = 100;
				band5 = 300;
				applyPreset(band1, band2, band3, band4, band5);
				break;
			case 7:
				band1 = 400;
				band2 = 200;
				band3 = -200;
				band4 = 200;
				band5 = 000;
				applyPreset(band1, band2, band3, band4, band5);
				break;
			case 8:
				band1 = -100;
				band2 = 200;
				band3 = 500;
				band4 = 100;
				band5 = -200;
				applyPreset(band1, band2, band3, band4, band5);
				break;
			case 9:
				band1 = 500;
				band2 = 300;
				band3 = -100;
				band4 = 300;
				band5 = 500;
				applyPreset(band1, band2, band3, band4, band5);
				break;
			default:
				break;
			}
		}

	}

	private void usePreset(int preset) {
		equalizer.usePreset((short) preset);
	}

	private void applyPreset(int band1, int band2, int band3, int band4,
			int band5) {
		equalizer.setBandLevel((short) 0, (short) band1);
		equalizer.setBandLevel((short) 1, (short) band2);
		equalizer.setBandLevel((short) 2, (short) band3);
		equalizer.setBandLevel((short) 3, (short) band4);
		equalizer.setBandLevel((short) 4, (short) band5);
	}

	public void setEqManual(int band, int level) {
		if (equalizer != null) {
			level = level * 100;
			equalizer.usePreset((short) 5);
			equalizer.setBandLevel((short) band, (short) level);
		}
	}

}
