package com.akshay.protocol10.asplayer;

import com.akshay.protocol10.asplayer.database.MediaManager;
import com.akshay.protocol10.asplayer.service.MediaServiceContoller;
import com.akshay.protocol10.asplayer.utils.ASUtils;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

public class ASPlayerWidget extends AppWidgetProvider {

	public final static String APPWIDGET_INIT = "com.akshay.protocol10.widget.INIT";
	public final static String APPWIDGET_UPDATE_TEXT = "com.akshay.protocol10.widget.UPDATETXT";
	public final static String APPWIDGET_UPDATE_COVER = "com.akshay.protocol10.widget.UPDATECOVER";
	public final static String APPWIDGET_PLAY = "com.akshay.protocol10.widget.PLAY";
	public final static String APPWIDGET_BACK = "com.akshay.protocol10.widget.PREVIOUS";
	public final static String APPWIDGET_NEXT = "com.akshay.protocol10.widget.NEXT";
	public final static String APPWIDGET_UPDATE_ICON = "com.akshay.protocol10.widget.ICONS";
	boolean isPlaying;

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onUpdate(context, appWidgetManager, appWidgetIds);

		Intent intent = new Intent(APPWIDGET_INIT);
		onReceive(context, intent);

		intent = new Intent(APPWIDGET_INIT);
		context.sendBroadcast(intent);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		super.onReceive(context, intent);
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.asplayer_widget);

		String action = intent.getAction();
		if (action.equals(APPWIDGET_INIT)) {
			Intent backwardIntent = new Intent(APPWIDGET_BACK);
			Intent nextIntent = new Intent(APPWIDGET_NEXT);
			Intent playIntent = new Intent(APPWIDGET_PLAY);

			PendingIntent piBack = PendingIntent.getBroadcast(context, 0,
					backwardIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent piNext = PendingIntent.getBroadcast(context, 0,
					nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			PendingIntent piPlay = PendingIntent.getBroadcast(context, 0,
					playIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			views.setOnClickPendingIntent(R.id.rewind, piBack);
			views.setOnClickPendingIntent(R.id.forward, piNext);
			views.setOnClickPendingIntent(R.id.play_pause, piPlay);
		}

		if (action.equals(APPWIDGET_UPDATE_TEXT)) {

			String title = intent
					.getStringExtra(MediaServiceContoller.TITLE_KEY);
			isPlaying = intent.getBooleanExtra(ASUtils.IS_PLAYING, false);
			views.setTextViewText(R.id.title_text, title.toString());
			views.setImageViewResource(R.id.play_pause,
					isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
		}
		if (action.equals(APPWIDGET_UPDATE_COVER)) {
			long album_id = intent.getLongExtra(MediaServiceContoller.ALBUM_ID,
					0);
			Bitmap bitmap = MediaManager.getAlbumArt(album_id, context);
			if (bitmap != null) {
				views.setImageViewBitmap(R.id.cover_art, bitmap);
			} else {
				views.setImageViewResource(R.id.cover_art,
						R.drawable.ic_launcher);
			}
		}
		if (action.equals(APPWIDGET_UPDATE_ICON)) {
			isPlaying = intent.getBooleanExtra(ASUtils.IS_PLAYING, false);
			views.setImageViewResource(R.id.play_pause,
					isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);

		}
		ComponentName widget = new ComponentName(context, ASPlayerWidget.class);
		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		manager.updateAppWidget(widget, views);
	}
}
