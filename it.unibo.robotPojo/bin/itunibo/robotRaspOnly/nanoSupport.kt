package it.unibo.robotRaspOnly
/*
 -------------------------------------------------------------------------------------------------
 A factory that creates the support for the nano robot
 -------------------------------------------------------------------------------------------------
 */

import it.unibo.iot.baseRobot.hlmodel.BasicRobot
import it.unibo.iot.models.commands.baseRobot.BaseRobotSpeed
import it.unibo.iot.models.commands.baseRobot.BaseRobotSpeedValue
import it.unibo.iot.models.commands.baseRobot.BaseRobotStop
import it.unibo.iot.models.commands.baseRobot.BaseRobotForward
import it.unibo.iot.models.commands.baseRobot.BaseRobotBackward
import it.unibo.iot.models.commands.baseRobot.BaseRobotLeft
import it.unibo.iot.models.commands.baseRobot.BaseRobotRight
import it.unibo.iot.models.commands.baseRobot.IBaseRobotCommand
import it.unibo.kactor.sysUtil
import it.unibo.kactor.ActorBasic
import itunibo.robotRaspOnly.sonarHCSR04Support
import itunibo.robotRaspOnly.motorscSupport
import itunibo.robot.rx.ApplActorDataStream
 
object nanoSupport { 
	
	val SPEED_LOW     = BaseRobotSpeed(BaseRobotSpeedValue.ROBOT_SPEED_LOW)
	val SPEED_MEDIUM  = BaseRobotSpeed(BaseRobotSpeedValue.ROBOT_SPEED_MEDIUM)
	val SPEED_HIGH    = BaseRobotSpeed(BaseRobotSpeedValue.ROBOT_SPEED_HIGH)
	 
	fun create(actor: ActorBasic, withSonar : Boolean = true){
		motorscSupport.create( actor )
		println("nanoSupport | CREATING  withSonar=$withSonar actor=${actor.name}")
		if(withSonar){
			sonarHCSR04Support.create( actor, " ")	//sonar data source
		} 
		else println("nanoSupport | CREATING   with no Sonar ")
	} 
	
	fun move( cmd : String ){
		//println( "nanoSupport | MOVE $cmd " )
		motorscSupport.write( "$cmd\n" )
	}
}