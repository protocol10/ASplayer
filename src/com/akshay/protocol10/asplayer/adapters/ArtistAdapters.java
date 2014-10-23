package com.akshay.protocol10.asplayer.adapters;

/**
 * @author akshay
 */
import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.utils.ASUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ArtistAdapters extends ArrayAdapter<HashMap<String, Object>> {

	Context context;
	int resource;
	int textViewResourceId;
	List<HashMap<String, Object>> objects;

	public ArtistAdapters(Context context, int resource,
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
		ArtistViewHolder holder = null;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.artist_row, parent, false);
			holder = new ArtistViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ArtistViewHolder) view.getTag();
		}

		String album_name = objects.get(position).get(ASUtils.ARTIST_KEY)
				.toString();
		String no_of_albums = objects.get(position).get(ASUtils.ARTIST_COUNT)
				.toString()
				+ " albums";

		holder.artist_name.setText(album_name);
		holder.no_of_album.setText(no_of_albums);

		return view;
	}

	static class ArtistViewHolder {

		TextView artist_name, no_of_album;

		public ArtistViewHolder(View view) {
			// TODO Auto-generated constructor stub
			artist_name = (TextView) view.findViewById(R.id.artist_name);
			no_of_album = (TextView) view.findViewById(R.id.no_albums);
		}
	}

}
