/**
 * ROSTopicPublisherRef.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.tcpros;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.ysuga.javros.ROSUri;
import net.ysuga.ros.javros.api.ReturnValue;
import net.ysuga.ros.javros.api.SlaveAPIHelper;
import net.ysuga.ros.javros.api.SlaveAPIRef;
import net.ysuga.ros.javros.api.XmlRpcRequestException;

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
public class ROSTopicSubscriberRef extends SlaveAPIHelper implements Runnable {

	private static final int ROS_PORT_BASE = 40000;

	private ROSNode ownerNode;

	private ROSUri protocol;

	public final int getPort() {
		return transport.getPort();
	}

	private TCPROSTransport transport;

	private ROSTopic topic;

	private String name;
	
	private Thread thread;
	
	private TCPROSHeader header;
	

	/**
	 * <div lang="ja"> コンストラクタ
	 * 
	 * @param hostUri
	 * @param hostUri
	 * @throws MalformedURLException
	 *             </div>
	 * @throws XmlRpcRequestException
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public ROSTopicSubscriberRef(String subscriberName, ROSUri uri, ROSTopic topic, ROSNode owner)
			throws XmlRpcRequestException, UnknownHostException, IOException {
		super(new SlaveAPIRef(uri.getUri()));
		this.ownerNode = owner;
		this.name = subscriberName;
		this.topic = topic;
		transport = new TCPROSTransport(ROS_PORT_BASE);
		this.protocol = new ROSUri("TCPROS", owner.getHostAddress(), transport.getPort());
		thread = new Thread(this);
		thread.start();
		
		header = new TCPROSHeader();
		header.put("callerid", owner.getName());
		header.put("topic", topic.getName());
		header.put("type", topic.getType());
		header.put("md5sum", topic.getMd5sum());
		header.put("latching", "0");
	}

	public void run() {
		try {
			System.out.println("Start!!!");
			accept();
			while(true) {
				System.out.println("ROSTopicSubscriberRef#run()");
				Thread.sleep(500);
			}
		} catch (Exception e) {

		}
	}

	private void accept() throws UnknownHostException, IOException {
		byte[] headerData = header.serialize();
		if (true) {
			transport.accept();
			TCPROSHeader rcvHeader = new TCPROSHeader(transport.receive());
			this.name = rcvHeader.get("callerid");
			transport.send(headerData);
			
		} else {
			System.out.println("ROSTopicPublisherRef: protocol("
					+ protocol.getProtocol()
					+ ") do not support in current version");
			// TODO: append UDPROS transport.
			throw new IOException();
		}

	}

	private void disconnect() throws IOException {
		transport.disconnect();

	}

	public void cleanup() throws IOException {
		disconnect();
		ownerNode = null;
	}

	/**
	 * getBusInfo <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return [connectionId1, destinationId1, direction1, transport1, topic1,
	 *         connected1] connectionId is defined by the node and is opaque.
	 *         destinationId is the XMLRPC URI of the destination. direction is
	 *         one of 'i', 'o', or 'b' (in, out, both). transport is the
	 *         transport type (e.g. 'TCPROS'). topic is the topic name.
	 *         connected1 indicates connection status. Note that this field is
	 *         only provided by slaves written in </div>
	 */
	public Object[] getBusInfo(int counter) {
		Integer connectionId = new Integer(counter);
		String destinationId = name ;//protocol.getUri();
		String direction = "o";
		String transport = protocol.getProtocol();
		String topic = this.topic.getName();
		Boolean connected = new Boolean(true);
		return new Object[] { connectionId, destinationId, direction,
				transport, topic};
	}
}
