/**
 * TCPROSServiceHeader.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.service;

import net.ysuga.javros.transport.TCPROSHeader;

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
	 * �R���X�g���N�^
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
