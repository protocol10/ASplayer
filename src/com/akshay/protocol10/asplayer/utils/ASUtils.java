package com.akshay.protocol10.asplayer.utils;

import com.akshay.protocol10.asplayer.R;

public class ASUtils {

	/* VALUES FOR LIST IN NAVIGATION DRAWER */
	private final static String OPTION_HOME = "Home";

	private final static String OPTION_EQUALIZER = "Equalizer";

	// private final static String OPTION_PLAYLIST = "Playlist";

	private final static String OPTION_SETTINGS = "Settings";

	private final static String OPTION_ABOUT = "About";

	/* OPTIONS FOR NAVIGATION DRAWER */
	public static final String[] options = { OPTION_HOME, OPTION_EQUALIZER,
			OPTION_SETTINGS, OPTION_ABOUT };

	/* ICONS FOR LIST IN NAVIGATION DRAWER */
	public static final int[] options_image = { R.drawable.ic_action_home,
			R.drawable.ic_action_equalizer, R.drawable.ic_action_settings,
			R.drawable.ic_about };

	private final static String NORMAL_PRESET = "Normal";
	private final static String CLASSIC_PRESET = "Classic";
	private final static String DANCE_PRESET = "Dance";
	private final static String FLAT_PRESET = "Flat";
	private final static String FOLK_PRESET = "Folk";
	private final static String HEAVYMETAL_PRESET = "Heavy Metal";
	private static final String HIP_HOP_PRESET = "Hip Hop";
	private final static String JAZZ_PRESET = "Jazz";
	private final static String POP_PRESET = "Pop";
	private final static String ROCK_PRESET = "Rock";

	public final static String[] DEFAULT_PRESETS = { NORMAL_PRESET,
			CLASSIC_PRESET, DANCE_PRESET, FLAT_PRESET, FOLK_PRESET,
			HEAVYMETAL_PRESET, HIP_HOP_PRESET, JAZZ_PRESET, POP_PRESET,
			ROCK_PRESET };

	/* KEYS FOR BUNDLE ELEMENTS */
	public static final String TITLE_KEY = "title";

	public static final String NAME_KEY = "name";

	public static final String ARTIST_KEY = "artist";

	public static final String ALBUM_KEY = "album";

	public static final String ALBUM_ID_KEY = "album_art";

	public static final String POSITION_KEY = "position";

	public static final String ALBUM_ART = "album_art";

	public static final String ARTIST_COUNT = "artist_count";

	public static final String DURATION_KEY = "duration";

	public static final String PAGE_SLIDER_TAG = "Home";

	public static final String EQAULIZER_TAG = "Equalizer";

	public static final String ALBUM_TAG = "ALBUMSTAG";

	public static final String ARTIST_TAG = "ARTISTTAG";

	public static final String GENRE_TAG = "GENRETAG";

	public static final String CONTROL_TAG = "CONTROLTAG";

	public static final int NOTIFICATION_ID = 25;

	public static final String MAX_DURATION = "totalduartion";

	public static final String CURRENT_POSITION = "currentPosition";

	public static final String ID_KEY = "id";
}
