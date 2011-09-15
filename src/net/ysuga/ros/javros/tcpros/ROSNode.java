/**
 * ROSNode.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/11
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.tcpros;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import net.ysuga.javros.ROSCore;
import net.ysuga.javros.ROSUri;
import net.ysuga.javros.remote.NoRemoteCommandServiceException;
import net.ysuga.ros.javros.api.ROS;
import net.ysuga.ros.javros.api.ROSAPIStatusCode;
import net.ysuga.ros.javros.api.ROSXmlRpcServer;
import net.ysuga.ros.javros.api.ReturnValue;
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
public class ROSNode implements SlaveAPI {
	Object publisherMapMutex;
	Map<ROSUri, ROSTopicPublisherRef> publisherMap;
	
	Object subscriberMapMutex;
	Map<ROSTopic, ROSTopicSubscriberRef> subscriberMap;
	
	static Logger logger = Logger.getLogger(ROSNode.class.getName());

	
	private String nodeName;
	
	private String hostAddress;
	
	final public String getHostAddress() {
		return hostAddress;
	}
	
	private ROSXmlRpcServer xmlRpcServer;
	
	public String getXmlRpcUri() {
		return xmlRpcServer.getUri() + "/";
	}
	
	
	
	public String getName() {
		return nodeName;
	}
	
	
	public ROSNode (String hostAddress, int port,
			String nodeName) throws UnknownHostException,
			IOException, XmlRpcRequestException,
			NoRemoteCommandServiceException, ROSServiceNotFoundException,
			XmlRpcException {
		subscriberMap = new HashMap<ROSTopic, ROSTopicSubscriberRef>();
		subscriberMapMutex = new Object();
		publisherMap = new HashMap<ROSUri, ROSTopicPublisherRef>();
		publisherMapMutex = new Object();

		this.hostAddress = hostAddress;
		this.nodeName = nodeName;
		
		xmlRpcServer = new ROSXmlRpcServer(this, hostAddress, port);
		xmlRpcServer.start();

		/**
		header = new TCPROSHeader();
		header.put("callerid", publisherName);
		header.put("topic", topic.getName());
		header.put("type", topic.getType());
		header.put("md5sum", topic.getMd5sum());
		header.put("latching", "0");
		**/
		
		//List<ROSUri> uris = ROSCore.getInstance().registerPublisher(this);
		ROSTopic rosoutTopic = ROSTopicFactory.createROSTopic("/rosout");
		List<ROSUri> ret = ROSCore.getInstance().registerPublisher(rosoutTopic, this);
		
	}
	
	
	public void shutdown() {
		//this.xmlRpcServer.shutdown();
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
		Object[] ret = new Object[] { new Integer(1), "bus info", objectArray.toArray() };
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
		return new Object[] { new Integer(1), "OK", new Integer(0) };
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
	public Object[] getPid(String callerId) throws XmlRpcRequestException {
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

		ROSTopic rosTopic = null;
		try {
			rosTopic = ROSTopicFactory.createROSTopic(topic);
		} catch (UnknownHostException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (XmlRpcRequestException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (NoRemoteCommandServiceException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (ROSServiceNotFoundException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		
		if(rosTopic == null) {
			return new Object[] {new Integer(0), "failed to create ROSTopic", new Object()};
		}
		
		List<Object> currentPublisher = Arrays.asList(publishers);

		synchronized (publisherMapMutex) {
			try {
				for (Object o : currentPublisher) {
					if (!this.publisherMap.containsKey(o)) {
						ROSTopicPublisherRef publisherRef = new ROSTopicPublisherRef(
								new ROSUri((String) o), rosTopic, this);
						publisherMap.put(new ROSUri((String)o), publisherRef);
					}
				}

				for (ROSUri publisherUri : publisherMap.keySet()) {
					if (!currentPublisher.contains(publisherUri)) {
						ROSTopicPublisherRef publisherRef = publisherMap
								.remove(publisherUri);
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
		return new Object[] { new Integer(1), "publisher update", new Integer(0) };
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
		try {
			Object[] protocol = (Object[])protocols[0];
			ROSUri uri = ROSCore.getInstance().getNodeUri((String)callerId);
			ROSTopic rosTopic = ROSTopicFactory.createROSTopic(topic);
			ROSTopicSubscriberRef subscriberRef = new ROSTopicSubscriberRef(callerId, uri, rosTopic, this);
			this.subscriberMap.put(rosTopic, subscriberRef);
			Thread.yield();
			Object[] ret = new Object[] { new Integer(1), "request topic", new Object[] { "TCPROS", hostAddress, new Integer(subscriberRef.getPort())} };
			return ret;
		} catch (UnknownHostException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (XmlRpcRequestException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (NoRemoteCommandServiceException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ROSServiceNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return new Object[] {new Integer(0), "failed.", new Object()};
	}



	/**
	 * addPublisherRef
	 * <div lang="ja">
	 * 
	 * @param publisher
	 * </div>
	 * <div lang="en">
	 *
	 * @param publisher
	 * </div>
	 */
	public void addPublisherRef(ROSTopicPublisherRef publisher) {
		this.publisherMap.put(new ROSUri(publisher.getHostUri()), publisher);
	}
}
