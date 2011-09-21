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
import java.util.List;
import java.util.logging.Logger;

import net.ysuga.javros.node.XmlRpcRequestException;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.javros.util.ReturnValue;

/**
 * MasterAPI helper.
 * 
 * This helper class helps MasterAPIRef class object.
 * 
 * @author ysuga
 * 
 */
public class MasterAPIHelper {

	/**
	 * logger
	 */
	Logger logger = Logger.getLogger(MasterAPIHelper.class.getName());

	private MasterAPIRef masterAPI;

	/**
	 * get MasterAPIRef object.
	 * 
	 * @return
	 */
	public MasterAPIRef getRef() {
		return masterAPI;
	}

	/**
	 * Constructor
	 * 
	 * @param ref
	 *            MasterAPIRef class object
	 * @throws MalformedURLException
	 */
	public MasterAPIHelper(MasterAPIRef ref) {
		masterAPI = ref;
	}

	/**
	 * register service to master
	 * 
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<Integer> registerService(String callerId,
			String service, String serviceAPI, String callerAPI)
			throws XmlRpcRequestException {
		logger.entering(MasterAPIHelper.class.getName(), "registerService",
				new Object[] { callerId, service, serviceAPI, callerAPI });

		Object[] ret = masterAPI.registerService(callerId, service, serviceAPI,
				callerAPI);
		return new ReturnValue<Integer>(((Integer) ret[0]).intValue(),
				(String) ret[1], (Integer) ret[2]);
	}

	/**
	 * unregister service from master
	 * 
	 * @param callerId
	 * @param service
	 * @param serviceAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<Integer> unregisterService(String callerId,
			String service, String serviceAPI) throws XmlRpcRequestException {
		logger.entering(MasterAPIHelper.class.getName(), "unregisterService",
				new Object[] { callerId, service, serviceAPI });

		Object[] ret = masterAPI.unregisterService(callerId, service,
				serviceAPI);
		return new ReturnValue<Integer>(((Integer) ret[0]).intValue(),
				(String) ret[1], (Integer) ret[2]);
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
	public ReturnValue<List<ROSUri>> registerSubscriber(String callerId,
			String topic, String topicType, String callerAPI)
			throws XmlRpcRequestException {
		logger.entering(MasterAPIHelper.class.getName(), "registerSubscriber",
				new Object[] { callerId, topic, topicType, callerAPI });

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
	 * 
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<Integer> unregisterSubscriber(String callerId,
			String topic, String callerAPI) throws XmlRpcRequestException {
		logger.entering(MasterAPIHelper.class.getName(),
				"unregisterSubscriber", new Object[] { callerId, topic,
						callerAPI });

		Object[] ret = masterAPI.unregisterSubscriber(callerId, topic,
				callerAPI);
		return new ReturnValue<Integer>(((Integer) ret[0]).intValue(),
				(String) ret[1], (Integer) ret[2]);
	}

	/**
	 * register publisher to roscore (master)
	 * 
	 * @param callerId
	 * @param topic
	 * @param topic_type
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<List<ROSUri>> registerPublisher(String callerId,
			String topic, String topic_type, String callerAPI)
			throws XmlRpcRequestException {

		logger.entering(MasterAPIHelper.class.getName(), "registerPublisher",
				new Object[] { callerId, topic, topic_type, callerAPI });

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
	 * unregister publisher from master.
	 * 
	 * @param callerId
	 * @param topic
	 * @param callerAPI
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<Integer> unregisterPublisher(String callerId,
			String topic, String callerAPI) throws XmlRpcRequestException {
		logger.entering(MasterAPIHelper.class.getName(), "unregisterPublisher",
				new Object[] { callerId, topic, callerAPI });

		Object[] ret = masterAPI
				.unregisterPublisher(callerId, topic, callerAPI);
		return new ReturnValue<Integer>(((Integer) ret[0]).intValue(),
				(String) ret[1], (Integer) ret[2]);
	}

	/**
	 * lookup node
	 * 
	 * @param callerId
	 * @param nodeName
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<String> lookupNode(String callerId, String nodeName)
			throws XmlRpcRequestException {

		logger.entering(MasterAPIHelper.class.getName(), "lookupNode",
				new Object[] { callerId, nodeName });

		Object[] ret = masterAPI.lookupNode(callerId, nodeName);
		return new ReturnValue<String>(((Integer) ret[0]).intValue(),
				(String) ret[1], (String) ret[2]);
	}

	/**
	 * 
	 * @param callerId
	 * @param subGraph
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<Object[]> getPublishedTopics(String callerId,
			String subGraph) throws XmlRpcRequestException {

		logger.entering(MasterAPIHelper.class.getName(), "getPublishedTopics",
				new Object[] { callerId, subGraph });

		Object[] ret = masterAPI.getPublishedTopics(callerId, subGraph);
		// Object[] topics = (Object[]) ret[2];
		return new ReturnValue<Object[]>(((Integer) ret[0]).intValue(),
				(String) ret[1], (Object[]) ret[2]);
	}

	/**
	 * get system state of master.
	 * 
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<Object[]> getSystemState(String callerId)
			throws XmlRpcRequestException {

		logger.entering(MasterAPIHelper.class.getName(), "getSystemState",
				new Object[] { callerId });

		Object[] ret = masterAPI.getSystemState(callerId);
		return new ReturnValue<Object[]>(((Integer) ret[0]).intValue(),
				(String) ret[1], (Object[]) ret[2]);
	}

	/**
	 * get uri of master
	 * 
	 * @param callerId
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<String> getUri(String callerId)
			throws XmlRpcRequestException {

		logger.entering(MasterAPIHelper.class.getName(), "getUri",
				new Object[] { callerId });

		Object[] ret = masterAPI.getUri(callerId);
		return new ReturnValue<String>(((Integer) ret[0]).intValue(),
				(String) ret[1], (String) ret[2]);
	}

	/**
	 * lookup services
	 * 
	 * @param callerId
	 * @param service
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public ReturnValue<String> lookupService(String callerId, String service)
			throws XmlRpcRequestException {

		logger.entering(MasterAPIHelper.class.getName(), "lookupService",
				new Object[] { callerId, service });

		Object[] ret = masterAPI.lookupService(callerId, service);
		return new ReturnValue<String>(((Integer) ret[0]).intValue(),
				(String) ret[1], (String) ret[2]);
	}
}