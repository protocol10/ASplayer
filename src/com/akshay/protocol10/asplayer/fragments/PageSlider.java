package com.akshay.protocol10.asplayer.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.adapters.MyPagerAdapter;
import com.akshay.protocol10.asplayer.callbacks.onItemSelected;
import com.akshay.protocol10.asplayer.database.Preferences;
import com.astuetz.PagerSlidingTabStrip;

public class PageSlider extends Fragment implements OnClickListener {

	ImageButton play, next, previous;
	TextView title, artist;
	LinearLayout viewLayout;
	String track, artistName;
	Preferences preferences;
	onItemSelected mcallBack;

	public PageSlider() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		preferences = new Preferences(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.page_slider, container, false);
		play = (ImageButton) view.findViewById(R.id.play_pause);
		next = (ImageButton) view.findViewById(R.id.next);
		previous = (ImageButton) view.findViewById(R.id.previous);
		title = (TextView) view.findViewById(R.id.title);
		artist = (TextView) view.findViewById(R.id.artist);
		viewLayout = (LinearLayout) view.findViewById(R.id.nowPlayingWidget);

		play.setOnClickListener(this);
		next.setOnClickListener(this);
		previous.setOnClickListener(this);

		title.setText(preferences.getTitle().toString());
		artist.setText(preferences.getArtist().toString());
		return view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		PagerSlidingTabStrip tab = (PagerSlidingTabStrip) view
				.findViewById(com.akshay.protocol10.asplayer.R.id.tabs);
		tab.setIndicatorColor(Color.parseColor("#0099cc"));
		ViewPager pager = (ViewPager) view
				.findViewById(com.akshay.protocol10.asplayer.R.id.view_pager);
		MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		tab.setViewPager(pager);

	}

	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {

	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mcallBack = (onItemSelected) getActivity();

	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	public void updateNowPlaying(String track, String artistName) {
		this.track = track;
		this.artistName = artistName;
		if (!viewLayout.isShown()) {
			viewLayout.setVisibility(View.VISIBLE);
		}
		title.setText(track.toString());
		artist.setText(artistName.toString());
	}

	public void setNowPlaying(String track, String artistName) {
		this.track = track;
		this.artistName = artistName;
	}

	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		boolean conditon = preferences.getNowPlaying();

		if (conditon) {
			track = preferences.getTitle();
			artistName = preferences.getArtist();
			updateNowPlaying(track, artistName);
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.play_pause:
			mcallBack.pausePlayBack();
			break;
		case R.id.next:
			mcallBack.nextPlayBack();
			break;
		case R.id.previous:
			mcallBack.previousPlayBack();
			break;
		default:
			break;
		}
	}

	public void updateIcon(boolean isPlaying) {
		play.setImageResource(isPlaying ? R.drawable.ic_pause
				: R.drawable.ic_play);
	}

}
