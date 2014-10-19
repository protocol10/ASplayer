package com.akshay.protocol10.asplayer.fragments;

import com.akshay.protocol10.asplayer.R;

import com.akshay.protocol10.asplayer.utils.ASUtils;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class EqualizerFragment extends Fragment {

	Spinner presestSpinner;

	ArrayAdapter<String> adapter;
	String[] defaultPresets;

	public EqualizerFragment() {
		// TODO Auto-generated constructor stub
		defaultPresets = ASUtils.DEFAULT_PRESETS;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.fragment_equalizer, container,
				false);
		presestSpinner = (Spinner) view.findViewById(R.id.equalizer_preset);
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, defaultPresets);
		presestSpinner.setAdapter(adapter);
		return view;
	}
}
