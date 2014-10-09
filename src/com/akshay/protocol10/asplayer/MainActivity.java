package com.akshay.protocol10.asplayer;

/**
 * @author akshay
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.activeandroid.ActiveAndroid;
import com.akshay.protocol10.asplayer.adapters.DrawerAdapter;
import com.akshay.protocol10.asplayer.callbacks.onItemSelected;
import com.akshay.protocol10.asplayer.database.Preferences;
import com.akshay.protocol10.asplayer.database.models.PresetModel;
import com.akshay.protocol10.asplayer.fragments.AboutFragment;
import com.akshay.protocol10.asplayer.fragments.AlbumSongsFragment;
import com.akshay.protocol10.asplayer.fragments.ArtistAlbum;
import com.akshay.protocol10.asplayer.fragments.ControlsFragments;
import com.akshay.protocol10.asplayer.fragments.EqualizerFragment;
import com.akshay.protocol10.asplayer.fragments.PageSlider;
import com.akshay.protocol10.asplayer.service.MediaServiceContoller;
import com.akshay.protocol10.asplayer.service.MediaServiceContoller.MediaBinder;
import com.akshay.protocol10.asplayer.utils.ASUtils;

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
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class MainActivity extends ActionBarActivity implements
		OnItemClickListener, onItemSelected {

	boolean isBound;
	String name, artist, album;
	String[] drawer_options = {};
	CharSequence title;
	ArrayList<HashMap<String, Object>> track;

	DrawerLayout drawer_layout;
	ListView list_view;

	ActionBarDrawerToggle drawer_toggle;
	DrawerAdapter drawerAdapter;

	MediaServiceContoller serviceController;
	MediaBinder binder;

	IntentFilter filter;
	Fragment fragment;
	Preferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		preferences = new Preferences(this);
		new AsyncLoader().execute();

		drawer_options = ASUtils.options;
		list_view = (ListView) findViewById(R.id.list_drawer_view);
		drawer_layout = (DrawerLayout) findViewById(R.id.drawer);

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
			fragment = new PageSlider();
			this.fragment.setRetainInstance(true);
			FragmentManager manager = getSupportFragmentManager();
			manager.beginTransaction()
					.replace(R.id.content, fragment, ASUtils.PAGE_SLIDER_TAG)
					.commit();
		}

		// IntentFilter for BroadCastReceiver
		filter = new IntentFilter();
		filter.addAction(MediaServiceContoller.BROADCAST_ACTION);
		filter.addAction(MediaServiceContoller.SEEKBAR_ACTION);
		Intent intent = getIntent();
		// bind service
		doBindService();

		if (intent != null) {
			String action = intent.getAction();
			// open file from file manager and action to check if blank
			if (action.equals("android.intent.action.VIEW") && action != null) {
				String path = intent.getData().getPath();
				if (path != null) {

					intent = new Intent(this, MediaServiceContoller.class);
					intent.setAction("com.akshay.protocol10.PLAYPATH");
					intent.putExtra("path", path);
					startService(intent);
				}

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
			f = new PageSlider();
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		if (drawer_toggle.onOptionsItemSelected(item))
			return true;

		return super.onOptionsItemSelected(item);
	}

	/**
	 * 
	 * The callback for fragments-activity communication
	 */

	@Override
	public void updateView(String title, String artist, String album,
			int position, long album_id) {

		ControlsFragments fragments = new ControlsFragments();

		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("artist", artist);
		args.putString("album", album);
		args.putInt("position", position);
		args.putLong("album_id", album_id);
		fragments.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction().replace(R.id.content, fragments,
						ASUtils.CONTROL_TAG);

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
		registerReceiver(broadcastReceiver, filter);
	}

	@Override
	protected void onResume() {

		super.onResume();
	}

	@Override
	public void pausePlayBack() {

		serviceController.pauseSong();
	}

	@Override
	public void nextPlayBack() {

		serviceController.nextSong();
	}

	@Override
	public void previousPlayBack() {

		serviceController.previousSong();
	}

	@Override
	public void startPlayBack(int index) {

		if (isBound == true) {
			serviceController.play(index);
		}
	}

	@Override
	public void updateList(List<HashMap<String, Object>> list) {

		serviceController.setSongs(list);
	}

	@Override
	public void UpdateView(String name) {

		AlbumSongsFragment fragment = new AlbumSongsFragment();
		Bundle args = new Bundle();
		args.putString("name", name);
		fragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction().replace(R.id.content, fragment,
						ASUtils.ALBUM_TAG);
		transaction.addToBackStack(null).commit();
	}

	@Override
	public void selectArtist(long artist_id) {
		ArtistAlbum fragment = new ArtistAlbum();
		Bundle args = new Bundle();
		args.putLong("artist_id", artist_id);
		fragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction().replace(R.id.content, fragment,
						ASUtils.ARTIST_TAG);
		transaction.addToBackStack(null).commit();
	}

	@Override
	public void seekTo(int progress) {

		Intent intent = new Intent(MediaServiceContoller.SEEKTO_ACTION);
		intent.putExtra("seekpos", progress);
		sendBroadcast(intent);
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
		}
	};

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			ControlsFragments controlFragments = (ControlsFragments) getSupportFragmentManager()
					.findFragmentByTag(ASUtils.CONTROL_TAG);

			if (intent.getAction() == MediaServiceContoller.BROADCAST_ACTION) {

				// Check whether fragment is in ONE-PAYNE layout
				if (controlFragments != null) {
					name = intent
							.getStringExtra(MediaServiceContoller.TITLE_KEY);
					artist = intent
							.getStringExtra(MediaServiceContoller.ARTIST_KEY);
					album = intent
							.getStringExtra(MediaServiceContoller.ALBUM_KEY);
					long id = intent.getLongExtra(
							MediaServiceContoller.ALBUM_ID, 0);
					controlFragments.updateView(name, artist, album, id);

				}
			}

			if (intent.getAction().equals(MediaServiceContoller.SEEKBAR_ACTION)) {
				if (controlFragments != null) {
					int maxDuration = intent.getIntExtra(ASUtils.MAX_DURATION,
							0);
					int currentDuration = intent.getIntExtra(
							ASUtils.CURRENT_POSITION, 0);
					controlFragments
							.updateSeekBar(maxDuration, currentDuration);
				}
			}
		}

	};

	// AsyncTask to load the initial presets from the database.
	class AsyncLoader extends android.os.AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			if (!preferences.checkDataBase()) {
				/**
				 * INSERT MULTIPLE RECORDS AS THE SAME TIME USE TRANSACTIONS.
				 * RECOMMENDED WAY TO PERFORM BULT TRANSACTION.
				 * REFERENCE:-ACTIVEANDROID WIKI
				 */
				ActiveAndroid.beginTransaction();
				try {
					for (int i = 0; i < ASUtils.DEFAULT_PRESETS.length; i++) {
						new PresetModel(ASUtils.DEFAULT_PRESETS[i]).save();
					}
					ActiveAndroid.setTransactionSuccessful();
				} finally {
					ActiveAndroid.endTransaction();
				}
			}
			return null;
		}
	}

}
