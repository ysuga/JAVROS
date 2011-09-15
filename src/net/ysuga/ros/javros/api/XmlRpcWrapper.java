/**
 * XmlRpcWrapper.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/05
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

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
public class XmlRpcWrapper {


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

	
	public XmlRpcWrapper(String hostUri) throws MalformedURLException {
		this.hostUri = hostUri;
		config = new XmlRpcClientConfigImpl();
		config.setServerURL(new URL(hostUri));
		client = new XmlRpcClient();
		
	}
	
	public XmlRpcWrapper() {
		config = new XmlRpcClientConfigImpl();
		client = new XmlRpcClient();
	}
	
	protected Object request(String method, List param)
			throws XmlRpcRequestException {
		try {
			return client.execute(config, method, param);
		} catch (XmlRpcException ex) {
			ex.printStackTrace();
			throw new XmlRpcRequestException();
		}
	}
}
