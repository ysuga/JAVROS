/**
 * TCPROSService.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.ysuga.javros.core.ROSCoreRef;
import net.ysuga.javros.core.rosref.RosRefException;
import net.ysuga.javros.transport.LittleEndianOutputStream;
import net.ysuga.javros.transport.TCPROSHeader;
import net.ysuga.javros.transport.TCPROSTransport;
import net.ysuga.javros.transport.TransportException;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.javros.value.ROSValue;
import net.ysuga.javros.value.ROSValueInvalidArgumentException;
import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

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
public class ROSServiceConsumer {

	private ROSService service;

	private TCPROSHeader header;

	private TCPROSTransport transport;

	/**
	 * 
	 * @param callerid
	 * @param service
	 * @throws XmlRpcRequestException
	 * @throws ROSServiceNotFoundException
	 * @throws TransportException
	 */
	public ROSServiceConsumer(String callerid, ROSService service)
			throws XmlRpcRequestException, ROSServiceNotFoundException,
			TransportException {
		List<String> uris = ROSCoreRef.getInstance().getServiceProviderUri(
				service);
		if (uris.size() == 0) {
			throw new ROSServiceNotFoundException();
		}
		this.service = service;
		header = new TCPROSHeader();
		header.put("callerid", callerid);
		header.put("md5sum", service.getMd5sum());
		header.put("service", service.getName());

		transport = new TCPROSTransport();
		transport.connect(new ROSUri(uris.get(0)));
		transport.send(header.serialize());
		TCPROSHeader serviceHeader = new TCPROSHeader(transport.receive());
	}

	/**
	 * <div lang="ja"> �R���X�g���N�^ </div> <div lang="en"> Constructor </div>
	 * 
	 * @throws IOException
	 * @throws TransportException
	 * @throws UnknownHostException
	 * @throws RosRefException
	 * @throws XmlRpcRequestException
	 * @throws ROSServiceNotFoundException
	 */
	public ROSValue call(Object... params) throws TransportException {
		ROSValue value;
		try {
			value = new ROSValue(service.getServiceTypeInfo().argumentInfo,
					params);

			transport.send(value.serialize());
			transport.readByte();
			byte[] byteBuffer = transport.receive();
			ROSValue retval = new ROSValue(
					service.getServiceTypeInfo().returnInfo, byteBuffer);
			return retval;
		} catch(IOException e) {
			throw new TransportException();
		} catch (ROSValueInvalidArgumentException e) {
			throw new TransportException();
		}
	}

}
