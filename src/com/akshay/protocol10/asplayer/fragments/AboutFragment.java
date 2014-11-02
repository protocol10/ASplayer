package com.akshay.protocol10.asplayer.fragments;

import com.akshay.protocol10.asplayer.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AboutFragment extends Fragment {

	LinearLayout layout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		layout = (LinearLayout) getActivity().findViewById(R.id.nowPlayingMain);
		layout.setVisibility(View.GONE);
		return inflater.inflate(R.layout.about_fragment, container, false);
	}
}
