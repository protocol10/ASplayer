package com.akshay.protocol10.asplayer.models;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import android.media.MediaMetadataRetriever;
import android.util.Log;
public class DataRetriver {

	ArrayList<HashMap<String, Object>> songs_list;
	MediaMetadataRetriever metadataRetriever;
	HashMap<String, Object> songs_map;
	String title, album_name, artist_name;
	int duration;

	private static final String TITLE_NAME = "TITLE";
	private static final String ALBUM_NAME = "ALBUM";
	private static final String ARTIST_NAME = "ARTIST";
	private static final String PATH = "PATH";

	
	public DataRetriver() {
		// TODO Auto-generated constructor stub
		Log.i("Path", "Path called");
		songs_list = new ArrayList<HashMap<String, Object>>();
		metadataRetriever = new MediaMetadataRetriever();
	}
	

	public void retriveData(String path) {

		File directory = new File(path);

		File[] listFiles = directory.listFiles();

		if (listFiles != null) {

			for (File file : listFiles) {
				if (file.isDirectory())
					retriveData(file.getAbsolutePath());
				else if (file.isFile()) {
					if (file.getName().endsWith(".mp3")) {
						songs_map = new HashMap<String, Object>();
						Log.i("File", file.getAbsolutePath());
						metadataRetriever.setDataSource(file.getAbsolutePath());
						title = metadataRetriever
								.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
						artist_name = metadataRetriever
								.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
						album_name = metadataRetriever
								.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);

						if (artist_name == null) {
							artist_name = "Unknown Artist";
						}

						if (album_name == null || album_name == "") {
							album_name = "UnKnown Album";
						}
						songs_map.put(PATH, file.getAbsolutePath());
						songs_map.put(TITLE_NAME, title);
						songs_map.put(ALBUM_NAME, album_name);
						songs_map.put(ARTIST_NAME, artist_name);
						songs_list.add(songs_map);

					}
				}
			}
		}
	}
}
