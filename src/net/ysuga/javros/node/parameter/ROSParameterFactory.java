package net.ysuga.javros.node.parameter;

public class ROSParameterFactory {

	public static ROSParameter createROSParameter(String key, Object value) {
		return new ROSParameter(key, value);
	}

}
