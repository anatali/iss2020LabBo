package it.unibo.dockerCompose.gui
import connQak.connQakBase
import connQak.ConnectionType
import it.unibo.kactor.MsgUtil


class SenderNoGui(val connType : ConnectionType=ConnectionType.TCP,
				  val hostIP : String="192.168.1.175", val port : String="8037", val destName : String="dcreceiver"){
	val connQakSupport : connQakBase  = connQakBase.create(connType, hostIP, port, destName ) 
	
	init{
		//createConn(connType)		//not yet, since fails when in a container
  		doJob(   )		 
	}
	
		 fun createConn(  ){
			 println("SenderNoGui | createConn to $destName $connQakSupport ")
			 var done = false
			 while( ! done  )
			 try{
 				 connQakSupport.createConnection()
				 done = true			 
			  }catch( e : Exception ){
				 println("SenderNoGui | $destName not yet ready ...")
				 Thread.sleep(2000)
			  }
		  }
	
	
		 fun doJob(){
			 Thread.sleep(2000)
			 println("SenderNoGui | forward")
			 forward( "w" )
			 Thread.sleep(1000)
			 println("SenderNoGui | forward")
			 forward( "h" )
			 //console()
		 }
 	
		 fun forward( move : String ){
			 if( connQakSupport.conn == null ) createConn( )
			 val msg = MsgUtil.buildDispatch("nogui", "cmd", "cmd($move)", destName )
			 connQakSupport.forward(msg)		
		 }
	
	fun console(){
		println("SenderNoGui | START")
		var cmd = readLine()
		while( cmd != null && cmd != "q"){
			forward( cmd )
			cmd = readLine()
		}
		println("SenderNoGui | END")
		
	}

}
	fun main(){
		SenderNoGui(  )
	}	
