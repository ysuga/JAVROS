/**
 * URIUtil.java
 *
 * @author Yuki Suga (ysuga.net)
 * @date 2011/09/07
 * @copyright 2011, ysuga.net allrights reserved.
 *
 */
package net.ysuga.javros.util;

import java.util.StringTokenizer;

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
public class ROSUri {

	final private String uri;

	final private String protocol;
	
	final private String path;
	
	final private int port;
	
	public ROSUri(String uri) {
		this.uri = uri;
		StringTokenizer tokens = new StringTokenizer(uri, ":/");
		protocol = tokens.nextToken();
		path = tokens.nextToken();
		port = Integer.parseInt(tokens.nextToken());
	}

	/**
	 * <div lang="ja">
	 * コンストラクタ
	 * @param protocol
	 * @param path
	 * @param port
	 * </div>
	 * <div lang="en">
	 * Constructor
	 * @param protocol
	 * @param path
	 * @param port
	 * </div>
	 */
	public ROSUri(String protocol, String path, int port) {
		super();
		this.protocol = protocol;
		this.path = path;
		this.port = port;
		uri = protocol + "://" + path + ":" + Integer.toString(port);
	}

	/**
	 * @return protocol
	 */
	public final String getProtocol() {
		return protocol;
	}
	
	public final String getPath() {
		return path;
	}
	
	public final int getPort() {
		return port;
	}

	/**
	 * @return uri
	 */
	public final String getUri() {
		return uri;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof ROSUri)) {
			return false;
		}
		return ((ROSUri)o).getUri().equals(getUri());
	}
}
