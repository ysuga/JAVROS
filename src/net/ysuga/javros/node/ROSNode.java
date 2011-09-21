/**
 * ROSNode.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/11
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import net.ysuga.javros.ROSCore;
import net.ysuga.javros.core.rosref.RosRefException;
import net.ysuga.javros.node.topic.ROSTopic;
import net.ysuga.javros.node.topic.ROSTopicFactory;
import net.ysuga.javros.node.topic.ROSTopicPublisherRef;
import net.ysuga.javros.node.topic.ROSTopicSubscriberRef;
import net.ysuga.javros.node.topic.ROSTopicValue;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.ros.javros.tcpros.TransportException;

import org.apache.xmlrpc.XmlRpcException;

/**
 * ROS node class.
 * 
 * This class is the node object of ROS world.
 * You can register this node as a publisher/subscriber/service provider to ROSCore service.
 * 
 * You can publish specific topic value with publish method, and 
 * If you register the node as subscriber, you can use subscribe
 * method to get topic value.
 * @author ysuga
 *
 */
public class ROSNode {

	private Object topicValueMapMutex;

	private Map<ROSTopic, ROSTopicValue> subscribedTopicValueMap;

	private Map<ROSTopic, Boolean> topicPublishedFlagMap;

	private Map<ROSTopic, ROSTopicValue> publishedTopicValueMap;

	private Set<ROSTopic> publishingTopicSet;

	private Set<ROSTopic> subscribingTopicSet;

	/**
	 * 
	 */
	Object publisherMapMutex;

	/**
	 * Map of PublisherRef.
	 */
	Map<ROSUri, ROSTopicPublisherRef> publisherMap;

	/**
	 * 
	 */
	Object subscriberMapMutex;

	/**
	 * Map of SubscriberRef
	 */
	Map<ROSTopic, ROSTopicSubscriberRef> subscriberMap;

	/**
	 * logger
	 */
	static Logger logger = Logger.getLogger(ROSNode.class.getName());

	/**
	 * Name of this node.
	 */
	final private String nodeName;

	/**
	 * get name of node.
	 * @return
	 */
	public String getName() {
		return nodeName;
	}

	/**
	 * String form of this class (name of node).
	 * return 
	 */
	final public String toString() {
		return getName() + ":ROSNode";
	}

	private SlaveAPIImpl xmlRpcService;

	/**
	 * Accessing XmlRpcService class object.
	 * @return
	 */
	final public SlaveAPIImpl getXmlRpcService() {
		return xmlRpcService;
	}

	/**
	 * XML-RPC slave server.
	 */
	private ROSXmlRpcServer slaveServer;

	private Object publishedTopicValueMapMutex;

	/**
	 * get xml-rpc slave server's uri 
	 * @return
	 */
	final public String getSlaveServerUri() {
		return slaveServer.getUri().getUri() + "/";
	}

	/**
	 * getSlaveServerAddress 
	 */
	final public String getSlaveServerAddress() {
		return slaveServer.getUri().getPath();
	}

	/**
	 * Constructor
	 * @param hostAddress host address of this machine.
	 * @param port port of xml-rpc service providing.
	 * @param nodeName
	 * @throws XmlRpcException
	 * @throws ROSXmlRpcServerException
	 * @throws XmlRpcRequestException
	 * @throws RosRefException
	 */
	public ROSNode(String hostAddress, int port, String nodeName)
			throws XmlRpcException, ROSXmlRpcServerException,
			XmlRpcRequestException, RosRefException {
		subscriberMap = new HashMap<ROSTopic, ROSTopicSubscriberRef>();
		subscriberMapMutex = new Object();
		publisherMap = new HashMap<ROSUri, ROSTopicPublisherRef>();
		publisherMapMutex = new Object();
		publishingTopicSet = new HashSet<ROSTopic>();
		subscribingTopicSet = new HashSet<ROSTopic>();
		topicValueMapMutex = new Object();
		subscribedTopicValueMap = new HashMap<ROSTopic, ROSTopicValue>();
		publishedTopicValueMap = new HashMap<ROSTopic, ROSTopicValue>();
		topicPublishedFlagMap = new HashMap<ROSTopic, Boolean>();
		publishedTopicValueMapMutex = new Object();

		// this.hostAddress = hostAddress;
		this.nodeName = nodeName;

		ROSTopic rosoutTopic;
		rosoutTopic = ROSTopicFactory.createROSTopic("/rosout",
				"rosgraph_msgs/Log");

		xmlRpcService = new SlaveAPIImpl(this);
		slaveServer = new ROSXmlRpcServer(this.xmlRpcService, hostAddress, port);
		slaveServer.start();
		ROSCore.getInstance().registerPublisher(rosoutTopic, this);
	}
	
	public void shutdownServer() {
		slaveServer.shutdown();
	}

	/**
	 * addPublisherRef <div lang="ja">
	 * 
	 * @param uri
	 * @param topic
	 *            </div> <div lang="en">
	 * 
	 * @param uri
	 * @param topic
	 *            </div>
	 * @throws TransportException
	 * @throws XmlRpcRequestException
	 * @throws MalformedURLException
	 */
	public void addPublisherRef(ROSUri uri, ROSTopic topic)
			throws MalformedURLException, XmlRpcRequestException,
			TransportException {
		ROSTopicPublisherRef publisher = new ROSTopicPublisherRef(new URL(uri
				.getUri()), topic, this);
		publisherMap.put(uri, publisher);
	}

	/**
	 * unregisterAll <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 */
	public void unregisterAll() {
		for (ROSTopic topic : subscribingTopicSet) {
			try {
				ROSCore.getInstance().unregisterSubscriber(topic, this);
			} catch (XmlRpcRequestException e) {
				// TODO �����������ꂽ catch �u���b�N
				e.printStackTrace();
			}
		}
		for (ROSTopic topic : publishingTopicSet) {
			try {
				ROSCore.getInstance().unregisterPublisher(topic, this);
			} catch (XmlRpcRequestException e) {
				// TODO �����������ꂽ catch �u���b�N
				e.printStackTrace();
			}
		}
	}

	/**
	 * addSubscribingTopic <div lang="ja">
	 * 
	 * @param topic
	 *            </div> <div lang="en">
	 * 
	 * @param topic
	 *            </div>
	 */
	public void addSubscribingTopic(ROSTopic topic) {
		subscribingTopicSet.add(topic);
	}

	/**
	 * 
	 * removeSubscribingTopic <div lang="ja">
	 * 
	 * @param topic
	 *            </div> <div lang="en">
	 * 
	 * @param topic
	 *            </div>
	 */
	public void removeSubscribingTopic(ROSTopic topic) {
		subscribingTopicSet.remove(topic);
	}

	/**
	 * 
	 * addPulishingTopic <div lang="ja">
	 * 
	 * @param topic
	 *            </div> <div lang="en">
	 * 
	 * @param topic
	 *            </div>
	 */
	public void addPublishingTopic(ROSTopic topic) {
		publishingTopicSet.add(topic);
	}

	/**
	 * 
	 * removePulishingTopic <div lang="ja">
	 * 
	 * @param topic
	 *            </div> <div lang="en">
	 * 
	 * @param topic
	 *            </div>
	 */
	public void removePulishingTopic(ROSTopic topic) {
		publishingTopicSet.remove(topic);
	}

	/**
	 * setTopicValue <div lang="ja">
	 * 
	 * @param topic
	 * @param topicValue
	 *            </div> <div lang="en">
	 * 
	 * @param topic
	 * @param topicValue
	 *            </div>
	 */
	public void setTopicValue(ROSTopic topic, ROSTopicValue topicValue) {
		synchronized (topicValueMapMutex) {
			subscribedTopicValueMap.put(topic, topicValue);
		}
	}

	public ROSTopicValue subscribe(ROSTopic topic) {
		synchronized (topicValueMapMutex) {
			return subscribedTopicValueMap.get(topic);
		}
	}

	public void publish(ROSTopicValue value) {
		synchronized (publishedTopicValueMapMutex) {
			ROSTopic topic = value.getTopic();
			publishedTopicValueMap.put(topic, value);
			topicPublishedFlagMap.put(topic, true);
		}
	}

	public boolean topicPublished(ROSTopic topic) {
		synchronized (publishedTopicValueMapMutex) {
			Boolean flag = topicPublishedFlagMap.get(topic);
			if (flag != null && flag) {
				return true;
			}
			return false;
		}
	}

	public ROSTopicValue getPublishedTopicValue(ROSTopic topic) {
		synchronized (publishedTopicValueMapMutex) {
			return publishedTopicValueMap.get(topic);
		}
	}

}