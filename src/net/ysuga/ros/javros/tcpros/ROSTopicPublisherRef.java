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
public class ROSTopicPublisherRef extends SlaveAPIHelper {

	private ROSNode ownerSubscriber;

	private ROSUri protocol;

	private TCPROSTransport transport;

	private ROSTopic topic;

	public ROSTopic getTopic() {
		return topic;
	}
	private TCPROSHeader header;
	
	private String name;

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
	public ROSTopicPublisherRef(ROSUri hostUri, ROSTopic topic,
			ROSNode owner) throws XmlRpcRequestException,
			UnknownHostException, IOException {
		super(new SlaveAPIRef(hostUri.getUri()));
		this.ownerSubscriber = owner;
		//this.name = publisherName;
		this.topic = topic;
		ReturnValue<ROSUri> ret = requestTopic(owner.getName(),topic.getName(), new Object[] {new Object[] { "TCPROS" } });
		protocol = ret.getValue();
		// transportList = new ArrayList<TCPROSTransport>();
		transport = new TCPROSTransport();
		
		header = new TCPROSHeader();
		header.put("callerid", owner.getName());
		header.put("topic", topic.getName());
		header.put("type", topic.getType());
		header.put("md5sum", topic.getMd5sum());
		header.put("latching", "0");

		connect();
	}

	private void connect() throws UnknownHostException, IOException {
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
			throw new IOException();
		}

	}

	private void disconnect() throws IOException {
		transport.disconnect();

	}

	public void cleanup() throws IOException {
		disconnect();
		ownerSubscriber = null;
	}
	
	public void receive() {
		try {
			byte[] val = transport.receive();
			LittleEndianInputStream is = new LittleEndianInputStream(val);
			int size = is.readInt();
			String v = is.readString(size);
			System.out.println("msg:::"+v);
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
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
		
		//	System.out.println(ret);
		return ret;

	}
}
