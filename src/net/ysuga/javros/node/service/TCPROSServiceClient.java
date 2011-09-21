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
import java.util.Set;

import net.ysuga.javros.ROSCore;
import net.ysuga.javros.core.rosref.RosRefException;
import net.ysuga.javros.node.XmlRpcRequestException;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.ros.javros.tcpros.LittleEndianInputStream;
import net.ysuga.ros.javros.tcpros.LittleEndianOutputStream;
import net.ysuga.ros.javros.tcpros.TCPROSHeader;
import net.ysuga.ros.javros.tcpros.TCPROSTransport;
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
public class TCPROSServiceClient {

	private ROSService service;

	private TCPROSServiceClientHeader header;

	private TCPROSTransport transport;

	/**
	 * <div lang="ja"> �R���X�g���N�^ </div> <div lang="en"> Constructor </div>
	 * 
	 * @throws IOException
	 * @throws UnknownHostException
	 * @throws RosRefException
	 * @throws XmlRpcRequestException
	 * @throws ROSServiceNotFoundException
	 * @throws TransportException
	 */
	public TCPROSServiceClient(String callerid, ROSService service) throws XmlRpcRequestException, ROSServiceNotFoundException, TransportException {
		List<String> uris = ROSCore.getInstance().getAllServiceProviderUri(
				service);
		if (uris.size() == 0) {
			throw new ROSServiceNotFoundException();
		}
		this.service = service;
		header = new TCPROSServiceClientHeader(callerid, service.getName(),
				service.getMd5sum());
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
	public List<Object> execute(List<Object> params) throws TransportException {
		byte[] message;
		try {
			ArrayList<String> retval = new ArrayList<String>();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			LittleEndianOutputStream outputStream = new LittleEndianOutputStream(
					baos);
			for (Object param : params) {
				if (param instanceof String) {
					outputStream.writeInt(((String) param).length());
					outputStream.write(((String) param).getBytes());
				} else if (param instanceof Integer) {
					outputStream.writeInt(((Integer) param).intValue());
				} else if (param instanceof Long) {
					outputStream.writeLong(((Long) param).longValue());
				}
			}
			message = baos.toByteArray();
		} catch (IOException e) {
			throw new TransportException();
		}
		
		
		transport.send(message);

		transport.readByte();

		byte[] byteBuffer = transport.receive();
		return decode(byteBuffer);
	}

	/**
	 * decode <div lang="ja">
	 * 
	 * @param byteBuffer
	 * @return </div> <div lang="en">
	 * 
	 * @param byteBuffer
	 * @return </div>
	 * @throws TransportException 
	 * @throws ROSServiceNotFoundException
	 * @throws RosRefException
	 * @throws XmlRpcRequestException
	 * @throws IOException
	 * @throws UnknownHostException
	 */
	private List<Object> decode(byte[] byteBuffer) throws TransportException {
		try {
		ArrayList<Object> retval = new ArrayList<Object>();
		LittleEndianInputStream inputStream = new LittleEndianInputStream(
				byteBuffer);
		ROSServiceTypeInfo info = service.getServiceTypeInfo();
		for (String type : (Set<String>)(info.returnParam.keySet())) {
			if (type.equals("string")) {
				int size = inputStream.readInt();
				retval.add(inputStream.readString(size));
			} else if (type.equals("int32")) {
				retval.add(new Integer(inputStream.readInt()));
			} else if (type.equals("int64")) {
				retval.add(new Long(inputStream.readLong()));
			} else if (type.equals("float32")) {
				retval.add(new Float(inputStream.readFloat()));
			} else if (type.equals("float64")) {
				retval.add(new Double(inputStream.readDouble()));
			}
		}

		return retval;
		} catch(IOException e) {
			throw new TransportException();
		}
	}

}
