package com.akshay.protocol10.asplayer;

/**
 * @author akshay
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.adapters.DrawerAdapter;
import com.akshay.protocol10.asplayer.callbacks.onItemSelected;
import com.akshay.protocol10.asplayer.database.MediaManager;
import com.akshay.protocol10.asplayer.database.Preferences;
import com.akshay.protocol10.asplayer.fragments.AboutFragment;
import com.akshay.protocol10.asplayer.fragments.AlbumSongsFragment;
import com.akshay.protocol10.asplayer.fragments.ArtistAlbumFragments;
import com.akshay.protocol10.asplayer.fragments.ControlsFragments;
import com.akshay.protocol10.asplayer.fragments.EqualizerFragment;
import com.akshay.protocol10.asplayer.fragments.GenreAlbumsFragment;
import com.akshay.protocol10.asplayer.fragments.PageSliderFragment;
import com.akshay.protocol10.asplayer.service.MediaServiceContoller;
import com.akshay.protocol10.asplayer.service.MediaServiceContoller.MediaBinder;
import com.akshay.protocol10.asplayer.utils.ASUtils;
import com.akshay.protocol10.asplayer.widget.SlidingUpPanelLayout;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements
		OnItemClickListener, onItemSelected, OnClickListener,
		OnSeekBarChangeListener {

	boolean isBound, visible;
	String name, artist, album;
	String[] drawer_options = {};
	CharSequence title;
	ArrayList<HashMap<String, Object>> track;

	DrawerLayout drawer_layout;
	ListView list_view;
	boolean isShuffle, isRepeat;
	ActionBarDrawerToggle drawer_toggle;
	SlidingUpPanelLayout slidingUpPanelLayout;
	DrawerAdapter drawerAdapter;

	MediaServiceContoller serviceController;
	MediaBinder binder;
	MediaManager manager;
	IntentFilter filter;
	Fragment fragment;
	Preferences preferences;
	ControlsFragments controlsFragments;
	LinearLayout nowPlaying, detailLayout;
	TextView titleText, artistText, currentTime, totalTime;
	ImageView albumArtImage;
	ImageView previousBtn, nextBtn, playBtn, repeatBtn, shuffleBtn;
	SeekBar slideSeekbar;
	String path;

	private static final String SEEKKEY = "seekpos";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		preferences = new Preferences(this);

		drawer_options = ASUtils.options;
		list_view = (ListView) findViewById(R.id.list_drawer_view);
		drawer_layout = (DrawerLayout) findViewById(R.id.drawer);

		/* Slide-Up Panel View Initialization */
		slidingUpPanelLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
		titleText = (TextView) findViewById(R.id.slide_title);
		artistText = (TextView) findViewById(R.id.slide_artist);
		currentTime = (TextView) findViewById(R.id.slide_currenttime);
		totalTime = (TextView) findViewById(R.id.slide_totalduration);
		albumArtImage = (ImageView) findViewById(R.id.slide_art);

		playBtn = (ImageView) findViewById(R.id.slide_play);
		nextBtn = (ImageView) findViewById(R.id.slide_next);
		previousBtn = (ImageView) findViewById(R.id.slide_previous);
		repeatBtn = (ImageView) findViewById(R.id.slide_repeat);
		shuffleBtn = (ImageView) findViewById(R.id.slide_shuffle);

		slideSeekbar = (SeekBar) findViewById(R.id.slide_progress);

		playBtn.setOnClickListener(this);
		nextBtn.setOnClickListener(this);
		previousBtn.setOnClickListener(this);
		repeatBtn.setOnClickListener(this);
		shuffleBtn.setOnClickListener(this);
		slideSeekbar.setOnSeekBarChangeListener(this);
		if (!preferences.getNowPlaying()) {
			slidingUpPanelLayout.hidePanel();
		}

		detailLayout = (LinearLayout) findViewById(R.id.detail_layout);

		manager = new MediaManager();
		controlsFragments = new ControlsFragments();
		drawerAdapter = new DrawerAdapter(this, R.layout.drawer_list_row,
				R.id.option_text, drawer_options);

		list_view.setAdapter(drawerAdapter);

		list_view.setOnItemClickListener(this);

		title = getTitle();
		track = new ArrayList<HashMap<String, Object>>();

		drawer_toggle = new ActionBarDrawerToggle(this, drawer_layout,
				R.drawable.ic_drawer, R.string.drawer_opened,
				R.string.drawer_closed) {
			@Override
			public void onDrawerOpened(View drawerView) {

				super.onDrawerOpened(drawerView);

			}

			@Override
			public void onDrawerClosed(View drawerView) {

				super.onDrawerClosed(drawerView);
				// invalidateOptionsMenu();
			}
		};
		drawer_layout.setDrawerListener(drawer_toggle);

		serviceController = new MediaServiceContoller();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		if (savedInstanceState == null) {
			fragment = new PageSliderFragment();
			this.fragment.setRetainInstance(true);
			FragmentManager manager = getSupportFragmentManager();
			manager.beginTransaction()
					.replace(R.id.content, fragment, ASUtils.PAGE_SLIDER_TAG)
					.commit();

		} else {
			titleText.setText(preferences.getTitle());
			artistText.setText(preferences.getArtist());
		}

		// IntentFilter for BroadCastReceiver
		filter = new IntentFilter();
		filter.addAction(MediaServiceContoller.BROADCAST_ACTION);
		filter.addAction(MediaServiceContoller.SEEKBAR_ACTION);
		filter.addAction(MediaServiceContoller.CONTROL_PLAY);

		Intent intent = getIntent();
		// bind service

		if (intent != null) {
			String action = intent.getAction();
			// open file fnowPlayingrom file manager and action to check if
			// blank
			if (action.equals("android.intent.action.VIEW") && action != null) {
				path = intent.getData().getPath();
				intent = new Intent(this, MediaServiceContoller.class);

				if (path != null) {

					ASPlayer.getAppContext().bindService(intent, mConnection,
							Context.BIND_AUTO_CREATE);
				}
			}
			if (action.equals(MediaServiceContoller.NOTIFICATION_ACTION)
					&& action != null) {
				boolean check = intent.getBooleanExtra("playing", false);
				if (check)
					playBtn.setImageResource(R.drawable.ic_pause);
				else
					playBtn.setImageResource(R.drawable.ic_play);
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		String select_fragments = drawer_options[position];

		Fragment current = getSupportFragmentManager().findFragmentById(
				R.id.content);
		if (current != null && current.getTag().equals(select_fragments)) {
			drawer_layout.closeDrawer(list_view);
			return;
		}

		/**
		 * Clear any backstack before switching different tabs. This avoids
		 * activating old backstack, when user presses the back button to quit
		 */
		getSupportFragmentManager().popBackStack(null,
				FragmentManager.POP_BACK_STACK_INCLUSIVE);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment f = null;

		if (select_fragments.equals("Home")) {
			f = new PageSliderFragment();
		} else if (select_fragments.equals("Equalizer")) {
			f = new EqualizerFragment();
		} else if (select_fragments.equals("About")) {
			f = new AboutFragment();
		}

		f.setRetainInstance(true);
		ft.replace(R.id.content, f, select_fragments).commit();
		current.setUserVisibleHint(false);
		f.setUserVisibleHint(true);
		drawer_layout.closeDrawer(list_view);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {

		super.onRestoreInstanceState(savedInstanceState);
		fragment = getSupportFragmentManager().getFragment(savedInstanceState,
				"view");
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {

		super.onPostCreate(savedInstanceState);
		drawer_toggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		drawer_toggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (drawer_toggle.onOptionsItemSelected(item))
			return true;

		return super.onOptionsItemSelected(item);
	}

	/**
	 * The callback for fragments-activity communication
	 */

	@Override
	public void updateView(String title, String artist, String album,
			int position, long album_id) {

		serviceController.play(position);
		updateNowPlaying(title, artist);
		if (slidingUpPanelLayout.isPanelHidden())
			slidingUpPanelLayout.showPanel();
	}

	@Override
	public void updateGenreAlbum(long genreId) {
		GenreAlbumsFragment fragment = new GenreAlbumsFragment();
		Bundle bundle = new Bundle();
		bundle.putLong("genre_id", genreId);
		fragment.setArguments(bundle);
		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction().replace(R.id.content, fragment, "GENRE");
		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		doUnbindService();
		unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onStart() {
		super.onStart();
		doBindService();
		registerReceiver(broadcastReceiver, filter);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if (!preferences.getNowPlaying()) {
			// nowPlaying.setVisibility(View.GONE);
		}
	}

	/**
	 * CALL BACK FOR PAUSE
	 */
	@Override
	public void pausePlayBack() {

		serviceController.pauseSong();
	}

	/**
	 * CALLBACK FOR PLAYING NEXT SONG
	 */
	@Override
	public void nextPlayBack() {

		serviceController.nextSong();
	}

	/**
	 * CALLBACK FOR PLAYING PREVIOUS SONG
	 */

	@Override
	public void previousPlayBack() {

		serviceController.previousSong();
	}

	/**
	 * CALLBACK FOR PLAYING SONG
	 * 
	 * @param songIndex
	 */
	@Override
	public void startPlayBack(int index) {

		if (isBound == true) {
			serviceController.play(index);
		}
	}

	/**
	 * UPDATE THE LIST AT IN SERVICE
	 */
	@Override
	public void updateList(List<HashMap<String, Object>> list) {

		serviceController.setSongs(list);
	}

	@Override
	public void setPresetReverb(int position) {
		serviceController.applyReverb(position);
	};

	/**
	 * Pass the ARTIST ID to retrieve the album w.r.t to Artist
	 * 
	 * @param artist_id
	 */
	@Override
	public void selectArtist(long artist_id) {
		ArtistAlbumFragments fragment = new ArtistAlbumFragments();
		Bundle args = new Bundle();
		args.putLong("artist_id", artist_id);
		fragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction().replace(R.id.content, fragment,
						ASUtils.ARTIST_TAG);
		transaction.addToBackStack(null).commit();
	}

	/**
	 * Pass the ALBUM NAME and ARTIST ID to retrieve songs
	 * 
	 * @param name
	 * @param id
	 */
	@Override
	public void updateArtistAlbum(String name, long id) {
		AlbumSongsFragment fragment = new AlbumSongsFragment();
		Bundle args = new Bundle();
		args.putString(ASUtils.NAME_KEY, name);
		args.putLong(ASUtils.ARTIST_ID_KEY, id);
		fragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction().replace(R.id.content, fragment,
						ASUtils.ALBUM_TAG);
		transaction.addToBackStack(null).commit();

	}

	/**
	 * callback for updating the SeekBar using broadcast receiver from service
	 * 
	 * @param progress
	 */
	@Override
	public void seekTo(int progress) {

		Intent intent = new Intent(MediaServiceContoller.SEEKTO_ACTION);
		intent.putExtra(SEEKKEY, progress);
		sendBroadcast(intent);
	}

	@Override
	public void setEqualizer(int band, int level) {
		serviceController.setEqManual(band, level);
	}

	@Override
	public void setTag(String tag) {
		serviceController.setTag(tag);
	}

	/**
	 * Select Preset available
	 */
	@Override
	public void selectPreset(int position) {
		serviceController.applyEffect(position);
	}

	/**
	 * Establish a connection with the service. We use an explicit class name
	 * because we want a specific service implementation that we know will be
	 * running in our own process (and thus won't be supporting component
	 * replacement by other applications).
	 */

	private void doBindService() {
		Intent serviceIntent = new Intent(this, MediaServiceContoller.class);
		bindService(serviceIntent, mConnection, BIND_AUTO_CREATE);

	}

	private void doUnbindService() {

		// Detach our existing connection.

		if (isBound) {
			unbindService(mConnection);
			isBound = false;
		}
	}

	ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {

			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.
			// Because it is running in our same process, we should never
			// see this happen.

			serviceController = null;
			isBound = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {

			// This is called when the connection with the service has been
			// established, giving us the service object we can use to
			// interact with the service. Because we have bound to a explicit
			// service that we know is running in our own process, we can
			// cast its IBinder to a concrete class and directly access it.

			MediaServiceContoller.MediaBinder bind = (MediaBinder) service;
			serviceController = bind.getService();
			isBound = true;
			if (path != null && !path.equals(preferences.getPath())) {
				serviceController.playFromPath(path);
				preferences.setPath(path);

			}
		}
	};

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			controlsFragments = (ControlsFragments) getSupportFragmentManager()
					.findFragmentByTag(ASUtils.CONTROL_TAG);
			String action = intent.getAction();

			if (action.equals(MediaServiceContoller.BROADCAST_ACTION)) {

				name = intent.getStringExtra(MediaServiceContoller.TITLE_KEY);
				artist = intent
						.getStringExtra(MediaServiceContoller.ARTIST_KEY);

				album = intent.getStringExtra(MediaServiceContoller.ALBUM_KEY);
				long id = intent
						.getLongExtra(MediaServiceContoller.ALBUM_ID, 0);
				preferences.setName(name, artist, album);
				preferences.setId(id);
				// Check whether Slide-Panel is Hidden.
				if (!slidingUpPanelLayout.isPanelHidden()) {
					updateNowPlaying(name, artist);
					updateAlbumArt();
				}
			}

			/**
			 * Update the SeekBar from BroadCast Receiver
			 */
			if (action.equals(MediaServiceContoller.SEEKBAR_ACTION)) {
				if (!slidingUpPanelLayout.isPanelHidden()) {
					int maxDuration = intent.getIntExtra(ASUtils.MAX_DURATION,
							0);
					int currentDuration = intent.getIntExtra(
							ASUtils.CURRENT_POSITION, 0);
					updateSeekBar(maxDuration, currentDuration);

				}
			}

			if (action.equals(MediaServiceContoller.CONTROL_PLAY)) {
				boolean isPlaying = intent.getBooleanExtra(ASUtils.IS_PLAYING,
						false);

				if (!slidingUpPanelLayout.isPanelHidden()) {
					updateAlbumArt();
				}
				updateIcon(isPlaying);
			}
		}

		private void updateSeekBar(int maxDuration, int progress) {
			slideSeekbar.setMax(maxDuration);
			slideSeekbar.setProgress(progress);
			currentTime.setText(ASUtils.updateText(progress));
			totalTime.setText(ASUtils.updateText(maxDuration));
		}

		/**
		 * Update the Album Art in Sliding Pane Layout
		 * 
		 * @param id
		 */
		private void updateAlbumArt() {
			new Handler().post(new Runnable() {

				@Override
				public void run() {
					Bitmap bitmap = MediaManager.getAlbumArt(
							preferences.getId(), getApplicationContext());
					if (bitmap != null) {
						albumArtImage.setImageBitmap(bitmap);
					} else {
						albumArtImage.setImageResource(R.drawable.ic_album_art);
					}
				}
			});
		}

	};

	/**
	 * Update the SlideUpPanel Widget
	 * 
	 * @param titleupdateNowPlaying
	 * @param artist
	 */
	private void updateNowPlaying(String title, String artist) {

		preferences.setName(title, artist);
		titleText.setText(title.toString());
		artistText.setText(artist.toString());
	}

	protected void updateIcon(boolean isPlaying) {

		playBtn.setImageResource(isPlaying ? R.drawable.ic_pause
				: R.drawable.ic_play);
	}

	@Override
	public void onBackPressed() {

		super.onBackPressed();
		if (preferences.getNowPlaying()) {
			if (slidingUpPanelLayout.isPanelHidden()) {
				slidingUpPanelLayout.showPanel();
			}
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.slide_play:
			serviceController.pauseSong();
			break;
		case R.id.slide_next:
			serviceController.nextSong();
			break;
		case R.id.slide_previous:
			serviceController.previousSong();
			break;
		case R.id.slide_repeat:
			if (isRepeat) {
				isRepeat = false;
				repeatBtn.setImageResource(R.drawable.ic_repeat);
			} else {
				isRepeat = true;
				repeatBtn.setImageResource(R.drawable.ic_repeatstate);
				shuffleBtn.setImageResource(R.drawable.ic_shuffle);
				isShuffle = false;
			}
			preferences.setRepeat(isRepeat);
			preferences.setShuffle(isShuffle);

			break;
		case R.id.slide_shuffle:
			if (isShuffle) {
				isShuffle = false;
				shuffleBtn.setImageResource(R.drawable.ic_shuffle);
			} else {
				isShuffle = true;
				shuffleBtn.setImageResource(R.drawable.ic_shufflestate);
				repeatBtn.setImageResource(R.drawable.ic_repeat);
				isRepeat = false;
			}
			preferences.setRepeat(isRepeat);
			preferences.setShuffle(isShuffle);
			break;
		case R.id.detail_layout:
			controlsFragments = new ControlsFragments();
			Bundle bundle = new Bundle();
			bundle.putBoolean("NoPlay", true);
			bundle.putString("title", preferences.getTitle());
			bundle.putString("artist", preferences.getArtist());
			bundle.putString("album", preferences.getArtist());
			bundle.putLong(ASUtils.ALBUM_ID_KEY, preferences.getId());
			controlsFragments.setArguments(bundle);
			getSupportFragmentManager()
					.beginTransaction()
					.replace(R.id.content, controlsFragments,
							ASUtils.CONTROL_TAG).addToBackStack(null).commit();
			break;
		default:
			break;
		}
	}

	@Override
	public void playFromPath(String path) {

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		if (fromUser) {
			seekTo(progress);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

	}

}
