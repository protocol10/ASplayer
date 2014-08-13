package com.akshay.protocol10.asplayer.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Albums;
import android.util.Log;
import android.widget.Toast;

public class MediaManager {

	List<HashMap<String, Object>> tracks_list;
	Uri INTERNAL;
	Uri EXTERNAL;
	String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
	private final String ID_KEY = "id";
	private final String TITLE_KEY = "title";
	private final String PATH_KEY = "src";
	private final String ARTIST_KEY = "artist";
	private final String ALBUM_KEY = "album";
	private final String DURATION_KEY = "duration";
	ContentResolver resolver;
	Cursor cursor;
	/**
	 * Columns to be returned for each row
	 */

	private String[] projection = { MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DISPLAY_NAME,
			MediaStore.Audio.Media.ARTIST, MediaStore.Audio.Media.ALBUM,
			MediaStore.Audio.Media.DURATION };

	public MediaManager() {
		// TODO Auto-generated constructor stub
		tracks_list = new ArrayList<HashMap<String, Object>>();
		INTERNAL = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
		EXTERNAL = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	}

	public List<HashMap<String, Object>> retriveContent(Context context) {

		resolver = context.getContentResolver();
		cursor = resolver.query(EXTERNAL, projection, null, null,
				MediaStore.Audio.Media.DISPLAY_NAME);

		if (cursor == null)
			Toast.makeText(context, "Unable to Fetch Data", Toast.LENGTH_SHORT)
					.show();
		else if (!cursor.moveToFirst()) {
			Toast.makeText(context, "No Media on Device", Toast.LENGTH_SHORT)
					.show();

		} else {

			do {
				String id = cursor.getString(0); // ID
				String path = cursor.getString(1);// DATA
				String title = cursor.getString(2);// TITLE

				String artist = cursor.getString(3);// ARTIST
				String album = cursor.getString(4);// ALBUMV
				String duration = cursor.getString(5);// DURATION

				HashMap<String, Object> songs_map = new HashMap<String, Object>();
				songs_map.put(ID_KEY, id);
				songs_map.put(PATH_KEY, path);
				songs_map.put(TITLE_KEY, title);
				songs_map.put(ARTIST_KEY, artist);
				songs_map.put(ALBUM_KEY, album);
				songs_map.put(DURATION_KEY, duration);
				tracks_list.add(songs_map);

			} while (cursor.moveToNext());
		}
		return (ArrayList<HashMap<String, Object>>) tracks_list;
	}

	public List<HashMap<String, Object>> retriveAlbum(Context context) {

		HashMap<String, Object> album;
		List<HashMap<String, Object>> album_list = new ArrayList<HashMap<String, Object>>();

		resolver = context.getContentResolver();

		String[] columns = { Albums.ALBUM, Albums._ID, Albums.ARTIST,
				Albums.ALBUM_ART };
		String selection = null;
		String[] selectionArgs = null;
		cursor = resolver.query(Albums.EXTERNAL_CONTENT_URI, columns,
				selection, selectionArgs, null);

		if (cursor == null) {

		} else if (!cursor.moveToFirst()) {
			Toast.makeText(context, "No Media on Device", Toast.LENGTH_SHORT)
					.show();
		} else {

			do {
				String album_name = cursor.getString(0);
				String id = cursor.getString(1);
				String artist = cursor.getString(2);
				String album_art = cursor.getString(3);
				// Log.i("art", album_art);
				Log.i("artist", artist);
				album = new HashMap<String, Object>();
				album.put("album", album_name);
				album.put("albumID", id);
				album.put("artist", artist);
				album.put("albumart", album_art);
				album_list.add(album);
			} while (cursor.moveToNext());
		}

		return album_list;
	}

}
