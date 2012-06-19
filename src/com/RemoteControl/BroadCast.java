package com.RemoteControl;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class BroadCast {
	public void sendBroadcast() throws Exception {
		DatagramSocket socket;
		DatagramPacket packet;
		byte[] data = { 'z', 'h', 'y', 'y' };

		socket = new DatagramSocket();
		socket.setBroadcast(true);
		// send端指定接受端的端口，自己的端口是随机的
		packet = new DatagramPacket(data, data.length,
				InetAddress.getByName("255.255.255.255"), 51705);
		socket.send(packet);
	}

	public String receiveBroadcast() throws Exception {
		byte[] buffer = new byte[65507];
		DatagramSocket server = new DatagramSocket(51705);
		DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
		while (true) {
			server.receive(packet);
			String s = new String(packet.getData(), 0, packet.getLength());
			return s;
		}
	}
}
