/**
 * MasterAPIRef.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import net.ysuga.javros.node.XmlRpcRequestException;
import net.ysuga.javros.node.XmlRpcWrapper;

/**
 * Reference Class to Master API. You can use Master API function through this
 * class. But the argument and value is difficult to use (Object class) so
 * please use MasterAPIHelper class as a wrapper of this class.
 * 
 * @author ysuga
 * 
 */
public class MasterAPIRef extends XmlRpcWrapper implements MasterAPI {

	/**
	 * Constructor
	 * 
	 * @param uri
	 */
	public MasterAPIRef(URL uri) {
		super(uri);
	}

	/**
	 * register service to master.
	 * 
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] registerService(String callerId, String service,
			String serviceAPI, String callerAPI) throws XmlRpcRequestException {
		return (Object[]) request("registerService", Arrays.asList(callerId,
				service, serviceAPI, callerAPI));
	}

	/**
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] unregisterService(String callerId, String service,
			String serviceAPI) throws XmlRpcRequestException {
		return (Object[]) request("unregisterService", Arrays.asList(callerId,
				service, serviceAPI));
	}

	/**
	 * 
	 * @param callerId
	 * @param topic
	 * @param topicType
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] registerSubscriber(String callerId, String topic,
			String topicType, String callerAPI) throws XmlRpcRequestException {
		return (Object[]) request("registerSubscriber", Arrays.asList(callerId,
				topic, topicType, callerAPI));
	}

	/**
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] unregisterSubscriber(String callerId, String topic,
			String callerAPI) throws XmlRpcRequestException {
		return (Object[]) request("unregisterSubscriber", Arrays.asList(
				callerId, topic, callerAPI));
	}

	/**
	 * @param callerId
	 * @param topic
	 * @param topic_type
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] registerPublisher(String callerId, String topic,
			String topic_type, String callerAPI) throws XmlRpcRequestException {
		return (Object[]) request("registerPublisher", Arrays.asList(callerId,
				topic, topic_type, callerAPI));
	}

	/**
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] unregisterPublisher(String callerId, String topic,
			String callerAPI) throws XmlRpcRequestException {
		return (Object[]) request("unregisterPublisher", Arrays.asList(
				callerId, topic, callerAPI));
	}

	/**
	 * @param callerId
	 * @param nodeName
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] lookupNode(String callerId, String nodeName)
			throws XmlRpcRequestException {
		return (Object[]) request("lookupNode", Arrays.asList(callerId,
				nodeName));
	}

	/**
	 * @param callerId
	 * @param subGraph
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] getPublishedTopics(String callerId, String subGraph)
			throws XmlRpcRequestException {
		return (Object[]) request("getPublishedTopics", Arrays.asList(callerId,
				subGraph));
	}

	/**
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] getSystemState(String callerId)
			throws XmlRpcRequestException {
		return (Object[]) request("getSystemState", Arrays.asList(callerId));
	}

	/**
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] getUri(String callerId) throws XmlRpcRequestException {
		return (Object[]) request("getUri", Arrays.asList(callerId));
	}

	/**
	 * @param callerId
	 * @param service
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@Override
	public Object[] lookupService(String callerId, String service)
			throws XmlRpcRequestException {
		return (Object[]) request("lookupService", Arrays.asList(callerId,
				service));
	}

}
