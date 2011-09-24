import net.ysuga.javros.ROS;
import net.ysuga.javros.core.ROSCoreRef;
import net.ysuga.javros.node.ROSNode;
import net.ysuga.javros.node.ROSNodeException;
import net.ysuga.javros.node.topic.ROSTopic;
import net.ysuga.javros.node.topic.ROSTopicFactory;
import net.ysuga.javros.value.ROSValue;

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
public class TurtleSimTest extends ROSNode {

	public TurtleSimTest() throws ROSNodeException {
		super("/javrosNode");
		//String myAddress = "192.168.1.100";
		//String hostAddress = "192.168.1.101"
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

			String myAddress = "192.168.42.128";
			String hostAddress = "192.168.42.129";

			ROS.init(myAddress, hostAddress);
			
			new TurtleSimTest();
		} catch (Exception ex) {
			System.out.println("Exception: "  + ex);
		}
	}

	@Override
	public int onExecute() throws Exception {

		Thread.sleep(500);
		ROSValue topicValue = subscribe(topic);
		System.out.println("Subscribed=\n" + topicValue);
		return 0;
	}

	@Override
	public int onFinalized() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	ROSTopic topic;
	
	@Override
	public int onInitialized() throws Exception {

		topic = ROSTopicFactory.createROSTopic("/turtle1/pose", "turtlesim/Pose");
		registerSubscriber(topic);
		return 0;
	}

}
