/**
 * RemoteCommandService.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.remote;

import net.ysuga.javros.node.service.ROSService;
import net.ysuga.javros.node.service.ROSServiceTypeInfo;

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

public class RemoteCommandService extends ROSService {
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
	static private RemoteCommandService instance;
	
	static public RemoteCommandService getInstance() {
		if(instance == null) {
			instance = new RemoteCommandService();
		}
		return instance;
	}
	
	private RemoteCommandService() {
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