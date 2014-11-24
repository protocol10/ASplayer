package com.akshay.protocol10.asplayer.fragments;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.database.Preferences;
import com.akshay.protocol10.asplayer.widget.SlidingUpPanelLayout;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;

public class AboutFragment extends Fragment {

	LinearLayout layout;
	RelativeLayout.LayoutParams params;
	ScrollView scrollview;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SlidingUpPanelLayout panelLayout = (SlidingUpPanelLayout) getActivity()
				.findViewById(R.id.sliding_layout);
		panelLayout.hidePanel();
		View view = inflater.inflate(R.layout.about_fragment, container, false);

		scrollview = (ScrollView) view.findViewById(R.id.scroll);
		params = (LayoutParams) scrollview.getLayoutParams();
		params.topMargin = new Preferences(getActivity()).getHeight();
		scrollview.setLayoutParams(params);

		return view;
	}
}
