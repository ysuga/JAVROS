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
import java.net.UnknownHostException;
import java.util.Random;

import net.ysuga.javros.ROSUri;

/**
 * <div lang="ja">
 *
 * </div>
 * <div lang="en">
 *
 * </div>
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
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	public TCPROSTransport() {
	}

	public TCPROSTransport(int port_base) throws IOException {
		while(true) {
			try {
				int port_range = 65535 - port_base;
				Random random = new Random();
				port = port_base + (int)(port_range*random.nextDouble());
				serverSocket = new ServerSocket(port);
				break;
			} catch (BindException bind) {
				
			} 
		}
	}
	
	public void connect(ROSUri uri) throws UnknownHostException, IOException {
		connect(uri.getPath(), uri.getPort());
	}
	
	public void connect(String hostAddress, int port) throws UnknownHostException, IOException {
		socket = new Socket(hostAddress, port);
		inputStream = new LittleEndianInputStream(new DataInputStream(socket.getInputStream()));
		outputStream = new LittleEndianOutputStream(new DataOutputStream(socket.getOutputStream()));
	}
	
	public void accept() throws IOException {
		if(serverSocket == null) {
			throw new IOException();
		}
		socket = serverSocket.accept();
		inputStream = new LittleEndianInputStream(new DataInputStream(socket.getInputStream()));
		outputStream = new LittleEndianOutputStream(new DataOutputStream(socket.getOutputStream()));
	}

	public void disconnect() throws IOException {
		socket.close();
		socket = null;
		inputStream = null;
		outputStream = null;
	}
	
	public void send(byte[] byteArray) throws IOException {
		outputStream.writeInt(byteArray.length);
		outputStream.write(byteArray);
	}
	
	public byte[] receive() throws IOException {
		int size = inputStream.readInt();
		byte[] b = new byte[size];
		inputStream.read(b, 0, size);
		return b;
	}
}
