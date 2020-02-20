package kotlindemo
//demoLambdaShortcut.kt
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


fun exec23( op:(Int,Int) -> Int ) : Int { return op(2,3) }
fun p2( op: ( Int ) -> Int )      : Int { return op(2) }

val v1 = exec23( { x:Int, y:Int -> x-y } ) //no shortcut

val v2 = exec23() { x:Int, y:Int -> x-y } //lamda is last arg

val v3 = exec23{ x:Int, y:Int -> x-y } //() can be removed

val v4 = exec23{ x,y -> x-y } //arg types inferred

val v5 = p2{ it }

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
    println("v1=$v1")   //-1
    println("v2=$v2")
    println("v3=$v3")
    println("v4=$v4")
    println("v5=$v5")   //2
    println("ENDS ${curThread()}")
}