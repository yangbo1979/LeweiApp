package lewei50.helpers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import lewei50.entities.jDeviceInfo;

public class UDPServer implements Runnable {

	private static final int PORT = 9960;

	private byte[] msg = new byte[1024];

	private boolean life = true;

	public String ReceiveString;

	public String GetReceiveString() {
		return ReceiveString;
	}

	public UDPServer() {
	}

	/**
	 * @return the life
	 */
	public boolean isLife() {
		return life;
	}

	/**
	 * @param life
	 *            the life to set
	 */
	public void setLife(boolean life) {
		this.life = life;
	}

	DatagramSocket dSocket;

	public DatagramPacket dPacket;

	@Override
	public void run() {
		dSocket = null;
		dPacket = new DatagramPacket(msg, msg.length);
		try {
			dSocket = new DatagramSocket(PORT);
			dSocket.setSoTimeout(1000);// 设置1秒超时
			// while (life) {
			try {
				dSocket.receive(dPacket);
				ReceiveString = new String(dPacket.getData(),
						dPacket.getOffset(), dPacket.getLength());
				
			} catch (IOException e) {
				// 超过2秒还没有接收到数据就报错了
				life = false;
				e.printStackTrace();
			}
			// }
		} catch (SocketException e) {
			e.printStackTrace();
		}

	}

	public void stop() {
		dSocket.close();
	}
}