package fsm
/*
 Fsm.kt
 Custom DSL for Moore Finite State Machine behavior.
 By Antonio Natali - DISI - University of Bologna
 */
 
import kotlinx.coroutines.*
import java.util.NoSuchElementException
import utils.AppMsg
import utils.Messages
import kotlinx.coroutines.channels.actor

val traceOn = false
fun trace( msg: String ){ if(traceOn) println( "		TRACE $msg" ) }

/*
================================================================
 STATE
================================================================
 */
class State(val stateName : String, val scope: CoroutineScope ) {
    private val edgeList          = mutableListOf<Transition>()
    private val stateEnterAction  = mutableListOf< suspend (State) -> Unit>()
    private val myself : State    = this

    fun transition(edgeName: String, targetState: String, cond: Transition.() -> Unit) {
        val trans = Transition(edgeName, targetState)
        //println("      trans $name $targetState")
        trans.cond() //set eventHandler (given by user) See fireIf
        edgeList.add(trans)
    }
    //Add an action which will be called when the state is entered
    fun action(  a:  suspend (State) -> Unit) {
        //println("State $stateName    | add action  $a")
        stateEnterAction.add( a )
    }
    /*
    fun addAction (action:  suspend (State) -> Unit) {
        stateEnterAction.add(action)
    }
    */
    suspend fun enterState() {
        val myself = this
        scope.launch {
            //trace(" --- | State $stateName    | enterState ${myself.stateName} ")
            stateEnterAction.get(0)( myself )
        }.join()
        //trace(" --- | State $stateName    | enterState DONE ")
    }

    //Get the appropriate Edge for the Message
    fun getTransitionForMessage(msg: AppMsg): Transition? {
        //println("State $name       | getTransitionForMessage  $msg  list=${edgeList.size} ")
        val first = edgeList.firstOrNull { it.canHandleMessage(msg) }
        return first
    }
}

/*
================================================================
 Transition
================================================================
 */
class Transition(val edgeName: String, val targetState: String) {

    lateinit var edgeEventHandler: ( AppMsg ) -> Boolean  //MsgId previous: String
    private val actionList = mutableListOf<(Transition) -> Unit>()

    fun action(action: (Transition) -> Unit) { //MEALY?
        //println("Transition  | add ACTION:  $action")
        actionList.add(action)
    }

    //Invoke when you go down the transition to another state
    fun enterTransition(retrieveState: (String) -> State): State {
//        println("Transition  | enterEdge  retrieveState: ${retrieveState} actionList=$actionList")
        actionList.forEach { it(this) }         //MEALY?
        return retrieveState(targetState)
    }

    fun canHandleMessage(msg: AppMsg): Boolean {
        //println("Transition  | canHandleMessage: ${msg}  ${msg is Message.Event}" )
        return edgeEventHandler( msg  ) //msg.msgId()
    }
}

/*
================================================================
 FSM
================================================================
 */
abstract class  Fsm(  val name:  String,
                      val scope: CoroutineScope = GlobalScope,
                      val confined :    Boolean = false,
                      val ioBound :     Boolean = false,
                      val channelSize : Int = 50 ){
	
	protected lateinit var currentState: State
	val NoMsg        = AppMsg.buildEvent( name, "local_noMsg", "noMsg")
	val autoStartMsg = AppMsg.buildDispatch(name, "autoStartSysMsg", "start", name)
	
	private val stateList          = mutableListOf<State>()
	protected var currentMsg       = NoMsg
	protected var myself : Fsm
	private var isStarted          = false
	private val msgQueueStore      = mutableListOf<AppMsg>()
	internal val requestMap : MutableMap<String, AppMsg> = mutableMapOf<String,AppMsg>()
	
	@kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
	protected val dispatcher =
        if( confined ) newSingleThreadContext("fsmsingle")
        else  if( ioBound ) newFixedThreadPoolContext(64, "fsmiopool")
              else Dispatchers.Default 

	
    //================================== STRUCTURAL =======================================
    fun state(stateName: String, build: State.() -> Unit) {
        val state = State(stateName, scope)
        state.build()
        stateList.add(state)
    }

    private fun getStateByName(name: String): State {
        return stateList.firstOrNull { it.stateName == name } ?: throw NoSuchElementException(name)
    }
    //===========================================================================================
	
	fun handleCurrentMessage(msg: AppMsg, nextState: State?, memo: Boolean = true): Boolean {
        if (nextState is State) {
            trace("Fsm $name | handleCurrentMessage in ${currentState.stateName} msg=${msg.MSGID} nextState=${nextState.stateName}")
            currentMsg   = msg
            if( currentMsg.isRequest() ){ requestMap.put(currentMsg.MSGID, currentMsg) }  //Request
            var msgBody = currentMsg.CONTENT
//            println("Fsm $name | handleCurrentMessage msgBody=${msgBody}")
//            val endTheTimer = currentMsg.MSGID != "local_noMsg" &&
//                            ( ! msgBody.startsWith("local_tout_")
//                                    ||
//                                     ( msgBody.contains(currentState.stateName) &&
//                                      msgBody.contains(this.name) )
//                            )
            currentState = nextState

            //println("               %%% Fsm $name | handleCurrentMessage currentState= ${currentState.stateName}  ")
//              if( endTheTimer && (stateTimer !== null) ){
//                stateTimer!!.endTimer() //terminate TimerActor
//                //stateTimer = null
//            }

            return true
        } else { //no nextState EXCLUDE EVENTS FROM msgQueueStore.  
            if (!memo) return false
            if (!(msg.isEvent())) {
                msgQueueStore.add(msg)
                println("Fsm $name |  state=${currentState.stateName} added $msg in msgQueueStore")
            }
            //else println("Fsm $name | DISCARDING THE EVENT: ${msg.msgId()}")
            return false
        }
	}
	
	suspend fun checkDoEmptyMove() {
        var nextState = checkTransition(NoMsg) //EMPTY MOVE
        while (nextState is State) {
            currentMsg = NoMsg
            currentState = nextState
            currentState.enterState()
            nextState = checkTransition(NoMsg) //EMPTY MOVE
        }		
	}

	private fun lookAtMsgQueueStore(): State? {
        trace("Fsm $name | lookAtMsgQueueStore :${msgQueueStore.size}")
        msgQueueStore.forEach {
            val state = checkTransition(it)
            if (state is State) {
                currentMsg = msgQueueStore.get( msgQueueStore.indexOf(it) )
                //println(" Fsm $name | lookAtMsgQueueStore FOUND $currentMsg")
                msgQueueStore.remove(it)
                return state
            }
        }
        return null
	}
	
	
	private fun checkTransition(msg: AppMsg): State? {
        val trans = currentState.getTransitionForMessage(msg)
        //println("Fsm $name | checkTransition $trans, $msg, curState=${currentState.stateName}")
        return if (trans != null) {
           trans.enterTransition { getStateByName(it) }
        } else {
            //println("println("Fsm $name | checkTransition in ${currentState.stateName} NO next State for $msg !!!")
            null
        }
    }
	
/*
*/
    init {
        trace("Fsm INIT setBody in state=${getInitialState()}")
        myself  = this
        setBody( getBody(), getInitialState() )
    }

	@kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
	fun terminate(){
		fsmactor.close()
	}
	
	@kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
	suspend fun autoMsg(  msg : AppMsg ) {
     	//println("ActorBasic $name | autoMsg $msg actor=${actor}")
     	fsmactor.send( msg )
    }
	
	fun doswitch(): Transition.() -> Unit { 
        return { edgeEventHandler = { true } }
    }
    fun doswitchGuarded( guard:()->Boolean ): Transition.() -> Unit {
        return { edgeEventHandler = { guard() } }
    }

	
    abstract fun getBody(): (Fsm.() -> Unit)
    abstract fun getInitialState(): String

	@kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    fun setBody( buildbody: Fsm.() -> Unit, initialStateName: String ) {
        buildbody()            //Build the structural part
        currentState = getStateByName(initialStateName)
        trace("Fsm $name | setBody send ${autoStartMsg}")
        scope.launch { fsmactor.send( autoStartMsg )    }  //auto-start
  	}

	suspend fun fsmStartWork() {
        isStarted = true
        trace("Fsm $name | fsmStartWork in STATE ${currentState.stateName}")
        currentState.enterState()
        checkDoEmptyMove()
    }
		
	suspend fun fsmwork(msg : AppMsg) {
        trace("Fsm $name | fsmwork in STATE ${currentState.stateName}")
        var nextState = checkTransition(msg)
        var b = handleCurrentMessage( msg, nextState )
        while(  b  ){ //handle previous messages
            currentState.enterState()
            checkDoEmptyMove()
            val nextState1 = lookAtMsgQueueStore()
            b = handleCurrentMessage( msg, nextState1, memo=false )
         }
	}

    suspend fun actorBody(msg: AppMsg) {
        trace("Fsm $name | actorBody msg=$msg")
        if (msg.MSGID == autoStartMsg.MSGID && !isStarted) {
             fsmStartWork()
            //println("Fsm $name | BACK TO MAIN ACTOR AFTER INIT")
        } else {
            fsmwork(msg)
            //println("Fsm $name | BACK TO MAIN ACTOR")
        }
    }
				
	@kotlinx.coroutines.ExperimentalCoroutinesApi
    @kotlinx.coroutines.ObsoleteCoroutinesApi
    val fsmactor = scope.actor<AppMsg>( dispatcher, capacity=channelSize ) {
        trace("Fsm $name | fsmactor RUNNING IN $dispatcher"  )
        for( msg in channel ) {
            //println("Fsm $name | fsmactor msg-driven msg= $msg   "  )
            if( msg.CONTENT == "stopTheActor") { channel.close() }
            else{ actorBody( msg ) }
        }
    }

	
//-----------------------------------------------------------------------
	fun whenDispatch(msgName: String): Transition.() -> Unit {
            return {
                edgeEventHandler = {
                    //println("Fsm $name | ${currentState.stateName} whenDispatch $it  $msgName")
                    it.isDispatch() && it.MSGID == msgName }
             }
    }
			

}//Fsm