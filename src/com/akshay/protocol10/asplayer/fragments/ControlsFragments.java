package com.akshay.protocol10.asplayer.fragments;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.callbacks.onItemSelected;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ControlsFragments extends Fragment implements OnClickListener {

	TextView title_view, artist_view, album_view;
	Button play_button, next_button, back_button;
	View view;

	onItemSelected mCallBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setRetainInstance(true);
		view = inflater.inflate(R.layout.controls_fragments, container, false);
		play_button = (Button) view.findViewById(R.id.play_button);
		next_button = (Button) view.findViewById(R.id.next_button);
		back_button = (Button) view.findViewById(R.id.back_button);
		setUpListeners();
		return view;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		setUpView();

		Bundle bundle = getArguments();
		if (bundle != null) {
			String title_text = bundle.getString("title");
			String album_text = bundle.getString("album");
			String artist_text = bundle.getString("artist");
			int position = bundle.getInt("position");

			title_view.setText(title_text.toString());
			artist_view.setText(artist_text.toString());
			album_view.setText(album_text.toString());

			mCallBack.startPlayBack(position);
		}
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
		mCallBack = (onItemSelected) activity;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.play_button:
			Toast.makeText(getActivity(), "PLAY", Toast.LENGTH_SHORT).show();
			mCallBack.pausePlayBack();
			break;
		case R.id.next_button:
			mCallBack.nextPlayBack();
			Toast.makeText(getActivity(), "NEXT", Toast.LENGTH_SHORT).show();
			break;
		case R.id.back_button:
			mCallBack.previousPlayBack();
			Toast.makeText(getActivity(), "PREVIOUS", Toast.LENGTH_SHORT)
					.show();
			break;

		default:
			break;
		}
	}

}
