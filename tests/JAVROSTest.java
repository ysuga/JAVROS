import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import org.apache.xmlrpc.XmlRpcException;

import net.ysuga.javros.ROSCore;
import net.ysuga.javros.remote.NoRemoteCommandServiceException;
import net.ysuga.ros.javros.api.ROS;
import net.ysuga.ros.javros.api.XmlRpcRequestException;
import net.ysuga.ros.javros.tcpros.ROSServiceNotFoundException;
import net.ysuga.ros.javros.tcpros.ROSTopic;
import net.ysuga.ros.javros.tcpros.ROSTopicFactory;
import net.ysuga.ros.javros.tcpros.ROSTopicSubscriber;

/**
 * JAVROSTest.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/05
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
public class JAVROSTest {
	public static Logger logger = Logger.getLogger(JAVROSTest.class.getName());
	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 * @throws XmlRpcRequestException 
	 * @throws ROSServiceNotFoundException 
	 * @throws NoRemoteCommandServiceException 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws XmlRpcException 
	 */
	public JAVROSTest() throws XmlRpcRequestException, UnknownHostException, IOException, NoRemoteCommandServiceException, ROSServiceNotFoundException, XmlRpcException {
		String myAddress = "192.168.42.130";
		//ROS.init(myAddress);
		ROSCore.init("192.168.42.130");
		
		ROSTopic topic = ROSTopicFactory.createROSTopic("/chatter");
		ROSTopicSubscriber subscriber = new ROSTopicSubscriber(myAddress, 40000, "/javrosSubscriber",  topic);
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
		// TODO 自動生成されたメソッド・スタブ
		try {
			new JAVROSTest();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} 	
	}

}
