package lewei50.helpers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.util.Log;

public class UDPClient {

	private static final int SERVER_PORT = 9959;

	private DatagramSocket dSocket = null;

	/**
	 * @param msg
	 */
	public UDPClient() {
		super();

	}

	DatagramSocket udpSocket = null;

	/**
	 * ������Ϣ��������
	 */
	public String send(String ipAddress, String msg) {
		StringBuilder sb = new StringBuilder();
		InetAddress local = null;

		try {
			local = InetAddress.getByName(ipAddress);

		} catch (UnknownHostException e) {
			sb.append("δ�ҵ�������.").append("/n");

		}
		try {
			dSocket = new DatagramSocket(); // ע��˴�Ҫ���������ļ�������Ȩ��,�������Ȩ�޲�����쳣

		} catch (SocketException e) {
			e.printStackTrace();
			sb.append("����������ʧ��.").append("/n");
		}
		int msg_len = msg == null ? 0 : msg.length();
		DatagramPacket dPacket = new DatagramPacket(msg.getBytes(), msg_len,
				local, SERVER_PORT);
		try {
			InetAddress broadcastAddr;

			broadcastAddr = InetAddress.getByName(ipAddress);
			dPacket.setAddress(broadcastAddr);

			dSocket.send(dPacket);

		} catch (IOException e) {
			e.printStackTrace();
			sb.append("��Ϣ����ʧ��.").append("/n");
		}
		dSocket.close();
		return sb.toString();
	}

}
