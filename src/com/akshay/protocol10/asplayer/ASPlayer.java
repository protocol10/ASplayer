package com.akshay.protocol10.asplayer;

/**
 * @author akshay 
 * Application Class for Initializing the ActiveAndroidLibrary.
 * Also for retriving the BaseContext of the Application
 */
import com.activeandroid.ActiveAndroid;

import android.app.Application;
import android.content.Context;

public class ASPlayer extends Application {

	static ASPlayer instance;
	public final static String INCOMING_CALL_INTENT = "com.akshay.protocol10.asplayer.IncominCallIntent";
	public final static String CALL_ENDED_INTENT = "com.akshay.protocol10.asplayer.CallEndedIntent";

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		ActiveAndroid.initialize(this);
		instance = this;
	}

	public static Context getAppContext() {
		return instance;
	}
}
