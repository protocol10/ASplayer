package com.akshay.protocol10.asplayer.fragments;

/**
 * @author akshay
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.adapters.AlbumSampleAdapter;
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
import android.widget.GridView;

public class AlbumsFragment extends Fragment implements OnItemClickListener {

	View view;
	List<HashMap<String, Object>> album_list;
	MediaManager manager;

	GridView gridView;
	AlbumSampleAdapter adapter;
	onItemSelected mcallBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		manager = new MediaManager();
		album_list = new ArrayList<HashMap<String, Object>>();
		album_list = manager.retriveAlbum(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.album_layout, container, false);

		gridView = (GridView) view.findViewById(R.id.gridView1);
		adapter = new AlbumSampleAdapter(getActivity(), R.layout.album_layout,
				R.id.album_name_text_view, album_list);

		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);

		return view;
	}

	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		mcallBack = (onItemSelected) getActivity();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		String name = album_list.get(position).get(ASUtils.ALBUM_KEY)
				.toString();

		mcallBack.updateArtistAlbum(name, 0);

	}
}
