import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import net.ysuga.javros.ROSCore;
import net.ysuga.javros.remote.NoRemoteCommandServiceException;
import net.ysuga.ros.javros.api.XmlRpcRequestException;
import net.ysuga.ros.javros.tcpros.ROSService;
import net.ysuga.ros.javros.tcpros.ROSServiceFactory;
import net.ysuga.ros.javros.tcpros.ROSServiceNotFoundException;
import net.ysuga.ros.javros.tcpros.TCPROSServiceClient;

/**
 * ServiceClientTest.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
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
public class ServiceClientTest {

	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws XmlRpcRequestException 
	 * @throws NoRemoteCommandServiceException 
	 * @throws ROSServiceNotFoundException 
	 */
	public ServiceClientTest() throws UnknownHostException, IOException, XmlRpcRequestException, NoRemoteCommandServiceException, ROSServiceNotFoundException {
		String hostAddress = "192.168.42.133";
		ROSCore.init(hostAddress);
		
		ROSService service = ROSServiceFactory.createROSService("/add_two_ints");
		TCPROSServiceClient client = new TCPROSServiceClient("/javrosCore", service);
		ArrayList<Object> args = new ArrayList<Object>();
		args.add(new Long("20"));
		args.add(new Long("30"));
		List<Object> ret = client.execute(args);
		System.out.println("RETURN=" + ret.get(0));
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
	 * @throws XmlRpcRequestException 
	 */
	public static void main(String[] args) {
		// TODO 自動生成されたメソッド・スタブ
		try {
			new ServiceClientTest();
		} catch (UnknownHostException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (XmlRpcRequestException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (NoRemoteCommandServiceException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (ROSServiceNotFoundException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

}
