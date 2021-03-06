package robotMbot
/*
 -------------------------------------------------------------------------------------------------
 A factory that creates the support for the mbot
 
 -------------------------------------------------------------------------------------------------
 */

import it.unibo.kactor.ActorBasic
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import it.unibo.kactor.ActorBasicFsm
import it.unibo.kactor.MsgUtil


object mbotSupport{
	lateinit var owner   : ActorBasic
 	lateinit var conn    : SerialPortConnSupport
	var dataSonar        : Int = 0 ; //Double = 0.0
 			
	fun create( owner: ActorBasic, port : String, withSonar : Boolean = true    ){
		this.owner = owner	//
		initConn( port, withSonar   )
		
	}
	
	private fun initConn( port : String, withSonar : Boolean  ){ 
		try {
			//println("   	%%% mbotSupport | initConn starts port=$port")
			val serialConn = JSSCSerialComm()
			conn = serialConn.connect(port)	//returns a SerialPortConnSupport
			println("   	--- mbotSupport |  initConn port=$port conn= $conn")
			move( "z" )				//to see if it works						
			if( withSonar ) {
		 		val realsonar = robotDataSourceArduino("realsonar", owner,   conn)
				//Context injection  
				owner.context!!.addInternalActor(realsonar)  
		  		println("   	--- mbotSupport | has created the realsonar")
			}
		}catch(  e : Exception) {
			println("   	--- mbotSupport |  ERROR ${e }"   );
		}		
	}
	
	/*
 	 Aug 2019
     The moves l, r, z, x can be executed  
 	  by the Python application robotCmdExec that exploits GY521
    */
	fun  move( cmd : String ){
		println("  	%%% mbotSupport | move cmd=$cmd ")
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
	
	private fun sendToPython( msg : String ){
		println("mbotSupport sendToPython $msg")
		owner.scope.launch{ owner.emit("rotationCmd","rotationCmd($msg)") }
	}
	
 }