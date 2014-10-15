package com.akshay.protocol10.asplayer.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.adapters.TrackAdapters;
import com.akshay.protocol10.asplayer.callbacks.onItemSelected;
import com.akshay.protocol10.asplayer.database.MediaManager;
import com.akshay.protocol10.asplayer.utils.ASUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TracksFragment extends Fragment implements OnItemClickListener {

	ListView tracks_list_view;
	View view;
	MediaManager mediaManager;
	List<HashMap<String, Object>> tracks_list_data;
	TrackAdapters adapters;

	onItemSelected mcallback;

	// Empty constructor good practice
	public TracksFragment() {
		// TODO Auto-generated constructor stub
		mediaManager = new MediaManager();

		tracks_list_data = new ArrayList<HashMap<String, Object>>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		tracks_list_data = mediaManager.retriveContent(getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_tracks, container, false);
		tracks_list_view = (ListView) view.findViewById(R.id.tracks_view);
		adapters = new TrackAdapters(getActivity(), R.layout.fragment_tracks,
				R.id.title_text_view, tracks_list_data);
		tracks_list_view.setAdapter(adapters);
		tracks_list_view.setFastScrollEnabled(true);
		tracks_list_view.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mcallback = (onItemSelected) activity;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		int index = position;
		String title = tracks_list_data.get(position).get(ASUtils.TITLE_KEY)
				.toString();
		String album = tracks_list_data.get(position).get(ASUtils.ALBUM_KEY)
				.toString();
		String artist = tracks_list_data.get(position).get(ASUtils.ARTIST_KEY)
				.toString();
		long album_id = (Long) tracks_list_data.get(position).get(
				ASUtils.ALBUM_ART);
		mcallback.setTag(ASUtils.TRACKS_TAGS);
		mcallback.updateView(title, artist, album, index, album_id);

	}
}
