package fsm

import kotlinx.coroutines.*
import org.eclipse.paho.client.mqttv3.*
import utils.AppMsg

class MqttUtils  {
 	protected var eventId: String? = "mqtt"
	protected var eventMsg: String? = ""
	protected lateinit var client: MqttClient
 	protected lateinit var workActor : Fsm
	protected val RETAIN      = false
	protected var isConnected = false
			
	fun connect(clientid: String, brokerAddr: String ): Boolean {
		try {
  			//println("     %%% MqttUtils doing connect for $clientid to $brokerAddr "  );
			client = MqttClient(brokerAddr, clientid)
            //println("     %%% MqttUtils for $clientid connect $brokerAddr client = $client" )
			val options = MqttConnectOptions()
			options.setKeepAliveInterval(480)
			options.setWill("unibo/clienterrors", "crashed".toByteArray(), 2, true)
			client.connect(options)
			println("       %%% MqttUtils connect DONE $clientid to $brokerAddr " )//+ " " + client
			isConnected = true
		} catch (e: Exception) {
			println("       %%% MqttUtils for $clientid connect ERROR for: $brokerAddr" )
			isConnected = false
		}
 			return isConnected
	}
	
	fun connectDone() : Boolean{
		return isConnected
	}

	fun disconnect() {
		try {
			//println("       %%% MqttUtils disconnect "  )
			client.disconnect()
		} catch (e: Exception) {
			//println("       %%% MqttUtils disconnect ERROR ${e}")
		}
	}


	fun subscribe(  actor: Fsm, topic: String ) {
		//println("	%%% MqttUtils ${actor.name} subscribe to topic=$topic client=$client "  )
		try {
			this.workActor = actor
			//client.setCallback(this)
			client.setCallback(actor)
			client.subscribe(topic)
		}catch( e: Exception ){
			println("       %%% MqttUtils ${actor.name} subscribe topic=$topic ERROR=$e "  )
		}
	}

	fun sendMsg( sender: String, msgID: String,  dest: String,  msgType: String ){
	  	val msgNum = 0
	  	//msg( MSGID, MSGTYPE SENDER, RECEIVER, CONTENT, SEQNUM )	  				
	  	val msgout = "msg( $msgID, $msgType, $sender, $dest, $msgNum )"
	  	//println(" ************ SENDING VIA MQTT mout=$msgout" )
	  	publish(   "unibo/qasys", msgout, 1, RETAIN);		
	}
	fun sendMsg(  msg: String, topic: String ){
		//println(" ************ SENDING VIA MQTT mout=$msg on $topic" )
 	  	publish( topic, msg, 1, RETAIN);
	}
	fun sendMsg(  msg: AppMsg, topic: String ){
		//println(" ************ SENDING VIA MQTT mout=$msg on $topic" )
		publish( topic, msg.toString(), 1, RETAIN);
	}
 
	/*
         * sends to a tpoic a content of the form
         * 	 msg( MSGID, MSGTYPE, SENDER, RECEIVER, CONTENT, SEQNUM )
     */
	@Throws(MqttException::class)
	fun publish( topic: String, msg: String?, qos: Int=2, retain: Boolean=false) {
		val message = MqttMessage()
		message.setRetained(retain)
		if (qos == 0 || qos == 1 || qos == 2) {
			//qos=0 fire and forget; qos=1 at least once(default);qos=2 exactly once
			message.setQos(qos)
		}
		message.setPayload(msg!!.toByteArray())
		try {
			client.publish(topic, message)
 			println("       %%% MqttUtils publish $message topic=$topic"  )
		} catch (e:Exception) {
			println("       %%% MqttUtils publish ERROR $e topic=$topic msg=$msg"  )
 		}
	}


	fun sendMsgToWorkActor( msg: String ){
		workActor.scope.launch{
			val m = AppMsg.create( msg )
			workActor.fsmactor.send( m )
 		}
	}
	
	fun clearTopic( topic : String )  {
  		println("       %%%  MqttUtils clearTopic $topic" );
		publish( topic, "", 1, true);	//send a retained message !!
	}

/*
	fun println(msg: String?) {
		System.out.println(msg)
	}
*/
}