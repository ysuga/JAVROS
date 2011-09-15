/**
 * SlaveAPIImpl.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/05
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.api;

import java.util.Hashtable;
import java.util.logging.Logger;

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
public class SlaveAPIImpl implements SlaveAPI {

	static Logger logger = Logger.getLogger(SlaveAPIImpl.class.getName());
	/**
	 * <div lang="ja">
	 * @param callerId
	 * @return
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @return
	 * </div>
	 */
	@Override
	public Object[] getBusStats(String callerId) {
		logger.entering(this.getClass().getName(), "getBusStatus");
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
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	@Override
	public Object[] getBusInfo(String callerId) {
		logger.entering(this.getClass().getName(), "getBusInfo");
		int connectionNum = 0;
		Object[] retval = new Object[connectionNum];
		return retval;
	}

	/**
	 * <div lang="ja">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	@Override
	public Object[] getMasterUri(String callerId)
			throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "getMasterUri");
		String masterUri = ROS.getInstance().getUri();
		
		return  new Object[]{masterUri};
	}

	/**
	 * <div lang="ja">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	@Override
	public Object[] shutdown(String callerId) throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "shutdown");
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/**
	 * <div lang="ja">
	 * @param callerId
	 * @param msg
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @param msg
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	@Override
	public Object[] shutdown(String callerId, String msg)
			throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "shutdown");
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/**
	 * <div lang="ja">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	@Override
	public Object[] getPid(String callerId) throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "getPid");
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	/**
	 * <div lang="ja">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
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
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
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
	 * @param callerId
	 * @param parameterKey
	 * @param parameterValue
	 * @return
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @param parameterKey
	 * @param parameterValue
	 * @return
	 * </div>
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
	 * @param callerId
	 * @param topic
	 * @param publishers
	 * @return
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @param topic
	 * @param publishers
	 * @return
	 * </div>
	 */
	@Override
	public Object[] publisherUpdate(String callerId, String topic,
			Object[] publishers) {
		logger.entering(this.getClass().getName(), "publisherUpdate");
		for(Object o : publishers) {
			System.out.println("Topic(" + topic + ") has publisher:" + o.toString());
		}
		
		return new Object[]{1, "OK", 0};
	}

	/**
	 * <div lang="ja">
	 * @param callerId
	 * @param topic
	 * @param protocols
	 * @return
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @param topic
	 * @param protocols
	 * @return
	 * </div>
	 */
	@Override
	public Object[] requestTopic(String callerId, String topic,
			Object[] protocols) {
		logger.entering(this.getClass().getName(), "requestTopic");
		System.out.println("requestTopic called.(" + callerId + ", " + topic + ", [" + protocols.length + "])");
		for(Object protocol: protocols) {
			Object[] props = (Object[])protocol;
			String protocolName = (String)props[0];
			logger.finer("argument:" + callerId + ", " + topic + ", " + (String)protocolName);
			for(Object o : props) {
				System.out.println("o=" + o.toString());
			}
		}
		/*
		Hashtable table = new Hashtable();
		table.put("code", new Integer(0));
		table.put("statusMessage", new String("OK"));
		table.put("protocolParams", new Object[]{"TCPROS"});
		
		return table;
		*
		*/
		return new Object[]{new Integer(0), "OK", new Object[]{"TCPROS"}};
	}

}
