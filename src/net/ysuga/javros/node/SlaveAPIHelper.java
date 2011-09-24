/**
 * SlaveAPIHelper.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node;

import net.ysuga.javros.util.ROSAPIStatusCode;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.javros.util.ReturnValue;
import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

/**
 * 
 * @author ysuga
 * 
 */
public class SlaveAPIHelper {

	private SlaveAPIRef slaveAPI;

	final public String getHostUri() {
		return slaveAPI.getHostUri();
	}

	public SlaveAPIRef getRef() {
		return slaveAPI;
	}

	/**
	 * <div lang="ja"> �R���X�g���N�^ </div> <div lang="en"> Constructor </div>
	 */
	public SlaveAPIHelper(SlaveAPIRef ref) {
		slaveAPI = ref;
	}

	public void setSlaveAPIRef(SlaveAPIRef ref) {
		slaveAPI = ref;
	}

	public ReturnValue<ROSUri> requestTopic(String callerId, String topic,
			Object[] protocols) throws XmlRpcRequestException {
		Object[] ret = slaveAPI.requestTopic(callerId, topic, protocols);
		ROSUri uri = null;
		Integer retval = (Integer) ret[0];
		if (retval.intValue() == ROSAPIStatusCode.ROSAPI_SUCCESS) {
			Object[] prop = (Object[]) ret[2];
			uri = new ROSUri((String) prop[0] + "://" + (String) prop[1] + ":"
					+ ((Integer) prop[2]).toString());
		}
		return new ReturnValue<ROSUri>(retval, (String) ret[1], uri);
	}

	public ReturnValue<Object[]> getBusInfo(String callerId)
			throws XmlRpcRequestException {
		Object[] ret = slaveAPI.getBusInfo(callerId);
		return new ReturnValue<Object[]>(Integer.parseInt((String) ret[0]),
				(String) ret[1], (Object[]) ret[2]);
	}

}
