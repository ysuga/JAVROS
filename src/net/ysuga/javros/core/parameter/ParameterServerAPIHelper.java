package net.ysuga.javros.core.parameter;

import java.util.List;

import net.ysuga.javros.util.ReturnValue;
import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

public class ParameterServerAPIHelper {

	private ParameterServerAPIRef ref;
	public ParameterServerAPIHelper(ParameterServerAPIRef api) {
		this.ref = api;
	}
	
	

	/**
	 * deleteParam(caller_id, key)
	 * 
	 * Delete parameter
	 * 
	 * Parameters caller_id (str) ROS caller ID key (str) Parameter name.
	 * Returns (int, str, int) (code, statusMessage, ignore)
	 */
	public ReturnValue<Integer> deleteParam(String callerId, String key)
			throws XmlRpcRequestException {
		Object[] ret = ref.deleteParam(callerId, key);
		return new ReturnValue<Integer>(ret[0], ret[1], (Integer)ret[2]);
	}

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
	 * (XMLRPCLegalValue) Parameter value. 
	 * Returns (int, str, int) (code,
	 * statusMessage, ignore)
	 */
	public ReturnValue<Integer> setParam(String callerId, String key, Object value)
			throws XmlRpcRequestException  {
		Object[] ret = ref.setParam(callerId, key, value);
		return new ReturnValue<Integer>(ret[0], ret[1], (Integer)ret[2]);
	}

	/**
	 * getParam(caller_id, key)
	 * 
	 * Retrieve parameter value from server.
	 * 
	 * Parameters caller_id (str) ROS caller ID key (str) Parameter name. If key
	 * is a namespace, getParam() will return a parameter tree. 
	 * Returns (int,
	 * str, XMLRPCLegalValue) (code, statusMessage, parameterValue)
	 * 
	 * If code is not 1, parameterValue should be ignored. If key is a
	 * namespace, the return value will be a dictionary, where each key is a
	 * parameter in that namespace. Sub-namespaces are also represented as
	 * dictionaries.
	 */
	public ReturnValue<Object> getParam(String callerId, String key)
			throws XmlRpcRequestException {
		Object[] ret = ref.getParam(callerId, key);
		return new ReturnValue<Object>(ret[0], ret[1], (Object)ret[2]);
	}

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
	 * search for. 
	 * Returns (int, str, str) (code, statusMessage, foundKey)
	 * 
	 * If code is not 1, foundKey should be ignored.
	 */
	public ReturnValue<String> searchParam(String callerId, String key)
			throws XmlRpcRequestException {
		Object[] ret = ref.searchParam(callerId, key);
		return new ReturnValue<String>(ret[0], ret[1], (String)ret[2]);
	}

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
	public ReturnValue<Object> subscribeParam(String callerId, String callerAPI, String key)
			throws XmlRpcRequestException {
		Object[] ret = ref.subscribeParam(callerId, callerAPI, key);
		return new ReturnValue<Object>(ret[0], ret[1], (Object)ret[2]);
	}

	/**
	 * unsubscribeParam(caller_id, caller_api, key)
	 * 
	 * Retrieve parameter value from server and subscribe to updates to that
	 * param. See paramUpdate() in the Node API.
	 * 
	 * Parameters caller_id (str) ROS caller ID. key (str) Parameter name.
	 * caller_api (str) Node API URI of subscriber. 
	 * Returns (int, str, int)
	 * (code, statusMessage, numUnsubscribed)
	 * 
	 * If numUnsubscribed is zero it means that the caller was not subscribed to
	 * the parameter.
	 */
	public ReturnValue<Integer> unsubscribeParam(String callerid, String callerAPI,
			String key) throws XmlRpcRequestException {
		Object[] ret = ref.unsubscribeParam(callerid, callerAPI, key);
		return new ReturnValue<Integer>(ret[0], ret[1], (Integer)ret[2]);
	}

	/**
	 * hasParam(caller_id, key)
	 * 
	 * Check if parameter is stored on server.
	 * 
	 * Parameters caller_id (str) ROS caller ID. key (str) Parameter name.
	 * Returns (int, str, bool) (code, statusMessage, hasParam)
	 */
	public ReturnValue<Boolean> hasParam(String callerId, String key)
			throws XmlRpcRequestException {
		Object[] ret = ref.hasParam(callerId, key);
		return new ReturnValue<Boolean>(ret[0], ret[1], (Boolean)ret[2]);
	}

	/**
	 * getParamNames(caller_id)
	 * 
	 * Get list of all parameter names stored on this server.
	 * 
	 * Parameters caller_id (str) ROS caller ID. Returns (int, str, [str])
	 * (code, statusMessage, parameterNameList)
	 */

	public ReturnValue<String[]> getParamNames(String callerid)
			throws XmlRpcRequestException {
		Object[] ret = ref.getParamNames(callerid);
		Object[] retval = (Object[])ret[2];
		String[] strList = new String[retval.length];
		for(int i = 0;i < retval.length;i++) {
			strList[i] = (String)retval[i];
		}
		return new ReturnValue<String[]>(ret[0], ret[1], strList);
	}

}
