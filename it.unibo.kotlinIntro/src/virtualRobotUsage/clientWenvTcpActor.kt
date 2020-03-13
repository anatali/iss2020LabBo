package virtualRobotUsage
//clientWenvTcpActor.kt

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.io.PrintWriter
import java.net.Socket
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.IOException
import kotlinx.coroutines.delay
import kotlinx.coroutines.channels.Channel
 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
val robotActor  : SendChannel<String>	= CoroutineScope( Dispatchers.Default ).actor {
/*
Includes the business logic
Behavior  msg-driven 
*/
	var state    = "working"
	
	while( state == "working" ){
		var msg = channel.receive()
		println("robotActor receives: $msg ")
		val msgSplitted = msg.split('(')
		val msgFunctor  = msgSplitted[0]
		//println("robotActor msgFunctor $msgFunctor ")
		when( msgFunctor ){
			"end"       -> state = "end"
			"sensor"    -> println("robotActor receives $msg")
			"collision" -> {
				println("robotActor receives $msg");
				state = "collision"
				val back =  "{ 'type': 'moveBackward', 'arg': 100 }"
				clientWenvTcpActor.sendMsg( back  )  // not for plasticBox
				state = "working"  
			}
			"cmd"       -> {
 				val msgBody = msgSplitted[1].replace(")","")
 				clientWenvTcpActor.sendMsg( msgBody )
			}
			else -> println("robotActor DOES NOT HANDLE $msg")
		}		
 	}
 	println("robotActor ENDS state=$state")
}

/*
 clientWenvTcpActor becomes a support
 */
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
object clientWenvTcpActor {
        private var hostName = "localhost"
        private var port     = 8999
        private val sep      = ";"
        private var outToServer : PrintWriter?     = null
 	
	fun initClientConn( hostNameStr: String = hostName, portStr: String = "$port"  ) {
            hostName         = hostNameStr
            port             = Integer.parseInt(portStr)
             try {
                 val clientSocket = Socket(hostName, port)
                 println("clientWenvTcpActor |  CONNECTION DONE")
                  outToServer  = PrintWriter(clientSocket.getOutputStream())
                 startSensorObserver( clientSocket )
             }catch( e:Exception ){
                 println("clientWenvTcpActor | ERROR $e")
             }
	}

/*
 	Send a message wriiten in JSON on the TCP connection
*/		
         fun sendMsg(jsonString: String) {
            val jsonObject = JSONObject(jsonString)
            val msg = "$sep${jsonObject.toString()}$sep"
            outToServer?.println(msg)
            outToServer?.flush()
        }
	

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
        private fun startSensorObserver( clientSocket : Socket ) {
		val inFromServer = BufferedReader(InputStreamReader(clientSocket.getInputStream()))
		val scope : CoroutineScope = CoroutineScope( Dispatchers.Default )
	    scope.launch {
//			println("clientWenvTcpActor | startSensorObserver STARTING ")
                while (true) {
                    try {
                        val inpuStr = inFromServer.readLine()
                        val jsonMsgStr =
                            inpuStr!!.split(";".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                        //println("clientWenvTcpActor | inpuStr= $jsonMsgStr")
                        val jsonObject = JSONObject(jsonMsgStr)
                        //println( "type: " + jsonObject.getString("type"));
                        when (jsonObject.getString("type")) {
                            "webpage-ready" -> println("webpage-ready ")
                            "sonar-activated" -> {
                                val jsonArg   = jsonObject.getJSONObject("arg")
                                val sonarName = jsonArg.getString("sonarName")
                                val distance  = jsonArg.getInt("distance")
								robotActor.send( "sensor($sonarName, $distance)" )
 
                            }
                            "collision" -> {
                                val jsonArg    = jsonObject.getJSONObject("arg")
                                val objectName = jsonArg.getString("objectName")
								robotActor.send( "collision($objectName)" )
                             }
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
         }//startSensorObserver
	 
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun doJob(   ) {
    clientWenvTcpActor.initClientConn( )
    var jsonString  : String
	val time = 800
    for (i in 1..3) {
        jsonString = "{ 'type': 'moveForward', 'arg': $time }"
        robotActor.send("cmd($jsonString)")
        delay(1000)

        jsonString = "{ 'type': 'moveBackward', 'arg': $time }"
		robotActor.send("cmd($jsonString)")
        delay(1000)
    }
	robotActor.send("end")
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
	doJob(   )
    println("BYE")
}








