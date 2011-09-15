/**
 * ROS.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/05
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.api;

import java.io.IOException;

import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.XmlRpcRequest;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.RequestProcessorFactoryFactory;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.server.XmlRpcStreamServer;
import org.apache.xmlrpc.webserver.WebServer;

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
public class ROSXmlRpcServer {

	private Class handlerClass;

	private String className;

	public class ROSRequestProcessorFactoryFactory implements
			RequestProcessorFactoryFactory {
		private final RequestProcessorFactory factory = new ROSRequestProcessorFactory();
		private final Object processor;

		public ROSRequestProcessorFactoryFactory(Object processor) {
			this.processor = processor;
		}

		@Override
		public RequestProcessorFactory getRequestProcessorFactory(Class aClass)
				throws XmlRpcException {
			return factory;
		}

		private class ROSRequestProcessorFactory implements
				RequestProcessorFactory {
			
			@Override
			public Object getRequestProcessor(XmlRpcRequest xmlRpcRequest)
					throws XmlRpcException {
				return processor;
			}
		}
	}

	/**
	 * 
	 * <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 * 
	 * @author ysuga
	 * 
	 */
	public class ROSHandlerMapping extends PropertyHandlerMapping {
		public ROSHandlerMapping() {
			super();
		}

		@Override
		public XmlRpcHandler getHandler(java.lang.String handlerName)
				throws XmlRpcNoSuchHandlerException, XmlRpcException {

			StringBuilder sb = new StringBuilder();
			sb.append(className);
			sb.append(".");
			sb.append(handlerName);
			return super.getHandler(sb.toString());
		}
	}

	private String hostAddress;

	private int port;

	private WebServer webServer;

	public ROSXmlRpcServer(Object handler, String hostAddress, int port)
			throws XmlRpcException {

		this.hostAddress = hostAddress;
		this.port = port;
		this.className = handler.getClass().getName();

		PropertyHandlerMapping propertyHandlerMapping = new ROSHandlerMapping();
	    propertyHandlerMapping.setRequestProcessorFactoryFactory(new ROSRequestProcessorFactoryFactory(handler));
	    propertyHandlerMapping.addHandler(handler.getClass().getName(), handler.getClass());
	      
		webServer = new WebServer(port);
		XmlRpcServerConfigImpl config = new XmlRpcServerConfigImpl();
		XmlRpcStreamServer server = webServer.getXmlRpcServer();
		server.setConfig(config);
		server.setHandlerMapping(propertyHandlerMapping);

	}

	public void start() throws IOException {
		webServer.start();
	}

	/**
	 * getUri <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return </div>
	 */
	public String getUri() {
		return "http://" + hostAddress + ":" + port;
	}

	/**
	 * shutdown <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * 
	 * </div>
	 */
	public void shutdown() {
		webServer.shutdown();
	}
}
