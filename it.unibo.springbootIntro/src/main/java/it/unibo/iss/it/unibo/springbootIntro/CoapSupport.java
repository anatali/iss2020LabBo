package it.unibo.iss.it.unibo.springbootIntro;
import org.eclipse.californium.core.CoapClient;
import org.eclipse.californium.core.CoapResponse;
import org.eclipse.californium.core.coap.MediaTypeRegistry;

public class CoapSupport {
 	private static CoapClient client;
	private static String host = "";
	private static String port = "";
	public static void createConnection( String hostIP ,  String portID  ,  String destName ) {
		host = hostIP;
		port = portID;
		String ctxName = "ctx"+destName;
 		String url = "coap://"+host+":"+port+"/"+ctxName+"/"+destName;
 		client = new CoapClient( url );
		println("connQakCoap | createConnection url=" + url);
 		client.setTimeout( 1000L );
 		//initialCmd: to make console more reactive at the first user cmd
 		CoapResponse respGet  = client.get( );  
 		if( respGet != null )
 			println("connQakCoap | createConnection done get | CODE=  " + respGet.getResponseText() );
 		else
 			println("connQakCoap | FAILURE url=" + url);	
	}
	
	public static void forward( String move  ){
		//msg(cmd,dispatch,connQakTcp,basicrobot,cmd(l),11)
		String payload = "cmd("+move+")";
 		String d = "msg(cmd,dispatch,coapsupport,basicrobot,"+ payload +",1)";
   		CoapResponse respPut = client.put(d, MediaTypeRegistry.TEXT_PLAIN);
 		println("connQakCoap | answer="+ respPut.getResponseText() );				
	}

	public static void request( String move  ){
		String payload = move;
 		String d = "msg(cmd,dispatch,coapsupport,basicrobot,"+ payload +",1)";
   		CoapResponse respPut = client.put(d.toString(), MediaTypeRegistry.TEXT_PLAIN);
 		println("connQakCoap | answer="+ respPut.getResponseText() );						
	}

	public static void emit( String ev  ){
//		String url = "coap://"+host+":"+port+"ctxbasicrobot";  
	}

	private static void println( String msg ) {
		System.out.println(msg);
	}
	
}
