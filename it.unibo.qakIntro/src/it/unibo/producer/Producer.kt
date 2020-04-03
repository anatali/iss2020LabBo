/* Generated by AN DISI Unibo */ 
package it.unibo.producer

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Producer ( name: String, scope: CoroutineScope ) : ActorBasicFsm( name, scope){
 	
	override fun getInitialState() : String{
		return "s0"
	}
		
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("producer | START")
						delay(1000) 
						forward("cmd", "cmd(w)" ,"consumer" ) 
						delay(1000) 
						forward("cmd", "cmd(h)" ,"consumer" ) 
						delay(1000) 
						forward("end", "end(0)" ,"consumer" ) 
						terminate()
					}
				}	 
			}
		}
}
