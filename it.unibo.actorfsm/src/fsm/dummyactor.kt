package fsm

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

@kotlinx.coroutines.ObsoleteCoroutinesApi
@kotlinx.coroutines.ExperimentalCoroutinesApi
class dummyactor ( scope: CoroutineScope ) : Fsm( "dummyactor", scope ){
 	
	override fun getInitialState() : String{
		return "init"
	}
	
	override fun getBody() : (Fsm.() -> Unit){	
		return { //this:Fsm
			state("init") {	
				action { //it:State
					//delay( 1000 )
					//println("I am the dummy actor with ephemeral existence ...")
					terminate()
				}
			}
		}//return
	}
}
