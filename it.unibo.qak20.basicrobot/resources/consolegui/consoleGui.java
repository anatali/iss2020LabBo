package consolegui;

import java.util.Observable;
import java.util.Observer;
import org.eclipse.paho.client.mqttv3.MqttException;
import it.unibo.kactor.ApplMessage;
import it.unibo.kactor.MsgUtil;
import utils.MqttUtils;
 

/*
 * This Java code makes reference to Kotlin code
 * However Messages.forward is more difficult to use since it is a suspend function
 * that includes a Continuation as its last argument.
 * Fir the sake of simplicity, we redefine here the forward operation.
 */

public class consoleGui implements  Observer{
private String[] buttonLabels  = new String[] {"w", "s", "l", "r", "p", "h"};  //p means step
//private String brokerAddr      = FsmKt.getMqttbrokerAddr() ;    //Using kotlin from Java
private MqttUtils   mqtt       = new MqttUtils("gui");  		//Using kotlin from Java
private String destName        = "";

public static void create( String destName, String brokerAddr ) {
	new consoleGui( destName, brokerAddr );
}
	public consoleGui( String destName, String brokerAddr) {
		ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
		concreteButton.addObserver( this );
		this.destName = destName;
		mqtt.connect("gui", brokerAddr );
 	}
	
	protected void forward(String sender,String msgId,String move,String destName,MqttUtils mqtt  ){
		// 
		try {
			ApplMessage msg;
			if( move.equals("p") ){
				msg = MsgUtil.buildDispatch(sender,"step", "step(300)", destName);
			}else {
				msg = MsgUtil.buildDispatch(sender,msgId, "cmd("+move+")", destName);
			}
			mqtt.publish("unibo/qak/"+destName, msg.toString(), 0, false); //fire and forget do not retain
		} catch (MqttException e) {
 			e.printStackTrace();
		}	
	}
	
	public void update( Observable o , Object arg ) {
		String move = arg.toString();
		//System.out.println("GUI input move=" + move);
		forward( "gui", "cmd", move, destName, mqtt );
		//Messages.forward("gui","cmd", move, destName, mqtt, ???);  //Java/Kotlin => missing the last argument Continuation	
	}
	
	public static void main( String[] args) {
		//new ConsoleGui( "basicrobot" );
		new consoleGui( "basicrobot","tcp://broker.hivemq.com");
	}
}

