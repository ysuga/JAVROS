/**
 * XmlRpcWrapper.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/05
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.logging.Logger;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 * XML-RPC caller wrapper class.
 * 
 * In this step, this uses Apache XML-RPC.
 * 
 * @author ysuga
 *
 */
public class XmlRpcWrapper {


	static Logger logger = Logger.getLogger(XmlRpcWrapper.class.getName())
	;
	protected String hostUri;
	
	public void setHostUri(String hostUri) throws MalformedURLException {
		this.hostUri = hostUri;
		config.setServerURL(new URL(hostUri));
	}
	
	final public String getHostUri() {
		return hostUri;
	}
	
	protected final XmlRpcClient client;
	protected final XmlRpcClientConfigImpl config;

	
	/**
	 * Constructor
	 * @param uri
	 */
	public XmlRpcWrapper(URL uri) {
		this.hostUri = uri.toString();
		config = new XmlRpcClientConfigImpl();
		config.setServerURL(uri);
		client = new XmlRpcClient();
		
	}
	
	public XmlRpcWrapper() {
		config = new XmlRpcClientConfigImpl();
		client = new XmlRpcClient();
	}
	
	/**
	 * Requesting Function of Xml-Rpc service.
	 * @param method
	 * @param param
	 * @return
	 * @throws XmlRpcRequestException
	 */
	@SuppressWarnings("unchecked")
	protected Object request(String method, List param)
			throws XmlRpcRequestException {
		logger.entering(this.getClass().getName(), "request("+method+ ", \n" + param + ")");
		try {
			return client.execute(config, method, param);
		} catch (XmlRpcException ex) {
			logger.severe("XmlRpcWrapper.request failed.");
			
			throw new XmlRpcRequestException();
		}
	}
}
