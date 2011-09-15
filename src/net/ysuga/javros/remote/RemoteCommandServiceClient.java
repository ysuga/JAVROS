/**
 * RemoteCommandServiceClient.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.remote;

import java.io.IOException;
import java.net.UnknownHostException;

import net.ysuga.javros.node.XmlRpcRequestException;
import net.ysuga.javros.node.service.ROSServiceNotFoundException;
import net.ysuga.javros.node.service.TCPROSServiceClient;
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
public class RemoteCommandServiceClient extends TCPROSServiceClient {

	/**
	 * <div lang="ja"> コンストラクタ
	 * 
	 * @param hostAddress
	 * @param port
	 * @param callerid
	 * @param service
	 * @throws UnknownHostException
	 * @throws IOException
	 *             </div> <div lang="en"> Constructor
	 * @param hostAddress
	 * @param port
	 * @param callerid
	 * @param service
	 * @throws TransportException
	 * @throws UnknownHostException
	 * @throws IOException
	 *             </div>
	 * @throws RemoteCommandServiceException
	 * @throws XmlRpcRequestException
	 * @throws ROSServiceNotFoundException
	 */
	public RemoteCommandServiceClient(String callerid) throws XmlRpcRequestException, ROSServiceNotFoundException, TransportException {
		super(callerid, RemoteCommandService.getInstance());
	}

}
