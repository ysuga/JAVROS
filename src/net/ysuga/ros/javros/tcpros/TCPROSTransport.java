/**
 * TCPROSTransportBase.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.tcpros;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import net.ysuga.javros.util.ROSUri;

/**
 * <div lang="ja">
 * 
 * </div> <div lang="en">
 * 
 * </div>
 * 
 * @author ysuga
 * 
 */
public class TCPROSTransport {

	private int port;

	public int getPort() {
		return port;
	}

	private Socket socket;

	private ServerSocket serverSocket;

	public LittleEndianInputStream inputStream;

	protected LittleEndianOutputStream outputStream;

	/**
	 * <div lang="ja"> コンストラクタ </div> <div lang="en"> Constructor </div>
	 */
	public TCPROSTransport() {
	}

	public TCPROSTransport(int port_base) throws TransportException {
		try {
			while (true) {
				try {
					int port_range = 65535 - port_base;
					Random random = new Random();
					port = port_base + (int) (port_range * random.nextDouble());
					serverSocket = new ServerSocket(port);
					break;
				} catch (BindException bind) {

				}
			}
		} catch (IOException e) {
			throw new TransportException();
		}
	}

	public void connect(ROSUri uri) throws TransportException {
		connect(uri.getPath(), uri.getPort());
	}

	public void connect(String hostAddress, int port) throws TransportException {
		try {
			socket = new Socket(hostAddress, port);
			inputStream = new LittleEndianInputStream(new DataInputStream(
					socket.getInputStream()));
			outputStream = new LittleEndianOutputStream(new DataOutputStream(
					socket.getOutputStream()));
		} catch (IOException e) {
			throw new TransportException();
		}
	}

	public void accept() throws TransportException {
		if (serverSocket == null) {
			throw new TransportException();
		}
		try {
			socket = serverSocket.accept();
			inputStream = new LittleEndianInputStream(new DataInputStream(
					socket.getInputStream()));
			outputStream = new LittleEndianOutputStream(new DataOutputStream(
					socket.getOutputStream()));
		} catch (IOException e) {
			throw new TransportException();
		}
	}

	public void disconnect() throws TransportException {
		try {
			socket.close();
		} catch (IOException e) {
			throw new TransportException();
		}
		socket = null;
		inputStream = null;
		outputStream = null;
	}

	public void writeInt(int b) throws TransportException {
		try {
			outputStream.writeInt(b);
		} catch (IOException e) {
			e.printStackTrace();
			throw new TransportException();
		}
	}

	public int readInt() throws TransportException {
		try {
			return inputStream.readInt();
		} catch (IOException e) {
			e.printStackTrace();
			throw new TransportException();
		}
	}

	public byte readByte() throws TransportException {
		byte[] b = new byte[1];
		try {
			inputStream.read(b, 0, 1);
		} catch (IOException e) {
			throw new TransportException();
		}
		return b[0];
	}

	public void send(byte[] byteArray) throws TransportException {
		try {
			outputStream.writeInt(byteArray.length);
			outputStream.write(byteArray);
		} catch (IOException e) {
			e.printStackTrace();
			throw new TransportException();
		}
	}

	public byte[] receive() throws TransportException {
		try {
			int size = inputStream.readInt();
			byte[] b = new byte[size];
			inputStream.read(b, 0, size);
			return b;
		} catch (IOException e) {
			e.printStackTrace();
			throw new TransportException();
		}
	}
}
