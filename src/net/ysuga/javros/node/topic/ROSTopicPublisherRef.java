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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import net.ysuga.javros.node.ROSNode;
import net.ysuga.javros.node.SlaveAPIHelper;
import net.ysuga.javros.node.SlaveAPIRef;
import net.ysuga.javros.node.XmlRpcRequestException;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.javros.util.ReturnValue;
import net.ysuga.ros.javros.tcpros.LittleEndianInputStream;
import net.ysuga.ros.javros.tcpros.TCPROSHeader;
import net.ysuga.ros.javros.tcpros.TCPROSTransport;
import net.ysuga.ros.javros.tcpros.TransportException;

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
public class ROSTopicPublisherRef extends SlaveAPIHelper implements Runnable {

	private ROSNode ownerSubscriber;

	private ROSUri protocol;

	private TCPROSTransport transport;

	private ROSTopic topic;

	public ROSTopic getTopic() {
		return topic;
	}
	private TCPROSHeader header;
	
	private String name;
	

	private Thread thread;

	/**
	 * <div lang="ja"> �R���X�g���N�^
	 * 
	 * @param hostUri
	 * @param hostUri
	 * @throws MalformedURLException
	 *             </div>
	 * @throws XmlRpcRequestException
	 * @throws TransportException 
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	public ROSTopicPublisherRef(URL uri, ROSTopic topic,
			ROSNode owner) throws XmlRpcRequestException, TransportException {
		super(new SlaveAPIRef(uri));
		this.ownerSubscriber = owner;
		this.topic = topic;
		ReturnValue<ROSUri> ret = requestTopic(owner.getName(),topic.getName(), new Object[] {new Object[] { "TCPROS" } });
		protocol = ret.getValue();
		transport = new TCPROSTransport();
		
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
			connect();
			while (true) {
				byte[] value = transport.receive();
				ROSTopicValue topicValue = new ROSTopicValue(topic.getTopicTypeInfo(), value, this);
				this.ownerSubscriber.setTopicValue(this.topic, topicValue);
				Thread.sleep(30);
			}
		} catch (Exception e) {

		}
	}

	private void connect() throws TransportException {
		byte[] headerData = header.serialize();
		if (protocol.getProtocol().equals("TCPROS")) {

			transport.connect(protocol);
			transport.send(headerData);
			TCPROSHeader rcvHeader = new TCPROSHeader(transport.receive());
			this.name = rcvHeader.get("callerid");
		} else {
			System.out.println("ROSTopicPublisherRef: protocol("
					+ protocol.getProtocol()
					+ ") do not support in current version");
			// TODO: append UDPROS transport.
			throw new TransportException();
		}

	}

	private void disconnect() throws TransportException {
		transport.disconnect();

	}

	public void cleanup() throws TransportException {
		disconnect();
		ownerSubscriber = null;
	}
	
	public void receive() throws TransportException {
		try {
			byte[] val = transport.receive();
			LittleEndianInputStream is = new LittleEndianInputStream(val);
			int size = is.readInt();
			String v = is.readString(size);
			System.out.println("msg:::"+v);
		} catch (IOException e) {
			// TODO �����������ꂽ catch �u���b�N
			e.printStackTrace();
		}
		System.out.println("received.");
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
		String destinationId = getHostUri();//protocol.getUri();
		String direction = "i";
		String transport = protocol.getProtocol();
		String topic = this.topic.getName();
		Boolean connected = new Boolean(true);
		Object[] ret = new Object[] { connectionId, destinationId, direction,
				transport, topic, connected };
		return ret;
	}
}