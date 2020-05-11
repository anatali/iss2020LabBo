package robotNano
import it.unibo.kactor.ActorBasic

object nanoSupport{
	lateinit var owner   : ActorBasic
 	var dataSonar        : Int = 0 ; //Double = 0.0

	fun create( owner: ActorBasic, withSonar : Boolean = true  ){
		this.owner = owner	//
		motorscSupport.create( owner )
		if( withSonar ) {
	 		val realsonar = robotNano.sonarHCSR04SupportActor("realsonar")
			//Context injection  
			owner.context!!.addInternalActor(realsonar)  
	  		println("		--- nanoSupport | has created the realsonar")
		}
 	}
	
	fun  move( cmd : String ){
		motorscSupport.move( cmd)
	}
	
}	
