package com.akshay.protocol10.asplayer.fragments;

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
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ArtistAlbum extends Fragment implements OnItemClickListener {

	List<HashMap<String, Object>> album_list;

	GridView gridView;
	View view;

	onItemSelected mcallBack;
	MediaManager manager;

	AlbumSampleAdapter adapter;

	public ArtistAlbum() {
		// TODO Auto-generated constructor stub
		manager = new MediaManager();
		album_list = new ArrayList<HashMap<String, Object>>();
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
		gridView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle bundle = getArguments();
		if (bundle != null) {
			long name = bundle.getLong("artist_id");
			album_list = manager.retriveArtistContent(getActivity(), name);
		}
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mcallBack = (onItemSelected) getActivity();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		String name = album_list.get(position).get(ASUtils.ALBUM_KEY)
				.toString();
		mcallBack.UpdateView(name);
	}
}
