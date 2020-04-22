/* Generated by AN DISI Unibo */ 
package it.unibo.watcher

import it.unibo.kactor.*
import alice.tuprolog.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Watcher ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	@kotlinx.coroutines.ObsoleteCoroutinesApi
	@kotlinx.coroutines.ExperimentalCoroutinesApi			
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						println("			watcher emits fire")
						emit("alarm", "alarm(fire)" ) 
						delay(1500) 
						println("			watcher emits tsunami")
						emit("alarm", "alarm(tsunami)" ) 
					}
				}	 
			}
		}
}
