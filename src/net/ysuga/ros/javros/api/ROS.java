/**
 * ROS.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/05
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.api;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

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
public class ROS {
	
	static private ROS instance = null;
	
	static public ROS getInstance() {
		return instance;
	}
	
	public class ROSHandlerMapping extends PropertyHandlerMapping {
		public ROSHandlerMapping() {
			super();
		}
		
		@Override
		public XmlRpcHandler getHandler(java.lang.String handlerName)
                throws XmlRpcNoSuchHandlerException,
                       XmlRpcException{

			return super.getHandler("net.ysuga.ros.javros.api.SlaveAPIImpl."+handlerName);
		}
	}
	
	private String hostAddress;
	private int port;
	private ROS(String hostAddress) {
		try {

			System.out.println("Attempting to start XML-RPC Server...");
			// WebServer server = new WebServer(80);

			this.hostAddress = hostAddress;
			port = 11311;
			//final String propertyFile = "ROSServer.properties";

			PropertyHandlerMapping mapping = new ROSHandlerMapping();
			ClassLoader cl = Thread.currentThread().getContextClassLoader();

			//mapping.load(cl, propertyFile);
			mapping.addHandler("net.ysuga.ros.javros.api.SlaveAPIImpl", SlaveAPIImpl.class);
			WebServer webServer = new WebServer(port);
			XmlRpcServerConfigImpl config = new XmlRpcServerConfigImpl();
			XmlRpcServer server = webServer.getXmlRpcServer();
			server.setConfig(config);
			server.setHandlerMapping(mapping);
			webServer.start();

			System.out.println("Started successfully.");
			System.out.println("Accepting requests. (Halt program to stop.)");
		} catch (Exception exception) {
			exception.printStackTrace();
			System.err.println("ROS: " + exception);
		}
	}
	
	static public void init(String hostAddress) {
		if(instance == null) {
			instance = new ROS(hostAddress);
		}
	}

	/**
	 * getUri
	 * <div lang="ja">
	 * 
	 * @return
	 * </div>
	 * <div lang="en">
	 *
	 * @return
	 * </div>
	 */
	public String getUri() {
		return "http://" + hostAddress + ":" + port;
	}
}
