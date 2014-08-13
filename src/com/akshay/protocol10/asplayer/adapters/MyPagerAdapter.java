package com.akshay.protocol10.asplayer.adapters;

import com.akshay.protocol10.asplayer.fragments.Albums;
import com.akshay.protocol10.asplayer.fragments.ArtistFragment;
import com.akshay.protocol10.asplayer.fragments.GenreFragment;
import com.akshay.protocol10.asplayer.fragments.TracksFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MyPagerAdapter extends FragmentPagerAdapter {

	private final String[] TITLES = { "Album", "Artist", "Songs", "Genre" };

	public MyPagerAdapter(FragmentManager childFragmentManager) {
		// TODO Auto-generated constructor stub
		super(childFragmentManager);
	}

	@Override
	public CharSequence getPageTitle(int position) {
		// TODO Auto-generated method stub
		return TITLES[position];
	}

	@Override
	public Fragment getItem(int index) {
		// TODO Auto-generated method stub
		Fragment fragment = null;
		if (index == 0) {
			fragment = new Albums();
		} else if (index == 1) {
			fragment = new ArtistFragment();
		} else if (index == 2) {
			fragment = new TracksFragment();
		} else if (index == 3) {
			fragment = new GenreFragment();
		}
		return fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return TITLES.length;
	}

}