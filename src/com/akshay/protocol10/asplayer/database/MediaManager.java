package com.akshay.protocol10.asplayer.database;

/**
 * @author akshay
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Audio.Artists;
import android.widget.Toast;

public class MediaManager {

	Uri INTERNAL;
	Uri EXTERNAL;
	String selection = null;
	String[] selectionArgs = null;

	private final String ID_KEY = "id";
	private final String TITLE_KEY = "title";
	private final String PATH_KEY = "src";
	private final String ARTIST_KEY = "artist";
	private final String ALBUM_KEY = "album";
	private final String DURATION_KEY = "duration";

	private static final String UNABLE_TAG = "Unable to fetch media";
	private static final String NOMEDIA_TAG = "No media on device";
	private static final String NO_OF_ALBUMS = "artist_count";

	ContentResolver resolver;
	Cursor cursor;

	List<HashMap<String, Object>> tracks_list;
	HashMap<String, Object> songs_map;

	/**
	 * PROJECTION - Retrieves the value for Tracks from MediaStore DataBase in
	 * android Only used for MediaProjection for general tracks
	 * 
	 */

	private static final String[] TRACKS_COLUMNS = {
			MediaStore.Audio.Media._ID, MediaStore.Audio.Media.DATA,
			MediaStore.Audio.Media.DISPLAY_NAME, MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION };
	/**
	 * ALBUM_COLUMNS - Used for projection no of Albums available for available
	 * tracks on the device
	 */

	private static final String[] ALBUM_COLUMNS = { Albums.ALBUM, Albums._ID,
			Albums.ARTIST, Albums.ALBUM_ART };

	/**
	 * ARTIST_COLUMNS - Used for projection no of Artist's available for
	 * available tracks on the device
	 */
	private static final String[] ARTIST_COLUMNS = { Artists._ID,
			Artists.ARTIST, Artists.NUMBER_OF_ALBUMS };

	/**
	 * Constructor
	 */

	public MediaManager() {
		// TODO Auto-generated constructor stub
		tracks_list = new ArrayList<HashMap<String, Object>>();
		INTERNAL = MediaStore.Audio.Media.INTERNAL_CONTENT_URI;
		EXTERNAL = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
	}

	/**
	 * Used for retrieving all the media tracks from the device
	 * 
	 * @param Context
	 *            provide the reference for the ContentResolver from the
	 *            Activity
	 * @return list
	 * 
	 */

	public List<HashMap<String, Object>> retriveContent(Context context) {

		resolver = context.getContentResolver();
		cursor = resolver.query(EXTERNAL, TRACKS_COLUMNS, selection,
				selectionArgs, MediaStore.Audio.Media.DISPLAY_NAME);

		if (cursor == null)
			Toast.makeText(context, UNABLE_TAG, Toast.LENGTH_SHORT).show();
		else if (!cursor.moveToFirst()) {
			Toast.makeText(context, NOMEDIA_TAG, Toast.LENGTH_SHORT).show();

		} else {

			do {
				String id = cursor.getString(0); // ID
				String path = cursor.getString(1);// DATA
				String title = cursor.getString(2);// TITLE

				String artist = cursor.getString(3);// ARTIST
				String album = cursor.getString(4);// ALBUMV
				String duration = cursor.getString(5);// DURATION

				songs_map = new HashMap<String, Object>();
				songs_map.put(ID_KEY, id);
				songs_map.put(PATH_KEY, path);
				songs_map.put(TITLE_KEY, title);
				songs_map.put(ARTIST_KEY, artist);
				songs_map.put(ALBUM_KEY, album);
				songs_map.put(DURATION_KEY, duration);
				tracks_list.add(songs_map);

			} while (cursor.moveToNext());
		}
		cursor.close();
		return (ArrayList<HashMap<String, Object>>) tracks_list;
	}

	/**
	 * Used for retrieving Unique names of albums for the available the media
	 * tracks from the device
	 * 
	 * @param Context
	 *            provide the reference for the ContentResolver from the
	 *            Activity
	 * @return list
	 * 
	 */

	public List<HashMap<String, Object>> retriveAlbum(Context context) {

		HashMap<String, Object> album;
		List<HashMap<String, Object>> album_list = new ArrayList<HashMap<String, Object>>();

		resolver = context.getContentResolver();

		cursor = resolver.query(Albums.EXTERNAL_CONTENT_URI, ALBUM_COLUMNS,
				selection, selectionArgs, null);

		if (cursor == null) {
			Toast.makeText(context, UNABLE_TAG, Toast.LENGTH_SHORT).show();
		} else if (!cursor.moveToFirst()) {
			Toast.makeText(context, NOMEDIA_TAG, Toast.LENGTH_SHORT).show();

		} else {

			do {
				String album_name = cursor.getString(0); // album
				String id = cursor.getString(1); // id
				String artist = cursor.getString(2); // artist
				String album_art = cursor.getString(3); // album-art

				album = new HashMap<String, Object>();
				album.put(ALBUM_KEY, album_name);
				album.put(ID_KEY, id);
				album.put(ARTIST_KEY, artist);
				album.put("albumart", album_art);
				album_list.add(album);

			} while (cursor.moveToNext());
		}
		cursor.close();
		return album_list;
	}

	/**
	 * 
	 * @param context
	 *            provide the reference for the ContentResolver from the
	 *            Activity
	 * @return list
	 */
	public List<HashMap<String, Object>> retriveArtist(Context context) {

		HashMap<String, Object> artist_map;
		List<HashMap<String, Object>> artist_list = new ArrayList<HashMap<String, Object>>();

		resolver = context.getContentResolver();
		cursor = resolver.query(Artists.EXTERNAL_CONTENT_URI, ARTIST_COLUMNS,
				selection, selectionArgs, null);

		if (cursor == null) {
			Toast.makeText(context, UNABLE_TAG, Toast.LENGTH_SHORT).show();

		} else if (!cursor.moveToFirst()) {
			Toast.makeText(context, NOMEDIA_TAG, Toast.LENGTH_SHORT).show();
		} else {

			do {
				String artist_id = cursor.getString(0); // ARTIST._ID
				String artist = cursor.getString(1); // Artist.ARTIST
				String album_count = cursor.getString(2);// Artist.NUMBER_OF_ALBUMS

				artist_map = new HashMap<String, Object>();
				artist_map.put(ID_KEY, artist_id);
				artist_map.put(ARTIST_KEY, artist);
				artist_map.put(NO_OF_ALBUMS, album_count);
				artist_list.add(artist_map);

			} while (cursor.moveToNext());
		}
		cursor.close();
		return artist_list;
	}

	public List<HashMap<String, Object>> retriveContent(Context context,
			String name) {

		resolver = context.getContentResolver();
		selection = MediaStore.Audio.Media.ALBUM + "=?";

		String selectionArgs[] = { name };
		cursor = resolver.query(EXTERNAL, TRACKS_COLUMNS, selection,
				selectionArgs, null);

		if (cursor == null)
			Toast.makeText(context, UNABLE_TAG, Toast.LENGTH_SHORT).show();
		else if (!cursor.moveToFirst())
			Toast.makeText(context, NOMEDIA_TAG, Toast.LENGTH_SHORT).show();
		else {
			do {
				String id = cursor.getString(0); // ID
				String path = cursor.getString(1);// DATA
				String title = cursor.getString(2);// TITLE

				String artist = cursor.getString(3);// ARTIST
				String album = cursor.getString(4);// ALBUMV
				String duration = cursor.getString(5);// DURATION

				songs_map = new HashMap<String, Object>();
				songs_map.put(ID_KEY, id);
				songs_map.put(PATH_KEY, path);
				songs_map.put(TITLE_KEY, title);
				songs_map.put(ARTIST_KEY, artist);
				songs_map.put(ALBUM_KEY, album);
				songs_map.put(DURATION_KEY, duration);
				tracks_list.add(songs_map);

			} while (cursor.moveToNext());
		}
		cursor.close();
		return tracks_list;
	}
}
