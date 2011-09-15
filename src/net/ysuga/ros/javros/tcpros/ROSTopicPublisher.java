/**
 * ROSTopicPublisher.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.tcpros;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import net.ysuga.javros.ROSCore;
import net.ysuga.javros.ROSUri;
import net.ysuga.javros.remote.NoRemoteCommandServiceException;
import net.ysuga.ros.javros.api.ROS;
import net.ysuga.ros.javros.api.ROSXmlRpcServer;
import net.ysuga.ros.javros.api.SlaveAPI;
import net.ysuga.ros.javros.api.XmlRpcRequestException;

import org.apache.xmlrpc.XmlRpcException;

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
public class ROSTopicPublisher implements SlaveAPI {

	static Logger logger = Logger.getLogger(ROSTopicPublisher.class.getName());
	
	private ROSTopicSubscriberRef subscriberRef;

	
	
	private Object subscriberMapMutex;

	private final ROSTopic topic;

	public final ROSTopic getTopic() {
		return topic;
	}

	private final String publisherName;

	public final String getName() {
		return publisherName;
	}

	private final HashMap<String, ROSTopicSubscriberRef> subscriberMap;

	private TCPROSHeader header;
	
	public TCPROSHeader getHeader() {
		return header;
	}

	private ROSXmlRpcServer xmlRpcServer;

	public String getXmlRpcUri() {
		return xmlRpcServer.getUri();
	}
	
	private String hostAddress;
	
	final public String getHostAddress() {
		return hostAddress;
	}
	private Thread serverHookThread;
	
	/**
	 * <div lang="ja"> コンストラクタ </div> <div lang="en"> Constructor </div>
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws NoRemoteCommandServiceException
	 * @throws XmlRpcRequestException
	 * @throws ROSServiceNotFoundException
	 * @throws XmlRpcException
	 */
	public ROSTopicPublisher(String hostAddress, int port,
			String publisherName, ROSTopic topic) throws UnknownHostException,
			IOException, XmlRpcRequestException,
			NoRemoteCommandServiceException, ROSServiceNotFoundException,
			XmlRpcException {
		subscriberMap = new HashMap<String, ROSTopicSubscriberRef>();
		subscriberMapMutex = new Object();

		this.hostAddress = hostAddress;
		this.topic = topic;
		this.publisherName = publisherName;

		xmlRpcServer = new ROSXmlRpcServer(this, hostAddress, port);
		xmlRpcServer.start();
		
		//List<ROSUri> uris = ROSCore.getInstance().registerPublisher(this);
		
		
	}


	
	public void shutdown() {
		xmlRpcServer.shutdown();
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
		
	//	int connectionNum = 0;
		ArrayList<Object> objectArray = new ArrayList<Object>();
		synchronized(subscriberMapMutex) {
			for(ROSTopicSubscriberRef subscriber : subscriberMap.values()) {
			//	Object[] busInfo = subscriber.getBusInfo();
			//	objectArray.add(busInfo);
			}
		}
		return new Object[]{new Integer(1), "OK", objectArray.toArray()};
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
		logger.entering(this.getClass().getName(), "getMasterUri");
		String masterUri = ROS.getInstance().getUri();
		return new Object[] { masterUri };
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
	public Object[] shutdown(String callerId) throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "shutdown(" + callerId + ")");
		shutdown();
		return new Object[]{new Integer(1), "OK", new Integer(0)};
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param msg
	 * @return
	 * @throws XmlRpcRequestException
	 *             </div> <div lang="en">
	 * @param callerId
	 * @param msg
	 * @return
	 * @throws XmlRpcRequestException
	 *             </div>
	 */
	@Override
	public Object[] shutdown(String callerId, String msg)
			throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "shutdown(" + callerId + ", " + msg + ")");
		shutdown();
		return new Object[]{new Integer(1), "OK", new Integer(0)};
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
	public Object[] getPid(String callerId) throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "getPid");
		int pid = 10001;
		return new Object[] {new Integer(1), "OK", new Integer(pid) };
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
	public Object[] getSubscriptions(String callerId)
			throws XmlRpcRequestException {
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
	public Object[] getPublications(String callerId)
			throws XmlRpcRequestException {
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

		List<Object> currentPublisher = Arrays.asList(publishers);

		/**
		synchronized (subscriberMapMutex) {
			try {
				for (Object o : currentPublisher) {
					if (!this.subscriberMap.containsKey(o)) {
						ROSTopicPublisherRef publisherRef = new ROSTopicPublisherRef(
								"publisher", new ROSUri((String) o), this);
						subscriberMap.put((String) o, publisherRef);
					}
				}

				for (String uri : subscriberMap.keySet()) {
					if (!currentPublisher.contains(uri)) {
						ROSTopicPublisherRef publisherRef = subscriberMap
								.remove(uri);
						publisherRef.cleanup();
						publisherRef = null;
					}
				}
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return new Object[] { ROSAPIStatusCode.ROSAPI_FAILURE,
						"MalformedURLException", 0 };
			} catch (XmlRpcRequestException e) {
				e.printStackTrace();
				return new Object[] { ROSAPIStatusCode.ROSAPI_FAILURE,
						"XmlRpcRequestException", 0 };
			} catch (UnknownHostException e) {
				e.printStackTrace();
				return new Object[] { ROSAPIStatusCode.ROSAPI_FAILURE,
						"UnknownHostException", 0 };
			} catch (IOException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				return new Object[] { ROSAPIStatusCode.ROSAPI_FAILURE,
						"IOException", 0 };
			}
		}
		*/
		return new Object[] { 1, "OK", 0 };
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param topic
	 * @param protocols
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param topic
	 * @param protocols
	 * @return </div>
	 */
	@Override
	public Object[] requestTopic(String callerId, String topic,
			Object[] protocols) {
		logger.entering(this.getClass().getName(), "requestTopic(" + callerId + ", " + topic + ")");
		/*
		try {
			Object[] protocol = (Object[])protocols[0];
			ROSUri uri = ROSCore.getInstance().getNodeUri((String)callerId);
			subscriberRef = new ROSTopicSubscriberRef(callerId, uri, this);
			this.subscriberMap.put(topic, subscriberRef);
			Thread.yield();
		} catch (UnknownHostException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (XmlRpcRequestException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		*/
		Object[] ret = new Object[] { new Integer(1), "OK", new Object[] { "TCPROS", hostAddress, new Integer(subscriberRef.getPort())} };
		return ret;
	}
	
}
