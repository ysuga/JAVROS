import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

public class JavaClient {
	public static void main(String[] args) {
		try {

			XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
			config.setServerURL(new URL("http://localhost:11311"));
			XmlRpcClient client = new XmlRpcClient();

			
			List<Integer> param = new ArrayList<Integer>();
			param.add(new Integer(17));
			param.add(new Integer(13));
			
			Object result = client.execute(config, "sum",param);

			int sum = ((Integer) result).intValue();
			System.out.println("The sum is: " + sum);

		} catch (Exception exception) {
			System.err.println("JavaClient: " + exception);
		}
	}
}