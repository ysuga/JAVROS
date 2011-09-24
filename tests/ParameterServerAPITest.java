import java.net.URL;

import net.ysuga.javros.core.parameter.ParameterServerAPIHelper;
import net.ysuga.javros.core.parameter.ParameterServerAPIRef;
import net.ysuga.javros.util.ReturnValue;

public class ParameterServerAPITest {

	private ParameterServerAPIHelper api;

	/**
	 * 
	 * @throws Exception
	 */
	public ParameterServerAPITest() throws Exception {
		String hostAddress = "127.0.0.1";
		int port = 11311;
		URL uri = new URL("http://" + hostAddress + ":" + port);
		api = new ParameterServerAPIHelper(new ParameterServerAPIRef(uri));
	}

	/**
	 * 
	 * @throws Exception
	 */
	public void nameListTest() throws Exception {
		ReturnValue<String[]> ret = api.getParamNames("/script");
		for (String s : ret.getValue()) {
			System.out.println("Param:" + s);
		}
	}
	
	public void getValueTest() throws Exception {
		ReturnValue<Object> ret = api.getParam("/script", "/test");
		System.out.println("Param:" + ret.getValue());
	}
	
	public void setValueTest() throws Exception {
		ReturnValue<Integer> ret = api.setParam("/script", "test", "value");
		System.out.println("Param:" + ret.getValue());
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			ParameterServerAPITest test = new ParameterServerAPITest();
			test.setValueTest();
			test.nameListTest();
			test.getValueTest();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
