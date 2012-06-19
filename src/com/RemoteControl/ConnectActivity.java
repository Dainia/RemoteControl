package com.RemoteControl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

public class ConnectActivity extends Activity {
	private Button autoConnect;
	private Button manualConnect;
	private EditText ipAddress;
	private CommConnect commConnect;
	private WifiConnect wifiConnect;
	private MyApp myApp;
	private BroadCast broadCast;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect);

		autoConnect = (Button) findViewById(R.id.autoConnect);
		manualConnect = (Button) findViewById(R.id.manualConnect);
		ipAddress = (EditText) findViewById(R.id.address);
		wifiConnect = new WifiConnect();
		commConnect = wifiConnect;
		myApp = (MyApp) getApplication();
		broadCast = new BroadCast();
		
		autoConnect.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				try {
					broadCast.sendBroadcast();
				} catch (Exception e) {
					e.printStackTrace();
				}
				try {
					if (broadCast.receiveBroadcast().equals("damon")) {
						connectInet();
						myApp.setCommConnect(commConnect);
						Intent intent = new Intent(ConnectActivity.this,
								RemoteControlActivity.class);
						startActivity(intent);
					} else {

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		ipAddress.setText("172.20.25.85");

		manualConnect.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				
					connectInet();
					myApp.setCommConnect(commConnect);
					Intent intent = new Intent(ConnectActivity.this,
							RemoteControlActivity.class);
					startActivity(intent);
				
			}
		});

		ipAddress.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				Toast.makeText(ConnectActivity.this,
						"Connecting to " + ipAddress.getText().toString(),
						Toast.LENGTH_SHORT).show();
				if (ipAddress.getText().toString().equals("172.20.25.85")) {
					connectInet();
					myApp.setCommConnect(commConnect);
					Intent intent = new Intent(ConnectActivity.this,
							RemoteControlActivity.class);
					startActivity(intent);
				}
				return false;
			}
		});
	}

	// 建立连接
	private void connectInet() {
		String inetServerAddr = ipAddress.getText().toString();
		String inetServerPort = "51706";
		if (inetServerAddr == null || inetServerAddr.trim().length() == 0
				|| inetServerPort == null
				|| inetServerPort.trim().length() == 0) {
			Toast.makeText(this, "Address can't be blank!", Toast.LENGTH_SHORT)
					.show();
			return;
		}

		String[] inetServerAddrAndPort = new String[] { inetServerAddr,
				inetServerPort };
		commConnect.connect(inetServerAddrAndPort);
	}
}
