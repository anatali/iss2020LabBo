var coap        = require('coap')
  , server      = coap.createServer()
const SerialPort = require('serialport');
var arduinoPort = "/dev/ttyUSB0";	//COM6


var arduinoSerialPort = new SerialPort(arduinoPort, { baudRate: 115200  });

 
arduinoSerialPort.on('open',function() {
  console.log('Serial Port ' + arduinoPort + ' is opened.');
});
 

server.on('request', function(req, res) {
  console.log( "server | url=" + req.url )
  var move = req.url.split('/')[1]
  arduinoSerialPort.write( move );
  res.end('Coap server to Arduinio move:' + move + '\n')
})

// the default CoAP port is 5683
server.listen(function() {
	console.log('server started')
})

 
function sendToServer( msg ){
	var req = coap.request('coap://localhost/'+msg)
	//console.log("sendToServer | " + msg)
	req.on('response' , function(res){
			//console.log( "listen | response=" + res.payload.toString('utf8') )
	        res.pipe(process.stdout)
		})
	req.end()
}

function w( ){ sendToServer("w") }
function r( ){ sendToServer("r") }
function l( ){ sendToServer("l") }
function h( ){ sendToServer("h") }
function q( ){ 
	var req = coap.request('coap://localhost/q') 
	req.on('response' , function(res){
	        res.pipe(process.stdout)
		    res.on('end', function() {
		      	console.log( "q | end") 
		      	process.exit(0)
		    })
		})
	req.end()
}

setTimeout( function(){ 
 
	console.log('============================================================================='); 
	console.log('WARNING: this should work alone AFTER Arduino SETUP time'); 
 	console.log('============================================================================='); 

setTimeout( function(){ console.log('writing ...'); l() },2000);
	//q()
},1000);


 