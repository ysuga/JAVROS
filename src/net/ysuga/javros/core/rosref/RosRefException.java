/**
 * NoRemoteCommandServiceException.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.core.rosref;

/**
 * 
 * @author ysuga
 *
 */
public class RosRefException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4152240500261258192L;

	/**
	 * constructor
	 * @param msg
	 */
	public RosRefException(String msg) {
		super(msg);
	}
}
