package com.akshay.protocol10.asplayer.utils;

import com.akshay.protocol10.asplayer.R;

public class ASUtils {

	/* VALUES FOR LIST IN NAVIGATION DRAWER */
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

	/* KEYS FOR BUNDLE ELEMENTS */
	public static final String TITLE_KEY = "title";

	public static final String NAME_KEY = "name";

	public static final String ARTIST_KEY = "artist";

	public static final String ALBUM_KEY = "album";

	public static final String ALBUM_ID_KEY = "album_id";

	public static final String POSITION_KEY = "position";

	public static final String ALBUM_ART = "album_art";

	public static final String ARTIST_COUNT = "artist_count";

	public static final String DURATION_KEY = "duration";

}
