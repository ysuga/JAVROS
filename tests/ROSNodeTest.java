import net.ysuga.javros.ROSCore;
import net.ysuga.ros.javros.api.ROS;
import net.ysuga.ros.javros.tcpros.ROSNode;
import net.ysuga.ros.javros.tcpros.ROSTopic;
import net.ysuga.ros.javros.tcpros.ROSTopicFactory;
import net.ysuga.ros.javros.tcpros.ROSTopicPublisher;

/**
 * ROSNodeTest.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/12
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
public class ROSNodeTest {

	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	public ROSNodeTest() {
		String myAddress = "192.168.42.130";
		String hostAddress = "192.168.42.133";

		//String myAddress = "192.168.1.100";
		//String hostAddress = "192.168.1.101";

		try {
			ROS.init(myAddress);
			ROSCore.init(hostAddress);

			ROSNode node = new ROSNode(myAddress, 40000, "/javrosNode");

			ROSTopic topic = ROSTopicFactory.createROSTopic("/chatter", "std_msgs/String");
			ROSCore.getInstance().registerSubscriber(topic, node);

		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

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
		try {
			new ROSNodeTest();
		} catch (Exception ex) {
			System.out.println("Exception: "  + ex);
		}
	}

}
