package com.akshay.protocol10.asplayer.fragments;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.adapters.MyPagerAdapter;
import com.akshay.protocol10.asplayer.callbacks.onItemSelected;
import com.astuetz.PagerSlidingTabStrip;

public class PageSlider extends Fragment {

	onItemSelected mcallBack;

	public PageSlider() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.page_slider, container, false);
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

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

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

	public void playFromPath(String path) {
		mcallBack.playFromPath(path);
	}

}
