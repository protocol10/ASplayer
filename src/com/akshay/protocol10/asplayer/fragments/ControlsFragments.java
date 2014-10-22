package com.akshay.protocol10.asplayer.fragments;

/**
 * @author akshay
 */
import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.callbacks.onItemSelected;
import com.akshay.protocol10.asplayer.database.MediaManager;
import com.akshay.protocol10.asplayer.database.Preferences;
import com.akshay.protocol10.asplayer.utils.ASUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class ControlsFragments extends Fragment implements OnClickListener,
		OnSeekBarChangeListener {

	int position;
	long album_id;
	String title_text, album_text, artist_text;
	boolean play;
	ImageView album_art_view;
	TextView title_view, artist_view, album_view;

	ImageButton play_button, next_button, back_button, repeatButton,
			shuffle_button;
	TextView current_time, total_duration;
	View view;
	SeekBar seekbar;
	LinearLayout layout;
	Handler handler;
	onItemSelected mCallBack;
	boolean isRepeat, isShuffle;
	Preferences preferences;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		handler = new Handler();
		preferences = new Preferences(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.controls_fragments, container, false);

		play_button = (ImageButton) view.findViewById(R.id.play_button);
		next_button = (ImageButton) view.findViewById(R.id.next_button);
		back_button = (ImageButton) view.findViewById(R.id.back_button);

		shuffle_button = (ImageButton) view.findViewById(R.id.shuffle);
		repeatButton = (ImageButton) view.findViewById(R.id.repeat);
		title_view = (TextView) view.findViewById(R.id.song_title_text_view);
		album_view = (TextView) view.findViewById(R.id.album_name_text_view);
		artist_view = (TextView) view.findViewById(R.id.artist_text_view);
		album_art_view = (ImageView) view.findViewById(R.id.album_art);
		layout = (LinearLayout) getActivity().findViewById(R.id.nowPlayingMain);
		layout.setVisibility(View.GONE);
		current_time = (TextView) view.findViewById(R.id.current_pos);
		total_duration = (TextView) view.findViewById(R.id.total_duration);
		seekbar = (SeekBar) view.findViewById(R.id.progress_Bar);

		Bundle bundle = getArguments();
		if (bundle != null) {

			title_text = bundle.getString(ASUtils.TITLE_KEY);
			album_text = bundle.getString(ASUtils.ALBUM_KEY);
			artist_text = bundle.getString(ASUtils.ARTIST_KEY);
			position = bundle.getInt(ASUtils.POSITION_KEY);
			album_id = bundle.getLong(ASUtils.ALBUM_ID_KEY);
			play = bundle.getBoolean("NoPlay");

		}

		if (savedInstanceState == null) {

			if (!play) {
				mCallBack.startPlayBack(position);
			}
			preferences.setName(title_text, artist_text, album_text);
			preferences.setId(album_id);

		}
		title_view.setText(preferences.getTitle());
		artist_view.setText(preferences.getArtist());
		album_view.setText(preferences.getAlbum());
		isRepeat = preferences.getRepeat();
		isShuffle = preferences.getShuffle();
		total_duration.setText(updateText(preferences.getDuration()));
		if (isRepeat)
			repeatButton.setImageResource(R.drawable.ic_repeatstate);
		if (isShuffle)
			shuffle_button.setImageResource(R.drawable.ic_shufflestate);
		updateAlbumArt();
		setUpListeners();

		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putInt(ASUtils.POSITION_KEY, position);

	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	private void setUpListeners() {
		// TODO Auto-generated method stub
		play_button.setOnClickListener(this);
		next_button.setOnClickListener(this);
		back_button.setOnClickListener(this);
		shuffle_button.setOnClickListener(this);
		repeatButton.setOnClickListener(this);
		seekbar.setOnSeekBarChangeListener(this);
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
			mCallBack.pausePlayBack();
			break;
		case R.id.next_button:
			mCallBack.nextPlayBack();
			break;
		case R.id.back_button:
			mCallBack.previousPlayBack();
			break;
		case R.id.shuffle:
			if (isShuffle) {
				isShuffle = false;
				shuffle_button.setImageResource(R.drawable.ic_shuffle);
			} else {
				isShuffle = true;
				shuffle_button.setImageResource(R.drawable.ic_shufflestate);
				repeatButton.setImageResource(R.drawable.ic_repeat);
				isRepeat = false;
			}
			preferences.setRepeat(isRepeat);
			preferences.setShuffle(isShuffle);
			break;
		case R.id.repeat:
			if (isRepeat) {
				isRepeat = false;
				repeatButton.setImageResource(R.drawable.ic_repeat);
			} else {
				isRepeat = true;
				repeatButton.setImageResource(R.drawable.ic_repeatstate);
				shuffle_button.setImageResource(R.drawable.ic_shuffle);
				isShuffle = false;
			}
			preferences.setRepeat(isRepeat);
			preferences.setShuffle(isShuffle);
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
	 * @param id
	 */
	public void updateView(String name, String artist, String album, long id) {

		preferences.setName(name, artist, album);
		preferences.setId(id);
		title_view.setText(preferences.getTitle());
		artist_view.setText(preferences.getArtist());
		album_view.setText(preferences.getAlbum());
		long album_id = id;
		updateAlbumArt();

	}

	private void updateAlbumArt() {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bitmap bitmap = MediaManager.getAlbumArt(preferences.getId(),
						getActivity());

				if (bitmap != null)
					album_art_view.setImageBitmap(bitmap);
				else
					album_art_view.setImageResource(R.drawable.ic_album_art);
			}
		});
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		if (fromUser) {
			int seekTo = seekBar.getProgress();
			mCallBack.seekTo(seekTo);
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	public void updateSeekBar(int maxDuration, int progress) {

		seekbar.setMax(maxDuration);
		current_time.setText(updateText(progress));
		seekbar.setProgress(progress);
		total_duration.setText(updateText(maxDuration));
	}

	public static String updateText(long milliseconds) {
		String finalTimeString = "";
		String secondsString = "";

		int hours = (int) (milliseconds / (1000 * 60 * 60));
		int minutes = (int) ((milliseconds % (1000 * 60 * 60)) / (1000 * 60));
		int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

		if (hours > 0)
			finalTimeString = hours + ":";

		if (seconds < 10)
			secondsString = "0" + seconds;
		else
			secondsString = "" + seconds;

		finalTimeString = finalTimeString + minutes + ":" + secondsString;

		return finalTimeString;
	}

	public void updateIcon(boolean isPlaying) {
		play_button.setImageResource(isPlaying ? R.drawable.ic_pause
				: R.drawable.ic_play);
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		preferences.updateWidget(true);
	}

}
