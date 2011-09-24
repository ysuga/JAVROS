package net.ysuga.javros;

import java.io.IOException;
import java.net.BindException;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.ysuga.javros.core.ROSCoreRef;
import net.ysuga.javros.node.ROSNode;

/**
 * 
 * @author ysuga
 * 
 */
public class ROS {

	static boolean initialized = false;

	private static String hostAddress;

	static final public String getHostAddress() {
		return hostAddress;
	}

	/**
	 * Initialization function. This must be called first.
	 * 
	 * @param hostAddress
	 * @param masterAddress
	 * @throws MalformedURLException
	 */
	static public void init(String hostAddress, String masterAddress)
			throws MalformedURLException {
		if (initialized)
			return;

		ROS.hostAddress = hostAddress;
		ROSCoreRef.init(masterAddress);
		nodeThreadList = new ArrayList<ROSNodeThread>();
		initialized = true;
	}

	/**
	 * Initialization function. This must be called first.
	 * 
	 * @param hostAddress
	 * @param masterAddress
	 * @param masterPort
	 * @throws MalformedURLException
	 */
	static public void init(String hostAddress, String masterAddress,
			int masterPort) throws MalformedURLException {
		if (initialized)
			return;

		ROS.hostAddress = hostAddress;
		ROSCoreRef.init(masterAddress, masterPort);
		nodeThreadList = new ArrayList<ROSNodeThread>();

		initialized = true;
	}

	/**
	 * Offset of Usable ROS Node's port offset.
	 */
	static public int PORT_OFFSET = 40000;

	public static int searchFreePort() throws IOException {
		while (true) {
			try {
				int port_range = 65535 - PORT_OFFSET;
				Random random = new Random();
				int port = PORT_OFFSET
						+ (int) (port_range * random.nextDouble());
				ServerSocket ss = new ServerSocket(port);
				ss.close();
				return port;
			} catch (BindException bind) {
				// Port is busy.
				// Do nothing and try again!
			}
		}
	}



	static List<ROSNodeThread> nodeThreadList;

	public static void launchNode(ROSNode node) {
		ROSNodeThread thread = new ROSNodeThread(node);
		thread.start();
		nodeThreadList.add(thread);
	}
	
	
	public static void exit() {
		for(ROSNodeThread thread: nodeThreadList) {
			thread.stopExecute();
		}
		System.exit(0);
	}

}

class ROSNodeThread extends Thread {
	private ROSNode node;
	private boolean endflag;

	public ROSNodeThread(ROSNode node) {
		this.node = node;
	}

	public void run() {

		try {
			node.onInitialized();
			while (!endflag) {
				node.onExecute();
			}
			node.onFinalized();
			node.unregisterAll();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopExecute() {
		this.endflag = true;
		try {
			join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}