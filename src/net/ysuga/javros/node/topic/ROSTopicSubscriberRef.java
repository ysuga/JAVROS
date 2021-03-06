/**
 * ROSTopicPublisherRef.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.topic;


import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import net.ysuga.javros.node.ROSNode;
import net.ysuga.javros.node.SlaveAPIHelper;
import net.ysuga.javros.node.SlaveAPIRef;
import net.ysuga.javros.transport.TCPROSHeader;
import net.ysuga.javros.transport.TCPROSTransport;
import net.ysuga.javros.transport.TransportException;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.javros.value.ROSValue;
import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

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

	private static Logger logger = Logger.getLogger(ROSTopicSubscriberRef.class.getName());
	
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
	 * <div lang="ja"> �R���X�g���N�^
	 * 
	 * @param hostUri
	 * @param hostUri
	 * @throws TransportException
	 * @throws MalformedURLException
	 *             </div>
	 * @throws XmlRpcRequestException
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public ROSTopicSubscriberRef(String subscriberName, URL uri,
			ROSTopic topic, ROSNode owner) throws TransportException {
		super(new SlaveAPIRef(uri));
		this.ownerNode = owner;
		this.name = subscriberName;
		this.topic = topic;
		transport = new TCPROSTransport(ROS_PORT_BASE);
		this.protocol = new ROSUri("TCPROS", owner.getSlaveServerUri(),
				transport.getPort());

		header = new TCPROSHeader();
		header.put("callerid", owner.getName());
		header.put("topic", topic.getName());
		header.put("type", topic.getType());
		header.put("md5sum", topic.getMd5sum());
		header.put("latching", "0");
		thread = new Thread(this);
		thread.start();
	}

	public void run() {
		try {
			accept();
			while (true) {
				if (ownerNode.isTopicPublished(topic)) {
					ROSValue value = ownerNode
							.getPublishedTopicValue(topic);
					if (value != null) {
						byte[] b = value.serialize();
						transport.send(b);
					}
				}
				Thread.yield();
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.severe("ROSTopicSubscriberRef.run failed:\n"
					+ sw.getBuffer().toString());		
		}
	}

	private void accept() throws TransportException {
		byte[] headerData = header.serialize();
		transport.accept();
		TCPROSHeader rcvHeader = new TCPROSHeader(transport.receive());
		this.name = rcvHeader.get("callerid");
		transport.send(headerData);

	}

	private void disconnect() throws TransportException {
		transport.disconnect();

	}

	public void cleanup() throws TransportException {
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
		String destinationId = name;// protocol.getUri();
		String direction = "o";
		String transport = protocol.getProtocol();
		String topic = this.topic.getName();
		Boolean connected = new Boolean(true);
		return new Object[] { connectionId, destinationId, direction,
				transport, topic, connected };
	}
}