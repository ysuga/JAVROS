import java.util.List;

import net.ysuga.javros.ROSCore;
import net.ysuga.ros.javros.tcpros.ROSServiceTypeInfo;
import net.ysuga.ros.javros.tcpros.ROSTopic;
import net.ysuga.ros.javros.tcpros.ROSTopicFactory;

/**
 * RefrectionServiceTest.java
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
public class RefrectionServiceTest {

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
			ROSCore core = ROSCore.init("192.168.42.139");
			System.out.println ("Type = " + core.getServiceType("/refrection"));
			ROSServiceTypeInfo info = core.getServiceTypeInfo("refrection_service/Refrection");
			System.out.println(info.returnParam);
			System.out.println(info.argumentParam);
			
			List<String> list = core.getTopicPublisherNameList(ROSTopicFactory.createROSTopic("/rosout"));
			System.out.println("/rosout:" + list);
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
