package com.akshay.protocol10.asplayer.callbacks;

import java.util.HashMap;
import java.util.List;

public interface onItemSelected {

	void updateView(String title, String artist, String album, int index,
			long album_id);

	void startPlayBack(int index);

	void pausePlayBack();

	void nextPlayBack();

	void previousPlayBack();

	void selectArtist(long name);

	void updateList(List<HashMap<String, Object>> list);

	void seekTo(int progress);

	void updateArtistAlbum(String name, long id);
}
