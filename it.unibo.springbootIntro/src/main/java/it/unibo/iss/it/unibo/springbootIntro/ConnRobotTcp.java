package it.unibo.iss.it.unibo.springbootIntro;

import it.unibo.is.interfaces.protocols.IConnInteraction;
import it.unibo.supports.FactoryProtocol;

public class ConnRobotTcp {
private IConnInteraction conn ;
private String destName = "";
 

	public ConnRobotTcp() {}
	
	public void connect( String hostIP , Integer  port , String dest   ) {
	    try {
	    	destName              = dest;
			FactoryProtocol fp    = new FactoryProtocol( null,"TCP","connRobotTcp" );
			conn                  = fp.createClientProtocolSupport(hostIP, port ) ;
		} catch (Exception e) {
 			e.printStackTrace();
		}    
		
	}
	
	public void forward( String move ) {
 		try {
 			String cmdmove = "cmd("+move+")";
   	 		String msg = "msg(cmd,dispatch,spring,"+destName+","+cmdmove+",0)";
 			conn.sendALine( msg   ); 
		} catch (Exception e) {
 			e.printStackTrace();
		}
	}
}
