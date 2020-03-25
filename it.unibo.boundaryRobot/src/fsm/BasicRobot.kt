package fsm

import kotlinx.coroutines.CoroutineScope

class Basicrobot ( name: String, scope: CoroutineScope ) : Fsm( name, scope){

	override fun getInitialState() : String{
		return "s0"
	}
	
	override fun getBody() : (Fsm.() -> Unit){	
		return { //this:Fsm
			state("s0") {
				action { //it:State
					println("s0")
				}
				transition( edgeName="t0",targetState="work", cond=whenDispatch("cmd") )
			}
			
			state("work"){
				action { //it:State
					println("work")
				}				
			}
		}
	}
}//Basicrobot
