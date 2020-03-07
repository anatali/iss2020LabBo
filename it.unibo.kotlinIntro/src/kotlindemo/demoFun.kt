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

fun fsum(a:Int, b:Int) : Int {
  return a+b
}


var fcounter = 0
fun incCounter() : Unit{ fcounter++ }
fun decCounter() { fcounter-- }

fun fsquare(v: Int) = v * v		//oneline function


val ftaction : () -> Unit
					= { println("hello") } 		//lambda expression
val ftsum : ( Int,  Int) -> Int
					= {  x:Int, y:Int -> x+y }  //lambda expression

val ftgreet: (String )-> ()->Unit
					= {  m: String -> { println(m)}   }

val fva = ftsum(1,2)

val fel = {  print( "Last exp val=" ); 100  }

//--------------------------------------------------------------
fun funDemoWork(){
	println("Hello from funDemoWork")
	println(  fsum(3,6) )	   	//9
	
	println( "pre=$fcounter  " ) //pre=0
	incCounter()
	println( "post=$fcounter " ) //post=1

	println(  fsquare(3)  ) 	//9
	
	ftaction()		//hello
	
	println("fva=$fva")	      //fva=3
	
	
	ftgreet( "Hello World" )() 	//Hello World
	
	println( "${fel()}" )  		//Last exp val=100
	
 	println( { println( "Welcome" ) } )			//() -> kotlin.Unit
 	println( { println( "Welcome" ) }() )		//Welcome  kotlin.Unit
}

fun main() = runBlocking{
    println("BEGINS CPU=$cpus ${curThread()}")
	
    println( "work done in time= ${measureTimeMillis(  { funDemoWork() } )}"  )
	
    println("ENDS ${curThread()}")
}