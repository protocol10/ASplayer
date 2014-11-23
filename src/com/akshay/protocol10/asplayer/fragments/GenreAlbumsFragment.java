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
import android.widget.GridView;
import android.widget.RelativeLayout.LayoutParams;

public class GenreAlbumsFragment extends Fragment implements
		OnItemClickListener {

	View view;
	List<HashMap<String, Object>> album_list;
	MediaManager manager;
	RelativeLayout.LayoutParams layoutParams;
	GridView gridView;
	AlbumSampleAdapter adapter;
	onItemSelected mcallBack;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		album_list = new ArrayList<HashMap<String, Object>>();
		manager = new MediaManager();
		Bundle bundle = getArguments();
		if (bundle != null) {
			long id = bundle.getLong("genre_id");
			album_list = manager.retriveGenreAlbum(getActivity(), id);
		}

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.album_layout, container, false);

		gridView = (GridView) view.findViewById(R.id.gridView1);
		adapter = new AlbumSampleAdapter(getActivity(), R.layout.album_layout,
				R.id.album_name_text_view, album_list);
		layoutParams = (LayoutParams) gridView.getLayoutParams();
		layoutParams.topMargin = new Preferences(getActivity()).getHeight();
		gridView.setLayoutParams(layoutParams);
		gridView.setAdapter(adapter);
		gridView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		long artistId = (Long) album_list.get(position).get("artist_id");
		String albumName = album_list.get(position).get(ASUtils.ALBUM_KEY)
				.toString();
		mcallBack.updateArtistAlbum(albumName, artistId);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mcallBack = (onItemSelected) activity;
	}
}
