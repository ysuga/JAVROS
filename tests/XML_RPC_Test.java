import net.ysuga.ros.javros.api.MasterAPIHelper;
import net.ysuga.ros.javros.api.MasterAPIRef;
import net.ysuga.ros.javros.api.ReturnValue;

/**
 * XML_RPC_Test.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/02
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */



/**
 * <div lang="ja">
 * 
 * </div> <div lang="en">
 * 
 * </div>
 * 
 * @author ysuga
 * 
 */
public class XML_RPC_Test {

	/**
	 * main <div lang="ja">
	 * 
	 * @param args
	 *            </div> <div lang="en">
	 * 
	 * @param args
	 *            </div>
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		try {
			MasterAPIHelper obj = new MasterAPIHelper(new MasterAPIRef("http://192.168.42.139:11311"));
			
			ReturnValue<String> ret1 = obj.lookupService("/script", "/remote_command");

			System.out.println("Ret = " + ret1.getValue());
			
			//ReturnValue<String> ret2 = obj.test();
			//System.out.println("Ret = " + ret2.getValue());
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}
}
