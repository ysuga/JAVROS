/**
 * ROSCore.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.ysuga.javros.remote.NoRemoteCommandServiceException;
import net.ysuga.javros.remote.RemoteCommandService;
import net.ysuga.javros.remote.RemoteCommandServiceClient;
import net.ysuga.ros.javros.api.MasterAPIHelper;
import net.ysuga.ros.javros.api.MasterAPIRef;
import net.ysuga.ros.javros.api.ReturnValue;
import net.ysuga.ros.javros.api.XmlRpcRequestException;
import net.ysuga.ros.javros.tcpros.ROSNode;
import net.ysuga.ros.javros.tcpros.ROSService;
import net.ysuga.ros.javros.tcpros.ROSServiceFactory;
import net.ysuga.ros.javros.tcpros.ROSServiceNotFoundException;
import net.ysuga.ros.javros.tcpros.ROSServiceTypeInfo;
import net.ysuga.ros.javros.tcpros.ROSTopic;
import net.ysuga.ros.javros.tcpros.ROSTopicPublisherRef;
import net.ysuga.ros.javros.tcpros.ROSTopicTypeInfo;

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
			throws java.net.MalformedURLException {
		this.callerid = "/javrosCore";
		this.hostAddress = hostAddress;
		this.port = portNumber;
		masterAPI = new MasterAPIHelper(new MasterAPIRef("http://"
				+ hostAddress + ":" + port));
	}

	static public ROSCore init(String hostAddress) throws MalformedURLException {
		return init(hostAddress, DEFAULT_PORT);
	}

	static public ROSCore init(String hostAddress, int portNumber)
			throws MalformedURLException {
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
			throws XmlRpcRequestException, UnknownHostException, IOException,
			NoRemoteCommandServiceException, ROSServiceNotFoundException {
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
			throws XmlRpcRequestException, UnknownHostException, IOException,
			NoRemoteCommandServiceException, ROSServiceNotFoundException {
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

	public String getServiceType(String serviceName)
			throws UnknownHostException, IOException, XmlRpcRequestException,
			NoRemoteCommandServiceException, ROSServiceNotFoundException {
		return remoteCommand("service", "type", serviceName);
	}

	public String getTopicType(String topicName) throws UnknownHostException,
			IOException, XmlRpcRequestException,
			NoRemoteCommandServiceException, ROSServiceNotFoundException {
		return remoteCommand("topic", "type", topicName);
	}

	public String getServiceMd5Sum(String serviceType)
			throws UnknownHostException, IOException, XmlRpcRequestException,
			NoRemoteCommandServiceException, ROSServiceNotFoundException {
		return remoteCommand("srv", "md5", serviceType);
	}

	public String getTopicMd5Sum(String topicType) throws UnknownHostException,
			IOException, XmlRpcRequestException,
			NoRemoteCommandServiceException, ROSServiceNotFoundException {
		return remoteCommand("msg", "md5", topicType);
	}

	public ROSServiceTypeInfo getServiceTypeInfo(String serviceType)
			throws UnknownHostException, IOException, XmlRpcRequestException,
			NoRemoteCommandServiceException, ROSServiceNotFoundException {
		return new ROSServiceTypeInfo(remoteCommand("srv", "show", serviceType));
	}

	public ROSTopicTypeInfo getTopicTypeInfo(String topicType)
			throws UnknownHostException, IOException, XmlRpcRequestException,
			NoRemoteCommandServiceException, ROSServiceNotFoundException {
		return new ROSTopicTypeInfo(remoteCommand("msg", "show", topicType));
	}

	private String remoteCommand(String command, String option, String arg)
			throws UnknownHostException, IOException, XmlRpcRequestException,
			NoRemoteCommandServiceException, ROSServiceNotFoundException {
		List<String> services = getProvidedServiceNameList();
		if (!services.contains(RemoteCommandService.getInstance().getName())) {
			System.out.println("Start remote_command Service in roscore host.");
			throw new NoRemoteCommandServiceException();
		}

		List<String> providers = getAllServiceProviderUri(RemoteCommandService
				.getInstance());
		if (providers.size() == 0) {
			System.out.println("Start remote_command Service in roscore host.");
			throw new NoRemoteCommandServiceException();
		}

		RemoteCommandServiceClient client = new RemoteCommandServiceClient(
				callerid);
		List<Object> args = Arrays.asList((Object) command, (Object) option,
				(Object) arg);
		List<Object> ret = client.execute(args);
		return ((String) ret.get(0)).trim();
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
	public List<ROSUri> registerSubscriber(ROSTopic topic,
			ROSNode node)
			throws XmlRpcRequestException {
		System.out.println("ROSCore#registerSubscriber(" + topic.toString() + ", " + node.getName()+")");
		ReturnValue<List<ROSUri>> ret = getMaster().registerSubscriber(
				node.getName(), topic.getName(),
				topic.getType(), node.getXmlRpcUri());
		System.out.println("Code = " + ret.getCode());
		try {
			for(ROSUri uri : ret.getValue()) {
				ROSTopicPublisherRef publisher = new ROSTopicPublisherRef(uri, topic, node);
				node.addPublisherRef(publisher);
			}
			
		} catch (UnknownHostException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		return ret.getValue();
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
		System.out.println("ROSCore#registerPublisher(" + topic.toString() + ", " + node.getName()+")");
		ReturnValue<List<ROSUri>> ret = getMaster().registerPublisher(
				node.getName(), topic.getName(), topic.getType(),
				node.getXmlRpcUri());
		System.out.println("Code = " + ret.getCode());

		return ret.getValue();
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
