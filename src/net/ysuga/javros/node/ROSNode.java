/**
 * ROSNode.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/11
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import net.ysuga.javros.ROS;
import net.ysuga.javros.core.ROSCoreRef;
import net.ysuga.javros.core.rosref.RosRefException;
import net.ysuga.javros.node.parameter.ROSParameter;
import net.ysuga.javros.node.topic.ROSTopic;
import net.ysuga.javros.node.topic.ROSTopicFactory;
import net.ysuga.javros.node.topic.ROSTopicPublisherRef;
import net.ysuga.javros.node.topic.ROSTopicSubscriberRef;
import net.ysuga.javros.transport.TransportException;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.javros.value.ROSValue;
import net.ysuga.javros.xmlrpc.ROSXmlRpcServer;
import net.ysuga.javros.xmlrpc.ROSXmlRpcServerException;
import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

/**
 * ROS node class.
 * 
 * This class is the node object of ROS world. You can register this node as a
 * publisher/subscriber/service provider to ROSCore service.
 * 
 * You can publish specific topic value with publish method, and If you register
 * the node as subscriber, you can use subscribe method to get topic value.
 * 
 * @author ysuga
 * 
 */
public abstract class ROSNode {

	private Object topicValueMapMutex;

	private Map<ROSTopic, ROSValue> subscribedTopicValueMap;

	private Map<ROSTopic, Boolean> topicPublishedFlagMap;

	private Map<ROSTopic, ROSValue> publishedTopicValueMap;
	
	private Set<ROSTopic> publishingTopicSet;

	private Set<ROSTopic> subscribingTopicSet;

	private Map<String, ROSParameter> subscribingParameterMap;

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
	 * 
	 * @return
	 */
	public String getName() {
		return nodeName;
	}

	/**
	 * String form of this class (name of node). return
	 */
	final public String toString() {
		return getName() + ":ROSNode";
	}

	private SlaveAPIImpl xmlRpcService;

	/**
	 * Accessing XmlRpcService class object.
	 * 
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
	 * 
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
	 * Constructor. This starts Xml-Rpc Slave API service automatically.
	 * 
	 * @param hostAddress
	 *            host address of this machine.
	 * @param port
	 *            port of xml-rpc service providing.
	 * @param nodeName
	 * @throw ROSNodeException
	 */
	public ROSNode(String nodeName) throws ROSNodeException {
		subscriberMap = new HashMap<ROSTopic, ROSTopicSubscriberRef>();
		subscriberMapMutex = new Object();
		publisherMap = new HashMap<ROSUri, ROSTopicPublisherRef>();
		publisherMapMutex = new Object();
		publishingTopicSet = new HashSet<ROSTopic>();
		subscribingTopicSet = new HashSet<ROSTopic>();
		subscribingParameterMap = new HashMap<String, ROSParameter>();
		topicValueMapMutex = new Object();
		subscribedTopicValueMap = new HashMap<ROSTopic, ROSValue>();
		publishedTopicValueMap = new HashMap<ROSTopic, ROSValue>();
		topicPublishedFlagMap = new HashMap<ROSTopic, Boolean>();
		publishedTopicValueMapMutex = new Object();

		this.nodeName = nodeName;

		String hostAddress = ROS.getHostAddress();
		int port;
		try {
			port = ROS.searchFreePort();
		} catch (IOException e) {
			throw new ROSNodeException("cannnot find free port.");
		}

		// Starting Xml-Rpc Service.
		xmlRpcService = new SlaveAPIImpl(this);
		try {
			slaveServer = new ROSXmlRpcServer(this.xmlRpcService, hostAddress,
					port);
			slaveServer.start();
		} catch (ROSXmlRpcServerException e) {
			throw new ROSNodeException("creating xml-rpc server in ("
					+ hostAddress + ":" + port + ") failed.");
		}

		// Registering rosout topic publisher
		ROSTopic rosoutTopic;
		try {
			rosoutTopic = ROSTopicFactory.createROSTopic("/rosout",
					"rosgraph_msgs/Log");
		} catch (RosRefException e) {
			throw new ROSNodeException("creating rosout topic failed.");
		}
		registerPublisher(rosoutTopic);

		try {
			onInitialized();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param topic
	 * @throws ROSNodeException
	 */
	protected void registerPublisher(ROSTopic topic) throws ROSNodeException {
		try {
			ROSCoreRef.getInstance().registerPublisher(topic, this);
			publishingTopicSet.add(topic);
		} catch (XmlRpcRequestException e) {
			throw new ROSNodeException("register publisher in " + topic
					+ " failed.");
		}
	}

	protected void unregisterPublisher(ROSTopic topic) throws ROSNodeException {
		try {
			ROSCoreRef.getInstance().unregisterPublisher(topic, this);
		} catch (XmlRpcRequestException e) {
			logger.severe("ROSNode.unregisterPublisher failed.");
			throw new ROSNodeException("unregisterPublisher failed.");
		}
		publishingTopicSet.remove(topic);
	}

	/**
	 * 
	 * @param topic
	 * @throws ROSNodeException
	 */
	protected void registerSubscriber(ROSTopic topic) throws ROSNodeException {
		try {
			List<ROSUri> uriList = ROSCoreRef.getInstance().registerSubscriber(
					topic, this);
			subscribingTopicSet.add(topic);
			for (ROSUri uri : uriList) {
				addPublisherRef(uri, topic);
			}
		} catch (XmlRpcRequestException e) {
			throw new ROSNodeException("register subscriber in " + topic
					+ " failed.");
		}
	}

	/**
	 * 
	 * @param topic
	 * @throws ROSNodeException
	 */
	protected void unregisterSubscriber(ROSTopic topic) throws ROSNodeException {
		try {
			ROSCoreRef.getInstance().unregisterSubscriber(topic, this);
		} catch (XmlRpcRequestException e) {
			logger.severe("ROSNode.unregisterSubscriber failed.");
			throw new ROSNodeException("unregisterSubscriber failed.");
		}
		subscribingTopicSet.remove(topic);

	}

	/**
	 * 
	 * @param parameter
	 * @throws ROSNodeException
	 */
	protected void registerParameter(ROSParameter parameter)
			throws ROSNodeException {
		try {
			ROSCoreRef.getInstance().setParameter(parameter.getKey(),
					parameter.getValue());
			String updatedKey = ROSCoreRef.getInstance()
					.registerParameter(this, parameter.getKey());
			subscribingParameterMap.put(updatedKey, parameter);
		} catch (XmlRpcRequestException e) {
			logger.severe("ROSNode.registerParameter failed.");
			e.printStackTrace();
			throw new ROSNodeException("registerParameter failed.");
		}
	}

	/**
	 * 
	 * @param parameter
	 * @throws ROSNodeException
	 */
	protected void unregisterParameter(ROSParameter parameter)
			throws ROSNodeException {
		try {
			ROSCoreRef.getInstance().removeParameter(parameter.getKey());
		} catch (XmlRpcRequestException e) {
			logger.severe("ROSNode.unregisterParameter failed.");
			throw new ROSNodeException("unregisterParameter failed.");
		}
		subscribingParameterMap.remove(parameter.getKey());
	}

	/**
	 * 
	 */
	void shutdownServer() {
		slaveServer.shutdown();
	}

	/**
	 * 
	 * @param uri
	 * @param topic
	 * @throws MalformedURLException
	 * @throws XmlRpcRequestException
	 * @throws TransportException
	 */
	final protected void addPublisherRef(ROSUri uri, ROSTopic topic)
			throws ROSNodeException {
		ROSTopicPublisherRef publisher;
		try {
			publisher = new ROSTopicPublisherRef(new URL(uri.getUri()), topic,
					this);
		} catch (MalformedURLException e) {
			throw new ROSNodeException("uri(" + uri + ") is wrong.");
		} catch (XmlRpcRequestException e) {
			throw new ROSNodeException("XmlRpcRequest failed.");
		} catch (TransportException e) {
			throw new ROSNodeException("ROS Transport exception.");
		}
		publisherMap.put(uri, publisher);
	}

	/**
	 * unregister all subscriber and register.
	 */
	public void unregisterAll() {
		for (ROSTopic topic : subscribingTopicSet) {
			try {
				ROSCoreRef.getInstance().unregisterSubscriber(topic, this);
			} catch (XmlRpcRequestException e) {
				// TODO �����������ꂽ catch �u���b�N
				e.printStackTrace();
			}
		}
		for (ROSTopic topic : publishingTopicSet) {
			try {
				ROSCoreRef.getInstance().unregisterPublisher(topic, this);
			} catch (XmlRpcRequestException e) {
				// TODO �����������ꂽ catch �u���b�N
				e.printStackTrace();
			}
		}
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
	public void updatePublishedTopicValue(ROSTopic topic, ROSValue topicValue) {
		synchronized (topicValueMapMutex) {
			subscribedTopicValueMap.put(topic, topicValue);
		}
	}

	public ROSValue subscribe(ROSTopic topic) {
		synchronized (topicValueMapMutex) {
			return subscribedTopicValueMap.get(topic);
		}
	}

	public void publish(ROSTopic topic, ROSValue value) {
		synchronized (publishedTopicValueMapMutex) {
			publishedTopicValueMap.put(topic, value);
			topicPublishedFlagMap.put(topic, true);
		}
	}

	public boolean isTopicPublished(ROSTopic topic) {
		synchronized (publishedTopicValueMapMutex) {
			Boolean flag = topicPublishedFlagMap.get(topic);
			if (flag != null && flag) {
				return true;
			}
			return false;
		}
	}

	public ROSValue getPublishedTopicValue(ROSTopic topic) {
		synchronized (publishedTopicValueMapMutex) {
			return publishedTopicValueMap.get(topic);
		}
	}

	@Override
	public void finalize() {
		logger.entering(ROSNode.class.getName(), "finalize");
		try {
			onFinalized();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public abstract int onFinalized() throws Exception;

	public abstract int onExecute() throws Exception;

	public abstract int onInitialized() throws Exception;

	public ROSParameter getParameter(String parameterKey) {
		return subscribingParameterMap.get(parameterKey);
	}

}