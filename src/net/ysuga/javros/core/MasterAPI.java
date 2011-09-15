/**
 * MasterAPI.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/02
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.core;

import java.util.List;


import net.ysuga.javros.node.XmlRpcRequestException;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

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
public interface MasterAPI {

	/**
	 * 
	 * registerService
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * Register the caller as a provider of the specified service.
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	public Object[] registerService(String callerId, String service, String serviceAPI, String callerAPI) throws XmlRpcRequestException;
	
	
	/**
	 * 
	 * unregisterService
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * Unregister the caller as a provider of the specified service.
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	public Object[] unregisterService(String callerId, String service, String serviceAPI) throws XmlRpcRequestException;
	
	/**
	 * 
	 * registerSubscriber
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param topic
	 * @param topicType
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * Subscribe the caller to the specified topic. In addition to receiving a list of current publishers, the subscriber will also receive notifications of new publishers via the publisherUpdate API.
	 * @param callerId
	 * @param topic
	 * @param topicType
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	public Object[] registerSubscriber(String callerId, String topic, String topicType, String callerAPI) throws XmlRpcRequestException;
	
	/**
	 * 
	 * unregisterSubscriber
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * Unregister the caller as a publisher of the topic.
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	public Object[] unregisterSubscriber(String callerId, String topic, String callerAPI) throws XmlRpcRequestException;
	
	/**
	 * 
	 * registerPublisher
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param topic
	 * @param topic_type
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * Register the caller as a publisher the topic.
	 * @param callerId
	 * @param topic
	 * @param topic_type
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	public Object[] registerPublisher(String callerId, String topic, String topic_type, String callerAPI) throws XmlRpcRequestException;
	
	
	/**
	 * 
	 * unregisterPublisher
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * Unregister the caller as a publisher of the topic.
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	public Object[] unregisterPublisher(String callerId, String topic, String callerAPI) throws XmlRpcRequestException;
	
	/**
	 * 
	 * lookupNode
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param nodeName
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * Get the XML-RPC URI of the node with the associated name/caller_id. This API is for looking information about publishers and subscribers. Use lookupService instead to lookup ROS-RPC URIs.
	 * @param callerId
	 * @param nodeName
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	public Object[] lookupNode(String callerId, String nodeName) throws XmlRpcRequestException;
	
	/**
	 * 
	 * getPublishedTopics
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param subGraph
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * Get list of topics that can be subscribed to. This does not return topics that have no publishers. See getSystemState() to get more comprehensive list.
	 * @param callerId
	 * @param subGraph
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	public Object[] getPublishedTopics(String callerId, String subGraph) throws XmlRpcRequestException;
	


	/**
	 * 
	 * getSystemState
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * Retrieve list representation of system state (i.e. publishers, subscribers, and services).
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	public Object[] getSystemState(String callerId) throws XmlRpcRequestException;
	
	/**
	 * 
	 * getUri
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * Get the URI of the the master.
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	public Object[] getUri(String callerId) throws XmlRpcRequestException;
	
	/**
	 * 
	 * lookupService
	 * <div lang="ja">
	 * 
	 * @param callerId
	 * @param service
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 * Lookup all provider of a particular service.
	 * @param callerId
	 * @param service
	 * @return
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	public Object[] lookupService(String callerId, String service) throws XmlRpcRequestException;
	
}
