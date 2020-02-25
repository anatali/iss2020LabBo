package itunibo.robotMbot
import it.unibo.robot.robotAdapter 

object mbotSupport : robotAdapter{
  	lateinit var conn    : SerialPortConnSupport
	var dataSonar        : Int = 0 ; //Double = 0.0
 			
 	override fun create(   ){
 		initConn( "COM20"   )
	}
	
	private fun initConn( port : String ){ 
		try {
			//println("   	%%% mbotSupport | initConn starts port=$port")
			val serialConn = JSSCSerialComm()
			conn = serialConn.connect(port)	//returns a SerialPortConnSupport
			println("   	%%% mbotSupport |  initConn port=$port conn= $conn")						
 			//robotDataSourceArduino("robotDataSourceArduino", owner,   conn )
		}catch(  e : Exception) {
			println("   	%%% mbotSupport |  ERROR ${e }"   );
		}		
	}
	

 
	override fun  move( cmd : String ){
		//println("  	%%% mbotSupport | move cmd=$cmd ")
		when( cmd ){
			"msg(w)", "w" -> conn.sendALine("w")
			"msg(s)", "s" -> conn.sendALine("s")
			"msg(a)", "a" -> conn.sendALine("a")
			"msg(d)", "d" -> conn.sendALine("d")
			"msg(l)", "l" -> conn.sendALine("l") //sendToPython("l")	//
			"msg(r)", "r" -> conn.sendALine("r") //sendToPython("r")	//
			"msg(z)", "z" -> conn.sendALine("z") //sendToPython("z")
			"msg(x)", "x" -> conn.sendALine("x") //sendToPython("x")	//
			"msg(h)", "h" -> conn.sendALine("h")
			else -> println("   	%%% mbotSupport | command $cmd unknown")
		}
		
	}
 
	
 }