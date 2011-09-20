/**
 * ROSTopicValue.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/16
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.node.topic;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.ysuga.ros.javros.tcpros.LittleEndianInputStream;
import net.ysuga.ros.javros.tcpros.LittleEndianOutputStream;

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
public class ROSTopicValue {

	private Map<String, Object> valueMap;

	// private ROSTopicTypeInfo typeInfo;

	private ROSTopic topic;

	private ROSTopicPublisherRef publisher;

	public void setTopic(ROSTopic topic) {
		this.topic = topic;
	}

	public ROSTopic getTopic() {
		return topic;
	}

	/**
	 * <div lang="ja"> �R���X�g���N�^ </div> <div lang="en"> Constructor </div>
	 * 
	 * @param rosTopicPublisherRef
	 * @throws IOException
	 */
	public ROSTopicValue(ROSTopic topic, byte[] value,
			ROSTopicPublisherRef rosTopicPublisherRef) throws IOException {
		valueMap = new HashMap<String, Object>();
		this.setTopic(topic);
		this.publisher = rosTopicPublisherRef;

		decode(topic.getTopicTypeInfo(), value);
	}

	public ROSTopicValue(ROSTopic topic, Object... objs)
			throws ROSTopicValueInvalidArgumentException {
		valueMap = new HashMap<String, Object>();
		this.setTopic(topic);
		this.publisher = null;

		if (!validateArgument(topic.getTopicTypeInfo(), objs)) {
			throw new ROSTopicValueInvalidArgumentException();
		}
	}

	private boolean validateArgument(ROSTopicTypeInfo typeInfo, Object[] objs) {
		try {
			for (int i = 0; i < typeInfo.typeList.size(); i++) {
				String type = (String) typeInfo.typeList.get(i);
				String name = (String) typeInfo.nameList.get(i);
				Object o = objs[i];
				if (type.equals("string")) {
					valueMap.put(name, (String) o);
				} else if (type.equals("bool")) {

				} else if (type.equals("int8")) {
					valueMap.put(name, (Byte) o);
				} else if (type.equals("uint8")) {

				} else if (type.equals("int16")) {
					valueMap.put(name, (Short) o);
				} else if (type.equals("uint16")) {

				} else if (type.equals("int32")) {
					valueMap.put(name, (Integer) o);
				} else if (type.equals("uint32")) {
					valueMap.put(name, (Long) o);
				} else if (type.equals("int64")) {
					valueMap.put(name, (Long) o);
				} else if (type.equals("uint64")) {

				} else if (type.equals("float32")) {
					valueMap.put(name, (Float) o);
				} else if (type.equals("float64")) {
					valueMap.put(name, (Double) o);
				} else if (type.equals("time")) {
					valueMap.put(name, (Integer) o);
				} else if (type.equals("duration")) {
					valueMap.put(name, (Integer) o);
				}
			}
		} catch (java.lang.ClassCastException e) {
			return false;
		}
		return true;
	}

	/**
	 * decode <div lang="ja">
	 * 
	 * @param typeInfo
	 * @param value
	 *            </div> <div lang="en">
	 * 
	 * @param typeInfo
	 * @param value
	 *            </div>
	 * @throws IOException
	 */
	private void decode(ROSTopicTypeInfo typeInfo, byte[] value)
			throws IOException {
		LittleEndianInputStream inputStream = new LittleEndianInputStream(value);
		for (int i = 0; i < typeInfo.typeList.size(); i++) {
			String type = (String) typeInfo.typeList.get(i);
			String name = (String) typeInfo.nameList.get(i);
			if (type.equals("string")) {
				int size = inputStream.readInt();
				String str = inputStream.readString(size);
				this.valueMap.put(name, str);
			} else if (type.equals("bool")) {

			} else if (type.equals("int8")) {
				Byte byteValue = inputStream.readByte();
				this.valueMap.put(name, byteValue);
			} else if (type.equals("uint8")) {

			} else if (type.equals("int16")) {
				Short shortValue = inputStream.readShort();
				this.valueMap.put(name, shortValue);
			} else if (type.equals("uint16")) {

			} else if (type.equals("int32")) {
				Integer intValue = inputStream.readInt();
				this.valueMap.put(name, intValue);
			} else if (type.equals("uint32")) {
				Long uintValue = inputStream.readUInt();
				this.valueMap.put(name, uintValue);
			} else if (type.equals("int64")) {
				Long longValue = inputStream.readLong();
				this.valueMap.put(name, longValue);
			} else if (type.equals("uint64")) {

			} else if (type.equals("float32")) {
				Float floatValue = inputStream.readFloat();
				this.valueMap.put(name, floatValue);
			} else if (type.equals("float64")) {
				Double doubleValue = inputStream.readDouble();
				this.valueMap.put(name, doubleValue);
			} else if (type.equals("time")) {
				Integer intValue = inputStream.readInt();
				this.valueMap.put(name, intValue);
			} else if (type.equals("duration")) {
				Integer intValue = inputStream.readInt();
				this.valueMap.put(name, intValue);
			}
		}
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (String valueName : this.valueMap.keySet()) {
			ROSTopicTypeInfo typeInfo = getTopic().getTopicTypeInfo();
			String typeName = (String) typeInfo.typeList.get(typeInfo.nameList
					.indexOf(valueName));
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

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		LittleEndianOutputStream outputStream = new LittleEndianOutputStream(
				byteStream);

		ROSTopicTypeInfo typeInfo = getTopic().getTopicTypeInfo();
		for (int i = 0; i < typeInfo.typeList.size(); i++) {
			String type = (String) typeInfo.typeList.get(i);
			String name = (String) typeInfo.nameList.get(i);
			Object o = valueMap.get(name);
			if (type.equals("string")) {
				outputStream.writeInt(((String) o).length());
				outputStream.writeString((String) o);
			} else if (type.equals("bool")) {

			} else if (type.equals("int8")) {
				outputStream.writeByte((Byte) o);
			} else if (type.equals("uint8")) {

			} else if (type.equals("int16")) {
				outputStream.writeShort((Short) o);
			} else if (type.equals("uint16")) {

			} else if (type.equals("int32")) {
				outputStream.writeInt((Integer) o);
			} else if (type.equals("uint32")) {
				outputStream.writeUInt((Long) o);
			} else if (type.equals("int64")) {
				outputStream.writeLong((Long) o);
			} else if (type.equals("uint64")) {

			} else if (type.equals("float32")) {
				outputStream.writeFloat((Float) o);
			} else if (type.equals("float64")) {
				outputStream.writeDouble((Double) o);
			} else if (type.equals("time")) {
				outputStream.writeInt((Integer) o);
			} else if (type.equals("duration")) {
				outputStream.writeInt((Integer) o);
			}
		}
		return byteStream.toByteArray();
	}
}
