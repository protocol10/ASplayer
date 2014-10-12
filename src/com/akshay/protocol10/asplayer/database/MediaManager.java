package com.akshay.protocol10.asplayer.database;

/**
 * @author akshay
 */

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Audio.Artists;
import android.provider.MediaStore.Audio.Genres;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
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
	public static final String ALBUM_ART = "album_art";
	private final String ALBUM_ID = "album_art";

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
			MediaStore.Audio.Media.ALBUM, MediaStore.Audio.Media.DURATION,
			MediaStore.Audio.Media.ALBUM_ID, MediaStore.Audio.Media.ARTIST_ID };
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

	private static final String[] GENRE_COLUMNGS = { Genres._ID, Genres.NAME, };

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
				String album = cursor.getString(4);// ALBUM
				String duration = cursor.getString(5);// DURATION
				long album_id = cursor.getLong(6);// ALBUM_ART

				songs_map = new HashMap<String, Object>();
				songs_map.put(ID_KEY, id);
				songs_map.put(PATH_KEY, path);
				songs_map.put(TITLE_KEY, title);
				songs_map.put(ARTIST_KEY, artist);
				songs_map.put(ALBUM_KEY, album);
				songs_map.put(DURATION_KEY, duration);
				songs_map.put(ALBUM_ART, album_id);
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
				String album_art = cursor.getString(cursor
						.getColumnIndex(Audio.Albums.ALBUM_ART));
				album = new HashMap<String, Object>();
				album.put(ALBUM_KEY, album_name);
				album.put(ID_KEY, id);
				album.put(ARTIST_KEY, artist);
				album.put(ALBUM_ART, album_art);
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
				long artist_id = cursor.getLong(0); // ARTIST._ID
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
				long album_id = cursor.getLong(6);

				songs_map = new HashMap<String, Object>();
				songs_map.put(ID_KEY, id);
				songs_map.put(PATH_KEY, path);
				songs_map.put(TITLE_KEY, title);
				songs_map.put(ARTIST_KEY, artist);
				songs_map.put(ALBUM_KEY, album);
				songs_map.put(DURATION_KEY, duration);
				songs_map.put(ALBUM_ID, album_id);

				tracks_list.add(songs_map);

			} while (cursor.moveToNext());
		}
		cursor.close();
		return tracks_list;
	}

	public List<HashMap<String, Object>> retriveArtistContent(Context context,
			long id_album) {

		List<HashMap<String, Object>> album_list = new ArrayList<HashMap<String, Object>>();
		resolver = context.getContentResolver();

		/**
		 * 
		 * Little tweak as by query albums by name produces uncertainty. It may
		 * or may not display albums. Used this workaround to get the things
		 * done. REF: Google Media Frameworks
		 */
		cursor = resolver.query(MediaStore.Audio.Artists.Albums.getContentUri(
				"external", id_album), ALBUM_COLUMNS, null, null, null);

		if (cursor == null)
			Toast.makeText(context, UNABLE_TAG, Toast.LENGTH_SHORT).show();
		else if (!cursor.moveToFirst())
			Toast.makeText(context, NOMEDIA_TAG, Toast.LENGTH_SHORT).show();
		else {
			do {
				String album_name = cursor.getString(0); // album

				String id = cursor.getString(1); // id
				String artist = cursor.getString(2); // artist

				String album_art = cursor.getString(3); // album-art

				songs_map = new HashMap<String, Object>();
				songs_map.put(ALBUM_KEY, album_name);
				songs_map.put(ID_KEY, id);
				songs_map.put(ARTIST_KEY, artist);
				songs_map.put(ALBUM_ART, album_art);
				album_list.add(songs_map);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return album_list;
	}

	public List<HashMap<String, Object>> retriveTracks(Context context,
			String name, long id) {
		List<HashMap<String, Object>> albumSongs = new ArrayList<HashMap<String, Object>>();
		String selection = MediaStore.Audio.Media.ALBUM + "= ? AND "
				+ MediaStore.Audio.Media.ARTIST_ID + "= ?";

		String[] selectArgs = new String[] { name, String.valueOf(id) };

		cursor = context.getContentResolver().query(EXTERNAL, TRACKS_COLUMNS,
				selection, selectArgs, null);

		if (cursor == null) {

		} else if (!cursor.moveToFirst()) {

		} else {
			do {
				int trackId = cursor.getInt(cursor.getColumnIndex(Media._ID));
				String path = cursor.getString(cursor
						.getColumnIndex(Media.DATA));
				String title = cursor.getString(cursor
						.getColumnIndex(Media.DISPLAY_NAME));
				String artist = cursor.getString(cursor
						.getColumnIndex(Media.ARTIST));
				String album = cursor.getString(cursor
						.getColumnIndex(Media.ALBUM));
				String duration = cursor.getString(cursor
						.getColumnIndex(Media.DURATION));
				long album_id = cursor.getLong(cursor
						.getColumnIndex(Media.ALBUM_ID));

				songs_map = new HashMap<String, Object>();
				songs_map.put(ID_KEY, trackId);
				songs_map.put(PATH_KEY, path);
				songs_map.put(TITLE_KEY, title);
				songs_map.put(ARTIST_KEY, artist);
				songs_map.put(ALBUM_KEY, album);
				songs_map.put(DURATION_KEY, duration);
				songs_map.put(ALBUM_ART, album_id);
				albumSongs.add(songs_map);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return albumSongs;
	}

	public List<HashMap<String, Object>> retriveGenre(Context context) {

		cursor = context.getContentResolver().query(
				Genres.EXTERNAL_CONTENT_URI, GENRE_COLUMNGS, null, null,
				Genres.DEFAULT_SORT_ORDER);
		if (cursor == null) {

		} else if (!cursor.moveToFirst()) {

		} else {
			do {
				long id = cursor.getLong(cursor.getColumnIndex(Genres._ID));
				String genreName = cursor.getString(cursor
						.getColumnIndex(Genres.NAME));
				songs_map = new HashMap<String, Object>();
				songs_map.put(ID_KEY, id);
				songs_map.put("genre", genreName);
				tracks_list.add(songs_map);
			} while (cursor.moveToNext());
		}
		cursor.close();
		return tracks_list;
	}

	/**
	 * Tweaks with terribly design GENRES COLUMN in ANDROID MEDIA FRAMEWORKS
	 * 
	 * @param context
	 * @param id
	 * @return
	 */
	public List<HashMap<String, Object>> retriveGenreAlbum(Context context,
			long id) {
		String[] projection = { Albums.ALBUM, Albums.ALBUM_ID, Albums.ARTIST };

		cursor = context.getContentResolver().query(
				Genres.Members.getContentUri("external", id), projection, null,
				null, null);

		if (cursor == null) {

		} else if (cursor.moveToFirst()) {
			do {
				String album = cursor.getString(cursor
						.getColumnIndex(Albums.ALBUM));
				String albumId = cursor.getString(cursor
						.getColumnIndex(Albums.ALBUM_ID));
				String artist = cursor.getString(cursor
						.getColumnIndex(Albums.ARTIST));
				songs_map = new HashMap<String, Object>();
				Cursor artCursor = context.getContentResolver().query(
						Albums.EXTERNAL_CONTENT_URI, null,
						Albums.ALBUM + " =?", new String[] { album }, null);
				// Fetch the Album Art for respective Albums in genre.
				if (artCursor.moveToFirst()) {
					String album_art = artCursor.getString(artCursor
							.getColumnIndex(Albums.ALBUM_ART));
					songs_map.put(ALBUM_ART, album_art);
				}
				artCursor.close();

				// Fetch the artist id genre columns do not
				Cursor idCursor = context.getContentResolver().query(
						Artists.EXTERNAL_CONTENT_URI, null,
						Artists.ARTIST + " = ?", new String[] { artist }, null);
				if (idCursor.moveToFirst()) {
					long artist_id = idCursor.getLong(idCursor
							.getColumnIndex(Artists._ID));
					songs_map.put("artist_id", artist_id);
				}
				idCursor.close();
				songs_map.put(ARTIST_KEY, artist);
				songs_map.put(ALBUM_KEY, album);
				songs_map.put(ID_KEY, albumId);
				tracks_list.add(songs_map);
			} while (cursor.moveToNext());

		}
		cursor.close();
		return tracks_list;
	}

	/**
	 * 
	 * @param id
	 *            album_id
	 * @param context
	 * 
	 * @return bitmap
	 */
	public static Bitmap getAlbumArt(long id, Context context) {

		Bitmap bitmap = null;

		Uri artwork = Uri.parse("content://media/external/audio/albumart");

		Uri uri = ContentUris.withAppendedId(artwork, id);

		try {
			ParcelFileDescriptor pDescriptor = context.getContentResolver()
					.openFileDescriptor(uri, "r");
			if (pDescriptor != null) {
				FileDescriptor fileDescriptor = pDescriptor.getFileDescriptor();
				bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bitmap;
	}
}
