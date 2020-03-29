package consolegui;

import java.util.Observable;
import java.util.Observer;

import org.eclipse.paho.client.mqttv3.MqttException;

import fsm.MqttUtils;
import utils.AppMsg;
 
 

public class ConsoleGui implements  Observer{
private String[] buttonLabels  = new String[] {"e","w", "s", "l", "r", "z", "x", "b", "p", "h"};
private String brokerAddr = "tcp://mqtt.eclipse.org:1883";
private MqttUtils   mqtt  = new MqttUtils();
private String destName   = "";
 
	public ConsoleGui( String hostIP, String port, String destName) {
		ButtonAsGui concreteButton = ButtonAsGui.createButtons( "", buttonLabels );
		concreteButton.addObserver( this );
		this.destName = destName;
		mqtt.connect("gui", brokerAddr);
 	}
	
	protected void forward(  String move ){
		try {
			AppMsg msg = AppMsg.buildDispatch("gui","cmd", move, destName);
			mqtt.publish("unibo/qak/"+destName, msg.toString(), 0, false)	;
		} catch (MqttException e) {
 			e.printStackTrace();
		}	
	}
	
	public void update( Observable o , Object arg ) {
		String move = arg.toString();
		System.out.println("GUI input move=" + move);
		forward( move );
	}
	
	public static void main( String[] args) {
		new ConsoleGui( "mqtt.eclipse.org","1883","basicrobot");
	}
}
