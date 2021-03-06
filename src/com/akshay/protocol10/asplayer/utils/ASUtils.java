package com.akshay.protocol10.asplayer.utils;

import com.akshay.protocol10.asplayer.R;

public class ASUtils {

	/* VALUES FOR LIST IN NAVIGATION DRAWER */
	private final static String OPTION_HOME = "Home";

	private final static String OPTION_EQUALIZER = "Equalizer";

	private final static String OPTION_ABOUT = "About";

	/* OPTIONS FOR NAVIGATION DRAWER */
	public static final String[] options = { OPTION_HOME, OPTION_EQUALIZER,
			OPTION_ABOUT };

	/* ICONS FOR LIST IN NAVIGATION DRAWER */
	public static final int[] options_image = { R.drawable.ic_action_home,
			R.drawable.ic_action_equalizer, R.drawable.ic_about };

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
	private static final String CUSTOM = "Custom";

	public final static String[] DEFAULT_PRESETS = { NORMAL_PRESET,
			CLASSIC_PRESET, DANCE_PRESET, FLAT_PRESET, FOLK_PRESET,
			HEAVYMETAL_PRESET, HIP_HOP_PRESET, JAZZ_PRESET, POP_PRESET,
			ROCK_PRESET, CUSTOM };

	/* KEYS FOR BUNDLE ELEMENTS */
	public static final String TITLE_KEY = "title";

	public static final String NAME_KEY = "name";

	public static final String ARTIST_KEY = "artist";

	public static final String ARTIST_ID_KEY = "artist_id";

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

	public static final String TRACKS_TAGS = "TRACKS";

	public static final String OTHER_TAGS = "OTHERS";

	public static final String IS_PLAYING = "isPlaying";

	private static final String REVERB_LARGEHALL = "Large Hall";

	private static final String REVERB_LARGEROOM = "Large Room";

	private static final String REVERB_MEDIUMHALL = "Medium Hall";

	private static final String REVERB_MEDIUMROOM = "Medium Room";

	private static final String REVERB_NONE = "None";

	private static final String REVERB_PLATE = "Plate";

	private static final String REVERB_SMALLROOM = "Small Room";

	public static final String[] PRESET_REVERBS = { REVERB_LARGEHALL,
			REVERB_LARGEROOM, REVERB_MEDIUMHALL, REVERB_MEDIUMROOM,
			REVERB_NONE, REVERB_PLATE, REVERB_SMALLROOM };

	/**
	 * Used for conversion for proper time format
	 * 
	 * @param milliseconds
	 * @return
	 */
	public static String updateText(long milliseconds) {
		String finalTimeString = "";
		String secondsString = "";

		int hours = (int) (milliseconds / (1000 * 60 * 60));
		int minutes = (int) ((milliseconds % (1000 * 60 * 60)) / (1000 * 60));
		int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);

		if (hours > 0)
			finalTimeString = hours + ":";

		if (seconds < 10)
			secondsString = "0" + seconds;
		else
			secondsString = "" + seconds;

		finalTimeString = finalTimeString + minutes + ":" + secondsString;

		return finalTimeString;
	}

}
