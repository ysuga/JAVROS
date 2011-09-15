/**
 * MasterAPIImpl.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/02
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.core;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ysuga.javros.node.XmlRpcRequestException;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.javros.util.ReturnValue;

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
public class MasterAPIHelper {

	private MasterAPIRef masterAPI;

	public MasterAPIRef getRef() {
		return masterAPI;
	}

	/**
	 * <div lang="ja"> コンストラクタ </div> <div lang="en"> Constructor </div>
	 */
	public MasterAPIHelper(MasterAPIRef ref) throws MalformedURLException {
		masterAPI = ref;
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @param callerAPI
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @param callerAPI
	 * @return </div>
	 * @throws XmlRpcException
	 */
	public ReturnValue<Integer> registerService(String callerId,
			String service, String serviceAPI, String callerAPI)
			throws XmlRpcRequestException {
		Object[] ret = masterAPI.registerService(callerId, service, serviceAPI,
				callerAPI);
		return new ReturnValue<Integer>(((Integer) ret[0]).intValue(),
				(String) ret[1], (Integer) ret[2]);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @return </div>
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<Integer> unregisterService(String callerId,
			String service, String serviceAPI) throws XmlRpcRequestException {
		Object[] ret = masterAPI.unregisterService(callerId, service,
				serviceAPI);
		return new ReturnValue<Integer>(((Integer) ret[0]).intValue(),
				(String) ret[1], (Integer) ret[2]);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param topic
	 * @param topicType
	 * @param callerAPI
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param topic
	 * @param topicType
	 * @param callerAPI
	 * @return </div>
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<List<ROSUri>> registerSubscriber(String callerId,
			String topic, String topicType, String callerAPI)
			throws XmlRpcRequestException {
		Object[] ret = masterAPI.registerSubscriber(callerId, topic, topicType,
				callerAPI);
		ArrayList<ROSUri> list = new ArrayList<ROSUri>();
		for (Object o : (Object[]) ret[2]) {
			list.add(new ROSUri((String) o));
		}
		return new ReturnValue<List<ROSUri>>(((Integer) ret[0]).intValue(),
				(String) ret[1], list);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return </div>
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<Integer> unregisterSubscriber(String callerId,
			String topic, String callerAPI) throws XmlRpcRequestException {
		Object[] ret = masterAPI.unregisterSubscriber(callerId, topic,
				callerAPI);
		return new ReturnValue<Integer>(((Integer) ret[0]).intValue(),
				(String) ret[1], (Integer) ret[2]);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param topic
	 * @param topic_type
	 * @param callerAPI
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param topic
	 * @param topic_type
	 * @param callerAPI
	 * @return </div>
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<List<ROSUri>> registerPublisher(String callerId,
			String topic, String topic_type, String callerAPI)
			throws XmlRpcRequestException {
		Object[] ret = masterAPI.registerPublisher(callerId, topic, topic_type,
				callerAPI);
		ArrayList<ROSUri> list = new ArrayList<ROSUri>();
		for (Object o : (Object[]) ret[2]) {
			list.add(new ROSUri((String) o));
		}
		return new ReturnValue<List<ROSUri>>(((Integer) ret[0]).intValue(),
				(String) ret[1], list);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return </div>
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<Integer> unregisterPublisher(String callerId,
			String topic, String callerAPI) throws XmlRpcRequestException {
		Object[] ret = masterAPI
				.unregisterPublisher(callerId, topic, callerAPI);
		return new ReturnValue<Integer>(((Integer) ret[0]).intValue(),
				(String) ret[1], (Integer) ret[2]);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param nodeName
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param nodeName
	 * @return </div>
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<String> lookupNode(String callerId, String nodeName)
			throws XmlRpcRequestException {
		Object[] ret = masterAPI.lookupNode(callerId, nodeName);
		return new ReturnValue<String>(((Integer) ret[0]).intValue(),
				(String) ret[1], (String) ret[2]);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param subGraph
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param subGraph
	 * @return </div>
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<Object[]> getPublishedTopics(String callerId,
			String subGraph) throws XmlRpcRequestException {
		Object[] ret = masterAPI.getPublishedTopics(callerId, subGraph);
		Object[] topics = (Object[]) ret[2];
		return new ReturnValue<Object[]>(((Integer) ret[0]).intValue(),
				(String) ret[1], (Object[]) ret[2]);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @return </div>
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<Object[]> getSystemState(String callerId)
			throws XmlRpcRequestException {
		Object[] ret = masterAPI.getSystemState(callerId);
		return new ReturnValue<Object[]>(((Integer) ret[0]).intValue(),
				(String) ret[1], (Object[]) ret[2]);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @return </div>
	 */
	public ReturnValue<String> getUri(String callerId)
			throws XmlRpcRequestException {
		Object[] ret = masterAPI.getUri(callerId);
		return new ReturnValue<String>(((Integer) ret[0]).intValue(),
				(String) ret[1], (String) ret[2]);
	}

	/**
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param service
	 * @return </div> <div lang="en">
	 * @param callerId
	 * @param service
	 * @return </div>
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<String> lookupService(String callerId, String service)
			throws XmlRpcRequestException {
		Object[] ret = masterAPI.lookupService(callerId, service);
		return new ReturnValue<String>(((Integer) ret[0]).intValue(),
				(String) ret[1], (String) ret[2]);
	}
}