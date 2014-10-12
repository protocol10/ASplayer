package com.akshay.protocol10.asplayer.fragments;

import java.util.ArrayList;
import java.util.List;

import com.activeandroid.query.Select;
import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.adapters.EqualizerAdapter;
import com.akshay.protocol10.asplayer.database.models.PresetModel;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;

public class EqualizerFragment extends Fragment {

	Spinner presestSpinner;

	List<PresetModel> preset;
	ArrayList<String> default_presets;
	EqualizerAdapter adapter;

	public EqualizerFragment() {
		// TODO Auto-generated constructor stub
		default_presets = new ArrayList<String>();
		preset = new ArrayList<PresetModel>();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		new LoadPresetAsync().execute();
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

		return view;
	}

	class LoadPresetAsync extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			preset = new Select().from(PresetModel.class).execute();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			adapter = new EqualizerAdapter(getActivity(),
					(ArrayList<PresetModel>) preset);
			presestSpinner.setAdapter(adapter);
		}
	}
}
