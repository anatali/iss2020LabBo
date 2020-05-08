package usingCoap;
//
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class basicrobotCoapClient {

private static CoapClient client = new CoapClient();

private static void sendToServer( String move ) {
   		String uriStr = "coap://192.168.1.8/"+move;
 		client.setURI( uriStr );
 		String arg = "{ \"activationCode\"  :  \"ACTIVATION_CODE\" }";
 		System.out.println("USING: " + uriStr);
 		CoapResponse res = client.put(arg, MediaTypeRegistry.TEXT_PLAIN);
 		System.out.println("RESPONSE=" + res.getResponseText() );
	}


private static void h() { sendToServer("h"); }
private static void w() { sendToServer("w"); }
private static void s() { sendToServer("s"); }
private static void r() { sendToServer("r"); }
private static void l() { sendToServer("l"); }
private static void x() { sendToServer("x"); }
private static void z() { sendToServer("z"); }

public static void main(String[] args) throws Exception {
	l();
	Thread.sleep(1000);
	r();
}

}
