/**
 * ROSCore.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import net.ysuga.javros.core.MasterAPIHelper;
import net.ysuga.javros.core.MasterAPIRef;
import net.ysuga.javros.node.ROSNode;
import net.ysuga.javros.node.XmlRpcRequestException;
import net.ysuga.javros.node.service.ROSService;
import net.ysuga.javros.node.service.ROSServiceFactory;
import net.ysuga.javros.node.service.ROSServiceNotFoundException;
import net.ysuga.javros.node.service.ROSServiceTypeInfo;
import net.ysuga.javros.node.topic.ROSTopic;
import net.ysuga.javros.node.topic.ROSTopicInvalidTypeInfoException;
import net.ysuga.javros.node.topic.ROSTopicTypeInfo;
import net.ysuga.javros.remote.RemoteCommandService;
import net.ysuga.javros.remote.RemoteCommandServiceClient;
import net.ysuga.javros.remote.RemoteCommandServiceException;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.javros.util.ReturnValue;
import net.ysuga.ros.javros.tcpros.TransportException;

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
public class ROSCore {

	static private Logger logger = Logger.getLogger(ROSCore.class.getName());
	
	public static final int DEFAULT_PORT = 11311;

	static private ROSCore theOnlyInstance;

	final static public ROSCore getInstance() {
		return theOnlyInstance;
	}

	final private String callerid;

	final private String hostAddress;

	final public String getHostAddress() {
		return hostAddress;
	}

	private int port;

	final public int getPort() {
		return port;
	}

	final private MasterAPIHelper masterAPI;

	final public MasterAPIHelper getMaster() {
		return masterAPI;
	}

	/**
	 * <div lang="ja"> コンストラクタ </div> <div lang="en"> Constructor </div>
	 */
	private ROSCore(String hostAddress, int portNumber)
			throws MalformedURLException {
		this.callerid = "/javrosCore";
		this.hostAddress = hostAddress;
		this.port = portNumber;
		masterAPI = new MasterAPIHelper(new MasterAPIRef(new URL("http://"
				+ hostAddress + ":" + port)));
	}

	static public ROSCore init(String hostAddress) throws MalformedURLException {
		return init(hostAddress, DEFAULT_PORT);
	}

	static public ROSCore init(String hostAddress, int portNumber)
			throws MalformedURLException {
		logger.entering(ROSCore.class.getName(), "init("+hostAddress + ", " + portNumber+ ")");
		theOnlyInstance = new ROSCore(hostAddress, portNumber);
		return getInstance();
	}

	public List<String> getAllServiceProviderUri(ROSService service)
			throws XmlRpcRequestException {
		ArrayList<String> retval = new ArrayList<String>();
		ReturnValue<String> result = masterAPI.lookupService(callerid,
				service.getName());
		String uri = result.getValue();
		retval.add(uri);
		return retval;
	}

	public List<String> getTopicPublisherNameList(ROSTopic topic)
			throws XmlRpcRequestException {
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

	public List<String> getProvidedServiceNameList()
			throws XmlRpcRequestException {
		ArrayList<String> retval = new ArrayList<String>();
		ReturnValue<Object[]> result = masterAPI.getSystemState(callerid);
		Object[] serviceList = (Object[]) result.getValue()[2];
		for (Object service : serviceList) {
			Object[] serviceState = (Object[]) service;
			retval.add((String) serviceState[0]);
		}
		return retval;
	}

	public List<ROSService> getProvidedServiceList()
			throws XmlRpcRequestException, RemoteCommandServiceException {
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

	public List<String> getPublishedTopicNameList()
			throws XmlRpcRequestException {
		ArrayList<String> retval = new ArrayList<String>();
		ReturnValue<Object[]> result = masterAPI.getSystemState(callerid);
		Object[] serviceList = (Object[]) result.getValue()[0];
		for (Object service : serviceList) {
			Object[] serviceState = (Object[]) service;
			retval.add((String) serviceState[0]);
		}
		return retval;
	}

	final public String getServiceType(String serviceName)
			throws RemoteCommandServiceException {
		return remoteCommand("service", "type", serviceName);
	}

	final public String getTopicType(String topicName)
			throws RemoteCommandServiceException {
		return remoteCommand("topic", "type", topicName);
	}

	final public String getServiceMd5Sum(String serviceType)
			throws RemoteCommandServiceException {
		return remoteCommand("srv", "md5", serviceType);
	}

	final public String getTopicMd5Sum(String topicType)
			throws RemoteCommandServiceException {
		return remoteCommand("msg", "md5", topicType);
	}

	final public ROSServiceTypeInfo getServiceTypeInfo(String serviceType)
			throws RemoteCommandServiceException {
		return new ROSServiceTypeInfo(remoteCommand("srv", "show", serviceType));
	}

	final public ROSTopicTypeInfo getTopicTypeInfo(String topicType)
			throws RemoteCommandServiceException, ROSTopicInvalidTypeInfoException {
		return new ROSTopicTypeInfo(remoteCommand("msg", "show", topicType));
	}

	final private String remoteCommand(String command, String option, String arg)
			throws RemoteCommandServiceException {
		try {
			List<String> services = getProvidedServiceNameList();
			if (!services
					.contains(RemoteCommandService.getInstance().getName())) {
				System.out
						.println("Start remote_command Service in roscore host.");
				throw new RemoteCommandServiceException("not found");
			}

			List<String> providers = getAllServiceProviderUri(RemoteCommandService
					.getInstance());
			if (providers.size() == 0) {
				System.out
						.println("Start remote_command Service in roscore host.");
				throw new RemoteCommandServiceException("not found");
			}

			RemoteCommandServiceClient client = new RemoteCommandServiceClient(
					callerid);
			List<Object> args = Arrays.asList((Object) command,
					(Object) option, (Object) arg);
			List<Object> ret = client.execute(args);
			return ((String) ret.get(0)).trim();
		} catch (XmlRpcRequestException e) {
			throw new RemoteCommandServiceException(
					"master api request failed.");
		} catch (TransportException e) {
			throw new RemoteCommandServiceException(
					"transporting service value is failed..");
		} catch (ROSServiceNotFoundException e) {
			throw new RemoteCommandServiceException(
					"Remote command service is not found..");
		}
	}

	/**
	 * registerSubscriber <div lang="ja">
	 * 
	 * @param tcprosTopicSubscriber
	 * @return </div> <div lang="en">
	 * 
	 * @param tcprosTopicSubscriber
	 * @return </div>
	 * @throws XmlRpcRequestException
	 */
	public List<ROSUri> registerSubscriber(ROSTopic topic, ROSNode node)
			throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "registerSubscriber("+topic+", "+node+")");
		ReturnValue<List<ROSUri>> ret = getMaster().registerSubscriber(
				node.getName(), topic.getName(), topic.getType(),
				node.getSlaveServerUri());
		node.addSubscribingTopic(topic);
		try {
			for (ROSUri uri : ret.getValue()) {
				node.addPublisherRef(uri, topic);
			}
		} catch (Exception e) {
			throw new XmlRpcRequestException();
		}
		return ret.getValue();
	}
	/**
	 * unregisterSubscriber
	 * <div lang="ja">
	 * 
	 * @param topic
	 * @param node
	 * </div>
	 * <div lang="en">
	 *
	 * @param topic
	 * @param node
	 * </div>
	 */
	public ReturnValue<Integer> unregisterSubscriber(ROSTopic topic, ROSNode node) throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "unregisterSubscriber("+topic+", "+node+")");
		ReturnValue<Integer> ret = getMaster().unregisterSubscriber(
				node.getName(), topic.getName(), node.getSlaveServerUri());
		node.removeSubscribingTopic(topic);
		return ret;
	}

	/**
	 * registerPublisher <div lang="ja">
	 * 
	 * @param rosTopicPublisher
	 * @return </div> <div lang="en">
	 * 
	 * @param rosTopicPublisher
	 * @return </div>
	 * @throws XmlRpcRequestException
	 */
	public List<ROSUri> registerPublisher(ROSTopic topic, ROSNode node)
			throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "registerPublisher("+topic+", "+node);
		ReturnValue<List<ROSUri>> ret = getMaster().registerPublisher(
				node.getName(), topic.getName(), topic.getType(),
				node.getSlaveServerUri());
		node.addPublishingTopic(topic);
		return ret.getValue();
	}

	/**
	 * 
	 * unregisterPublisher
	 * <div lang="ja">
	 * 
	 * @param topic
	 * @param node
	 * @throws XmlRpcRequestException
	 * </div>
	 * <div lang="en">
	 *
	 * @param topic
	 * @param node
	 * @throws XmlRpcRequestException
	 * </div>
	 */
	public ReturnValue<Integer> unregisterPublisher(ROSTopic topic, ROSNode node) throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "unregisterPublisher("+topic+", "+node+")");
		ReturnValue<Integer> ret = getMaster().unregisterPublisher(
				node.getName(), topic.getName(), node.getSlaveServerUri());
		node.removeSubscribingTopic(topic);
		return ret;
	}
	
	/**
	 * getNodeUri <div lang="ja">
	 * 
	 * @param string
	 * @return </div> <div lang="en">
	 * 
	 * @param string
	 * @return </div>
	 * @throws XmlRpcRequestException
	 */
	public ROSUri getNodeUri(String nodeName) throws XmlRpcRequestException {
		ReturnValue<String> uri = getMaster().lookupNode(this.callerid,
				nodeName);
		return new ROSUri(uri.getValue());
	}

	
}
