import javax.swing.JOptionPane;

import net.ysuga.javros.ROS;
import net.ysuga.javros.node.ROSNode;
import net.ysuga.javros.node.ROSNodeException;
import net.ysuga.javros.node.parameter.ROSParameter;
import net.ysuga.javros.node.parameter.ROSParameterFactory;
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
public class TurtleSimTest2 extends ROSNode {


	private ROSTopic poseTopic;
	
	private ROSTopic velocityTopic;
	
	private ROSParameter velocityParameter;
	
	
	public TurtleSimTest2() throws ROSNodeException {
		super("/javrosNode");
	}

	
	public static void main(String[] args) {
		String myAddress = "127.0.0.1";
		String hostAddress = "127.0.0.1";

		try {
			ROS.init(myAddress, hostAddress);
			ROSNode node = new TurtleSimTest2();
			ROS.launchNode(node);
			
			JOptionPane.showMessageDialog(null, "Press OK to exit");
			
			ROS.exit();
		} catch (Exception ex) {
			System.out.println("Exception: "  + ex);
		}
	}

	@Override
	public int onExecute() throws Exception {
		
		// You can access topic value w/ ROSNode.subscribe(ROSTopic) method.
		ROSValue topicValue = subscribe(poseTopic);
		System.out.println("----------------------------Subscribed---------------------------------");
		// ROSValue is a map. you can access each member by name of value.
		System.out.println("x     = " + topicValue.get("x"));
		System.out.println("y     = " + topicValue.get("y"));
		System.out.println("theta = " + topicValue.get("theta"));
		// You can also simply call the toString method to confirm what the ROSValue contains.
		// This also inform the type information of the value.
		// System.out.println(topicValue.toString());

		// Creating ROSValue to call publish method. ROSValue constructor can allow variable number argument call.
		// This execute the auto-boxing function of Java. so the type cast should be strictly done by you.
		ROSValue command = new ROSValue(velocityTopic.getTypeInfo(), (float)1.0f, (float)1.0f);
		// You can publish the topic value with ROSNode.publish(ROSTopic, ROSValue) method.
		publish(velocityTopic, command);

		// onExecute is called as rapidly as possible.
		// Caution: If sleep is not placed here, the CPU might be used with 100%!!!
		Thread.sleep(500);
		return 0;
	}

	@Override
	public int onFinalized() throws Exception {
		// TODO Auto-generated method stub
		return 0;
	}

	
	@Override
	public int onInitialized() throws Exception {
		// Creating Topics.
		// Topic's Type information is collected through the rosref service. 
		poseTopic = ROSTopicFactory.createROSTopic("/turtle1/pose", "turtlesim/Pose");
		registerSubscriber(poseTopic);
		
		velocityTopic = ROSTopicFactory.createROSTopic("/turtle1/command_velocity", "turtlesim/Velocity");
		registerPublisher(velocityTopic);
		
		//Creating Parameters.
		
		// Currently Parameter allows String only.
		velocityParameter = ROSParameterFactory.createROSParameter("/javros/velocity/", "1.0f");
		registerParameter(velocityParameter);

		return 0;
	}

}
