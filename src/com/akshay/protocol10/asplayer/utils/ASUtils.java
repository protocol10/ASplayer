package com.akshay.protocol10.asplayer.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.akshay.protocol10.asplayer.R;

public class ASUtils {

	private final static String OPTION_HOME = "Home";

	private final static String OPTION_EQUALIZER = "Equalizer";

	private final static String OPTION_PLAYLIST = "Playlist";

	private final static String OPTION_SETTINGS = "Settings";

	private final static String OPTION_ABOUT = "About";

	public static final String[] options = { OPTION_HOME, OPTION_EQUALIZER,
			OPTION_PLAYLIST, OPTION_SETTINGS, OPTION_ABOUT };

	public static final int[] options_image = { R.drawable.ic_action_home,
			R.drawable.ic_action_equalizer, R.drawable.ic_drawer,
			R.drawable.ic_action_settings, R.drawable.ic_about };

	public Bitmap getCoverArt(String path) {
		Bitmap bitmap = BitmapFactory.decodeFile(path);
		return bitmap;
	}

}
