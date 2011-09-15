/**
 * LEByteArrayInputStream.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.tcpros;

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
	 * コンストラクタ
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
		// TODO 自動生成されたコンストラクター・スタブ
	}
	
	public LittleEndianInputStream(byte[] byteArray) {
		super();
		this.inputStream = new ByteArrayInputStream(byteArray);
	}

	
	final public int readInt() throws IOException {
		byte[] bb = new byte[4];
		inputStream.read(bb, 0, 4);
		return ((int)bb[0]) & 0xFF | ((((int)bb[1]) << 8) & 0xFF) | ((((int)bb[2]) << 16) & 0xFF) | ((((int)bb[3]) << 24) & 0xFF);
	}
	
	public long readLong() throws IOException {
		byte[] bb = new byte[8];
		inputStream.read(bb, 0, 8);
		return ((long)bb[0]) & 0xFF | ((((long)bb[1]) << 8) & 0xFF) | ((((long)bb[2]) << 16) & 0xFF) | ((((long)bb[3]) << 24) & 0xFF) |
				(((long)bb[4]) << 32) & 0xFF | ((((long)bb[5]) << 40) & 0xFF) | ((((long)bb[6]) << 48) & 0xFF) | ((((long)bb[7]) << 56) & 0xFF);
	}
	
	final public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
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

}
