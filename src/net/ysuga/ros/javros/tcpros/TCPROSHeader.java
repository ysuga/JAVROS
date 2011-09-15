/**
 * TCPROSHeaderBase.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/06
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.ros.javros.tcpros;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.StringTokenizer;

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
public class TCPROSHeader extends HashMap<String, String> implements
		TCPROSMessage {

	/**
	 * <div lang="ja"> コンストラクタ </div> <div lang="en"> Constructor </div>
	 */
	public TCPROSHeader() {
		// TODO 自動生成されたコンストラクター・スタブ
	}

	public TCPROSHeader(byte[] byteArray) throws TransportException {
		try {
			LittleEndianInputStream inputStream = new LittleEndianInputStream(
					byteArray);
			int amountSize = 0;
			while (byteArray.length > amountSize) {
				int size = inputStream.readInt();
				String str = inputStream.readString(size);
				StringTokenizer tokenizer = new StringTokenizer(str, "=");
				String key = tokenizer.nextToken();
				String value = tokenizer.nextToken();
				put(key, value);
				amountSize += 4 + size;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new TransportException();
		}
	}

	@Override
	public byte[] serialize() throws TransportException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			LittleEndianOutputStream outputStream = new LittleEndianOutputStream(
					baos);
			for (String key : this.keySet()) {
				StringBuilder msg_buf = new StringBuilder();
				msg_buf.append(key);
				msg_buf.append("=");
				msg_buf.append(get(key));
				byte[] byteArray = msg_buf.toString().getBytes();
				int length = byteArray.length;
				outputStream.writeInt(length);
				outputStream.write(byteArray);
			}
			return baos.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw new TransportException();
		}
	}

}
