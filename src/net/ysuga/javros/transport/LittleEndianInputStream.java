/**
 * LEByteArrayInputStream.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.transport;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

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
public class LittleEndianInputStream extends InputStream {

	private InputStream inputStream;
	/**
	 * <div lang="ja">
	 * �R���X�g���N�^
	 * @param buf
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * @param buf
	 * </div>
	 */
	public LittleEndianInputStream(InputStream inputStream) {
		super();
		this.inputStream = inputStream;
		// TODO �����������ꂽ�R���X�g���N�^�[�E�X�^�u
	}
	
	public LittleEndianInputStream(byte[] byteArray) {
		super();
		this.inputStream = new ByteArrayInputStream(byteArray);
	}
	
	

	final public byte readByte() throws IOException {
		byte[] b = new byte[1];
		inputStream.read(b, 0, 1);
		return b[0];
	}

	final public short readUByte() throws IOException {
		byte[] b = new byte[1];
		inputStream.read(b, 0, 1);
		return (short)(((int)b[0]) & 0xFF);
	}
	
	final public short readShort() throws IOException {
		byte[] b = new byte[2];
		inputStream.read(b, 0, 2);
		return (short)(((short)b[0] & 0xFF) | ((((short)b[1]& 0xFF) << 8) ));
	}
	
	final public int readUShort() throws IOException {
		byte[] b = new byte[2];
		inputStream.read(b, 0, 2);
		return (((int)b[0] & 0xFF) | ((((int)b[1] & 0xFF) << 8)));
	}
		
	final public int readInt() throws IOException {
		byte[] bb = new byte[4];
		inputStream.read(bb, 0, 4);
		return (((int)bb[0] & 0xFF)) | ((((int)bb[1]& 0xFF) << 8) ) | ((((int)bb[2] & 0xFF) << 16)) | ((((int)bb[3] & 0xFF) << 24));
	}
	
	final public long readUInt() throws IOException {
		byte[] bb = new byte[8];
		inputStream.read(bb, 0, 8);
		return ((long)bb[0] & 0xFF) | ((((long)bb[1]& 0xFF) << 8) ) | ((((long)bb[2]& 0xFF) << 16) ) | ((((long)bb[3] & 0xFF) << 24));
	}
	
	public long readLong() throws IOException {
		byte[] bb = new byte[8];
		inputStream.read(bb, 0, 8);
		return ((long)bb[0]) & 0xFF | ((((long)bb[1] & 0xFF) << 8)) | ((((long)bb[2] & 0xFF) << 16)) | ((((long)bb[3] & 0xFF) << 24)) |
				(((long)bb[4] & 0xFF) << 32) | ((((long)bb[5] & 0xFF) << 40)) | ((((long)bb[6] & 0xFF) << 48)) | ((((long)bb[7] & 0xFF) << 56));
	}
	
	final public float readFloat() throws IOException {
		//byte[] bb = new byte[4];
		//inputStream.read(bb, 0, 4);
		//int val = (((int)bb[3]) & 0xFF) | ((((int)bb[2]) << 8) & 0xFF) | ((((int)bb[1]) << 16) & 0xFF) | ((((int)bb[0]) << 24) & 0xFF);
		//return Float.intBitsToFloat(val);
		int v = readInt();
		return Float.intBitsToFloat(v);
	}
	
	final public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}
	
	final public String readString(int size) throws IOException {
		byte[] bb = new byte[size];
		inputStream.read(bb, 0, size);
		return new String(bb);
	}

	/**
	 * <div lang="ja">
	 * @return
	 * @throws IOException
	 * </div>
	 * <div lang="en">
	 * @return
	 * @throws IOException
	 * </div>
	 */
	@Override
	public int read() throws IOException {
		return inputStream.read();
	}

	@Override
	public int available() throws IOException {
		return inputStream.available();
	}
}
