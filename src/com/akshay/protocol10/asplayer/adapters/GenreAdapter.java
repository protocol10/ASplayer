package com.akshay.protocol10.asplayer.adapters;

import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class GenreAdapter extends ArrayAdapter<HashMap<String, Object>> {

	Context context;
	int resource;
	int textViewResourceId;
	List<HashMap<String, Object>> list;

	public GenreAdapter(Context context, int resource, int textViewResourceId,
			List<HashMap<String, Object>> list) {
		super(context, resource, textViewResourceId, list);
		this.context = context;
		this.resource = resource;
		this.textViewResourceId = textViewResourceId;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		GenreHolder holder = null;
		if (view == null) {

			view = inflater.inflate(R.layout.genre_row, parent, false);
			holder = new GenreHolder(view);
			view.setTag(holder);

		} else {
			holder = (GenreHolder) view.getTag();
		}
		String genreName = list.get(position).get("genre").toString();
		holder.genreName.setText(genreName);
		return view;
	}

	public class GenreHolder {
		TextView genreName;

		public GenreHolder(View view) {
			// TODO Auto-generated constructor stub
			genreName = (TextView) view.findViewById(R.id.genre_name);
		}
	}
}
