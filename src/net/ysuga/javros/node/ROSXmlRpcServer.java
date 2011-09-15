/**
 * ROS.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/05
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import net.ysuga.javros.util.ROSUri;

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
	
	private static Logger logger =  Logger.getLogger(ROSXmlRpcServer.class.getName());  
	
	private ROSUri uri;
	
	/**
	 * getUri <div lang="ja">
	 * 
	 * @return </div> <div lang="en">
	 * 
	 * @return </div>
	 */
	public ROSUri getUri() {
		return uri;
	}
	

	private WebServer webServer;
	
	private String className;

	/**
	 * 
	 * <div lang="ja">
	 * コンストラクタ
	 * @param handler
	 * @param hostAddress
	 * @param port
	 * @throws XmlRpcException
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * @param handler
	 * @param hostAddress
	 * @param port
	 * @throws XmlRpcException
	 * </div>
	 */
	public ROSXmlRpcServer(Object handler, String hostAddress, int port)
			throws ROSXmlRpcServerException {
		logger.info("Creating ROSXmlRpcServer("+hostAddress + ":" + port+")");
		this.uri = new ROSUri("http", hostAddress, port);
		this.className = handler.getClass().getName();

		PropertyHandlerMapping propertyHandlerMapping = new ROSHandlerMapping();
	    propertyHandlerMapping.setRequestProcessorFactoryFactory(new ROSRequestProcessorFactoryFactory(handler));
	    try {
	    	propertyHandlerMapping.addHandler(handler.getClass().getName(), handler.getClass());
	    } catch (XmlRpcException ex) {
	    	logger.severe("ROSXmlRpcServer create failed.");
	    	throw new ROSXmlRpcServerException();
	    }
	      
		webServer = new WebServer(port);
		XmlRpcServerConfigImpl config = new XmlRpcServerConfigImpl();
		XmlRpcStreamServer server = webServer.getXmlRpcServer();
		server.setConfig(config);
		server.setHandlerMapping(propertyHandlerMapping);
	}

	/**
	 * 
	 * start
	 * <div lang="ja">
	 * 
	 * @throws IOException
	 * </div>
	 * <div lang="en">
	 * This method starts xml-rpc server.
	 * @throws IOException
	 * </div>
	 * @throws ROSXmlRpcServerException 
	 */
	public void start() throws ROSXmlRpcServerException {
		try {
			webServer.start();
		} catch (IOException e) {
			PrintWriter pw = new PrintWriter(new StringWriter());
			e.printStackTrace(pw);
			logger.severe("ROSXmlRpcServer.start() failed(" + pw.toString() + ")");
			throw new ROSXmlRpcServerException();
		}
	}



	/**
	 * shutdown <div lang="ja">
	 * 
	 * </div> <div lang="en">
	 * This method stop the xml-rpc server.
	 * </div>
	 */
	public void shutdown() {
		webServer.shutdown();
	}



	/**
	 * 
	 * <div lang="ja">
	 *
	 * </div>
	 * <div lang="en">
	 * ROSHandlerMapping. This class helps name space resolution.
	 * ROSMaster do not have any name space. It just call without class name.
	 * This mapping class attache the class name.
	 * </div>
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
	
	
	/**
	 * ROSRequestProcessorFactoryFactory
	 * <div lang="ja">
	 *
	 * </div>
	 * <div lang="en">
	 * This enables that one the XML-RPC request handler object handles all request.
	 * Usually apache xml-rpc server creates a request handler in every request. 
	 * </div>
	 * @author ysuga
	 *
	 */
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
	
}
