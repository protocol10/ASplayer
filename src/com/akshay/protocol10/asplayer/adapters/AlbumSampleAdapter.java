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

public class AlbumSampleAdapter extends ArrayAdapter<HashMap<String, Object>> {

	Context context;
	int resource;
	int textViewResourceId;
	List<HashMap<String, Object>> objects;

	public AlbumSampleAdapter(Context context, int resource,
			int textViewResourceId, List<HashMap<String, Object>> objects) {
		super(context, resource, textViewResourceId, objects);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resource = resource;
		this.textViewResourceId = textViewResourceId;
		this.objects = objects;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		AlbumHolder holder = null;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.album_new_row, parent, false);
			holder = new AlbumHolder(view);
			view.setTag(holder);
		} else {
			holder = (AlbumHolder) view.getTag();
		}
		String album_name = objects.get(position).get("album").toString();
		String artist_name = objects.get(position).get("artist").toString();
		holder.textview.setText(album_name);
		holder.artistTextView.setText(artist_name);

		return view;
	}

	static class AlbumHolder {
		TextView textview;
		TextView artistTextView;

		public AlbumHolder(View view) {
			// TODO Auto-generated constructor stub
			textview = (TextView) view.findViewById(R.id.album_name_text_view);
			artistTextView = (TextView) view
					.findViewById(R.id.album_artist_text_view);

		}
	}
}
