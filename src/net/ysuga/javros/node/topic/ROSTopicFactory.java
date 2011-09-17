/**
 * ROSServiceFactory.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.topic;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;

import net.ysuga.javros.ROSCore;
import net.ysuga.javros.node.XmlRpcRequestException;
import net.ysuga.javros.node.service.ROSServiceNotFoundException;
import net.ysuga.javros.remote.RemoteCommandServiceException;

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
public class ROSTopicFactory {

	static private HashMap<String, ROSTopic> createdROSTopicMap;
	static {
		createdROSTopicMap = new HashMap<String, ROSTopic>();
	}
	
	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	private ROSTopicFactory() {
	}

	static public ROSTopic createROSTopic(String topicName) throws RemoteCommandServiceException  {
		if(!createdROSTopicMap.containsKey(topicName)) {
			String topicType = ROSCore.getInstance().getTopicType(topicName);
			ROSTopic topic = new ROSTopic(topicName, topicType, ROSCore.getInstance().getTopicMd5Sum(topicType));
			try {
				topic.setTopicTypeInfo(ROSCore.getInstance().getTopicTypeInfo(topic.getType()));
			} catch (ROSTopicInvalidTypeInfoException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				throw new RemoteCommandServiceException("invalid topic type:" + topicName);
			}
			createdROSTopicMap.put(topicName, topic);
		}
		return createdROSTopicMap.get(topicName);
	}
	
	static public ROSTopic createROSTopic(String topicName, String topicType) throws RemoteCommandServiceException {
		if(!createdROSTopicMap.containsKey(topicName)) {
			ROSTopic topic = new ROSTopic(topicName, topicType, ROSCore.getInstance().getTopicMd5Sum(topicType));
			try {
				topic.setTopicTypeInfo(ROSCore.getInstance().getTopicTypeInfo(topic.getType()));
			} catch (ROSTopicInvalidTypeInfoException e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
				throw new RemoteCommandServiceException("invalid topic type:" + topicName);
			}
			createdROSTopicMap.put(topicName, topic);
		}
		return createdROSTopicMap.get(topicName);
	}
}
