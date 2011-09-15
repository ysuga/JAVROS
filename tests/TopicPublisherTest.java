import net.ysuga.javros.ROSCore;
import net.ysuga.ros.javros.api.ROS;
import net.ysuga.ros.javros.tcpros.ROSTopic;
import net.ysuga.ros.javros.tcpros.ROSTopicFactory;
import net.ysuga.ros.javros.tcpros.ROSTopicPublisher;
import net.ysuga.ros.javros.tcpros.ROSTopicSubscriber;

/**
 * TopicSubscriberTest.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */

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
public class TopicPublisherTest {

	/**
	 * main
	 * <div lang="ja">
	 * 
	 * @param args
	 * </div>
	 * <div lang="en">
	 *
	 * @param args
	 * </div>
	 */
	public static void main(String[] args) {
		//String myAddress = "192.168.42.130";
		//String hostAddress = "192.168.42.133";

		String myAddress = "192.168.1.100";
		String hostAddress = "192.168.1.101";
		
		
		try {
			ROS.init(myAddress);
			ROSCore.init(hostAddress);
			
			ROSTopic topic = ROSTopicFactory.createROSTopic("/rosout");
			ROSTopicPublisher subscriber = new ROSTopicPublisher(myAddress, 40000, "/javros_publisher",  topic);
			
		} catch (Exception e) {
			// TODO é©ìÆê∂ê¨Ç≥ÇÍÇΩ catch ÉuÉçÉbÉN
			e.printStackTrace();
		}

	}

}
