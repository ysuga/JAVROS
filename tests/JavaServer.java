import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.XmlRpcHandler;
import org.apache.xmlrpc.server.PropertyHandlerMapping;
import org.apache.xmlrpc.server.XmlRpcNoSuchHandlerException;
import org.apache.xmlrpc.server.XmlRpcServer;
import org.apache.xmlrpc.server.XmlRpcServerConfigImpl;
import org.apache.xmlrpc.webserver.WebServer;

public class JavaServer {

	public JavaServer() {
		try {

			System.out.println("Attempting to start XML-RPC Server...");
			// WebServer server = new WebServer(80);

			
			final int port = 11311;
			final String propertyFile = "JavaServer.properties";

			PropertyHandlerMapping mapping = new MyHandlerMapping();
			ClassLoader cl = Thread.currentThread().getContextClassLoader();

			mapping.load(cl, propertyFile);
			WebServer webServer = new WebServer(port);
			XmlRpcServerConfigImpl config = new XmlRpcServerConfigImpl();
			XmlRpcServer server = webServer.getXmlRpcServer();
			server.setConfig(config);
			server.setHandlerMapping(mapping);
			webServer.start();

			
			/**
			 * server.addHandler("sample", new JavaServer()); server.start();
			 */
			System.out.println("Started successfully.");
			System.out.println("Accepting requests. (Halt program to stop.)");
		} catch (Exception exception) {
			System.err.println("JavaServer: " + exception);
		}
	}

	public class MyHandlerMapping extends PropertyHandlerMapping {
		public MyHandlerMapping() {
			super();
		}
		
		@Override
		public XmlRpcHandler getHandler(java.lang.String handlerName)
                throws XmlRpcNoSuchHandlerException,
                       XmlRpcException{
			System.out.println("HandlerName = " + handlerName);
			
			return super.getHandler("ServerHandler."+handlerName);
		}
	}

	public static void main(String[] args) {
		new JavaServer();
	}
}