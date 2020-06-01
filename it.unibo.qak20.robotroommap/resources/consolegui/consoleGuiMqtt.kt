package consolegui
 
object consoleGuiMqtt{
	fun create(  hostIP : String,     port : String,  ctxDest : String,   destName : String) {
		consoleGuiSimple( connQak.ConnectionType.MQTT, hostIP, port, ctxDest, destName)
	}
}
 
fun main(){
	consoleGuiMqtt.create( mqtthostAddr, mqttport, ctxqadest, qakdestination)
}
 