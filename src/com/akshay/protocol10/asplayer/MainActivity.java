package com.akshay.protocol10.asplayer;

import java.util.ArrayList;
import java.util.HashMap;

import com.akshay.protocol10.asplayer.adapters.DrawerAdapter;
import com.akshay.protocol10.asplayer.database.MediaManager;
import com.akshay.protocol10.asplayer.fragments.PageSlider;
import com.akshay.protocol10.asplayer.utils.ASUtils;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements
		OnItemClickListener {

	String[] drawer_options = {};
	DrawerLayout drawer_layout;
	ListView list_view;

	ActionBarDrawerToggle drawer_toggle;
	DrawerAdapter drawerAdapter;
	CharSequence title;
	ArrayList<HashMap<String, Object>> track;

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

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		getSupportActionBar().setHomeButtonEnabled(true);

		Fragment fragment = new PageSlider();
		FragmentManager manager = getSupportFragmentManager();
		manager.beginTransaction().replace(R.id.content, fragment).commit();
		MediaManager mediamanager = new MediaManager();
		track = (ArrayList<HashMap<String, Object>>) mediamanager
				.retriveContent(this);
		int i = track.size();
		Toast.makeText(this, "Tracks Present" + i, Toast.LENGTH_SHORT).show();

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
}
