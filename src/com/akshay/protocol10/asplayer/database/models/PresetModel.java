package com.akshay.protocol10.asplayer.database.models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Preset")
public class PresetModel extends Model {

	@Column(name = "presetName")
	public String preset;
	public boolean readOnly;

	public PresetModel() {
		// TODO Auto-generated constructor stub
		super();
	}

	public PresetModel(String preset) {
		super();
		this.preset = preset;
	}

}
