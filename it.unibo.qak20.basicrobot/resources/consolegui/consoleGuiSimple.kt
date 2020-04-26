package consolegui

import connQak.connQakBase
import it.unibo.`is`.interfaces.IObserver
import java.util.Observable
import connQak.ConnectionType

class consoleGuiSimple( val connType : ConnectionType,
						val hostIP : String,   val port : String,   val destName : String ) : IObserver {
lateinit var connQakSupport : connQakBase
	
 		 val buttonLabels = arrayOf("e","w", "s", "l", "r", "z", "x", "b", "p", "h")
	
	init{
		create( connType )
	}
		 fun create( connType : ConnectionType){
			 connQakSupport = connQakBase.create(connType, hostIP, port,destName )
			 connQakSupport.createConnection()
			 var guiName = ""
			 when( connType ){
				 ConnectionType.COAP -> guiName="GUI-COAP"
				 ConnectionType.MQTT -> guiName="GUI-MQTT"
				 ConnectionType.TCP  -> guiName="GUI-TCP"
				 ConnectionType.HTTP -> guiName="GUI-HTTP"
			 }
			 createTheGui( guiName )		 
		  }
		  fun createTheGui( guiName : String ){
	  			val concreteButton = ButtonAsGui.createButtons( guiName, buttonLabels )
	            concreteButton.addObserver( this )		  
		  }
	 
	  override fun update(o: Observable, arg: Any) {	   
   		  var move = arg as String
		  if( move == "p" ) connQakSupport.request("step")
		  else if( move == "e" ) connQakSupport.emit("alarm")
 		  else connQakSupport.forward( move ) 		  
       }//update
	
}


fun main(){
	consoleGuiSimple( ConnectionType.COAP, "localhost", "8020", "basicrobot")
}