package com.akshay.protocol10.asplayer.adapters;

import java.util.HashMap;
import java.util.List;

import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.fragments.ControlsFragments;
import com.akshay.protocol10.asplayer.utils.ASUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class TrackAdapters extends ArrayAdapter<HashMap<String, Object>> {

	Context context;
	int resource;
	int textViewResourceId;
	List<HashMap<String, Object>> list;

	public TrackAdapters(Context context, int resource, int textViewResourceId,
			List<HashMap<String, Object>> list) {
		super(context, resource, textViewResourceId, list);
		this.context = context;
		this.resource = resource;
		this.textViewResourceId = textViewResourceId;
		this.list = list;
	}

	/**
	 * Optimizing Technique using ViewHolder pattern improving the performance
	 * of list. Responsible for displaying the data in the ListView in
	 * TracksFragment View parameter points the Relative Layout of the
	 * tracks_row.xml
	 * */

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			LayoutInflater inflator = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflator.inflate(R.layout.tracks_row, parent, false);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		String title = (String) list.get(position).get(ASUtils.TITLE_KEY);
		String duration = (String) list.get(position).get(ASUtils.DURATION_KEY);
		String album = (String) list.get(position).get(ASUtils.ALBUM_KEY);
		String artist = (String) list.get(position).get(ASUtils.ARTIST_KEY);

		holder.title__text_view.setText(title);
		holder.duration_text_view.setText(ControlsFragments.updateText(Integer
				.parseInt(duration)));
		holder.artist__text_view.setText(artist);
		holder.album_text_view.setText(album);

		return view;
	}

	/**
	 * @author akshay Best practice use ViewHolder as per android Design
	 *         GuideLines
	 */

	static class ViewHolder {
		TextView title__text_view, artist__text_view, album_text_view,
				duration_text_view;

		public ViewHolder(View view) {
			// TODO Auto-generated constructor stub
			title__text_view = (TextView) view
					.findViewById(R.id.title_text_view);
			duration_text_view = (TextView) view
					.findViewById(R.id.duration_text_view);
			album_text_view = (TextView) view
					.findViewById(R.id.album_text_view);
			artist__text_view = (TextView) view
					.findViewById(R.id.artist_text_view);
		}
	}

}
