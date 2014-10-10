package com.akshay.protocol10.asplayer.fragments;

/**
 * @author akshay
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.adapters.ArtistAdapters;
import com.akshay.protocol10.asplayer.callbacks.onItemSelected;
import com.akshay.protocol10.asplayer.database.MediaManager;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ArtistFragment extends Fragment implements OnItemClickListener {

	View view;
	List<HashMap<String, Object>> artist_list;
	MediaManager manager;
	ListView artist_list_view;
	ArtistAdapters adapters;

	onItemSelected mcallBack;

	public ArtistFragment() {
		// Required empty public constructor
		artist_list = new ArrayList<HashMap<String, Object>>();
		manager = new MediaManager();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		artist_list = manager.retriveArtist(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		view = inflater.inflate(R.layout.fragment_artist, container, false);

		artist_list_view = (ListView) view.findViewById(R.id.artist_list);
		adapters = new ArtistAdapters(getActivity(), R.layout.fragment_artist,
				R.id.artist_name, artist_list);
		artist_list_view.setAdapter(adapters);
		artist_list_view.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mcallBack = (onItemSelected) getActivity();

	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		long artist_id = (Long) artist_list.get(position).get("id");
		mcallBack.selectArtist(artist_id);
	}

}
