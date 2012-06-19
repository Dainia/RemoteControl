package com.RemoteControl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class MediaActivity extends Activity {
	private CommConnect commConnect;
	private MyApp myApp;
	private ImageView open;
	private ImageView previous;
	private ImageView play;
//	private ImageView stop;
	private ImageView next;
	private static char OPEN = 'k';
	private static char PREVIOUS = 'l';
	private static char PLAY = 'm';
//	private static char STOP = 'n';
	private static char NEXT = 'o';
	private boolean flag = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media);

		open = (ImageView) findViewById(R.id.open);
		previous = (ImageView) findViewById(R.id.previous);
		play = (ImageView) findViewById(R.id.play);
//		stop = (ImageView) findViewById(R.id.stop);
		next = (ImageView) findViewById(R.id.next);
		myApp = (MyApp) getApplication();
		commConnect = myApp.getCommConnect();

		open.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ControlData controlData = new ControlData();
				controlData.setHead(OPEN);
				commConnect.send(controlData);
			}
		});

		previous.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ControlData controlData = new ControlData();
				controlData.setHead(PREVIOUS);
				commConnect.send(controlData);
			}
		});

		play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ImageView play = (ImageView) findViewById(R.id.play);
				if (flag) {
					play.setImageDrawable(play.getResources().getDrawable(
							R.drawable.pause));
					flag = false;
				} else {
					play.setImageDrawable(play.getResources().getDrawable(
							R.drawable.play));
					flag = true;
				}
				ControlData controlData = new ControlData();
				controlData.setHead(PLAY);
				commConnect.send(controlData);
			}
		});

//		stop.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				ControlData controlData = new ControlData();
//				controlData.setHead(STOP);
//				commConnect.send(controlData);
//			}
//		});

		next.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ControlData controlData = new ControlData();
				controlData.setHead(NEXT);
				commConnect.send(controlData);
			}
		});

	}
}
