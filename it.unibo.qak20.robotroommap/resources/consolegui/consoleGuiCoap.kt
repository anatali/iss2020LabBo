package consolegui

object consoleGuiCoap{
	fun create(  hostIP : String,     port : String,  ctxDest : String,   destName : String) {
		consoleGuiSimple( connQak.ConnectionType.COAP, hostIP, port, ctxDest, destName)
	}
}

fun main(){
	consoleGuiCoap.create( hostAddr, port, ctxqadest, qakdestination)
}
 