package kotlindemo

import kotlinx.coroutines.runBlocking

//------------------ CLOSURE -----------------------------
fun counterCreate()  : ( cmd : String ) -> Int {
    var localCounter = 0
    return { msg ->
        when (msg) {
            "inc" -> ++localCounter
            "dec" -> --localCounter
            "val" -> localCounter
            else -> throw Exception( "unknown" )
        }
    }
}

//------------------ NORMAL -----------------------------
fun handle( msg: String ){
    println("Handle $msg | ${curThread()}")
}

fun getInput() : String{
    println("Input  ... | ${curThread()}")
    return "myinput"
}
fun submit( v: Int, msg: String ) : String{
    println("Submit ... | ${curThread()}")
    return "$msg: $v"
}

fun doJob(n:Int){
    val s = getInput()
    val v = submit( n, s )
    handle( v )
}
//------------------ CPS -----------------------------
fun doJobCps( n: Int  ){
    //getInputCps(callback = { input : String -> println("doJobCps $n, $input") })
    //getInputCps { input : String -> println("doJobCps $n, $input") }
    //getInputCps(callback = { input -> submitCps( n, input, { msg ->  handle( msg ) })}
    //getInputCps  { input -> submitCps( n, input, { msg ->  handle( msg ) }  ) }   //lambda shortcut
    getInputCps  { input -> submitCps( n, input) { handle( it ) } }   //lambda shortcut
}//doJobCps

//getInputCps { input : String -> println("doJobCps $n, $input") }

fun getInputCps( callback:( String )-> Unit ):Unit{
    println("Input  ... | ${curThread()}")
    callback( "myinputcps" )
}
fun submitCps(v:Int,msg:String,callback:(String)->Unit){
    println("Submit ... | ${curThread()}")
    callback( "$msg: $v" )
}

//------------------ ASYNCH CPS -----------------------------

fun getInputAsynchCps(  callback : ( String ) -> Unit ) : Unit{
    kotlin.concurrent.thread(start = true) {
        println("Input  ... | ${curThread()} ")
        Thread.sleep(1000)
        println("Input received ")
        callback( "myinputasynchcps" )
    }
}
fun doJobAsynchCps( n: Int  ){
    getInputAsynchCps( { input -> submitCps( n, input, { msg ->  handle( msg ) })}//submitCps
    )//getInputAsynchCps
}

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
fun main() = runBlocking{
    println("BEGINS ${curThread()}")
//    val c1 = counterCreate()
//    for( i in 1..3 ) c1("inc")
//    println("c1=${c1("val")}")
//    println("---------------------------------")
//    doJob( 100 )
//    println("---------------------------------")//      doJobCps( 10  )
//    println("---------------------------------")
     doJobAsynchCps( 10 )
     println("ENDS ${curThread()}")
}

