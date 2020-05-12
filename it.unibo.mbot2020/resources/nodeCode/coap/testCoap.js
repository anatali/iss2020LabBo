var coap        = require('coap')
  , server      = coap.createServer()

server.on('request', function(req, res) {
  console.log( "server | url=" + req.url )
  res.end('Hello from Coap server to robot move:' + req.url.split('/')[1] + '\n')
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
	console.log('working ...'); 
	w()
	r()
	q( )
},1000);


 