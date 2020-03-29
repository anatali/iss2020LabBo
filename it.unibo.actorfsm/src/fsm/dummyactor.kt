package fsm

import kotlinx.coroutines.CoroutineScope

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
					println("I am the dummy actor with ephemeral existence ...")
					terminate()
				}
			}
		}//return
	}
}
