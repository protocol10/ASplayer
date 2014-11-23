package com.akshay.protocol10.asplayer.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.adapters.TrackAdapters;
import com.akshay.protocol10.asplayer.callbacks.onItemSelected;
import com.akshay.protocol10.asplayer.database.MediaManager;
import com.akshay.protocol10.asplayer.database.Preferences;
import com.akshay.protocol10.asplayer.utils.ASUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ListView;

public class AlbumSongsFragment extends Fragment implements OnItemClickListener {

	List<HashMap<String, Object>> albums_media;
	MediaManager manager;
	TrackAdapters adapters;
	View view;
	ListView albums_media_view;
	long artist_id = 0;
	onItemSelected mcallback;
	String key = null;
	FrameLayout.LayoutParams layoutParams;

	public AlbumSongsFragment() {
		albums_media = new ArrayList<HashMap<String, Object>>();
		manager = new MediaManager();
	}

	@Override
	public void onStart() {
		super.onStart();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Bundle args = getArguments();
		if (args != null) {
			String name = args.getString(ASUtils.NAME_KEY);
			artist_id = args.getLong("artist_id");
			if (artist_id != 0) {
				albums_media = manager.retriveTracks(getActivity(), name,
						artist_id);
				key = ASUtils.ALBUM_ART;
			} else {
				albums_media = manager.retriveContent(getActivity(), name);
				key = ASUtils.ALBUM_ID_KEY;
			}

		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_tracks, container, false);
		albums_media_view = (ListView) view.findViewById(R.id.tracks_view);
		adapters = new TrackAdapters(getActivity(), R.layout.fragment_tracks,
				R.id.title_text_view, albums_media);
		albums_media_view.setAdapter(adapters);
		layoutParams = (LayoutParams) albums_media_view.getLayoutParams();
		layoutParams.topMargin = new Preferences(getActivity()).getHeight();
		albums_media_view.setLayoutParams(layoutParams);
		albums_media_view.setLayoutParams(layoutParams);
		albums_media_view.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		mcallback = (onItemSelected) getActivity();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		int index = position;
		String title = albums_media.get(position).get(ASUtils.TITLE_KEY)
				.toString();
		String album = albums_media.get(position).get(ASUtils.ALBUM_KEY)
				.toString();
		String artist = albums_media.get(position).get(ASUtils.ARTIST_KEY)
				.toString();
		long album_id = (Long) albums_media.get(position)
				.get(ASUtils.ALBUM_ART);
		mcallback.setTag(ASUtils.OTHER_TAGS);
		mcallback.updateList(albums_media);
		mcallback.updateView(title, artist, album, index, album_id);
	}

}
