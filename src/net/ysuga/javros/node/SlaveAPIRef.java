/**
 * SlaveAPIRef.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

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
public class SlaveAPIRef extends XmlRpcWrapper implements SlaveAPI {

	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 * @throws MalformedURLException 
	 */
	public SlaveAPIRef(URL uri) {
		super(uri);
	}

	/**
	 * <div lang="ja">
	 * @param callerId
	 * @return
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @return
	 * </div>
	 * @throws XmlRpcRequestException 
	 */
	@Override
	public Object[] getBusStats(String callerId) throws XmlRpcRequestException {
		return (Object[]) request("getBusStats",
				Arrays.asList(callerId));
	}

	/**
	 * <div lang="ja">
	 * @param callerId
	 * @return
	 * </div>
	 * <div lang="en">
	 * @param callerId
	 * @return
	 * </div>
	 * @throws XmlRpcRequestException 
	 */
	@Override
	public Object[] getBusInfo(String callerId) throws XmlRpcRequestException {
		return (Object[]) request("getBusInfo",
				Arrays.asList(callerId));
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
	public Object[] getMasterUri(String callerId) throws XmlRpcRequestException {
		return (Object[]) request("getMasterUri",
				Arrays.asList(callerId));
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
		return (Object[]) request("shutdown",
				Arrays.asList(callerId));

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
		return (Object[]) request("shutdown",
				Arrays.asList(callerId, msg));
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
		return (Object[]) request("getPid",
				Arrays.asList(callerId));
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
		return (Object[]) request("getSubscriptions",
				Arrays.asList(callerId));

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
		return (Object[]) request("getPublications",
				Arrays.asList(callerId));

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
	 * @throws XmlRpcRequestException 
	 */
	@Override
	public Object[] paramUpdate(String callerId, String parameterKey,
			Object[] parameterValue) throws XmlRpcRequestException {
		return (Object[]) request("paramUpdate",
				Arrays.asList(callerId, parameterKey, (Object)parameterValue));
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
	 * @throws XmlRpcRequestException 
	 */
	@Override
	public Object[] publisherUpdate(String callerId, String topic,
			Object[] publishers) throws XmlRpcRequestException {
		return (Object[]) request("publisherUpdate",
				Arrays.asList(callerId, topic, publishers));
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
	 * @throws XmlRpcRequestException 
	 */
	@Override
	public Object[] requestTopic(String callerId, String topic,
			Object[] protocols) throws XmlRpcRequestException {
		return (Object[]) request("requestTopic",
				Arrays.asList(callerId, topic, protocols));
	}

}
