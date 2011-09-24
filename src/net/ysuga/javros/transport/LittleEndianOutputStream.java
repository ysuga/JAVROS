/**
 * LEByteArrayOutputStream.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.transport;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

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
public class LittleEndianOutputStream extends OutputStream {

	OutputStream outputStream;
	/**
	 * <div lang="ja">
	 * �R���X�g���N�^
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * </div>
	 */
	public LittleEndianOutputStream() {
		outputStream = new ByteArrayOutputStream();
	}
	
	public LittleEndianOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}

	public void writeInt(int value) throws IOException {
		byte[] bv = {(byte)value, (byte)(value >> 8), (byte)(value >> 16), (byte)(value >> 24)};
		outputStream.write(bv);
	}
	
	public void writeLong(long value) throws IOException {
		byte[] bv = {(byte)value, (byte)(value >> 8), (byte)(value >> 16), (byte)(value >> 24),
				(byte)(value >> 32), (byte)(value >> 40), (byte)(value >> 48), (byte)(value >> 56)};
		outputStream.write(bv);
	}
	
	
	public void writeString(String arg) throws IOException {
		outputStream.write(arg.getBytes());
	}

	/**
	 * <div lang="ja">
	 * @param b
	 * @throws IOException
	 * </div>
	 * <div lang="en">
	 * @param b
	 * @throws IOException
	 * </div>
	 */
	@Override
	public void write(int b) throws IOException {
		outputStream.write(b);
	}

	public void writeShort(Short o) throws IOException {
		short value = o.shortValue();
		byte[] bv = {(byte)value, (byte)(value >> 8)};
		outputStream.write(bv);
	}

	public void writeByte(Byte o) throws IOException {
		write(o);
	}

	public void writeUInt(Long o) throws IOException {
		int bit = (int)(o.longValue() & 0xFFFFFFFF);
		writeInt(bit);
	}

	public void writeFloat(Float o) throws IOException {
		writeInt(Float.floatToRawIntBits(o));
	}

	public void writeDouble(Double o) throws IOException {
		writeLong(Double.doubleToRawLongBits(o));
	}
}