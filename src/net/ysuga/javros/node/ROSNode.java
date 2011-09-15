/**
 * ROSNode.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/11
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.ysuga.javros.ROSCore;
import net.ysuga.javros.node.topic.ROSTopic;
import net.ysuga.javros.node.topic.ROSTopicFactory;
import net.ysuga.javros.node.topic.ROSTopicPublisherRef;
import net.ysuga.javros.node.topic.ROSTopicSubscriberRef;
import net.ysuga.javros.remote.RemoteCommandServiceException;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.ros.javros.tcpros.TransportException;

import org.apache.xmlrpc.XmlRpcException;

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
public class ROSNode implements SlaveAPI {

	/**
	 * 
	 */
	private Object publisherMapMutex;

	/**
	 * Map of PublisherRef.
	 */
	private Map<ROSUri, ROSTopicPublisherRef> publisherMap;

	/**
	 * 
	 */
	private Object subscriberMapMutex;

	/**
	 * Map of SubscriberRef
	 */
	private Map<ROSTopic, ROSTopicSubscriberRef> subscriberMap;

	/**
	 * logger
	 */
	private static Logger logger = Logger.getLogger(ROSNode.class.getName());

	/**
	 * Name of this node.
	 */
	final private String nodeName;

	/**
	 * 
	 * getName <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return Name of this node. </div>
	 */
	public String getName() {
		return nodeName;
	}

	/**
	 * XML-RPC slave server.
	 */
	private ROSXmlRpcServer slaveServer;

	/**
	 * 
	 * getSlaveServerUri <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return slave server's uri</div>
	 */
	final public String getSlaveServerUri() {
		return slaveServer.getUri().getUri() + "/";
	}

	/**
	 * 
	 * getSlaveServerAddress <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return Host address of this machine </div>
	 */
	final public String getSlaveServerAddress() {
		return slaveServer.getUri().getPath();
	}

	/**
	 * 
	 * <div lang="ja"> コンストラクタ
	 * 
	 * @param hostAddress
	 * @param port
	 * @param nodeName
	 * @throws XmlRpcException
	 * @throws ROSXmlRpcServerException
	 * @throws XmlRpcRequestException
	 * @throws RemoteCommandServiceException
	 *             </div> <div lang="en"> Constructor
	 * @param hostAddress
	 * @param port
	 * @param nodeName
	 * @throws XmlRpcException
	 * @throws ROSXmlRpcServerException
	 * @throws XmlRpcRequestException
	 * @throws RemoteCommandServiceException
	 *             </div>
	 */
	public ROSNode(String hostAddress, int port, String nodeName)
			throws XmlRpcException, ROSXmlRpcServerException,
			XmlRpcRequestException, RemoteCommandServiceException {
		subscriberMap = new HashMap<ROSTopic, ROSTopicSubscriberRef>();
		subscriberMapMutex = new Object();
		publisherMap = new HashMap<ROSUri, ROSTopicPublisherRef>();
		publisherMapMutex = new Object();

		// this.hostAddress = hostAddress;
		this.nodeName = nodeName;

		slaveServer = new ROSXmlRpcServer(this, hostAddress, port);
		slaveServer.start();

		ROSTopic rosoutTopic = ROSTopicFactory.createROSTopic("/rosout");
		List<ROSUri> ret = ROSCore.getInstance().registerPublisher(rosoutTopic,
				this);
	}

	public void shutdown() {
		this.slaveServer.shutdown();
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @return </div>
	 */
	@Override
	public Object[] getBusStats(String callerId) {
		logger.entering(this.getClass().getName(), "getBusStats");
		int topicNum = 0;
		Object[] publishStats = new Object[topicNum];
		Object[] subscribeStats = new Object[topicNum];
		Object[] serviceStats = new Object[0];

		Object[] stats = new Object[3];
		stats[0] = publishStats;
		stats[1] = subscribeStats;
		stats[2] = serviceStats;

		Object[] retval = new Object[3];
		retval[0] = new Integer(0);
		retval[1] = new String("OK");
		retval[3] = stats;
		return retval;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 *             </div> <div lang="en">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 *             </div>
	 */
	@Override
	public Object[] getBusInfo(String callerId) {
		logger.entering(this.getClass().getName(), "getBusInfo");
		ArrayList<Object> objectArray = new ArrayList<Object>();
		int counter = 0;
		synchronized (publisherMapMutex) {
			for (ROSTopicPublisherRef publisher : publisherMap.values()) {
				Object[] busInfo = publisher.getBusInfo(counter);
				objectArray.add(busInfo);
				counter++;
			}
		}
		synchronized (subscriberMapMutex) {
			for (ROSTopicSubscriberRef subscriber : subscriberMap.values()) {
				Object[] busInfo = subscriber.getBusInfo(counter);
				objectArray.add(busInfo);
				counter++;
			}
		}
		Object[] ret = new Object[] { new Integer(1), "bus info",
				objectArray.toArray() };
		return ret;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 *             </div> <div lang="en">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 *             </div>
	 */
	@Override
	public Object[] getMasterUri(String callerId) throws XmlRpcRequestException {
		String masterUri = ROSCore.getInstance().getHostAddress();
		return new Object[] { masterUri };
	}

	/**
	 * 
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @return </div>
	 */
	@Override
	public Object[] shutdown(String callerId) {
		logger.entering(this.getClass().getName(), "shutdown(" + callerId + ")");
		shutdown();
		return new Object[] { new Integer(1), "OK", new Integer(0) };
	}

	/**
	 * 
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param msg
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param msg
	 * @return </div>
	 */
	@Override
	public Object[] shutdown(String callerId, String msg) {
		logger.entering(this.getClass().getName(), "shutdown(" + callerId
				+ ", " + msg + ")");
		shutdown();
		return new Object[] { new Integer(1), "OK", new Integer(0) };
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 *             </div> <div lang="en">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 *             </div>
	 */
	@Override
	public Object[] getPid(String callerId) {
		logger.entering(this.getClass().getName(), "getPid");
		int pid = 10001;
		return new Object[] { new Integer(1), "pid", new Integer(pid) };
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 *             </div> <div lang="en">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 *             </div>
	 */
	@Override
	public Object[] getSubscriptions(String callerId) {
		logger.entering(this.getClass().getName(), "getSubscriptions");
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 *             </div> <div lang="en">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 *             </div>
	 */
	@Override
	public Object[] getPublications(String callerId) {
		logger.entering(this.getClass().getName(), "getPublications");
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param parameterKey
	 * @param parameterValue
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param parameterKey
	 * @param parameterValue
	 * @return </div>
	 */
	@Override
	public Object[] paramUpdate(String callerId, String parameterKey,
			Object[] parameterValue) {
		logger.entering(this.getClass().getName(), "paramUpdate");
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param topic
	 * @param publishers
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param topic
	 * @param publishers
	 * @return </div>
	 */
	@Override
	public Object[] publisherUpdate(String callerId, String topic,
			Object[] publishers) {
		logger.entering(this.getClass().getName(), "publisherUpdate");

		try {
			ROSTopic rosTopic = null;
			rosTopic = ROSTopicFactory.createROSTopic(topic);

			List<Object> currentPublisher = Arrays.asList(publishers);

			synchronized (publisherMapMutex) {
				for (Object o : currentPublisher) {
					if (!this.publisherMap.containsKey(o)) {
						ROSTopicPublisherRef publisherRef = new ROSTopicPublisherRef(
								new URL((String) o), rosTopic, this);
						publisherMap.put(new ROSUri((String) o), publisherRef);
					}
				}

				for (ROSUri publisherUri : publisherMap.keySet()) {
					if (!currentPublisher.contains(publisherUri.getUri())) {
						ROSTopicPublisherRef publisherRef = publisherMap
								.remove(publisherUri);
						publisherRef.cleanup();
						publisherRef = null;
					}
				}
			}
			return new Object[] { new Integer(1), "publisher update",
					new Integer(0) };
		} catch (Exception e) {
			PrintWriter pw = new PrintWriter(new StringWriter());
			e.printStackTrace(pw);
			logger.severe("ROSNode.requestTopic failed:\n" + pw.toString());
		}

		return new Object[] { new Integer(0), "failed.", new Integer(0) };
	}

	/**
	 * 
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param topic
	 * @param protocols
	 * @return </div> <div lang="en"> Called by Topic subscriber.
	 * @param callerId
	 * @param topic
	 * @param protocols
	 * @return </div>
	 */
	@Override
	public Object[] requestTopic(String callerId, String topic,
			Object[] protocols) {
		logger.entering(this.getClass().getName(), "requestTopic(" + callerId
				+ ", " + topic + ")");
		try {
			ROSUri uri = ROSCore.getInstance().getNodeUri((String) callerId);
			ROSTopic rosTopic = ROSTopicFactory.createROSTopic(topic);
			ROSTopicSubscriberRef subscriberRef = new ROSTopicSubscriberRef(
					callerId, new URL(uri.getUri()), rosTopic, this);
			this.subscriberMap.put(rosTopic, subscriberRef);
			Thread.yield();
			Object[] ret = new Object[] {
					new Integer(1),
					"request topic",
					new Object[] { "TCPROS", getSlaveServerAddress(),
							new Integer(subscriberRef.getPort()) } };
			return ret;
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.severe("ROSNode.requestTopic failed:\n" + sw.getBuffer().toString());
		}

		return new Object[] { new Integer(0), "failed.", new Integer(0) };
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
		ROSTopicPublisherRef publisher = new ROSTopicPublisherRef(new URL(
				uri.getUri()), topic, this);
		publisherMap.put(uri, publisher);
	}

}
