package fsm

import kotlinx.coroutines.runBlocking
import utils.Messages
import utils.AppMsg
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.CoroutineScope
import utils.virtualRobotSupport
import utils.virtualRobotSupportApp

val terminate = AppMsg.buildDispatch("main","any","stopTheActor","br")
val msg       = AppMsg.buildDispatch("main","cmd","cmd(w)","br")


@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
suspend fun runBoundaryRobot( scope: CoroutineScope ){
	val startMsg  = AppMsg.buildDispatch("main","start","start","robotboundaryfsm")

	
	val robot= robotboundaryfsm( "robot", scope )
  	virtualRobotSupportApp.setRobotTarget( robot   ) //Configure - Inject
	
	delay( 50 )  //give the time to start (elaborate the autoStartSysMsg)
 	Messages.forward( startMsg, robot  )
	
	(robot.fsmactor as Job).join()	//waits for termination  	
}

@kotlinx.coroutines.ExperimentalCoroutinesApi
@kotlinx.coroutines.ObsoleteCoroutinesApi
fun main()=runBlocking{
	println("main STARTS")
//	test( this )
	runBoundaryRobot( this )
	println("main ENDS")
}