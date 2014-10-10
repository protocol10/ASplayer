package com.akshay.protocol10.asplayer.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.adapters.GenreAdapter;
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

public class GenreFragment extends Fragment implements OnItemClickListener {

	MediaManager manager;
	List<HashMap<String, Object>> genreList;
	GenreAdapter adapter;
	ListView genreListView;

	public GenreFragment() {
		// Required empty public constructor
		manager = new MediaManager();
		genreList = new ArrayList<HashMap<String, Object>>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		genreList = manager.retriveGenre(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view;
		view = inflater.inflate(R.layout.fragment_genre, container, false);
		genreListView = (ListView) view.findViewById(R.id.genre_list);
		adapter = new GenreAdapter(getActivity(), R.layout.fragment_genre,
				R.id.genre_name, genreList);
		genreListView.setAdapter(adapter);
		genreListView.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

	}

	@Override
	public void onDetach() {
		super.onDetach();

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		long genre_id =  (Long) genreList.get(position).get(ASUtils.ID_KEY);
		manager.retriveGenreAlbum(getActivity(), genre_id);
	}
}
