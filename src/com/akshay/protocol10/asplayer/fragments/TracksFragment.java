package com.akshay.protocol10.asplayer.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.adapters.TrackAdapters;
import com.akshay.protocol10.asplayer.database.MediaManager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TracksFragment extends Fragment {

	ListView tracks_list_view;
	View view;
	MediaManager mediaManager;
	List<HashMap<String, Object>> tracks_list_data;
	TrackAdapters adapters;

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
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
	}

}
