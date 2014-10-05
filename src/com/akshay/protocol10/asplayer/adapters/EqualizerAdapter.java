package com.akshay.protocol10.asplayer.adapters;

import java.util.ArrayList;
import java.util.List;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.database.models.PresetModel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class EqualizerAdapter extends BaseAdapter {

	Context context;

	List<String> objects;
	ArrayList<PresetModel> model;

	public EqualizerAdapter(Context context, ArrayList<PresetModel> model) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.model = model;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		EqualizerHolder holder = null;
		View view = convertView;
		if (view == null) {
			LayoutInflater inflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflator.inflate(R.layout.equalizer_row, parent, false);
			holder = new EqualizerHolder(view);
			view.setTag(holder);
		} else {
			holder = (EqualizerHolder) view.getTag();
		}
		PresetModel presetModel = model.get(position);
		holder.preset_name.setText(presetModel.preset.toString());
		return view;
	}

	private class EqualizerHolder {
		TextView preset_name;

		EqualizerHolder(View view) {
			// TODO Auto-generated constructor stub
			preset_name = (TextView) view.findViewById(R.id.preset_name);
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return model.size();
	}

	@Override
	public PresetModel getItem(int position) {
		// TODO Auto-generated method stub
		return model.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

}
