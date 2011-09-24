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

import net.ysuga.javros.core.ROSCoreRef;
import net.ysuga.javros.core.rosref.RosRefException;
import net.ysuga.javros.value.ROSValueInvalidTypeInfoException;
import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

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

	static public ROSService createROSService(String serviceName) throws RosRefException, ROSValueInvalidTypeInfoException  {
		if(!createdROSServiceMap.containsKey(serviceName)) {
			String serviceType = ROSCoreRef.getInstance().getServiceType(serviceName);
			ROSService service = new ROSService(serviceName, serviceType, ROSCoreRef.getInstance().getServiceMd5Sum(serviceType));
			service.setServiceTypeInfo(ROSCoreRef.getInstance().getServiceTypeInfo(service.getType()));
			createdROSServiceMap.put(serviceName, service);
		}
		return createdROSServiceMap.get(serviceName);
	}
}
