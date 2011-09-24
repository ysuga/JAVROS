/**
 * ROSCore.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import net.ysuga.javros.core.master.MasterAPIHelper;
import net.ysuga.javros.core.master.MasterAPIRef;
import net.ysuga.javros.core.parameter.ParameterServerAPIHelper;
import net.ysuga.javros.core.parameter.ParameterServerAPIRef;
import net.ysuga.javros.core.rosref.RosRefClient;
import net.ysuga.javros.core.rosref.RosRefException;
import net.ysuga.javros.core.rosref.RosRefService;
import net.ysuga.javros.node.ROSNode;
import net.ysuga.javros.node.service.ROSService;
import net.ysuga.javros.node.service.ROSServiceFactory;
import net.ysuga.javros.node.service.ROSServiceNotFoundException;
import net.ysuga.javros.node.service.ROSServiceProvider;
import net.ysuga.javros.node.service.ROSServiceTypeInfo;
import net.ysuga.javros.node.topic.ROSTopic;
import net.ysuga.javros.transport.TransportException;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.javros.util.ReturnValue;
import net.ysuga.javros.value.ROSValue;
import net.ysuga.javros.value.ROSValueInvalidTypeInfoException;
import net.ysuga.javros.value.ROSValueTypeInfo;
import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

/**
 * Reference of the roscore (Master) object. Users can use master functions
 * through this object. This class also implements Singleton design pattern, so
 * users must get the instance of this class by calling getInstance static
 * method. </div>
 * 
 * @author ysuga
 * 
 */
public class ROSCoreRef {

	/**
	 * logger
	 */
	static private Logger logger = Logger.getLogger(ROSCoreRef.class.getName());

	/**
	 * default master port
	 */
	public static final int DEFAULT_PORT = 11311;

	/**
	 * the only instance of this class (singleton)
	 */
	static private ROSCoreRef theOnlyInstance;

	/**
	 * static method for getting the only instance of this class (singleton)
	 * 
	 * @return the only instance of this class
	 */
	final static public ROSCoreRef getInstance() {
		return theOnlyInstance;
	}

	/**
	 * callerid used in ROS world.
	 */
	final private String callerid;

	/**
	 * host address of roscore process. If you want to use multi-host
	 * environment, ROS_IP environmental value must be set to open the master
	 * service for everywhere outside the computer where roscore process is
	 * executed.
	 */
	final private String hostAddress;

	/**
	 * get host address of roscore process
	 * 
	 * @return host address of the pc where roscore process is executed.
	 */
	final public String getHostAddress() {
		return hostAddress;
	}

	/**
	 * port number of roscore process. Usually this value is 11311.
	 */
	private int port;

	/**
	 * get port number of roscore process.
	 * 
	 * @return port number of roscore process.
	 */
	final public int getPort() {
		return port;
	}

	/**
	 * The instance of helper class to use master API
	 */
	final private MasterAPIHelper masterAPI;

	final private MasterAPIHelper getMaster() {
		return masterAPI;
	}
	
	final private ParameterServerAPIHelper parameterServerAPI;
	
	final private ParameterServerAPIHelper getParameterServer() {
		return parameterServerAPI;
	}


	/**
	 * Constructor
	 * 
	 * @param hostAddress
	 *            host address of master computer
	 * @param portNumber
	 *            port number of master computer. this argument can be omitted.
	 * @throws MalformedURLException
	 */
	private ROSCoreRef(String hostAddress, int portNumber)
			throws MalformedURLException {
		this.callerid = "/javrosCore";
		this.hostAddress = hostAddress;
		this.port = portNumber;
		masterAPI = new MasterAPIHelper(new MasterAPIRef(new URL("http://"
				+ hostAddress + ":" + port)));
		
		parameterServerAPI = new ParameterServerAPIHelper(new ParameterServerAPIRef(new URL("http://"
				+ hostAddress + ":" + port)));
	}

	/**
	 * ROSCore initialization function. 
	 * You do not have to call this function. This function is called by ROS.init method.
	 * 
	 * @param hostAddress
	 *            host address of master PC.
	 * @return ROSCore instance. You can get the instance by calling getInstance
	 *         static method anywhere.
	 * @throws MalformedURLException
	 */
	static public ROSCoreRef init(String hostAddress) throws MalformedURLException {
		return init(hostAddress, DEFAULT_PORT);
	}

	/**
	 * ROSCore initialization function. This function must be called the first
	 * of all.
	 * 
	 * @param hostAddress
	 *            host address of master PC.
	 * @param portNumber
	 *            port number of roscore process. if you use unusual port
	 *            number, use this parameter.
	 * @return ROSCore instance. You can get the instance by calling getInstance
	 *         static method anywhere.
	 * @throws MalformedURLException
	 */
	static public ROSCoreRef init(String hostAddress, int portNumber)
			throws MalformedURLException {
		logger.entering(ROSCoreRef.class.getName(), "init", new Object[]{hostAddress, portNumber});
		theOnlyInstance = new ROSCoreRef(hostAddress, portNumber);
		return getInstance();
	}

	/**
	 * ------------------------------------------------------------------------
	 * -----------------------
	 */

	/**
	 * get all service provider's uri in the specific service.
	 * 
	 * @param service
	 *            ROSService object.
	 * @return list of all service provider's uri which registered as providers
	 *         of service (the argument).
	 * @throws XmlRpcRequestException
	 */
	public List<String> getAllServiceProviderUri(ROSService service)
			throws XmlRpcRequestException {
		logger.entering(ROSCoreRef.class.getName(), "getAllServiceProviderUri", service);
		ArrayList<String> retval = new ArrayList<String>();
		ReturnValue<String> result = masterAPI.lookupService(callerid, service
				.getName());
		String uri = result.getValue();
		retval.add(uri);
		return retval;
	}

	/**
	 * get publishers' name list in the specific topic.
	 * 
	 * @param topic
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public List<String> getTopicPublisherNameList(ROSTopic topic)
			throws XmlRpcRequestException {
		logger.entering(ROSCoreRef.class.getName(), "getTopicPublisherNameList", topic);
		
		ArrayList<String> retval = new ArrayList<String>();
		ReturnValue<Object[]> result = masterAPI.getSystemState(callerid);
		Object[] serviceList = (Object[]) result.getValue()[0];
		for (Object service : serviceList) {
			Object[] serviceState = (Object[]) service;
			if (((String) serviceState[0]).equals(topic.getName())) {
				Object[] publisherList = (Object[]) serviceState[1];
				for (Object publisher : publisherList) {
					retval.add((String) publisher);
				}
			}
		}
		return retval;
	}

	/**
	 * get all service name list
	 * 
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public List<String> getProvidedServiceNameList()
			throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "getProvidedServiceNameList");
		
		ArrayList<String> retval = new ArrayList<String>();
		ReturnValue<Object[]> result = masterAPI.getSystemState(callerid);
		Object[] serviceList = (Object[]) result.getValue()[2];
		for (Object service : serviceList) {
			Object[] serviceState = (Object[]) service;
			retval.add((String) serviceState[0]);
		}
		return retval;
	}

	/**
	 * get service object list.
	 * 
	 * @return
	 * @throws XmlRpcRequestException
	 * @throws RosRefException
	 * @throws ROSValueInvalidTypeInfoException 
	 * @see ROSService
	 */
	public List<ROSService> getProvidedServiceList()
			throws XmlRpcRequestException, RosRefException, ROSValueInvalidTypeInfoException {
		logger.entering(ROSCoreRef.class.getName(), "getProvidedServiceList");
		
		ArrayList<ROSService> retval = new ArrayList<ROSService>();
		ReturnValue<Object[]> result = masterAPI.getSystemState(callerid);
		Object[] serviceList = (Object[]) result.getValue()[2];
		for (Object service : serviceList) {
			Object[] serviceState = (Object[]) service;
			retval.add(ROSServiceFactory
					.createROSService((String) serviceState[0]));
		}
		return retval;
	}

	/**
	 * get all published topic name list.
	 * 
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public List<String> getPublishedTopicNameList()
			throws XmlRpcRequestException {
		logger.entering(ROSCoreRef.class.getName(), "getPublishedTopicNameList");
		
		ArrayList<String> retval = new ArrayList<String>();
		ReturnValue<Object[]> result = masterAPI.getSystemState(callerid);
		Object[] serviceList = (Object[]) result.getValue()[0];
		for (Object service : serviceList) {
			Object[] serviceState = (Object[]) service;
			retval.add((String) serviceState[0]);
		}
		return retval;
	}

	/**
	 * get service type from service name
	 * 
	 * @param serviceName
	 * @return
	 * @throws RosRefException
	 */
	final public String getServiceType(String serviceName)
			throws RosRefException {
		return rosrefCommand("service", "type", serviceName);
	}

	/**
	 * get topic type from topic name
	 */
	final public String getTopicType(String topicName) throws RosRefException {
		return rosrefCommand("topic", "type", topicName);
	}

	/**
	 * get md5 of service type.
	 * 
	 * @param serviceType
	 * @return
	 * @throws RosRefException
	 */
	final public String getServiceMd5Sum(String serviceType)
			throws RosRefException {
		return rosrefCommand("srv", "md5", serviceType);
	}

	/**
	 * get md5sum of topic type.
	 * 
	 * @param topicType
	 * @return
	 * @throws RosRefException
	 */
	final public String getTopicMd5Sum(String topicType) throws RosRefException {
		return rosrefCommand("msg", "md5", topicType);
	}

	/**
	 * get ROSServiceTypeInfo object from service type identifier.
	 * 
	 * @param serviceType
	 * @return
	 * @throws RosRefException
	 * @throws ROSValueInvalidTypeInfoException 
	 */
	final public ROSServiceTypeInfo getServiceTypeInfo(String serviceType)
			throws RosRefException, ROSValueInvalidTypeInfoException {
		return new ROSServiceTypeInfo(rosrefCommand("srv", "show", serviceType));
	}

	/**
	 * get ROSTopicTypeInfo object from topic type indentifier
	 * 
	 * @param topicType
	 * @return
	 * @throws RosRefException
	 * @throws ROSValueInvalidTypeInfoException
	 */
	final public ROSValueTypeInfo getTopicTypeInfo(String topicType)
			throws RosRefException, ROSValueInvalidTypeInfoException {
		return new ROSValueTypeInfo(rosrefCommand("msg", "show", topicType));
	}

	/**
	 * private method. rosref service command
	 * 
	 * @param command
	 * @param option
	 * @param arg
	 * @return
	 * @throws RosRefException
	 */
	final private String rosrefCommand(String command, String option, String arg)
			throws RosRefException {
		logger.entering(ROSCoreRef.class.getName(), "rosrefCommand" + new Object[]{command, option, arg});
		
		try {
			List<String> services = getProvidedServiceNameList();
			if (!services.contains(RosRefService.getInstance().getName())) {
				logger
						.severe("rosref service may not be launched in master pc.");
				throw new RosRefException("not found");
			}

			List<String> providers = getAllServiceProviderUri(RosRefService
					.getInstance());
			if (providers.size() == 0) {
				logger.severe("rosref service may not be launched in master pc.");
				throw new RosRefException("not found");
			}

			RosRefClient client = new RosRefClient(callerid);
			ROSValue ret = client.call(command, option, arg);
			String retval = ((String) ret.get("ret")).trim();
			logger.exiting(ROSCoreRef.class.getName(), "rosrefCommand", retval);
			return retval;
		} catch (XmlRpcRequestException e) {
			logger.severe("master api request failed.");
			throw new RosRefException("master api request failed.");
		} catch (TransportException e) {
			logger.severe("transporting service value is failed.");
			throw new RosRefException("transporting service value is failed..");
		} catch (ROSServiceNotFoundException e) {
			logger.severe("rosref service may not be launched in master pc.");
			throw new RosRefException("Remote command service is not found..");
		}
	}
	
	
	

	/**
	 * get node's uri from node name
	 * 
	 * @param nodeName
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public ROSUri getNodeUri(String nodeName) throws XmlRpcRequestException {
		ReturnValue<String> uri = getMaster().lookupNode(this.callerid,
				nodeName);
		return new ROSUri(uri.getValue());
	}

	/**
	 * register node as subscriber of a specific topic.
	 * 
	 * @param topic
	 * @param node
	 * @throws XmlRpcRequestException
	 */
	public List<ROSUri> registerSubscriber(ROSTopic topic, ROSNode node)
			throws XmlRpcRequestException {
		logger.entering(ROSCoreRef.class.getName(), "registerSubscriber", new Object[]{topic,node});
		ReturnValue<List<ROSUri>> ret = getMaster().registerSubscriber(
				node.getName(), topic.getName(), topic.getType(),
				node.getSlaveServerUri());
		if(!ret.isSuccess()) {
			logger.severe("registerSubscriber failed:" + ret.getStatusMessage());
			throw new XmlRpcRequestException("register subscriber failed.");
		}
		return ret.getValue();
	}

	/**
	 * unregister subscriber.
	 * @param topic
	 * @param node
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public void unregisterSubscriber(ROSTopic topic,
			ROSNode node) throws XmlRpcRequestException {
		logger.entering(ROSCoreRef.class.getName(), "unregisterSubscriber", new Object[]{topic, node});
		
		ReturnValue<Integer> ret = getMaster().unregisterSubscriber(
				node.getName(), topic.getName(), node.getSlaveServerUri());
		if(!ret.isSuccess()) {
			logger.severe("ROSCoreRef.unregisterSubscriber("+topic + ", " + node + ") failed.");
			throw new XmlRpcRequestException("unregister subscriber failed.");
		}
	}

	/**
	 * register node as publisher
	 * @param topic
	 * @param node
	 * @throws XmlRpcRequestException
	 */
	public void registerPublisher(ROSTopic topic, ROSNode node)
			throws XmlRpcRequestException {
		logger.entering(ROSCoreRef.class.getName(), "registerPublisher" + new Object[]{topic,node});
		
		ReturnValue<List<ROSUri>> ret = getMaster().registerPublisher(
				node.getName(), topic.getName(), topic.getType(),
				node.getSlaveServerUri());
		if(!ret.isSuccess()) {
			logger.severe("registerPublisher failed:" + ret.getStatusMessage());
			throw new XmlRpcRequestException("register publisher failed.");
		}
	}

	/**
	 * unregister publisher 
	 * 
	 * @param topic
	 * @param node
	 * @return
	 * @throws XmlRpcRequestException
	 */
	public void unregisterPublisher(ROSTopic topic, ROSNode node)
			throws XmlRpcRequestException {
		logger.entering(ROSCoreRef.class.getName(), "unregisterPublisher", new Object[]{topic,node});
		ReturnValue<Integer> ret = getMaster().unregisterPublisher(
				node.getName(), topic.getName(), node.getSlaveServerUri());
		if(!ret.isSuccess()) {
			logger.severe("ROSCoreRef.unregisterPublisher("+topic + ", " + node + ") failed.");
			throw new XmlRpcRequestException("unregister publisher failed.");
		}
	}

	/**
	 * register subscribe Parameter. This method will register the node to Parameter Server. 
	 * If the node registered, Parameter Server automatically notify the update of the parameter 
	 * value specified by the key.
	 * 
	 * @param node
	 * @param key
	 * @throws XmlRpcRequestException
	 */
	public void registerParameter(ROSNode node, String key) throws XmlRpcRequestException {
		logger.entering(ROSCoreRef.class.getName(), "registerParameter", new Object[]{node, key});
		
		getParameterServer().subscribeParam(node.getName(), node.getSlaveServerUri(), key);
	}
	
	/**
	 * unregister Parameter. This will unregiter the node from Parameter Server. See registerParameter method.
	 * @param node
	 * @param key
	 * @throws XmlRpcRequestException
	 */
	public void unregisterParameter(ROSNode node, String key) throws XmlRpcRequestException {
		logger.entering(ROSCoreRef.class.getName(), "unregisterParameter", new Object[]{node, key});

		getParameterServer().unsubscribeParam(node.getName(), node.getSlaveServerUri(), key);
	}
	
	/**
	 * set parameter value & key. if parameter is not registered, it's added to parameter server. 
	 * @param key
	 * @param value
	 * @throws XmlRpcRequestException
	 */
	public void setParameter(String key, Object value) throws XmlRpcRequestException {
		logger.entering(ROSCoreRef.class.getName(), "setParameter", new Object[]{key, value});
		
		getParameterServer().setParam("/javrosCore", key, value.toString());
	}
	
	/**
	 * remove parameter from parameter server.
	 * @param key
	 * @throws XmlRpcRequestException
	 */
	public void removeParameter(String key) throws XmlRpcRequestException {
		logger.entering(ROSCoreRef.class.getName(), "removeParameter", new Object[]{key});
		
		getParameterServer().deleteParam("/javrosCore", key);
	}
	
	/**
	 * 
	 * @param provider
	 * @param node
	 * @throws XmlRpcRequestException 
	 */
	public void registerServiceProvider(ROSServiceProvider provider, ROSNode node) throws XmlRpcRequestException {
		logger.entering(ROSCoreRef.class.getName(), "registerServiceProvider", new Object[]{provider, node});
		getMaster().registerService(node.getName(), provider.getService().getName(), provider.getUri(), node.getSlaveServerUri());
	}

}
