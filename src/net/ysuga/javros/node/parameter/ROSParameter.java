package net.ysuga.javros.node.parameter;

public class ROSParameter {

	private String key;
	
	private Object value;
	
	public ROSParameter(String key, Object value) {
		this.key = key;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public String getKey() {
		return key;
	}

	public void setValue(Object parameterValue) {
		this.value = parameterValue;
	}

}
