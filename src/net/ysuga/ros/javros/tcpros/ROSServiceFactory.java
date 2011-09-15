/**
 * ROSServiceFactory.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.tcpros;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import net.ysuga.javros.ROSCore;
import net.ysuga.javros.remote.NoRemoteCommandServiceException;
import net.ysuga.ros.javros.api.XmlRpcRequestException;

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
public class ROSServiceFactory {

	static private HashMap<String, ROSService> createdROSServiceMap;
	static {
		createdROSServiceMap = new HashMap<String, ROSService>();
	}
	
	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	private ROSServiceFactory() {
	}

	static public ROSService createROSService(String serviceName) throws UnknownHostException, IOException, XmlRpcRequestException, NoRemoteCommandServiceException, ROSServiceNotFoundException {
		if(!createdROSServiceMap.containsKey(serviceName)) {
			String serviceType = ROSCore.getInstance().getServiceType(serviceName);
			ROSService service = new ROSService(serviceName, serviceType, ROSCore.getInstance().getServiceMd5Sum(serviceType));
			service.setServiceTypeInfo(ROSCore.getInstance().getServiceTypeInfo(service.getType()));
			createdROSServiceMap.put(serviceName, service);
		}
		return createdROSServiceMap.get(serviceName);
	}
}
