package virtualRobotUsage
//robotActorCrilUsage.kt

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.delay
 
@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
suspend fun sendCrilCommands(   ) {
    robotActor.send("init")
    var jsonString  : String
	val time = 1000	//time = 1000 => collision
    for (i in 1..2) {
        jsonString = "{ 'type': 'moveForward', 'arg': $time }"
        robotActor.send("move($jsonString)")
        delay(1000)

        jsonString = "{ 'type': 'moveBackward', 'arg': ${time} }"
		robotActor.send("move($jsonString)")
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
	sendCrilCommands(   )
    println("BYE")
}

