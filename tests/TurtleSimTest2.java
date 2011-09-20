import net.ysuga.javros.ROSCore;
import net.ysuga.javros.node.ROSNode;
import net.ysuga.javros.node.topic.ROSTopic;
import net.ysuga.javros.node.topic.ROSTopicFactory;
import net.ysuga.javros.node.topic.ROSTopicValue;

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
public class TurtleSimTest {

	
	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	public TurtleSimTest() {
		String myAddress = "192.168.42.128";
		String hostAddress = "192.168.42.129";

		//String myAddress = "192.168.1.100";
		//String hostAddress = "192.168.1.101";

		try {
			ROSCore.init(hostAddress);

			ROSNode node = new ROSNode(myAddress, 40000, "/javrosNode");

			ROSTopic topic = ROSTopicFactory.createROSTopic("/turtle1/pose", "turtlesim/Pose");
			ROSCore.getInstance().registerSubscriber(topic, node);

			for(int i = 0;i < 100;i++) {
				Thread.sleep(500);
				ROSTopicValue topicValue = node.subscribe(topic);
				System.out.println("Subscribed=\n" + topicValue);
			}
			
			ROSCore.getInstance().unregisterSubscriber(topic, node);
			
			node.unregisterAll();
			System.exit(0);
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
			new TurtleSimTest();
		} catch (Exception ex) {
			System.out.println("Exception: "  + ex);
		}
	}

}
