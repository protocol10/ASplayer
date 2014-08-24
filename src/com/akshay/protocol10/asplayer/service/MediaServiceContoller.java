package com.akshay.protocol10.asplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MediaServiceContoller extends Service {

	MediaPlayer mediaplayer;
	AudioManager manager;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mediaplayer = new MediaPlayer();
	}

}
