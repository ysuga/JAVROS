package net.ysuga.javros.core.parameter;

import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

public interface ParameterServerAPI {

	/**
	 * deleteParam(caller_id, key)
	 * 
	 * Delete parameter
	 * 
	 * Parameters caller_id (str) ROS caller ID key (str) Parameter name.
	 * Returns (int, str, int) (code, statusMessage, ignore)
	 */
	public Object[] deleteParam(String callerId, String key)
			throws XmlRpcRequestException;

	/**
	 * setParam(caller_id, key, value)
	 * 
	 * Set parameter. NOTE: if value is a dictionary it will be treated as a
	 * parameter tree, where key is the parameter namespace. For example
	 * {'x':1,'y':2,'sub':{'z':3}} will set key/x=1, key/y=2, and key/sub/z=3.
	 * Furthermore, it will replace all existing parameters in the key parameter
	 * namespace with the parameters in value. You must set parameters
	 * individually if you wish to perform a union update.
	 * 
	 * Parameters caller_id (str) ROS caller ID key (str) Parameter name. value
	 * (XMLRPCLegalValue) Parameter value. Returns (int, str, int) (code,
	 * statusMessage, ignore)
	 */
	public Object[] setParam(String callerId, String key, Object value)
			throws XmlRpcRequestException;

	/**
	 * getParam(caller_id, key)
	 * 
	 * Retrieve parameter value from server.
	 * 
	 * Parameters caller_id (str) ROS caller ID key (str) Parameter name. If key
	 * is a namespace, getParam() will return a parameter tree. Returns (int,
	 * str, XMLRPCLegalValue) (code, statusMessage, parameterValue)
	 * 
	 * If code is not 1, parameterValue should be ignored. If key is a
	 * namespace, the return value will be a dictionary, where each key is a
	 * parameter in that namespace. Sub-namespaces are also represented as
	 * dictionaries.
	 */
	public Object[] getParam(String callerId, String key)
			throws XmlRpcRequestException;

	/**
	 * searchParam(caller_id, key)
	 * 
	 * Search for parameter key on the Parameter Server. Search starts in
	 * caller's namespace and proceeds upwards through parent namespaces until
	 * Parameter Server finds a matching key. searchParam()'s behavior is to
	 * search for the first partial match. For example, imagine that there are
	 * two 'robot_description' parameters /robot_description
	 * /robot_description/arm /robot_description/base /pr2/robot_description
	 * /pr2/robot_description/base If I start in the namespace /pr2/foo and
	 * search for robot_description, searchParam() will match
	 * /pr2/robot_description. If I search for robot_description/arm it will
	 * return /pr2/robot_description/arm, even though that parameter does not
	 * exist (yet).
	 * 
	 * Parameters caller_id (str) ROS caller ID key (str) Parameter name to
	 * search for. Returns (int, str, str) (code, statusMessage, foundKey)
	 * 
	 * If code is not 1, foundKey should be ignored.
	 */
	Object[] searchParam(String callerId, String key)
			throws XmlRpcRequestException;

	/**
	 * subscribeParam(caller_id, caller_api, key)
	 * 
	 * Retrieve parameter value from server and subscribe to updates to that
	 * param. See paramUpdate() in the Node API.
	 * 
	 * Parameters caller_id (str) ROS caller ID. key (str) Parameter name
	 * caller_api (str) Node API URI of subscriber for paramUpdate callbacks.
	 * Returns (int, str, XMLRPCLegalValue) (code, statusMessage,
	 * parameterValue)
	 * 
	 * If code is not 1, parameterValue should be ignored. parameterValue is an
	 * empty dictionary if the parameter has not been set yet.
	 */
	public Object[] subscribeParam(String callerId, String callerAPI, String key)
			throws XmlRpcRequestException;

	/**
	 * unsubscribeParam(caller_id, caller_api, key)
	 * 
	 * Retrieve parameter value from server and subscribe to updates to that
	 * param. See paramUpdate() in the Node API.
	 * 
	 * Parameters caller_id (str) ROS caller ID. key (str) Parameter name.
	 * caller_api (str) Node API URI of subscriber. Returns (int, str, int)
	 * (code, statusMessage, numUnsubscribed)
	 * 
	 * If numUnsubscribed is zero it means that the caller was not subscribed to
	 * the parameter.
	 */
	public Object[] unsubscribeParam(String callerid, String callerAPI,
			String key) throws XmlRpcRequestException;

	/**
	 * hasParam(caller_id, key)
	 * 
	 * Check if parameter is stored on server.
	 * 
	 * Parameters caller_id (str) ROS caller ID. key (str) Parameter name.
	 * Returns (int, str, bool) (code, statusMessage, hasParam)
	 */
	public Object[] hasParam(String callerId, String key)
			throws XmlRpcRequestException;

	/**
	 * getParamNames(caller_id)
	 * 
	 * Get list of all parameter names stored on this server.
	 * 
	 * Parameters caller_id (str) ROS caller ID. Returns (int, str, [str])
	 * (code, statusMessage, parameterNameList)
	 */

	public Object[] getParamNames(String callerid)
			throws XmlRpcRequestException;
}
