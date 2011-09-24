/**
 * SlaveAPIAdapter.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/05
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node;

import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

/**
 * ROS Slave API interfaces
 * @author ysuga
 *
 */
public interface SlaveAPI {


	public Object[] getBusStats(String callerId) throws XmlRpcRequestException;
	
	public Object[] getBusInfo(String callerId) throws XmlRpcRequestException;
	
	public Object[] getMasterUri(String callerId) throws XmlRpcRequestException;
	
	public Object[] shutdown(String callerId) throws XmlRpcRequestException;
	
	public Object[] shutdown(String callerId, String msg) throws XmlRpcRequestException;
	
	public Object[] getPid(String callerId) throws XmlRpcRequestException;

	public Object[] getSubscriptions(String callerId) throws XmlRpcRequestException;
	
	public Object[] getPublications(String callerId) throws XmlRpcRequestException;
	
	
	public Object[] publisherUpdate(String callerId, String topic, Object[] publishers) throws XmlRpcRequestException;
	
	public Object[] requestTopic(String callerId, String topic, Object[] protocols) throws XmlRpcRequestException;

	public Object[] paramUpdate(String callerId, String parameterKey, Object parameterValue) throws XmlRpcRequestException;
	
	


}
