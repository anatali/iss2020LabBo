/*
frontend/uniboSupports/coapClientToResourceModel
*/
const coap             = require("node-coap-client").CoapClient; 
const url              = require('url') 

var coapAddr             = "coap://localhost:8020"
var coapResourceAddr   	 = "coap://localhost:8020/ctxbasicrobot/basicrobot"

//const myUrl = url.parse(coapResourceAddr);

exports.setcoapAddr = function ( addr ){
	//coapAddr = "coap://"+ addr + ":5683";
	//coapResourceAddr   = coapAddr + "/robot/pos" // coap://localhost:5683/robot/pos
	coapResourceAddr = addr
	console.log("coap | coapResourceAddr=" + coapResourceAddr);
	createCoapClient( coapResourceAddr   );
	createCoapClient( sensorResourceAddr );
}

exports.coapGet = function (  ){
	coap
	    .request(
	         coapResourceAddr,
	        "get" /* "get" | "post" | "put" | "delete" */
 	        //[payload /* Buffer */,
	        //[options /* RequestOptions */]]
	    )
	    .then(response => { console.log("coap get done> " + response.payload );} )
	    .catch(err => {   	console.log("coap get error> " + err );} )
	    ;
	    
}//coapPut

exports.coapPut = function (  cmd ){ 
console.log("PUT " + coapResourceAddr);
	coap
	    .request(
	        coapResourceAddr,     
	        "put" ,			                          // "get" | "post" | "put" | "delete"   
	        new Buffer(cmd )                          // payload Buffer 
 	        //[options]]							//  RequestOptions 
	    )
	    .then(response => { 			// handle response  
	    	console.log("coap put done> " + cmd + " " + response);} 
	     )
	    .catch(err => { // handle error  
	    	console.log("coap | put error> " + err + " for cmd=" + cmd);}
	    )
	    ;
	    
}//coapPut

const myself   = require('./coapClientToResourceModel');


console.log("GET " + coapResourceAddr );

 
coap
    .ping(
        "coap://localhost:8020/ctxbasicrobot/basicrobot",
        1000
    )
    .then((success ) => { console.log("coap ...") })
    .catch(err => {   console.log("coap | ping error> " + err  );} )
    ;
 

//var req = coap.request('coap://localhost:8020/ctxbasicrobot/basicrobot')

function test(){
   	myself.coapGet();
 	//console.log("PUT");
 	//myself.coapPut("r")
 	//myself.coapGet();
}



test()
 

/*
 * ========= EXPORTS =======
 */

//module.exports = coap;
