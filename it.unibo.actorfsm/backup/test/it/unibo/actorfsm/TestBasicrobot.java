package it.unibo.actorfsm;

import org.junit.*;

import fsm.Fsm;
import kotlin.coroutines.experimental.jvm.internal.CoroutineImpl;
import robotAppl.basicrobot;
import utils.MqttUtils;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.GlobalScope;

//import org.junit.*;

//See https://medium.com/@igorwojda/migrating-java-junit-tests-into-kotlin-6ded57597666

//See https://medium.com/@esocogmbh/coroutines-in-pure-java-65661a379c85
public class TestBasicrobot {
	MqttUtils mqttTest  	= new MqttUtils("test");
	long initDelayTime  	= 2000L;
	boolean useMqttInTest 	= false;
	//CoroutineScope scope    = CoroutineImpl.first();
	
	@Before 
	public void setup(){
 		//Fsm robot = new basicrobot("basicrobot", GlobalScope, useMqttInTest );
	}
	
	@Test
	public void test(){
		
	}
}
