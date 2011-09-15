/**
 * TCPROSService.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.tcpros;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import net.ysuga.javros.ROSCore;
import net.ysuga.javros.ROSUri;
import net.ysuga.javros.remote.NoRemoteCommandServiceException;
import net.ysuga.ros.javros.api.XmlRpcRequestException;

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
public class TCPROSServiceClient {

	private ROSService service;
	
	private TCPROSServiceClientHeader header;
	
	private TCPROSTransport transport;
	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NoRemoteCommandServiceException 
	 * @throws XmlRpcRequestException 
	 * @throws ROSServiceNotFoundException 
	 */
	public TCPROSServiceClient(String callerid, ROSService service) throws UnknownHostException, IOException, XmlRpcRequestException, NoRemoteCommandServiceException, ROSServiceNotFoundException {
		List<String> uris = ROSCore.getInstance().getAllServiceProviderUri(service);
		if(uris.size() == 0) {
			throw new ROSServiceNotFoundException();
		}
		this.service = service;
		header = new TCPROSServiceClientHeader(callerid, service.getName(), service.getMd5sum());
		transport = new TCPROSTransport();
		transport.connect(new ROSUri(uris.get(0)));
		transport.send(header.serialize());
		TCPROSHeader serviceHeader = new TCPROSHeader(transport.receive());
	}
	
	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws NoRemoteCommandServiceException 
	 * @throws XmlRpcRequestException 
	 * @throws ROSServiceNotFoundException 
	 */
	/*
	public TCPROSServiceClient(String hostAddress, int port, String callerid, ROSService service) throws UnknownHostException, IOException, XmlRpcRequestException, NoRemoteCommandServiceException, ROSServiceNotFoundException {
		header = new TCPROSServiceClientHeader(callerid, service.getName(), service.getMd5sum());
		connect(hostAddress, port);
		sendMessage(header);
		TCPROSServiceClientHeader serviceHeader = receiveHeader();
	}
	*/

	public List<Object> execute(List<Object> params) throws IOException {
		ArrayList<String> retval = new ArrayList<String>();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		LittleEndianOutputStream outputStream = new LittleEndianOutputStream(baos);
		for(Object param : params) {
			if(param instanceof String) {
				outputStream.writeInt(((String)param).length());
				outputStream.write(((String)param).getBytes());
			} else if(param instanceof Integer) {
				outputStream.writeInt(((Integer)param).intValue());
			} else if(param instanceof Long) {
				outputStream.writeLong( ((Long)param).longValue());
			}
		}
		byte[] message = baos.toByteArray();
		transport.outputStream.writeInt(message.length);
		transport.outputStream.write(message);
		
		byte[] b = new byte[1];
		transport.inputStream.read(b, 0, 1);
		
		int headersize = transport.inputStream.readInt();
		byte[] byteBuffer = new byte[headersize];
		transport.inputStream.read(byteBuffer, 0, headersize);
		return decode(byteBuffer);
	}

	/**
	 * decode
	 * <div lang="ja">
	 * 
	 * @param byteBuffer
	 * @return
	 * </div>
	 * <div lang="en">
	 *
	 * @param byteBuffer
	 * @return
	 * </div>
	 * @throws ROSServiceNotFoundException 
	 * @throws NoRemoteCommandServiceException 
	 * @throws XmlRpcRequestException 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 */
	private List<Object> decode(byte[] byteBuffer) throws IOException  {
		ArrayList<Object> retval = new ArrayList<Object>();
		LittleEndianInputStream inputStream = new LittleEndianInputStream(byteBuffer);
		ROSServiceTypeInfo info = service.getServiceTypeInfo();
		for(String type:info.returnParam.keySet()) {
			if(type.equals("string")) {
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
	}

}
