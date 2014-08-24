package com.akshay.protocol10.asplayer.fragments;

import com.akshay.protocol10.asplayer.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ControlsFragments extends Fragment {

	TextView title_view, artist_view, album_view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.controls_fragments, container, false);
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Bundle bundle = getArguments();
		if (bundle != null) {
			String title_text = bundle.getString("title");
			String album_text = bundle.getString("album");
			String artist_text = bundle.getString("artist");

			title_view = (TextView) getActivity().findViewById(
					R.id.song_title_text_view);
			artist_view = (TextView) getActivity().findViewById(
					R.id.album_name_text_view);
			album_view = (TextView) getActivity().findViewById(
					R.id.artist_text_view);

			title_view.setText(title_text.toString());
			artist_view.setText(artist_text.toString());
			album_view.setText(album_text.toString());
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

}
