/**
 * ServiceState.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/02
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.api;

import java.util.ArrayList;
import java.util.List;

/**
 * <div lang="ja">
 *
 * </div>
 * <div lang="en">
 * Service Provier List for each service.
 * </div>
 * @author ysuga
 *
 */
public class ServiceList extends ArrayList<String>{
	
	final private String service;
	
	final public String getServiceName() {
		return service;
	}
	
	public ServiceList(String service, List<Object> list) {
		super();
		this.service = service;
		for(Object o:list) {
			add((String)o);
		}
	}

}
