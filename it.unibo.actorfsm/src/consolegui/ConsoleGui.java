package consolegui;

import java.util.Observable;
import java.util.Observer;
import org.eclipse.paho.client.mqttv3.MqttException;

import fsm.FsmKt;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import utils.MqttUtils;
import utils.AppMsg;
import utils.Messages;


public class ConsoleGui implements  Observer{
private String[] buttonLabels  = new String[] {"e","w", "s", "l", "r", "z", "x", "b", "p", "h"};  //p means step
private String brokerAddr      = FsmKt.getMqttbrokerAddr() ;   //Using kotlin from Java
private MqttUtils   mqtt       =  new MqttUtils("gui");
private String destName        = "";
 
	public ConsoleGui( String destName) {
		ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
		concreteButton.addObserver( this );
		this.destName = destName;
		mqtt.connect("gui", brokerAddr );
 	}
	
	protected void myforward( String move ){
		try {
			AppMsg msg;
			if( move.equals("p") ){
				msg = AppMsg.buildDispatch("gui","step", "300", destName);
			}else {
				msg = AppMsg.buildDispatch("gui","cmd", move, destName);
			}
			mqtt.publish("unibo/qak/"+destName, msg.toString(), 1, false)	;
		} catch (MqttException e) {
 			e.printStackTrace();
		}	
	}
	
	public void update( Observable o , Object arg ) {
		String move = arg.toString();
		System.out.println("GUI input move=" + move);
		myforward( move );
		//Messages.forward("gui","cmd", move, destName, mqtt, null);  //Java/Kotlin => missing the last argument Continuation	
	}
	
	public static void main( String[] args) {
		//new ConsoleGui( "basicrobot" );
		new ConsoleGui( "stepper" );
	}
}

