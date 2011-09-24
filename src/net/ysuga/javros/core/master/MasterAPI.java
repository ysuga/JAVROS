/**
 * MasterAPI.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/02
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.core.master;

import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

/**
 * Interface of Master API of ROS service.
 * @author ysuga
 *
 */
public interface MasterAPI {

	/**
	 * Register the caller as a provider of the specified service.
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public Object[] registerService(String callerId, String service, String serviceAPI, String callerAPI) throws XmlRpcRequestException;
	
	
	/**
	 * Unregister the caller as a provider of the specified service.
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public Object[] unregisterService(String callerId, String service, String serviceAPI) throws XmlRpcRequestException;
	
	/**
	 * Subscribe the caller to the specified topic. In addition to receiving a list of current publishers, the subscriber will also receive notifications of new publishers via the publisherUpdate API.
	 * @param callerId
	 * @param topic
	 * @param topicType
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public Object[] registerSubscriber(String callerId, String topic, String topicType, String callerAPI) throws XmlRpcRequestException;
	
	/**
	 * Unregister the caller as a publisher of the topic.
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public Object[] unregisterSubscriber(String callerId, String topic, String callerAPI) throws XmlRpcRequestException;
	
	/**
	 * Register the caller as a publisher the topic.
	 * @param callerId
	 * @param topic
	 * @param topic_type
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public Object[] registerPublisher(String callerId, String topic, String topic_type, String callerAPI) throws XmlRpcRequestException;
	
	
	/**
	 * Unregister the caller as a publisher of the topic.
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public Object[] unregisterPublisher(String callerId, String topic, String callerAPI) throws XmlRpcRequestException;
	
	/**
	 * Get the XML-RPC URI of the node with the associated name/caller_id. This API is for looking information about publishers and subscribers. Use lookupService instead to lookup ROS-RPC URIs.
	 * @param callerId
	 * @param nodeName
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public Object[] lookupNode(String callerId, String nodeName) throws XmlRpcRequestException;
	
	/**
	 * Get list of topics that can be subscribed to. This does not return topics that have no publishers. See getSystemState() to get more comprehensive list.
	 * @param callerId
	 * @param subGraph
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public Object[] getPublishedTopics(String callerId, String subGraph) throws XmlRpcRequestException;
	


	/**
	 * Retrieve list representation of system state (i.e. publishers, subscribers, and services).
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public Object[] getSystemState(String callerId) throws XmlRpcRequestException;
	
	/**
	 * Get the URI of the the master.
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public Object[] getUri(String callerId) throws XmlRpcRequestException;
	
	/**
	 * Lookup all provider of a particular service.
	 * @param callerId
	 * @param service
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public Object[] lookupService(String callerId, String service) throws XmlRpcRequestException;
	
}
