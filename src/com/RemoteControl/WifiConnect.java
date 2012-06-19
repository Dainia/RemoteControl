package com.RemoteControl;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import android.util.Log;

public class WifiConnect extends CommConnect {
	public WifiConnect() {
		super();
	}

	protected void doConnect(Object addr) throws IOException {
		socket = new Socket();
		String[] wifiAddrAndPort = (String[]) addr;
		String wifiAddrStr = wifiAddrAndPort[0];
		String wifiPortStr = wifiAddrAndPort[1];

		InetAddress wifiAddr = InetAddress.getByName(wifiAddrStr);
		int wifiPort = Integer.parseInt(wifiPortStr);
		InetSocketAddress wifiSocketAddr = new InetSocketAddress(wifiAddr,
				wifiPort);

		socket.connect(wifiSocketAddr);

		outputStream = socket.getOutputStream();
		inputStream = socket.getInputStream();
	}

	protected void doDisconnect() {
		try {
			if (outputStream != null) {
				outputStream.close();
			}
			socket.close();
		} catch (IOException ioe) {
			Log.e("doDisconnect",
					"unable to close() socket during connection failure", ioe);
		}
	}

}
