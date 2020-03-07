package kotlindemo

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

val cpus = Runtime.getRuntime().availableProcessors();

inline fun measureTimeMillis(block: () -> Unit): Long {
    val start = System.currentTimeMillis()
    block()
    return System.currentTimeMillis() - start
}
		
fun curThread() : String { 
	return "thread=${Thread.currentThread().name} | nthreads=${Thread.activeCount()}" 
}

fun myDemoWork(){
	println("Hello from myDemoWork"); 
}
//@kotlinx.coroutines.ObsoleteCoroutinesApi
//@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
	
    println( "work done in time= ${measureTimeMillis(  { myDemoWork() } )}"  )
	
    println("ENDS ${curThread()}")
}