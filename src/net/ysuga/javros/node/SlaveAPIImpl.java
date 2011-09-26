/**
 * 
 */
package net.ysuga.javros.node;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import net.ysuga.javros.core.ROSCoreRef;
import net.ysuga.javros.node.parameter.ROSParameter;
import net.ysuga.javros.node.topic.ROSTopic;
import net.ysuga.javros.node.topic.ROSTopicFactory;
import net.ysuga.javros.node.topic.ROSTopicPublisherRef;
import net.ysuga.javros.node.topic.ROSTopicSubscriberRef;
import net.ysuga.javros.util.ROSUri;
import net.ysuga.javros.xmlrpc.XmlRpcRequestException;

/**
 * Slave API implementation.
 * This is used in ROSNode class.
 * @author ysuga
 *
 */
public class SlaveAPIImpl implements SlaveAPI {

	static Logger logger = Logger.getLogger(SlaveAPIImpl.class.getName());

	/**
	 * 
	 */
	private final ROSNode owner;

	/**
	 * Constructor
	 * 
	 * @param rosNode
	 */
	public SlaveAPIImpl(ROSNode rosNode) {
		owner = rosNode;
	}

	/**
	 * get bus status api
	 * 
	 * @param callerId
	 * @return
	 */
	@Override
	public Object[] getBusStats(String callerId) {
		logger.entering(SlaveAPIImpl.class.getName(), "getBusStats", callerId);
		int topicNum = 0;
		Object[] publishStats = new Object[topicNum];
		Object[] subscribeStats = new Object[topicNum];
		Object[] serviceStats = new Object[0];

		Object[] stats = new Object[3];
		stats[0] = publishStats;
		stats[1] = subscribeStats;
		stats[2] = serviceStats;

		Object[] retval = new Object[3];
		retval[0] = new Integer(0);
		retval[1] = new String("OK");
		retval[3] = stats;
		return retval;
	}

	/**
	 * 
	 * @param callerId
	 * @return
	 */
	@Override
	public Object[] getBusInfo(String callerId) {
		logger.entering(SlaveAPIImpl.class.getName(), "getBusInfo", callerId);
		ArrayList<Object> objectArray = new ArrayList<Object>();
		int counter = 0;
		synchronized (owner.publisherMapMutex) {
			for (ROSTopicPublisherRef publisher : owner.publisherMap.values()) {
				Object[] busInfo = publisher.getBusInfo(counter);
				objectArray.add(busInfo);
				counter++;
			}
		}
		synchronized (owner.subscriberMapMutex) {
			for (ROSTopicSubscriberRef subscriber : owner.subscriberMap
					.values()) {
				Object[] busInfo = subscriber.getBusInfo(counter);
				objectArray.add(busInfo);
				counter++;
			}
		}
		Object[] ret = new Object[] { new Integer(1), "bus info",
				objectArray.toArray() };
		logger.exiting(SlaveAPIImpl.class.getName(), "getBusInfo", ret);
		return ret;
	}

	/**
	 * @param callerId
	 * @return
	 */
	@Override
	public Object[] getMasterUri(String callerId) throws XmlRpcRequestException {
		logger.entering(SlaveAPIImpl.class.getName(), "getMasterUri", callerId);
		String masterUri = ROSCoreRef.getInstance().getHostAddress();
		return new Object[] { masterUri };
	}

	/**
	 * @param callerId
	 * @return
	 */
	@Override
	public Object[] shutdown(String callerId) {
		logger.entering(SlaveAPIImpl.class.getName(), "shutdown", callerId);
		owner.shutdownServer();
		return new Object[] { new Integer(1), "OK", new Integer(0) };
	}

	/**
	 * @param callerId
	 * @param msg
	 * @return
	 */
	@Override
	public Object[] shutdown(String callerId, String msg) {
		ROSNode.logger.entering(SlaveAPIImpl.class.getName(), "shutdown",
				new Object[] { callerId, msg });
		owner.shutdownServer();
		return new Object[] { new Integer(1), "OK", new Integer(0) };
	}

	/**
	 * @param callerId
	 * @return
	 */
	@Override
	public Object[] getPid(String callerId) {
		logger.entering(SlaveAPIImpl.class.getName(), "getPid");
		int pid = 10001;
		return new Object[] { new Integer(1), "pid", new Integer(pid) };
	}

	/**
	 * @param callerId
	 * @return
	 */
	@Override
	public Object[] getSubscriptions(String callerId) {
		logger.entering(SlaveAPIImpl.class.getName(), "getSubscriptions");
		// TODO Not implemented yet
		return null;
	}

	/**
	 * @param callerId
	 * @return
	 */
	@Override
	public Object[] getPublications(String callerId) {
		logger.entering(SlaveAPIImpl.class.getName(), "getPublications");
		// TODO Not implemented yet
		return null;
	}

	/**
	 * @param callerId
	 * @param parameterKey
	 * @param parameterValue
	 * @return
	 */
	@Override
	public Object[] paramUpdate(String callerId, String parameterKey,
			Object parameterValue) {
		logger.entering(SlaveAPIImpl.class.getName(), "paramUpdate", new Object[]{callerId, parameterKey, parameterValue});
		// TODO Not Implemented
		ROSParameter param = this.owner.getParameter(parameterKey);
		if(param != null) {
			param.setValue(parameterValue);
		}
		return new Object[]{new Integer(1), "param update", new Integer(0)};
	}

	
	/**
	 * @param callerId
	 * @param topic
	 * @param publishers
	 * @return
	 */
	@Override
	public Object[] publisherUpdate(String callerId, String topic,
			Object[] publishers) {
		logger.entering(SlaveAPIImpl.class.getName(), "publisherUpdate", new Object[]{callerId, topic, publishers});

		try {
			ROSTopic rosTopic = null;
			rosTopic = ROSTopicFactory.createROSTopic(topic);

			List<Object> currentPublisher = Arrays.asList(publishers);

			synchronized (owner.publisherMapMutex) {
				for (Object o : currentPublisher) {
					if (!owner.publisherMap.containsKey(o)) {
						ROSTopicPublisherRef publisherRef = new ROSTopicPublisherRef(
								new URL((String) o), rosTopic, owner);
						owner.publisherMap.put(new ROSUri((String) o),
								publisherRef);
					}
				}

				for (ROSUri publisherUri : owner.publisherMap.keySet()) {
					if (!currentPublisher.contains(publisherUri.getUri())) {
						ROSTopicPublisherRef publisherRef = owner.publisherMap
								.remove(publisherUri);
						publisherRef.cleanup();
						publisherRef = null;
					}
				}
			}
			return new Object[] { new Integer(1), "publisher update",
					new Integer(0) };
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.severe("ROSNode.publisherUpdate failed:\n"
					+ sw.getBuffer().toString());
		}

		return new Object[] { new Integer(0), "failed.", new Integer(0) };
	}

	/**
	 * Called by Topic subscriber.
	 * 
	 * @param callerId
	 * @param topic
	 * @param protocols
	 * @return
	 */
	@Override
	public Object[] requestTopic(String callerId, String topic,
			Object[] protocols) {
		ROSNode.logger.entering(this.getClass().getName(), "requestTopic", new Object[]{callerId, topic, protocols});
		try {
			ROSUri uri = ROSCoreRef.getInstance().getNodeUri((String) callerId);
			ROSTopic rosTopic = ROSTopicFactory.createROSTopic(topic);
			ROSTopicSubscriberRef subscriberRef = new ROSTopicSubscriberRef(
					callerId, new URL(uri.getUri()), rosTopic, owner);
			owner.subscriberMap.put(rosTopic, subscriberRef);
			Thread.yield();
			Object[] ret = new Object[] {
					new Integer(1),
					"request topic",
					new Object[] { "TCPROS", owner.getSlaveServerAddress(),
							new Integer(subscriberRef.getPort()) } };
			return ret;
		} catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			logger.severe("ROSNode.requestTopic failed:\n"
					+ sw.getBuffer().toString());
		}

		return new Object[] { new Integer(0), "failed.", new Integer(0) };
	}

}