import javax.swing.JOptionPane;

import net.ysuga.javros.ROS;
import net.ysuga.javros.core.ROSCoreRef;
import net.ysuga.javros.core.rosref.RosRefException;
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
public class ROSNodeTest {

	
	class TestNode extends ROSNode {
		ROSTopic topic;
		public TestNode() throws ROSNodeException {
			super("/javrosNode");
		}

		@Override
		public int onExecute() throws Exception {
			Thread.sleep(500);
			ROSValue value = subscribe(topic);
			System.out.println("value=\n"+value);
			return 0;
		}

		@Override
		public int onFinalized() {
			return 0;
		}

		@Override
		public int onInitialized() throws Exception {
			topic = ROSTopicFactory.createROSTopic("/tutorial", "tutorial/Input");
			registerSubscriber(topic);
			return 0;
		}
	}
	/**
	 * <div lang="ja">
	 * �R���X�g���N�^
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	public ROSNodeTest() {
		String myAddress = "192.168.1.2";
		String hostAddress = "192.168.1.109";

		//String myAddress = "192.168.1.100";
		//String hostAddress = "192.168.1.101";

		try {
			ROS.init(myAddress, hostAddress);

			TestNode node = new TestNode();
			ROS.launchNode(node);
			
			JOptionPane.showMessageDialog(null, "Press OK to exit");
			
			System.exit(0);
		} catch (Exception e) {
			// TODO �����������ꂽ catch �u���b�N
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
