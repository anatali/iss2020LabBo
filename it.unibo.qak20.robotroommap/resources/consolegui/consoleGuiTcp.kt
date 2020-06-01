package consolegui

object consoleGuiTcp{
	fun create(  hostIP : String,     port : String,   ctxDest: String,  destName : String) {
		consoleGuiSimple( connQak.ConnectionType.TCP, hostIP, port, ctxDest, destName)
	}
}
fun main(){
	consoleGuiTcp.create( hostAddr, port, ctxqadest, qakdestination)
}
 