package net.ysuga.javros.node.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Logger;

import net.ysuga.javros.node.ROSNode;
import net.ysuga.javros.transport.TCPROSHeader;
import net.ysuga.javros.transport.TCPROSTransport;
import net.ysuga.javros.transport.TransportException;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.javros.value.ROSValue;

public abstract class ROSServiceProvider implements Runnable {

	static Logger logger = Logger.getLogger(ROSServiceProvider.class.getName());

	private ROSService service;

	private TCPROSTransport transport;

	private ROSUri protocol;

	
	/**
	 * 
	 * @return
	 */
	final public String getUri() {
		return protocol.getPath();
	}
	
	private ROSNode owner;

	private TCPROSHeader header;

	private Thread thread;

	private boolean persistent;

	public ROSService getService() {
		return service;
	}

	public ROSServiceProvider(ROSNode node, ROSService service) {

		try {
			this.owner = node;
			transport = new TCPROSTransport(40000);
			this.protocol = new ROSUri("TCPROS", owner.getSlaveServerUri(),
					transport.getPort());

			header = new TCPROSHeader();
			header.put("callerid", owner.getName());
			header.put("md5sum", service.getMd5sum());
			header.put("service", service.getName());
			thread = new Thread(this);
			thread.start();
		} catch (TransportException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			while (true) {
				accept();
				ROSServiceTypeInfo info = service.getServiceTypeInfo();
				while (true) {
					byte[] b = transport.receive();
					ROSValue argument = new ROSValue(info.argumentInfo, b);
					ROSValue retval = this.onCall(argument);
					if (!persistent) {
						disconnect();
						break;
					}
						Thread.yield();
				}
			}
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.severe("ROSTopicSubscriberRef.run failed:\n"
					+ sw.getBuffer().toString());
		}
	}

	private void accept() throws TransportException {
		byte[] headerData = header.serialize();
		transport.accept();
		TCPROSHeader rcvHeader = new TCPROSHeader(transport.receive());
		if (rcvHeader.containsKey("persistent")) {
			this.persistent = Boolean.parseBoolean(rcvHeader.get("persisten"));
		}
		transport.send(headerData);
	}
	
	private void disconnect() throws TransportException {
		transport.disconnect();
	}

	public abstract ROSValue onCall(ROSValue value);



}
