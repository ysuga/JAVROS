/**
 * ROSServiceFactory.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.service;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import net.ysuga.javros.ROSCore;
import net.ysuga.javros.core.rosref.RosRefException;
import net.ysuga.javros.node.XmlRpcRequestException;

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
	 * �R���X�g���N�^
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	private ROSServiceFactory() {
	}

	static public ROSService createROSService(String serviceName) throws RosRefException  {
		if(!createdROSServiceMap.containsKey(serviceName)) {
			String serviceType = ROSCore.getInstance().getServiceType(serviceName);
			ROSService service = new ROSService(serviceName, serviceType, ROSCore.getInstance().getServiceMd5Sum(serviceType));
			service.setServiceTypeInfo(ROSCore.getInstance().getServiceTypeInfo(service.getType()));
			createdROSServiceMap.put(serviceName, service);
		}
		return createdROSServiceMap.get(serviceName);
	}
}
