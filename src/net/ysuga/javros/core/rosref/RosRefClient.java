/**
 * RemoteCommandServiceClient.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.core.rosref;

import net.ysuga.javros.node.service.ROSServiceNotFoundException;
import net.ysuga.javros.node.service.ROSServiceConsumer;
import net.ysuga.javros.transport.TransportException;
import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

/**
 * rosref service client class.
 * Users do not have to use this class.
 * @author ysuga
 *
 */
public class RosRefClient extends ROSServiceConsumer {

	/**
	 * Constructor
	 * @param callerid
	 * @throws XmlRpcRequestException
	 * @throws ROSServiceNotFoundException
	 * @throws TransportException
	 */
	public RosRefClient(String callerid) throws XmlRpcRequestException, ROSServiceNotFoundException, TransportException {
		super(callerid, RosRefService.getInstance());
	}

}
