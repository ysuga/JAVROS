/**
 * TCPROSServer.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.service;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

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
public class TCPROSServer {

	int port;

	ServerSocket serverSocket;
	Socket socket;
	InputStream inputStream;
	OutputStream outputStream;
	
	/**
	 * 
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 * @throws IOException 
	 */
	public TCPROSServer(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		socket = serverSocket.accept();
		System.out.println("Accepted!");
		inputStream = new DataInputStream(socket.getInputStream());
		outputStream = new DataOutputStream(socket.getOutputStream());
	}

}
