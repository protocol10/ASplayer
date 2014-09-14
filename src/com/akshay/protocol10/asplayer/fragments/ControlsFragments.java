package com.akshay.protocol10.asplayer.fragments;

/**
 * @author akshay
 */
import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.callbacks.onItemSelected;

import android.R.raw;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class ControlsFragments extends Fragment implements OnClickListener {

	TextView title_view, artist_view, album_view;
	Button play_button, next_button, back_button;
	View view;
	int position;
	String title_text, album_text, artist_text;
	onItemSelected mCallBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.i("LIFECYCLE", "onCreate");

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// setRetainInstance(true);
		Log.i("LIFECYCLE", "onCreateView");
		view = inflater.inflate(R.layout.controls_fragments, container, false);

		play_button = (Button) view.findViewById(R.id.play_button);
		next_button = (Button) view.findViewById(R.id.next_button);
		back_button = (Button) view.findViewById(R.id.back_button);

		title_view = (TextView) view.findViewById(R.id.song_title_text_view);
		album_view = (TextView) view.findViewById(R.id.album_name_text_view);
		artist_view = (TextView) view.findViewById(R.id.artist_text_view);
		Bundle bundle = getArguments();
		if (bundle != null) {

			title_text = bundle.getString("title");
			album_text = bundle.getString("album");
			artist_text = bundle.getString("artist");
			position = bundle.getInt("position");
		}

		if (savedInstanceState == null) {
			mCallBack.startPlayBack(position);
		} else {

		}
		title_view.setText(title_text.toString());
		artist_view.setText(artist_text.toString());
		album_view.setText(album_text.toString());
		setUpListeners();

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt("position", position);

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("LIFECYCLE", "onStart");
		/*
		 * setUpView();
		 * 
		 * Bundle bundle = getArguments(); if (bundle != null) {
		 * 
		 * String title_text = bundle.getString("title"); String album_text =
		 * bundle.getString("album"); String artist_text =
		 * bundle.getString("artist"); int position = bundle.getInt("position");
		 * 
		 * title_view.setText(title_text.toString());
		 * artist_view.setText(artist_text.toString());
		 * album_view.setText(album_text.toString());
		 * 
		 * mCallBack.startPlayBack(position); }
		 */
	}

	private void setUpView() {
		// TODO Auto-generated method stub
		title_view = (TextView) getActivity().findViewById(
				R.id.song_title_text_view);
		artist_view = (TextView) getActivity().findViewById(
				R.id.album_name_text_view);
		album_view = (TextView) getActivity().findViewById(
				R.id.artist_text_view);
	}

	private void setUpListeners() {
		// TODO Auto-generated method stub
		play_button.setOnClickListener(this);
		next_button.setOnClickListener(this);
		back_button.setOnClickListener(this);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.i("LIFECYCLE", "onAttach");
		mCallBack = (onItemSelected) activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		Log.i("LIFECYCLE", "onActictyCreated");
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.play_button:
			mCallBack.pausePlayBack();
			break;
		case R.id.next_button:
			mCallBack.nextPlayBack();
			break;
		case R.id.back_button:
			mCallBack.previousPlayBack();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * Method to be called from Activity to update the UI Elements
	 * 
	 * @param name
	 *            - Title of the file
	 * @param artist
	 *            -Name of Artist
	 * @param album
	 *            -Name of Album
	 */
	public void updateView(String name, String artist, String album) {
		title_view.setText(name);
		artist_view.setText(artist);
		album_view.setText(album);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("LIFECYCLE", "onResume");
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.i("LIFECYCLE", "onPause");
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.i("LIFECYCLE", "onStop");
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("LIFECYCLE", "onDestroy");
	}

}
