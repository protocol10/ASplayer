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

public class AlbumsAdapter extends ArrayAdapter<HashMap<String, Object>> {

	Context context;
	int resource;
	int textViewId;
	List<HashMap<String, Object>> list;

	public AlbumsAdapter(Context context, int resource, int textViewId,
			List<HashMap<String, Object>> list) {
		super(context, resource, textViewId, list);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.resource = resource;
		this.textViewId = textViewId;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.album_row, parent, false);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.album_text.setText(list.get(position).toString());
		return view;
	}

	class ViewHolder {
		TextView album_text;

		public ViewHolder(View view) {
			// TODO Auto-generated constructor stub
			album_text = (TextView) view
					.findViewById(R.id.album_name_text_view);
		}
	}
}
