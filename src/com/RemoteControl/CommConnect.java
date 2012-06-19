package com.RemoteControl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import android.util.Log;

public abstract class CommConnect {
	protected OutputStream outputStream = null;
	protected InputStream inputStream = null;
	protected Socket socket = null;
	private ConnectThread connectThread = null;

	public CommConnect() {
		super();
	}

	public void connect(Object address) {
		connectThread = new ConnectThread(address);
		connectThread.start();
	}

	protected abstract void doConnect(Object addr) throws IOException;

	public void disconnect() {
		doDisconnect();
	}

	protected abstract void doDisconnect();

	public class ConnectThread extends Thread {
		Object address = null;

		public ConnectThread(Object address) {
			this.address = address;
		}

		public void run() {
			try {
				doConnect(address);
			} catch (IOException ioe) {
				Log.e("Connect", "doConnect() failed", ioe);
				return;
			}
		}
	}

	public final void send(ControlData cd) {
		String s = String.valueOf(cd.getHead()) + ";"
				+ Float.toString(cd.getDistanceX()) + ";"
				+ Float.toString(cd.getDistanceY()) + ";" + String.valueOf(cd.getKeyCode());
		byte[] b = s.getBytes();
		byte l = (byte) b.length;
		Log.d("string", s);
		Log.d("string", Integer.toString(b.length));
		try {
			outputStream.write(l);
			outputStream.write(b);
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	protected Socket getSocket() {
		return socket;
	}

}
