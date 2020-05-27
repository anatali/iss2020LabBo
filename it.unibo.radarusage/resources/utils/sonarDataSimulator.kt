package utils

import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
 
import it.unibo.kactor.MsgUtil
  
object sonarDataSimulator {
var dataCounter = 1	
    suspend fun sonarValFromUser( result : MutableMap<String,String> , key : String  ){
		print("\n$key>" )
    	var data = readLine()
        //println("data ${dataCounter++} = $data " )
		result.put( key, "$data" )
     }
}
 