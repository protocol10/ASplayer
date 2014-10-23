package com.akshay.protocol10.asplayer.adapters;

/**
 * @author akshay
 */
import com.akshay.protocol10.asplayer.R;
import com.akshay.protocol10.asplayer.utils.ASUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DrawerAdapter extends ArrayAdapter<String> {

	Context context;
	int resource;
	int textViewResourceId;
	String[] objects;

	public DrawerAdapter(Context context, int resource, int textViewResourceId,
			String[] objects) {
		super(context, resource, textViewResourceId, objects);
		this.context = context;
		this.resource = resource;
		this.textViewResourceId = textViewResourceId;
		this.objects = objects;

		// TODO Auto-generated constructor stub.
	}

	/**
	 * Responsible for displaying the data in the ListView in drawerList View
	 * parameter points the Relative Layout of the drawer_list_row.xml
	 */

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = convertView;
		ViewHolder holder = null;
		if (view == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			view = inflater.inflate(R.layout.drawer_list_row, parent, false);
			holder = new ViewHolder(view);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.icon.setImageResource(ASUtils.options_image[position]);
		holder.text.setText(objects[position]);

		return view;
	}

	/**
	 * Optimization technique used for improving the performance of list as per
	 * the design Guide Lines
	 *
	 */

	static class ViewHolder {

		ImageView icon;
		TextView text;

		public ViewHolder(View view) {
			// TODO Auto-generated constructor stub
			icon = (ImageView) view.findViewById(R.id.imageView1);
			text = (TextView) view.findViewById(R.id.option_text);
		}
	}
}