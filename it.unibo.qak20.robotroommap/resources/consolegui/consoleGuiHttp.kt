package consolegui
 
object consoleGuiHttp{
	fun create(  hostIP : String,     port : String,  ctxDest : String,   destName : String) {
		consoleGuiSimple( connQak.ConnectionType.HTTP, hostIP, port, ctxDest, destName)
	}
} 
fun main(){
	consoleGuiHttp.create( "localhost", "8080", "", "basicrobot")
}
 