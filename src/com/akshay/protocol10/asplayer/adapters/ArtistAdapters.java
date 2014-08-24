package com.akshay.protocol10.asplayer.adapters;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

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
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return super.getView(position, convertView, parent);
	}
	

}
