var coap        = require('coap')
 
function sendToServer( ){
	var req = coap.request('coap://192.168.1.8/time' )
	//console.log("sendToServer | " + msg)
	req.on('response' , function(res){
			//console.log( "listen | response=" + res.payload.toString('utf8') )
	        res.pipe(process.stdout)
		})
	req.end()
}

console.log('============================================================================='); 
console.log('WARNING: please RUN :'); 
console.log('sudo python3 -m webiopi -d -c /home/pi/nat/aiocoapExamples/basicrobotAiocoapServer.py'); 
console.log('============================================================================='); 
 

sendToServer( )
 