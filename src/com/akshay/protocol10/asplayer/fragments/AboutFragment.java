package com.akshay.protocol10.asplayer.fragments;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.database.Preferences;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class AboutFragment extends Fragment {

	LinearLayout layout;
	RelativeLayout.LayoutParams params;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		layout = (LinearLayout) getActivity().findViewById(R.id.nowPlayingMain);
		params = (LayoutParams) layout.getLayoutParams();
		params.topMargin = new Preferences(getActivity()).getHeight();
		layout.setLayoutParams(params);
		layout.setVisibility(View.GONE);
		return inflater.inflate(R.layout.about_fragment, container, false);
	}
}
