package com.akshay.protocol10.asplayer.fragments;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.callbacks.onItemSelected;
import com.akshay.protocol10.asplayer.utils.ASUtils;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;

public class EqualizerFragment extends Fragment implements
		OnSeekBarChangeListener {

	Spinner presestSpinner, reverbSpinner;

	ArrayAdapter<String> adapter, reverbAdapter;
	String[] defaultPresets;

	SeekBar frequency1, frequency2, frequency3, frequency4, frequency5;
	int progressBand1, progressBand2, progressBand3, progressBand4,
			progressBand5;
	onItemSelected mcallback;
	String[] defaultReverbs;
	int index = 15;

	public EqualizerFragment() {
		// TODO Auto-generated constructor stub
		defaultPresets = ASUtils.DEFAULT_PRESETS;
		defaultReverbs = ASUtils.PRESET_REVERBS;
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
		reverbSpinner = (Spinner) view.findViewById(R.id.presetReverb);
		reverbAdapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, defaultReverbs);
		reverbSpinner.setAdapter(reverbAdapter);

		frequency1 = (SeekBar) view.findViewById(R.id.equalizer_band1);
		frequency2 = (SeekBar) view.findViewById(R.id.equalizer_band2);
		frequency3 = (SeekBar) view.findViewById(R.id.equalizer_band3);
		frequency4 = (SeekBar) view.findViewById(R.id.equalizer_band4);
		frequency5 = (SeekBar) view.findViewById(R.id.equalizer_band5);

		frequency1.setOnSeekBarChangeListener(this);
		frequency2.setOnSeekBarChangeListener(this);
		frequency3.setOnSeekBarChangeListener(this);
		frequency4.setOnSeekBarChangeListener(this);
		frequency5.setOnSeekBarChangeListener(this);
		presestSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				selectPreset(position);
				mcallback.selectPreset(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub

			}
		});
		reverbSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mcallback.setPresetReverb(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				mcallback.setPresetReverb(5);
			}
		});
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mcallback = (onItemSelected) activity;
	}

	/**
	 * 
	 * @param position
	 *            Temporary Fix. Change in method in next update
	 */
	private void selectPreset(int position) {
		// TODO Auto-generated method stub
		switch (position) {
		case 0:
			progressBand1 = 18;
			progressBand2 = 15;
			progressBand3 = 15;
			progressBand4 = 15;
			progressBand5 = 18;
			setBandProgress(progressBand1, progressBand2, progressBand3,
					progressBand4, progressBand5);

			break;
		case 1:
			progressBand1 = 20;
			progressBand2 = 18;
			progressBand3 = 13;
			progressBand4 = 19;
			progressBand5 = 19;
			setBandProgress(progressBand1, progressBand2, progressBand3,
					progressBand4, progressBand5);
			break;
		case 2:
			progressBand1 = 21;
			progressBand2 = 15;
			progressBand3 = 17;
			progressBand4 = 19;
			progressBand5 = 17;
			setBandProgress(progressBand1, progressBand2, progressBand3,
					progressBand4, progressBand5);
			break;
		case 3:
			progressBand1 = 15;
			progressBand2 = 15;
			progressBand3 = 15;
			progressBand4 = 15;
			progressBand5 = 15;
			setBandProgress(progressBand1, progressBand2, progressBand3,
					progressBand4, progressBand5);
			break;
		case 4:
			progressBand1 = 18;
			progressBand2 = 15;
			progressBand3 = 15;
			progressBand4 = 17;
			progressBand5 = 14;
			setBandProgress(progressBand1, progressBand2, progressBand3,
					progressBand4, progressBand5);
			break;
		case 5:
			progressBand1 = 19;
			progressBand2 = 16;
			progressBand3 = 24;
			progressBand4 = 18;
			progressBand5 = 15;
			setBandProgress(progressBand1, progressBand2, progressBand3,
					progressBand4, progressBand5);
			break;
		case 6:
			progressBand1 = 20;
			progressBand2 = 18;
			progressBand3 = 15;
			progressBand4 = 16;
			progressBand5 = 18;
			setBandProgress(progressBand1, progressBand2, progressBand3,
					progressBand4, progressBand5);
			break;
		case 7:
			progressBand1 = 19;
			progressBand2 = 17;
			progressBand3 = 13;
			progressBand4 = 17;
			progressBand5 = 15;
			setBandProgress(progressBand1, progressBand2, progressBand3,
					progressBand4, progressBand5);
			break;
		case 8:
			progressBand1 = 16;
			progressBand2 = 17;
			progressBand3 = 20;
			progressBand4 = 16;
			progressBand5 = 13;
			setBandProgress(progressBand1, progressBand2, progressBand3,
					progressBand4, progressBand5);
			break;
		case 9:
			progressBand1 = 20;
			progressBand2 = 18;
			progressBand3 = 14;
			progressBand4 = 18;
			progressBand5 = 20;
			setBandProgress(progressBand1, progressBand2, progressBand3,
					progressBand4, progressBand5);
			break;
		default:
			break;
		}
	}

	private void setBandProgress(int progressBand1, int progressBand2,
			int progressBand3, int progressBand4, int progressBand5) {
		// TODO Auto-generated method stub
		frequency1.setProgress(progressBand1);
		frequency2.setProgress(progressBand2);
		frequency3.setProgress(progressBand3);
		frequency4.setProgress(progressBand4);
		frequency5.setProgress(progressBand5);

	}

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		// TODO Auto-generated method stub
		int value = 0;
		if (fromUser) {
			presestSpinner.setSelection(defaultPresets.length - 1);
			switch (seekBar.getId()) {
			case R.id.equalizer_band1:
				value = progress - index;
				mcallback.setEqualizer(0, value);
				break;
			case R.id.equalizer_band2:
				value = progress - index;
				mcallback.setEqualizer(1, value);
				break;
			case R.id.equalizer_band3:
				value = progress - index;
				mcallback.setEqualizer(2, value);
				break;
			case R.id.equalizer_band4:
				value = progress - index;
				mcallback.setEqualizer(3, value);
				break;
			case R.id.equalizer_band5:
				value = progress - index;
				mcallback.setEqualizer(4, value);
				break;
			default:
				break;
			}
		}
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub

	}

}
