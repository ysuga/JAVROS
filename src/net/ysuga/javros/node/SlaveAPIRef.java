/**
 * SlaveAPIRef.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node;

import java.net.URL;

import net.ysuga.javros.xmlrpc.XmlRpcRequestException;
import net.ysuga.javros.xmlrpc.XmlRpcWrapper;

/**
 * Slave API reference class. You can access slave service through this class.
 * Methods of this class is a little difficult to use, so use SlaveAPIHelper
 * class to access Slave API service more easily.
 * 
 * @author ysuga
 * 
 */
public class SlaveAPIRef extends XmlRpcWrapper implements SlaveAPI {

	/*
	 * Constructor
	 */
	public SlaveAPIRef(URL uri) {
		super(uri);
	}

	/**
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] getBusStats(String callerId) throws XmlRpcRequestException {
		return request("getBusStats", callerId);
	}

	/**
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] getBusInfo(String callerId) throws XmlRpcRequestException {
		return request("getBusInfo", callerId);
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
		return request("getMasterUri", callerId);
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
		return request("shutdown", callerId);

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
		return request("shutdown", callerId, msg);
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
		return request("getPid", callerId);
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
		return request("getSubscriptions", callerId);

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
		return request("getPublications", callerId);

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
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] paramUpdate(String callerId, String parameterKey,
			Object parameterValue) throws XmlRpcRequestException {
		return request("paramUpdate", callerId, parameterKey, parameterValue);
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
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] publisherUpdate(String callerId, String topic,
			Object[] publishers) throws XmlRpcRequestException {
		return request("publisherUpdate", callerId, topic, publishers);
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
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] requestTopic(String callerId, String topic,
			Object[] protocols) throws XmlRpcRequestException {
		return request("requestTopic", callerId, topic, protocols);
	}

}
