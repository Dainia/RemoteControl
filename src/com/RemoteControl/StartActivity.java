package com.RemoteControl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

public class StartActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.start);

		new Handler().postDelayed(new Runnable() {

			public void run() {
				Intent i = new Intent(StartActivity.this, ConnectActivity.class);
				StartActivity.this.startActivity(i);
				StartActivity.this.finish();
			}
		}, 3000);
	}
}
