/**
 * ReturnValue.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/02
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.util;

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
public class ReturnValue<RetType> {

	final private int code;
	
	final public int getCode() {
		return code;
	}
	
	final private String statusMessage;
	
	final public String getStatusMessage() {
		return statusMessage;
	}
	
	final private RetType value;
	
	final public RetType getValue() {
		return value;
	}
	
	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	public ReturnValue(int code, String statusMessage, RetType value) {
		this.code = code;
		this.statusMessage = statusMessage;
		this.value = value;
	}

}
