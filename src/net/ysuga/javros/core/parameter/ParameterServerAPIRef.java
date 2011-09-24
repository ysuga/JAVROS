package net.ysuga.javros.core.parameter;

import java.net.URL;

import net.ysuga.javros.xmlrpc.XmlRpcRequestException;
import net.ysuga.javros.xmlrpc.XmlRpcWrapper;

public class ParameterServerAPIRef extends XmlRpcWrapper implements ParameterServerAPI {

	public ParameterServerAPIRef(URL uri) {
		super(uri);
	}

	@Override
	public Object[] deleteParam(String callerId, String key) throws XmlRpcRequestException {
		return request("deleteParam", callerId, key);
	}

	@Override
	public Object[] getParam(String callerId, String key) throws XmlRpcRequestException{
		return request("getParam", callerId, key);
	}

	@Override
	public Object[] getParamNames(String callerid) throws XmlRpcRequestException{
		return request("getParamNames", callerid);
	}

	@Override
	public Object[] searchParam(String callerId, String key) throws XmlRpcRequestException{
		return request("searchParam", callerId, key);
	}

	@Override
	public Object[] setParam(String callerId, String key, Object value) throws XmlRpcRequestException{
		return request("setParam", callerId, key, value);
	}

	@Override
	public Object[] subscribeParam(String callerId, String callerAPI, String key) throws XmlRpcRequestException{
		return request("subscribeParam", callerId, callerAPI, key);
	}

	@Override
	public Object[] unsubscribeParam(String callerid, String callerAPI,
			String key) throws XmlRpcRequestException {
		return request("unsubscribeParam", callerid, callerAPI);
	}

	@Override
	public Object[] hasParam(String callerId, String key)
			throws XmlRpcRequestException {
		return request("hasParam", callerId, key);
	}

}
