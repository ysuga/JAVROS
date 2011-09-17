/**
 * ROSTopicValue.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/16
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.topic;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.ysuga.ros.javros.tcpros.LittleEndianInputStream;

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
public class ROSTopicValue {

	private Map<String, Object> valueMap;
	
	private ROSTopicTypeInfo typeInfo;
	
	private ROSTopicPublisherRef publisher;
	
	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 * @param rosTopicPublisherRef 
	 * @throws IOException 
	 */
	public ROSTopicValue(ROSTopicTypeInfo typeInfo, byte[] value, ROSTopicPublisherRef rosTopicPublisherRef) throws IOException {
		valueMap = new HashMap<String, Object>();
		this.typeInfo = typeInfo;
		this.publisher = rosTopicPublisherRef;
		
		decode(typeInfo, value);
	}

	public ROSTopicValue(ROSTopicTypeInfo typeInfo, Object... objs) {
		valueMap = new HashMap<String, Object>();
		this.typeInfo = typeInfo;		
		this.publisher = null;
		
		validateArgument(objs);
	}
	
	private boolean validateArgument(Object[] objs) {
		
		return true;
	}
	/**
	 * decode
	 * <div lang="ja">
	 * 
	 * @param typeInfo
	 * @param value
	 * </div>
	 * <div lang="en">
	 *
	 * @param typeInfo
	 * @param value
	 * </div>
	 * @throws IOException 
	 */
	private void decode(ROSTopicTypeInfo typeInfo, byte[] value) throws IOException {
		LittleEndianInputStream inputStream = new LittleEndianInputStream(value);
		for(int i = 0;i < typeInfo.typeList.size();i++) {
			String type = (String)typeInfo.typeList.get(i);
			String name = (String)typeInfo.nameList.get(i);
			if(type.equals("string")) {
				int size = inputStream.readInt();
				String str = inputStream.readString(size);
				this.valueMap.put(name, str);
			} else if(type.equals("bool")) {
				
			} else if(type.equals("int8")) {
				Byte byteValue = inputStream.readByte();
				this.valueMap.put(name, byteValue);
			} else if(type.equals("uint8")) {
				
			} else if(type.equals("int16")) {
				Short shortValue = inputStream.readShort();
				this.valueMap.put(name, shortValue);
			} else if(type.equals("uint16")) {	
			
			} else if(type.equals("int32")) {
				Integer intValue = inputStream.readInt();
				this.valueMap.put(name, intValue);
			} else if(type.equals("uint32")) {
				Long uintValue = inputStream.readUInt();
				this.valueMap.put(name, uintValue);
			} else if(type.equals("int64")) {
				Long longValue = inputStream.readLong();
				this.valueMap.put(name, longValue);
			} else if(type.equals("uint64")) {
			
			} else if(type.equals("float32")) {
				Float floatValue = inputStream.readFloat();
				this.valueMap.put(name, floatValue);
			} else if(type.equals("float64")) {
				Double doubleValue = inputStream.readDouble();
				this.valueMap.put(name, doubleValue);
			} else if(type.equals("time")) {
				Integer intValue = inputStream.readInt();
				this.valueMap.put(name, intValue);	
			} else if(type.equals("duration")) {
				Integer intValue = inputStream.readInt();
				this.valueMap.put(name, intValue);	
			}
		}
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for(String valueName : this.valueMap.keySet()) {
			String typeName = (String)typeInfo.typeList.get(typeInfo.nameList.indexOf(valueName));
			Object value = valueMap.get(valueName);
			stringBuilder.append(typeName);
			stringBuilder.append(" ");
			stringBuilder.append(valueName);
			stringBuilder.append(" = ");
			stringBuilder.append(value.toString());
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}
}
