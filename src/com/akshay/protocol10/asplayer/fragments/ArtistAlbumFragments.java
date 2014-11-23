package com.akshay.protocol10.asplayer.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.adapters.AlbumSampleAdapter;
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
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.GridView;

public class ArtistAlbumFragments extends Fragment implements
		OnItemClickListener {

	List<HashMap<String, Object>> album_list;
	RelativeLayout.LayoutParams layoutParams;
	GridView gridView;
	View view;

	onItemSelected mcallBack;
	MediaManager manager;

	AlbumSampleAdapter adapter;
	long artist_id;

	public ArtistAlbumFragments() {

		manager = new MediaManager();
		album_list = new ArrayList<HashMap<String, Object>>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.album_layout, container, false);
		gridView = (GridView) view.findViewById(R.id.gridView1);
		layoutParams = (LayoutParams) gridView.getLayoutParams();
		layoutParams.topMargin = new Preferences(getActivity()).getHeight();
		gridView.setLayoutParams(layoutParams);
		adapter = new AlbumSampleAdapter(getActivity(), R.layout.album_layout,
				R.id.album_name_text_view, album_list);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			artist_id = bundle.getLong("artist_id");
			album_list = manager.retriveArtistContent(getActivity(), artist_id);
		}
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
		mcallBack.updateArtistAlbum(name, artist_id);
	}
}
