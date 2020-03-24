package utils

import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.*
import kotlinx.coroutines.channels.SendChannel

class MqttUtils( val ownerActor: SendChannel<String>  ) : MqttCallback {
 	protected var eventId: String? = "mqtt"
	protected var eventMsg: String? = ""
	protected lateinit var client: MqttClient
 	
	protected val RETAIN = false;

	val mqttUrl  = "tcp://mqtt.eclipse.org:1883" //"ws://broker.hivemq.com:8000/mqtt" WebSockets;  //"tcp://localhost:1883"; 

	
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
	override fun messageArrived(topic: String, msg: MqttMessage) {
		val ms = msg.toString()
        println("MQTT messageArrived on "+ topic + ": "+msg.toString());
//        val m = AppMsg.create( ms ) //just for checking the format ...
	     (ownerActor as CoroutineScope).launch{ println(" 	propagate $ms");  Messages.forward( ms, ownerActor ) }
	     //GlobalScope.launch{  println(" 	propagate $ms"); ownerActor.send( ms ) }
      }
    override fun connectionLost(cause: Throwable?) {
        println("MQTT connectionLost $cause " )
    }
    override fun deliveryComplete(token: IMqttDeliveryToken?) {
//		println("MQTT deliveryComplete token=$token " )
    }

	fun connect(clientid: String, brokerAddr: String=mqttUrl ): Boolean {
		try {
  			//println("MQTT doing connect for $clientid to $brokerAddr "  );
			client = MqttClient(brokerAddr, clientid)
            //println("MQTT for $clientid connect $brokerAddr client = $client" )
			val options = MqttConnectOptions()
			options.setKeepAliveInterval(480)
			options.setWill("unibo/clienterrors", "crashed".toByteArray(), 2, true)
			client.connect(options)
			println("MQTT connect DONE $clientid to $brokerAddr " )//+ " " + client
 			return true
		} catch (e: Exception) {
			println("MQTT for $clientid connect ERROR for: $brokerAddr" )
			return false
		}
	}

	fun disconnect() {
		try {
			println("       %%% MqttUtils disconnect " + client)
			client.disconnect()
		} catch (e: Exception) {
			println("       %%% MqttUtils disconnect ERROR ${e}")
		}
	}


	fun subscribe(  topic: String ) {
		//println("MQTT subcribe to topic=$topic client=$client "  )
		try {
			client.setCallback(this)
 			client.subscribe(topic)
		}catch( e: Exception ){
			println("MQTT subscribe topic=$topic ERROR=$e "  )
		}
	}

	fun sendMsg( sender: String, msgID: String,  dest: String,  msgType: String ){
	  	val msgNum = 0
	  	//msg( MSGID, MSGTYPE SENDER, RECEIVER, CONTENT, SEQNUM )	  				
	  	val msgout = "msg( $msgID, $msgType, $sender, $dest, $msgNum )"
	  	//println("MQTT sending mout=$msgout" )
	  	publish(   "unibo/qasys", msgout, 1, RETAIN);		
	}
	fun sendMsg(  msg: String, topic: String ){
		//println("MQTT sendingmout=$msg on $topic" )
 	  	publish( topic, msg, 1, RETAIN);
	}
	fun sendMsg(  msg: AppMsg, topic: String ){
		//println("MQTTsending  mout=$msg on $topic" )
		publish( topic, msg.toString(), 1, RETAIN);
	}
 
	/*
         * sends to a tpoic a content of the form
         * 	 msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
     */
	 
	fun publish( topic: String, msg: String, qos: Int=1, retain: Boolean=false) {
		val message = MqttMessage()
		message.setRetained(retain)
		if (qos == 0 || qos == 1 || qos == 2) {
			//qos=0 fire and forget; qos=1 at least once(default);qos=2 exactly once
			message.setQos(0)
		}
		message.setPayload(msg.toByteArray())
		try {
			client.publish(topic, message)
//			println("MQTT published "+ message + " on topic=" + topic);
		} catch (e:Exception) {
			println("MQTT publish ERROR $e topic=$topic msg=$msg"  )
 		}
	}


//	fun sendMsgToWorkActor( msg: String ){
//		workActor.scope.launch{
//			val m = AppMsg( msg )
//			workActor.actor.send( m )
// 		}
//	}
	
	fun clearTopic( topic : String )  {
  		println("MQTT clearTopic $topic" );
		publish( topic, "", 1, true);	//send a retained message !!
	}

}