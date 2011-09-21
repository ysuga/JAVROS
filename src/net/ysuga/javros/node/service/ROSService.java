/**
 * ROSService.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.service;

import java.io.IOException;
import java.net.UnknownHostException;

import net.ysuga.javros.ROSCore;
import net.ysuga.javros.core.rosref.RosRefException;
import net.ysuga.javros.node.XmlRpcRequestException;

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
public class ROSService {

	final private String name;
	final private String type;
	final private String md5sum;
	private ROSServiceTypeInfo serviceTypeInfo;
	
	public ROSServiceTypeInfo getServiceTypeInfo() {
		return serviceTypeInfo;
	}
	
	public void setServiceTypeInfo(ROSServiceTypeInfo info) {
		this.serviceTypeInfo = info;
	}
	/**
	 * <div lang="ja">
	 * �R���X�g���N�^
	 * @param name
	 * @param type
	 * @param md5sum
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * @param name
	 * @param type
	 * @param md5sum
	 * </div>
	 */
	public ROSService(String name, String type, String md5sum) {
		super();
		this.name = name;
		this.type = type;
		this.md5sum = md5sum;
	}
	
	/**
	 * @return name
	 */
	public final String getName() {
		return name;
	}
	
	/**
	 * @return type
	 * @throws RosRefException 
	 * @throws XmlRpcRequestException 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws ROSServiceNotFoundException 
	 */
	public final String getType() {
		return type;
	}
	
	/**
	 * @return md5sum
	 * @throws RosRefException 
	 * @throws XmlRpcRequestException 
	 * @throws IOException 
	 * @throws UnknownHostException 
	 * @throws ROSServiceNotFoundException 
	 */
	public final String getMd5sum() {
		return md5sum;
	}  
	

	@Override
	public boolean equals(Object o) {
		if(o instanceof ROSService) {
			return ((ROSService) o).getName().equals(this.getName());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
}
