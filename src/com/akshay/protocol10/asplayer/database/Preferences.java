package com.akshay.protocol10.asplayer.database;

/**
 * @author akshay
 */
import com.akshay.protocol10.asplayer.service.MediaServiceContoller;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

	Context context;
	SharedPreferences settings;
	SharedPreferences.Editor editor;
	private static final String PREF_NAME = "Settings";
	private static final String DATABASE = "DATABASE_KEY";
	public static final String UPDATE_NOWPLAYING = "NOW_PLAYING";
	private static final String PATH_KEY = "path";
	private static final String ALBUM_ID = "album_art";
	private static final String REPEAT = "isRepeat";
	private static final String SHUFFLE = "isShuffle";

	public Preferences(Context context) {
		this.context = context;
		settings = context.getSharedPreferences(PREF_NAME, 0);
		editor = settings.edit();

	}

	/**
	 * STORE THE TITLE, ARTIST, ALBUM TO MANAGE THE ROTATIONAL CHANGES
	 * 
	 * @param title
	 * @param artist
	 * @param album
	 */
	public void setName(String title, String artist, String album) {

		editor.putString(MediaServiceContoller.TITLE_KEY, title);
		editor.putString(MediaServiceContoller.ARTIST_KEY, artist);
		editor.putString(MediaServiceContoller.ALBUM_KEY, album);
		editor.commit();
	}

	/**
	 * CHECK FOR DATABASE EXIST. FUTURE UPDATE
	 * 
	 * @param check
	 */
	public void setDataBase(boolean check) {
		editor.putBoolean(DATABASE, check);
		editor.commit();
	}

	/**
	 * RETRIEVE THE TITLE
	 * 
	 * @return
	 */
	public String getTitle() {
		return settings.getString(MediaServiceContoller.TITLE_KEY, "");
	}

	/**
	 * RETRIEVE THE ALBUM
	 * 
	 * @return
	 */
	public String getAlbum() {
		return settings.getString(MediaServiceContoller.ALBUM_KEY, "");
	}

	/**
	 * RETRIEVE THE ARTIST
	 *
	 * @return
	 */
	public String getArtist() {
		return settings.getString(MediaServiceContoller.ARTIST_KEY, "");
	}

	public boolean checkDataBase() {
		return settings.getBoolean(DATABASE, false);
	}

	/**
	 * SET THE CONDITION FOR ENABLE/DISABLE NOW PLAYING WIDGET
	 *
	 * @param condition
	 *            true/false
	 */
	public void updateWidget(boolean condition) {
		editor.putBoolean(UPDATE_NOWPLAYING, condition).commit();
		editor.commit();
	}

	/**
	 * Method to check if media player is playing.
	 *
	 * @return
	 */
	public boolean getNowPlaying() {
		return settings.getBoolean(UPDATE_NOWPLAYING, false);
	}

	public void setName(String title, String artist) {
		editor.putString(MediaServiceContoller.TITLE_KEY, title);
		editor.putString(MediaServiceContoller.ARTIST_KEY, artist);
		editor.commit();
	}

	/**
	 * IF THE APP STARTS FROM FILE MANAGER STORE THE PATH OF THE FILE
	 * 
	 * @param path
	 */
	public void setPath(String path) {
		editor.putString(PATH_KEY, path);
		editor.commit();
	}

	/**
	 * RETRIEVE THE PATH
	 *
	 * @return
	 */
	public String getPath() {
		return settings.getString(PATH_KEY, "/");
	}

	/**
	 * Method to store the album id.
	 *
	 * @param id
	 */
	public void setId(long id) {
		editor.putLong(ALBUM_ID, id);
		editor.commit();
	}

	/**
	 * Method to retrieve the album id in order to fetch album art
	 *
	 * @return
	 */
	public long getId() {
		return settings.getLong(ALBUM_ID, 0);
	}

	/**
	 * 
	 * Method to set the repeat property
	 *
	 * @param isRepeat
	 */
	public void setRepeat(boolean isRepeat) {
		editor.putBoolean(REPEAT, isRepeat);
		editor.commit();
	}

	public boolean getRepeat() {
		return settings.getBoolean(REPEAT, false);
	}

	/**
	 * SET THE SHUFFLE PROPERTY
	 *
	 * @param isShuffle
	 *            true
	 */
	public void setShuffle(boolean isShuffle) {
		editor.putBoolean(SHUFFLE, isShuffle);
		editor.commit();
	}

	public boolean getShuffle() {
		return settings.getBoolean(SHUFFLE, false);
	}

	/**
	 * CLEAR PREFERENCES
	 */
	public void clearData() {
		settings.edit().clear().commit();
	}

	/**
	 * SET THE DURATION OF TRACK.
	 *
	 * @param duration
	 */
	public void setDuration(int duration) {
		editor.putInt("duration", duration);
		editor.commit();
	}

	/**
	 * Method to fetch the current duration
	 *
	 * @return
	 */
	public int getDuration() {
		return settings.getInt("duration", 0);
	}
}
