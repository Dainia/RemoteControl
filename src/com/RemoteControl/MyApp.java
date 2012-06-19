package com.RemoteControl;

import android.app.Application;

public class MyApp extends Application {
	private CommConnect commConnect = null;

	public CommConnect getCommConnect() {
		return commConnect;
	}

	public void setCommConnect(CommConnect commConnect) {
		this.commConnect = commConnect;
	}
}
