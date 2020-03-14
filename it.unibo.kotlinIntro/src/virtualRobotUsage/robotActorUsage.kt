package virtualRobotUsage
//robotActorUsage.kt

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay

val initMsg         = AppMsg.create("init","main","robotactor")
val endMsg          = AppMsg.create("end","main","robotactor")
val moveForwardMsg  = AppMsg.create("move","main","robotactor","w") 
val moveBackwardMsg = AppMsg.create("move","main","robotactor","s") 
val haltRobotMsg     = AppMsg.create("move","main","robotactor","h") 

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun forward(  msg: AppMsg ){
	robotActor.send( msg.toString()  )
}
 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun sendCommands(   ) {
    forward( initMsg )
    for (i in 1..2) {
		 forward( moveForwardMsg )
         delay(1000)
		 forward( haltRobotMsg )
         delay(1000)

         forward( moveBackwardMsg )
         delay(1000)
 		 forward( haltRobotMsg )
         delay(500)
   }
	forward( endMsg )
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main( ) = runBlocking {
    println("==============================================")
    println("PLEASE, ACTIVATE WENV ")
    println("==============================================")
	sendCommands(   )
    println("BYE")
}

