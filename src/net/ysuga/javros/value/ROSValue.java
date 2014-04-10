package net.ysuga.javros.value;
/**
 * ROSTopicValue.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/16
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.ysuga.javros.node.topic.ROSTopicPublisherRef;
import net.ysuga.javros.transport.LittleEndianInputStream;
import net.ysuga.javros.transport.LittleEndianOutputStream;

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
public class ROSValue {

	//private Map<String, Object> valueMap;
	private ArrayList<Object> valueList;
	private ROSValueTypeInfo typeInfo;

	public Object get(String name) {
		for (int i = 0;i < typeInfo.nameList.size();i++) {
			if( typeInfo.nameList.get(i).equals(name)) {
				return valueList.get(i);
			}
		}
		return null;
	}
	
	public Object set(String name, Object value) {
		for (int i = 0;i < typeInfo.nameList.size();i++) {
			if( typeInfo.nameList.get(i).equals(name)) {
				return valueList.set(i, value);
			}
		}
		return null;
	}

	/**
	 * 
	 * @param typeInfo
	 * @param value
	 * @param rosTopicPublisherRef
	 * @throws IOException
	 */
	public ROSValue(ROSValueTypeInfo typeInfo, byte[] value,	ROSTopicPublisherRef rosTopicPublisherRef) throws IOException {
		//valueMap = new HashMap<String, Object>();
		valueList = new ArrayList<Object>();
	//	this.setTopic(topic);
	//	this.publisher = rosTopicPublisherRef;
		this.typeInfo = typeInfo;
		decode(typeInfo, value);
	}

	public ROSValue(ROSValueTypeInfo typeInfo, byte[] value) throws IOException {
		//valueMap = new HashMap<String, Object>();
		valueList = new ArrayList<Object>();
		this.typeInfo = typeInfo;
		decode(typeInfo, value);
	}
	
	public ROSValue(ROSValueTypeInfo typeInfo, Object... objs)
			throws ROSValueInvalidArgumentException {
		//valueMap = new HashMap<String, Object>();
		valueList = new ArrayList<Object>();
		this.typeInfo = typeInfo;
		if (!validateArgument(typeInfo, objs)) {
			throw new ROSValueInvalidArgumentException();
		}
	}

	private void validateArgument(String type, String name, Object o) {

		if (type.equals("string")) {
			//valueMap.put(name, (String) o);
			valueList.add((String)o);
		} else if (type.equals("bool")) {
			//valueMap.put(name, (Byte)o);
			valueList.add((Byte)o);
		} else if (type.equals("int8")) {
			//valueMap.put(name, (Byte) o);
			valueList.add((Byte)o);
		} else if (type.equals("uint8")) {
			//valueMap.put(name, (Byte) o);
			valueList.add((Byte)o);
		} else if (type.equals("int16")) {
			//valueMap.put(name, (Short) o);
			valueList.add((Short)o);
		} else if (type.equals("uint16")) {
			//valueMap.put(name, (Short) o);
			valueList.add((Short)o);
		} else if (type.equals("int32")) {
			//valueMap.put(name, (Integer) o);
			valueList.add((Integer)o);
		} else if (type.equals("uint32")) {
			//valueMap.put(name, (Long) o);
			valueList.add((Long)o);
		} else if (type.equals("int64")) {
			//valueMap.put(name, (Long) o);//
			valueList.add((Long)o);
		} else if (type.equals("uint64")) {
			//valueMap.put(name, (Long) o);
			valueList.add((Long)o);
		} else if (type.equals("float32")) {
			//valueMap.put(name, (Float) o);
			valueList.add((Float)o);
		} else if (type.equals("float64")) {
			//valueMap.put(name, (Double) o);
			valueList.add((Double)o);
		} else if (type.equals("time")) {
			//valueMap.put(name, (Integer) o);
			valueList.add((Integer)o);
		} else if (type.equals("duration")) {
			//valueMap.put(name, (Integer) o);
			valueList.add((Integer)o);
		} else if (type.endsWith("[]")) {
			if (!(o instanceof Collection) ){
				System.err.println("TypeError: " + name + " must be collection type, but constructor does not receive collection.");
				throw new java.lang.ClassCastException();
			}
			
			//valueMap.put(name, o);
			valueList.add(o);
		}
	}
	
	private boolean validateArgument(ROSValueTypeInfo typeInfo, Object[] objs) {
		try {
			for (int i = 0; i < typeInfo.typeList.size(); i++) {
				String type = (String) typeInfo.typeList.get(i);
				String name = (String) typeInfo.nameList.get(i);
				Object o = objs[i];
				validateArgument(type, name, o);
			}
		} catch (java.lang.ClassCastException e) {
			System.out.println(e);
			return false;
		}
		return true;
	}

	//this.valueMap.put(name, str);
	private Object decode_onedata(LittleEndianInputStream inputStream, String type, String name) throws IOException {
		if (type.equals("string")) {
			int size = inputStream.readInt();
			return inputStream.readString(size);
		} else if (type.equals("bool")) {
			Byte byteValue = inputStream.readByte();
			return new Boolean(byteValue.intValue() == 0);
		} else if (type.equals("int8")) {
			return inputStream.readByte();
		} else if (type.equals("uint8")) {
			return inputStream.readByte();	
		} else if (type.equals("int16")) {
			return inputStream.readShort();
		} else if (type.equals("uint16")) {
			// This might be failed because uint16
			return inputStream.readShort();
		} else if (type.equals("int32")) {
			return inputStream.readInt();
		} else if (type.equals("uint32")) {
			return inputStream.readUInt();
		} else if (type.equals("int64")) {
			return inputStream.readLong();
		} else if (type.equals("uint64")) {
			// This might be failed because uint64 is not represented in Java
			return inputStream.readLong();
		} else if (type.equals("float32")) {
			return inputStream.readFloat();
		} else if (type.equals("float64")) {
			return inputStream.readDouble();
		} else if (type.equals("time")) {
			return inputStream.readInt();
		} else if (type.equals("duration")) {
			return inputStream.readInt();
		} 
		
		if (type.endsWith("[]")) {
			//int hoge = inputStream.readInt();
			int size = inputStream.readInt();
			String typename = type.substring(0, type.length()-1-1);
			ArrayList<Object> al = new ArrayList<Object>();
			//System.out.println("c=" + inputStream.available()); 
			for (int j = 0;j < size;j++) {
				al.add(this.decode_onedata(inputStream, typename, typename));
			}
			return al;
		}
		return null;
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
	private void decode(ROSValueTypeInfo typeInfo, byte[] value)
			throws IOException {
		LittleEndianInputStream inputStream = new LittleEndianInputStream(value);
		for (int i = 0; i < typeInfo.typeList.size(); i++) {
			String type = (String) typeInfo.typeList.get(i);
			String name = (String) typeInfo.nameList.get(i);	
			Object o = this.decode_onedata(inputStream, type, name);
			if (o != null) {
				valueList.add(o);
				//this.valueMap.put(name, o);
				// Do not match premitive data type

				
			}
		}
	}

	public String toString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (String valueName : this.typeInfo.nameList) {
			int i = typeInfo.nameList
			.indexOf(valueName);
			String typeName = (String) typeInfo.typeList.get(i);
			Object value = valueList.get(i);
			stringBuilder.append(typeName);
			stringBuilder.append(" ");
			stringBuilder.append(valueName);
			stringBuilder.append(" = ");
			stringBuilder.append(value.toString());
			stringBuilder.append("\n");
		}
		return stringBuilder.toString();
	}
	
	public void serialize(LittleEndianOutputStream outputStream, String type, Object o) throws IOException {
		
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
			outputStream.writeLong((Long) o);
		} else if (type.equals("float32")) {
			outputStream.writeFloat((Float) o);
		} else if (type.equals("float64")) {
			outputStream.writeDouble((Double) o);
		} else if (type.equals("time")) {
			outputStream.writeInt((Integer) o);
		} else if (type.equals("duration")) {
			outputStream.writeInt((Integer) o);
		} else if (type.endsWith("[]")) {
			Collection c = (Collection)o;
			for (Object o_ : c) {
				serialize(outputStream, type.substring(0, type.length()-1-1), o_);
			}
		}
	}

	public byte[] serialize() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		LittleEndianOutputStream outputStream = new LittleEndianOutputStream(
				byteStream);

		for (int i = 0; i < typeInfo.typeList.size(); i++) {
			String type = (String) typeInfo.typeList.get(i);
			//String name = (String) typeInfo.nameList.get(i);
			Object o = valueList.get(i);
			serialize(outputStream, type, o);
		}
		return byteStream.toByteArray();
	}
}
