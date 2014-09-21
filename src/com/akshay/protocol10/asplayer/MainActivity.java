package com.akshay.protocol10.asplayer;

/**
 * @author akshay
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.adapters.DrawerAdapter;
import com.akshay.protocol10.asplayer.callbacks.onItemSelected;
import com.akshay.protocol10.asplayer.fragments.AlbumSongsFragment;
import com.akshay.protocol10.asplayer.fragments.ArtistAlbum;
import com.akshay.protocol10.asplayer.fragments.ControlsFragments;
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

	boolean isBound = false;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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
				// TODO Auto-generated method stub
				super.onDrawerOpened(drawerView);

			}

			@Override
			public void onDrawerClosed(View drawerView) {
				// TODO Auto-generated method stub
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
			FragmentManager manager = getSupportFragmentManager();
			manager.beginTransaction().replace(R.id.content, fragment).commit();
		}

		// new AsyncLoader().execute();
		filter = new IntentFilter();
		filter.addAction(MediaServiceContoller.BROADCAST_ACTION);

		// bind service
		doBindService();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "view", fragment);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		fragment = getSupportFragmentManager().getFragment(savedInstanceState,
				"view");
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onPostCreate(savedInstanceState);
		drawer_toggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		drawer_layout.closeDrawer(list_view);
	}

	/**
	 * 
	 * The callback for fragments-activity communication
	 */

	@Override
	public void updateView(String title, String artist, String album,
			int position, long album_id) {
		// TODO Auto-generated method stub
		ControlsFragments fragments = new ControlsFragments();

		Bundle args = new Bundle();
		args.putString("title", title);
		args.putString("artist", artist);
		args.putString("album", album);
		args.putInt("position", position);
		args.putLong("album_id", album_id);
		fragments.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction().replace(R.id.content, fragments);

		transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		doUnbindService();
		unregisterReceiver(broadcastReceiver);
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		registerReceiver(broadcastReceiver, filter);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void pausePlayBack() {
		// TODO Auto-generated method stub
		serviceController.pauseSong();
	}

	@Override
	public void nextPlayBack() {
		// TODO Auto-generated method stub
		serviceController.nextSong();
	}

	@Override
	public void previousPlayBack() {
		// TODO Auto-generated method stub
		serviceController.previousSong();
	}

	@Override
	public void startPlayBack(int index) {
		// TODO Auto-generated method stub

		if (isBound == true) {
			serviceController.play(index);
		}
	}

	@Override
	public void updateList(List<HashMap<String, Object>> list) {
		// TODO Auto-generated method stub
		serviceController.setSongs(list);
	}

	@Override
	public void UpdateView(String name) {
		// TODO Auto-generated method stub duration
		AlbumSongsFragment fragment = new AlbumSongsFragment();
		Bundle args = new Bundle();
		args.putString("name", name);
		fragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction().replace(R.id.content, fragment);
		transaction.addToBackStack(null).commit();
	}

	@Override
	public void selectArtist(String name) {
		// TODO Auto-generated method stub
		ArtistAlbum fragment = new ArtistAlbum();
		Bundle args = new Bundle();
		args.putString("name", name);
		fragment.setArguments(args);

		FragmentTransaction transaction = getSupportFragmentManager()
				.beginTransaction().replace(R.id.content, fragment);
		transaction.addToBackStack(null).commit();
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
		// TODO Auto-generated method stub
		// Detach our existing connection.

		if (isBound) {
			unbindService(mConnection);
			isBound = false;
		}
	}

	ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceDisconnected(ComponentName name) {
			// TODO Auto-generated method stub
			// This is called when the connection with the service has been
			// unexpectedly disconnected -- that is, its process crashed.
			// Because it is running in our same process, we should never
			// see this happen.

			serviceController = null;
			isBound = false;
		}

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
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

	class AsyncLoader extends android.os.AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			Fragment fragment = new PageSlider();
			FragmentManager manager = getSupportFragmentManager();
			manager.beginTransaction().replace(R.id.content, fragment).commit();
			return null;
		}
	}

	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			if (intent.getAction() == MediaServiceContoller.BROADCAST_ACTION) {
				ControlsFragments fragments = (ControlsFragments) getSupportFragmentManager()
						.findFragmentById(R.id.content);

				name = intent.getStringExtra(MediaServiceContoller.TITLE_KEY);
				artist = intent
						.getStringExtra(MediaServiceContoller.ARTIST_KEY);
				album = intent.getStringExtra(MediaServiceContoller.ALBUM_KEY);
				long id = intent
						.getLongExtra(MediaServiceContoller.ALBUM_ID, 0);
				fragments.updateView(name, artist, album, id);

			}
		}

	};

}
