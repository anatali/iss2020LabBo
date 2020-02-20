package kotlindemo
//demoSuspended.kt
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.system.measureTimeMillis
import kotlinx.coroutines.delay
import kotlinx.coroutines.async
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.channels.Channel

//------------------CONVENTIONAL -----------------------------
fun runBlockThread(){
    run { //Calls a function block; returns its result
        println("Out start: ${curThread()}")
        Thread.sleep(1500)
        println("Out ended: ${curThread()}")
    }
}
//------------------SUSPEND -----------------------------
suspend fun ioBoundFun(){
    val timeElapsed = measureTimeMillis {
        println("IO operation | STARTS in ${curThread()}")
        delay(1000)
    }
    println("IO operation | Done, TIME=$timeElapsed")
}

//------------------ASYNC -----------------------------

suspend fun activate(){
    val job1 = GlobalScope.async{
        ioBoundFun()
    }
    val job2 = GlobalScope.async{
        ioBoundFun()
    }
    if(! job1.isCompleted || ! job2.isCompleted)
        println("Waiting for completion")
    val end1 = job1.await()
    val end2 = job2.await()
    println("All jobs done")
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
/*
    runBlockThread()                             //(1)

    GlobalScope.launch{ runBlockThread() }       //(2)
    println("BYE")                               //(2)

    Thread.sleep(1600) // To see the output      //(2)
 
    launch{  runBlockThread()  }                 //(3)
    println("BYE")                               //(3)
	Thread.sleep(3000)
 
    val job = launch{  runBlockThread()  }       //(4)
    job.join()                                   //(4)
    println("BYE")                               //(4)

    ioBoundFun()                              //(5)
    launch{ ioBoundFun() }                    //(6)
*/
     activate()                              //(7)
/*
    println("BYE")
 */
    println("ENDS ${curThread()}")
}