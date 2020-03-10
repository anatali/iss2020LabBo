import eventBus from './eventBus/EventBus.js'
import eventBusEvents from './eventBus/events.js'

import { parseConfiguration, 
   updateSceneConstants, getinitialsceneConstants } from './utils/SceneConfigUtils.js' //DEC 2019
import sceneConfiguration from '../sceneConfig.js'                                     //DEC 2019
//import SceneManager from './SceneManager.js'                                           //DEC 2019

export default (onKeyUp, onKeyDown) => {
    const socket = io()
        
    socket.on( 'moveForward',  duration => moveForward(duration) )
    socket.on( 'moveBackward', duration => moveBackward(duration) )
    socket.on( 'turnRight',    duration => turnRight(duration) )
    socket.on( 'turnLeft',     duration => turnLeft(duration) )
    socket.on( 'alarm',        stopMoving   )
    socket.on( 'remove',       name => remove( name )  )   //DEC 2019  See WebpageServer.js
    
    socket.on( 'disconnect', () => console.log("server disconnected") )

    eventBus.subscribe( eventBusEvents.sonarActivated, sonarId => socket.emit('sonarActivated', sonarId))
    eventBus.subscribe( eventBusEvents.collision, objectName => { console.log(`collision: ${objectName}`); socket.emit('collision', objectName); stopMoving(); })

    const keycodes = {
        W: 87,
        A: 65,
        S: 83,
        D: 68,
        R: 82,
        F: 70
    }
        
    let moveForwardTimeoutId
    let moveBackwardTimeoutId

    function moveForward(duration) {
        clearTimeout(moveForwardTimeoutId)
        onKeyDown( { keyCode: keycodes.W } )
        if(duration >= 0) moveForwardTimeoutId = setTimeout( () => 
        							onKeyUp( { keyCode: keycodes.W } ), duration )
    }

    function moveBackward(duration) {
        clearTimeout(moveBackwardTimeoutId)
        onKeyDown( { keyCode: keycodes.S } )
        if(duration >= 0) moveBackwardTimeoutId = setTimeout( () => 
        							onKeyUp( { keyCode: keycodes.S } ), duration )
    }

    function turnRight(duration) {
        onKeyDown( { keyCode: keycodes.D }, duration )
    }

    function turnLeft(duration) {
        onKeyDown( { keyCode: keycodes.A }, duration )
    }

    function stopMoving() {
    console.log("stopMoving "  )
        onKeyUp( { keyCode: keycodes.W } )
        onKeyUp( { keyCode: keycodes.S } )
    }
    
//DEC 2019    
   function remove( objname ) {  //called by line 16
   		sceneConfiguration.staticObstacles.forEach( v  => {
   			//console.log(" ... "+ v.name)
   			if( v.name == objname ) {
		      console.log(" SocketIo remove " + v.name  )
		      v.centerPosition.x = 0
		      v.centerPosition.y = 0
		 	  v.size.x = 0
			  v.size.y = 0
		      //console.log(  " SocketIo remove " + v.size.x )       
		      const sceneConstants = getinitialsceneConstants()
		      updateSceneConstants(sceneConstants, parseConfiguration(sceneConfiguration) )			
   			}
   		})
   	  
  }//remove

}