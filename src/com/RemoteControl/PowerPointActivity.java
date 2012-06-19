package com.RemoteControl;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class PowerPointActivity extends Activity {
	private CommConnect commConnect;
	private MyApp myApp;
	private ImageView backward;
	private ImageView forward;
	private ImageView start;
	private static char BACKWARD = 'g';
	private static char FORWARD = 'h';
	private static char START_PPT = 'i';
	private static char END_PPT = 'j';
	private boolean flag = true;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ppt);

		backward = (ImageView) findViewById(R.id.backward);
		forward = (ImageView) findViewById(R.id.forward);
		start = (ImageView) findViewById(R.id.start);
		myApp = (MyApp) getApplication();
		commConnect = myApp.getCommConnect();

		backward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ControlData controlData = new ControlData();
				controlData.setHead(BACKWARD);
				commConnect.send(controlData);
			}
		});

		forward.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ControlData controlData = new ControlData();
				controlData.setHead(FORWARD);
				commConnect.send(controlData);
			}
		});

		start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag) {
					start.setImageDrawable(start.getResources().getDrawable(
							R.drawable.esc));
					ControlData controlData = new ControlData();
					controlData.setHead(START_PPT);
					commConnect.send(controlData);
					flag = false;
				} else {
					start.setImageDrawable(start.getResources().getDrawable(
							R.drawable.display));
					ControlData controlData = new ControlData();
					controlData.setHead(END_PPT);
					commConnect.send(controlData);
					flag = true;
				}
			}
		});

	}
}
