package com.akshay.protocol10.asplayer.callbacks;

/**
 * @author akshay
 */
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

	void updateGenreAlbum(long genreId);

	void setTag(String tag);

	void selectPreset(int position);

	void playFromPath(String path);

	void setPresetReverb(int position);

	void setEqualizer(int band, int level);
}
