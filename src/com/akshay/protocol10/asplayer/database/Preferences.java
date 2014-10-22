package com.akshay.protocol10.asplayer.database;

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
		// TODO Auto-generated constructor stub
		this.context = context;
		settings = context.getSharedPreferences(PREF_NAME, 0);
		editor = settings.edit();

	}

	public void setName(String title, String artist, String album) {

		editor.putString(MediaServiceContoller.TITLE_KEY, title);
		editor.putString(MediaServiceContoller.ARTIST_KEY, artist);
		editor.putString(MediaServiceContoller.ALBUM_KEY, album);
		editor.commit();
	}

	public void setDataBase(boolean check) {
		editor.putBoolean(DATABASE, check);
		editor.commit();
	}

	public String getTitle() {
		return settings.getString(MediaServiceContoller.TITLE_KEY, "");
	}

	public String getAlbum() {
		return settings.getString(MediaServiceContoller.ALBUM_KEY, "");
	}

	public String getArtist() {
		return settings.getString(MediaServiceContoller.ARTIST_KEY, "");
	}

	public boolean checkDataBase() {
		return settings.getBoolean(DATABASE, false);
	}

	public void updateWidget(boolean condition) {
		editor.putBoolean(UPDATE_NOWPLAYING, condition).commit();
	}

	public boolean getNowPlaying() {
		return settings.getBoolean(UPDATE_NOWPLAYING, false);
	}

	public void setName(String title, String artist) {
		editor.putString(MediaServiceContoller.TITLE_KEY, title);
		editor.putString(MediaServiceContoller.ARTIST_KEY, artist);
		editor.commit();
	}

	public void setPath(String path) {
		editor.putString(PATH_KEY, path);
		editor.commit();
	}

	public String getPath() {
		return settings.getString(PATH_KEY, "/");
	}

	public void setId(long id) {
		editor.putLong(ALBUM_ID, id);
		editor.commit();
	}

	public long getId() {
		return settings.getLong(ALBUM_ID, 0);
	}

	public void setRepeat(boolean isRepeat) {
		editor.putBoolean(REPEAT, isRepeat);
		editor.commit();
	}

	public boolean getRepeat() {
		return settings.getBoolean(REPEAT, false);
	}

	public void setShuffle(boolean isShuffle) {
		editor.putBoolean(SHUFFLE, isShuffle);
		editor.commit();
	}

	public boolean getShuffle() {
		return settings.getBoolean(SHUFFLE, false);
	}

	public void clearData() {
		editor.clear();
	}

	public void setDuration(int duration) {
		editor.putInt("duration", duration);
		editor.commit();
	}

	public int getDuration() {
		return settings.getInt("duration", 0);
	}
}
