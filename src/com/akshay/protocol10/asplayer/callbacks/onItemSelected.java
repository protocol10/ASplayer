package com.akshay.protocol10.asplayer.callbacks;

public interface onItemSelected {

	void updateView(String title, String artist, String album, int position);

	void startPlayBack(int index);

	void pausePlayBack();

	void nextPlayBack();

	void previousPlayBack();

}
