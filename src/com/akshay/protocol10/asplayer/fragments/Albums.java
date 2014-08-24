package com.akshay.protocol10.asplayer.fragments;

/**
 * @author akshay
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.adapters.AlbumSampleAdapter;
import com.akshay.protocol10.asplayer.database.MediaManager;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

public class Albums extends Fragment {

	View view;
	List<HashMap<String, Object>> album_list;
	MediaManager manager;

	GridView gridView;
	AlbumSampleAdapter adapter;

	public Albums() {
		// TODO Auto-generated constructor stub
		manager = new MediaManager();
		album_list = new ArrayList<HashMap<String, Object>>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		album_list = manager.retriveAlbum(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.album_layout, container, false);

		gridView = (GridView) view.findViewById(R.id.gridView1);
		adapter = new AlbumSampleAdapter(getActivity(), R.layout.album_layout,
				R.id.album_name_text_view, album_list);

		gridView.setAdapter(adapter);
		return view;
	}
}
