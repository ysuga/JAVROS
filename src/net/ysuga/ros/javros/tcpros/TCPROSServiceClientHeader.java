/**
 * TCPROSServiceHeader.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.tcpros;

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
public class TCPROSServiceClientHeader extends TCPROSHeader {

	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	public TCPROSServiceClientHeader(String callerid, String service, String md5sum) {
		super();
		put("callerid", callerid);
		put("service", service);
		put("md5sum", md5sum);
	}

	public TCPROSServiceClientHeader() {
		super();
	}
}
