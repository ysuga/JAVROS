/**
 * RosRefService.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.core.rosref;

import net.ysuga.javros.node.service.ROSService;
import net.ysuga.javros.node.service.ROSServiceTypeInfo;

/**
 * rosref service object.
 * 
 * Users do not have to use this class.
 * 
 * @author ysuga
 *
 */
public class RosRefService extends ROSService {
	final public static String SERVICE_SIGNATURE =  "/refrection";
	final public static String SERVICE_TYPE = "refrection_service/Refrection";
	final public static String SERVICE_MD5SUM = "fbd945844e2fc7a26e9684715f3a7f45";

	final public static String[][] params = {
		{"string", "command"},
		{"string", "option"},
		{"string", "arg"},
	};
	
	final public static String[][] retval = {
		{"string", "ret"},
	};
	
	/**
	 * 
	 * Singleton
	 */
	static private RosRefService instance;
	
	static public RosRefService getInstance() {
		if(instance == null) {
			instance = new RosRefService();
		}
		return instance;
	}
	
	private RosRefService() {
		super(SERVICE_SIGNATURE, SERVICE_TYPE, SERVICE_MD5SUM);
		ROSServiceTypeInfo info = new ROSServiceTypeInfo();
		for(String[] pair: params) {
			info.argumentParam.put(pair[0], pair[1]);
		}
		for(String[] pair: retval) {
			info.returnParam.put(pair[0], pair[1]);
		}
		this.setServiceTypeInfo(info);
	}
}